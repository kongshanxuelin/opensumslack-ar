package com.sumslack.opensource;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumslack.opensource.service.DownloadService;
import com.sumslack.opensource.utils.StrUtil;
import com.sumslack.opensource.utils.SumslackUtil;
import com.sumslack.opensource.weex.AbstractWeexActivity;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.utils.WXSoInstallMgrSdk;
import com.uuzuche.lib_zxing.activity.CaptureActivity;

import java.util.HashMap;
import java.util.Map;

import static android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM;

public class NetworkActivity extends AbstractWeexActivity {

  private static final String TAG = "NetworkActivity";

  private static final int CAMERA_PERMISSION_REQUEST_CODE = 0x1;
  private static final int WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 0x2;

  private ProgressBar mProgressBar;
  private TextView mTipView;

  //Service Binder
  private DownloadService.DownloadBinder downloadBinder;
  private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      downloadBinder = (DownloadService.DownloadBinder)service;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
  };

  //private static String TEST_URL = "http://dotwe.org/raw/dist/6fe11640e8d25f2f98176e9643c08687.bundle.js";
  //private static String TEST_URL = "http://wxapps.sumslack.com/demo2/demo-button.js";
  private String url = "";
  //Toolbar
  private Toolbar mToolbar;
  private TextView mToolBarTitle;

  private BroadcastReceiver mReloadReceiver;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_network);
    setContainer((ViewGroup) findViewById(R.id.index_container));
    //下载服务
    Intent serviceIntent = new Intent(this,DownloadService.class);
    startService(serviceIntent);
    bindService(serviceIntent,connection,BIND_AUTO_CREATE);

    mToolbar = (Toolbar)findViewById(R.id.toolbar_weex);
    mToolbar.setTitleTextColor(Color.WHITE);
    mToolbar.setTitle("");
    mToolBarTitle = (TextView)findViewById(R.id.toolbar_weex_title);
    mToolBarTitle.setText("Test");
    setSupportActionBar(mToolbar);
    getWindow().setFormat(PixelFormat.TRANSLUCENT);

    mProgressBar = (ProgressBar) findViewById(R.id.index_progressBar);
    mTipView = (TextView) findViewById(R.id.index_tip);
    mProgressBar.setVisibility(View.VISIBLE);
    mTipView.setVisibility(View.VISIBLE);

//    if (getSupportActionBar() != null) {
//      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//    }

    url = this.getIntent().getStringExtra("url");

    if (!WXSoInstallMgrSdk.isCPUSupport()) {
      mProgressBar.setVisibility(View.INVISIBLE);
      mTipView.setText(R.string.cpu_not_support_tip);
      return;
    }

    renderPageByURL(url);

    mReloadReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        createWeexInstance();
        renderPageByURL(url);
      }
    };
    LocalBroadcastManager.getInstance(this).registerReceiver(mReloadReceiver, new IntentFilter(WXSDKEngine.JS_FRAMEWORK_RELOAD));
  }

  public void refresh(){
    createWeexInstance();
    renderPageByURL(url);
    mProgressBar.setVisibility(View.VISIBLE);
  }

  public void scan(){
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
        Toast.makeText(this, "please give me the permission", Toast.LENGTH_SHORT).show();
      } else {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
      }
    } else {
      startActivity(new Intent(this, CaptureActivity.class));
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    unbindService(connection);
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mReloadReceiver);
  }

  @Override
  public void onRenderSuccess(WXSDKInstance wxsdkInstance, int i, int i1) {
    super.onRenderSuccess(wxsdkInstance,i,i1);
    mProgressBar.setVisibility(View.GONE);
    mTipView.setVisibility(View.GONE);
  }

  @Override
  public void onException(WXSDKInstance wxsdkInstance, String s, String s1) {
    super.onException(wxsdkInstance,s,s1);
    mProgressBar.setVisibility(View.GONE);
    mTipView.setVisibility(View.VISIBLE);
    SumslackUtil.toast("weex 渲染异常:"+s1);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    switch(requestCode){
      case 8://权限拒绝，无法使用下载服务
        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"You denied the permission.", Toast.LENGTH_LONG).show();
            finish();
        }else if (requestCode == CAMERA_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startActivity(new Intent(this, CaptureActivity.class));
        } else if (requestCode == WRITE_EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
          Toast.makeText(this, "request camara permission fail!", Toast.LENGTH_SHORT).show();
        }
        break;
    }
  }

  public void downloadFile(String url, JSCallback callback){
    downloadBinder.startDownload(url,callback);
  }
  public void setNavigationBarTitle(String title){
    if(!title.equals(""))
      mToolBarTitle.setText(title);
  }
  public void setLayoutMenus(String jsonStr){
    try{
      JSONObject json = JSON.parseObject(jsonStr);
      if(json!=null){
        String title = StrUtil.formatMullStr(json.get("title"));
        if(!title.equals(""))
          mToolBarTitle.setText(title);
        JSONArray items = json.getJSONArray("items");
        if(items!=null && items.size()>0){
          mToolbar.getMenu().clear();
          for(int i=0;i<items.size();i++){
            JSONObject menuItem = items.getJSONObject(i);
            mToolbar.getMenu().add(StrUtil.formatMullStr(menuItem.get("title"))).setShowAsAction(SHOW_AS_ACTION_IF_ROOM);
          }
          mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
              String _title = StrUtil.formatMullStr(item.getTitle());
              String _href = StrUtil.formatMullStr(getHref(items,_title));
              Log.d(TAG,"click menu:"+_title);
              Map<String, Object> params = new HashMap<>();
              params.put("title",_title);
              if(_href.startsWith("javascript:")){
                _href = _href.substring("javascript:".length());
                Log.d(TAG,"call js func:"+_href);
                mInstance.fireGlobalEventCallback(_href,params);
                return true;
              }else if(_href.startsWith("http://") || _href.startsWith("https://")){
                SumslackUtil.openWebview(NetworkActivity.this,_href);
              }
              return false;
            }
          });

        }
      }
    }catch(Exception ex){
      Log.e(TAG,"layoutNaviBar ERROR",ex);
    }
  }

  private String getHref(JSONArray items,String title){
    for(Object itemObj : items){
      JSONObject item = (JSONObject)itemObj;
      if(StrUtil.formatMullStr(item.get("title")).equals(title)){
        return StrUtil.formatMullStr(item.get("href"));
      }
    }
    return null;
  }
}
