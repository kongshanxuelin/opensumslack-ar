package com.sumslack.opensource;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.just.agentweb.AgentWeb;
import com.sumslack.opensource.eventbus.MsgEvent;
import com.sumslack.opensource.weex.module.SumslackModule;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.WXRenderStrategy;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.sumslack.opensource.utils.ImageUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class IndexActivity extends BaseActivity implements IWXRenderListener{
    private static final String TAG = "IndexActivity";

    //Toolbar
    private Toolbar mToolbar;
    private TextView mToolBarTitle;

    private static final int REQ_QRCODE_RESULT = 800;
    private static final int REQ_QRCODE_IMAGE_RESULT = 801;
    private static final int PARSE_QRCODE = 10;

    private DrawerLayout drawerLayout;

    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;

    //Weex
    private static String TEST_URL = "http://192.168.1.154:8081/index.weex.js";
    private WXSDKInstance mWXSDKInstance;
    private FrameLayout mContainer;

    @RequiresApi(api= Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navView = (NavigationView)findViewById(R.id.nav_view);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        mToolBarTitle = (TextView)findViewById(R.id.toolbar_title);
        mToolBarTitle.setText("OpenSumslack接口");
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.menuhome);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        initPermissions();

        //侧边栏按钮点击事件
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_scan_from_album:
                        scan(false);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_scan:
                        scan(true);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_h5api:
                        parseQrCode("file:///android_asset/jsbridge.html");
                        drawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "请扫描Weex二维码或网址二维码", Snackbar.LENGTH_LONG)/*.setAction("扫描", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Toast.makeText(IndexActivity.this,"请扫描Weex二维码或H5地址",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(IndexActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQ_QRCODE_RESULT);
                    }
                })*/.setAction("相册中选", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQ_QRCODE_IMAGE_RESULT);
                    }
                }).show();
            }
        });
        /*
        findViewById(R.id.btn_local).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, LocalActivity.class));
            }
        });

        findViewById(R.id.btn_network).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, NetworkActivity.class));
            }
        });

        findViewById(R.id.btn_fragment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(IndexActivity.this, WXFragmentActivity.class));
            }
        });

        findViewById(R.id.btn_sec).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示Intent
                //startActivity(new Intent(IndexActivity.this, SecActivity.class));
                //隐Intent
                Intent intent = new Intent("com.weex.sample.ACTION_START");
                //需在intent-filter中指定<category android:name="xxx" />
                //intent.addCategory("xxx");
                intent.putExtra("a", "11");
                startActivity(intent);
            }
        });
        */

        //加载Weex Demo
        mContainer = (FrameLayout) findViewById(R.id.container);
        mWXSDKInstance = new WXSDKInstance(this);
        mWXSDKInstance.registerRenderListener(this);
        Map<String, Object> options = new HashMap<>();
        options.put(WXSDKInstance.BUNDLE_URL, TEST_URL);
        mWXSDKInstance.renderByUrl("WeexDemoPage",TEST_URL,options,null, WXRenderStrategy.APPEND_ONCE);


        //接收广播的过滤器
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

    }

    private void scan(boolean isCamera){
        if(isCamera){
            Intent cameraIntent = new Intent(IndexActivity.this, CaptureActivity.class);
            startActivityForResult(cameraIntent, REQ_QRCODE_RESULT);
        }else{
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, REQ_QRCODE_IMAGE_RESULT);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data) {
        if(mWXSDKInstance!=null)
            mWXSDKInstance.onActivityResult(requestCode,resultCode,data);

        switch (requestCode ){
            case REQ_QRCODE_RESULT:
                if(data!=null){
                    Bundle bundle = data.getExtras();
                    if(bundle == null){
                        return;
                    }
                    if(bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS){
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    }else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                        Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case REQ_QRCODE_IMAGE_RESULT:
                if (data != null) {
                    Uri uri = data.getData();
                    //ContentResolver cr = getContentResolver();
                    try {
                        //Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                        CodeUtils.analyzeBitmap(ImageUtil.getImageAbsolutePath(this,uri), new CodeUtils.AnalyzeCallback() {
                            @Override
                            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                Toast.makeText(IndexActivity.this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                                parseQrCode(result);
                            }

                            @Override
                            public void onAnalyzeFailed() {
                                Toast.makeText(IndexActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理动作按钮的点击事件
        switch (item.getItemId()) {
            case R.id.action_scan:
                scan(true);
                return true;
            case R.id.action_scanByAlbum:
                scan(false);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStart();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        if (mWXSDKInstance != null) {
            mWXSDKInstance.onActivityDestroy();
        }
    }

    @Override
    public void onViewCreated(WXSDKInstance instance, View view) {
        if (view.getParent() != null) {
            ((ViewGroup) view.getParent()).removeView(view);
        }
        mContainer.addView(view);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance instance, String errCode, String msg) {

    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {
                Toast.makeText(context, "network is ok", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "network is unavailable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initPermissions(){
        String[] permissions = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CALL_PHONE,
                Manifest.permission.CAMERA
        };
        List<String> data = new ArrayList<>();//存储未申请的权限
        for (String permission : permissions) {
            int checkSelfPermission = ContextCompat.checkSelfPermission(this, permission);
            if(checkSelfPermission == PackageManager.PERMISSION_DENIED){//未申请
                data.add(permission);
            }
        }
        String[] canPermissions = data.toArray(new String[data.size()]);
        if (canPermissions.length == 0) {
            //权限都申请了
            //是否登录
        } else {
            //申请权限
            ActivityCompat.requestPermissions(this, permissions, 100);
        }

    }

    private void parseQrCode(final String result){
        if(result == null) return;
        boolean isWeb = (result.toLowerCase().startsWith("http://") || result.toLowerCase().startsWith("https://")) || (result.toLowerCase().startsWith("file:///android_asset") && result.toLowerCase().endsWith("html"));
        if(isWeb){
            //String result = data.getString("url");
            //H5页面
            if(result.toLowerCase().startsWith("file:///android_asset") && result.toLowerCase().endsWith("html")){
                Intent intent = new Intent(IndexActivity.this, WebViewActivity.class);
                intent.putExtra("url", result);
                startActivity(intent);
                return;
            }
            OkHttpClient client = new OkHttpClient();
            Request req = new Request.Builder().url(result).get().build();

            client.newCall(req).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.e(TAG,"GET JS ERROR",e);
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(response!=null && response.isSuccessful()){
                        String respStr = response.body().string();
                        Log.d(TAG,respStr);
                        if(respStr!=null) {
                            boolean isWeex = respStr.startsWith("// { \"framework\": \"Vue\"}");
                            if (isWeex) {
                                Intent intent = new Intent(IndexActivity.this, NetworkActivity.class);
                                intent.putExtra("url", result);
                                startActivity(intent);
                            } else {
                                //打开浏览器
                                Intent intent = new Intent(IndexActivity.this, WebViewActivity.class);
                                intent.putExtra("url", result);
                                startActivity(intent);
                            }
                        }
                    }
                }
            });



        }
    }
}
