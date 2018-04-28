package com.sumslack.opensource;

import android.Manifest;
import android.content.BroadcastReceiver;
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
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sumslack.opensource.location.CalculateRouteActivity;
import com.sumslack.opensource.utils.ImageUtil;
import com.sumslack.opensource.weex.AbstractWeexActivity;
import com.taobao.weex.IWXRenderListener;
import com.taobao.weex.WXRenderErrorCode;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.utils.WXFileUtils;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class IndexActivity extends AbstractWeexActivity implements IWXRenderListener{
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
    private ProgressBar mProgressBar;
    private TextView mTipView;
    private BroadcastReceiver mReloadReceiver;

    //weex url
    private EditText et_weexurl;

    //Weex
    //private static String TEST_URL = "http://192.168.1.154:8081/index.weex.js";

    private static String TEST_URL = "http://wxapps.sumslack.com/demo2/index.js";

    //private static String TEST_URL = "weex.demo.js";

    @RequiresApi(api= Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        setContainer((ViewGroup) findViewById(R.id.index_container));

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites()
//        .detectNetwork().penaltyLog().build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().penaltyLog().penaltyDeath().build());

        mProgressBar = (ProgressBar) findViewById(R.id.index_progressBar);
        mTipView = (TextView) findViewById(R.id.index_tip);
        mProgressBar.setVisibility(View.VISIBLE);
        mTipView.setVisibility(View.VISIBLE);

        et_weexurl = (EditText)findViewById(R.id.et_wxurl);

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
        //加载Weex Demo
        renderPage();
        mReloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                createWeexInstance();
                renderPage();
                mProgressBar.setVisibility(View.VISIBLE);
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mReloadReceiver, new IntentFilter(WXSDKEngine.JS_FRAMEWORK_RELOAD));


        //接收广播的过滤器
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeReceiver = new NetworkChangeReceiver();
        registerReceiver(networkChangeReceiver, intentFilter);

    }

    private void renderPage(){
        String url = et_weexurl.getText().toString();
        if(!url.equals("")){
            renderPageByURL(url);
        }else{
            if(TEST_URL.startsWith("http://")){
                renderPageByURL(TEST_URL);
            }else{
                renderPage(WXFileUtils.loadAsset(TEST_URL, this), null);
            }
        }
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
        if(mInstance!=null)
            mInstance.onActivityResult(requestCode,resultCode,data);

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
                        parseQrCode(result);
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

    private static final int ALL_PERMISSION_REQUEST_CODE = 0x10;
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ALL_PERMISSION_REQUEST_CODE){
            createWeexInstance();
            renderPage();
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 处理动作按钮的点击事件
        switch (item.getItemId()) {
            case R.id.action_refresh:
                createWeexInstance();
                renderPage();
                mProgressBar.setVisibility(View.VISIBLE);
                break;
            case R.id.action_scan:
                scan(true);
                break;
            case R.id.action_scanByAlbum:
                scan(false);
                break;
            case R.id.action_lbs_select:
                Intent intent = new Intent(IndexActivity.this, MapActivity.class);
                intent.putExtra("lat",29.816730);
                intent.putExtra("lot",121.536688);
                startActivity(intent);
                break;
            case R.id.action_lbs_loc:
                Intent navIntent = new Intent(IndexActivity.this, CalculateRouteActivity.class);
                navIntent.putExtra("lat",29.816730);
                navIntent.putExtra("lot",121.536688);
                navIntent.putExtra("lat2",29.826730);
                navIntent.putExtra("lot2",121.546688);
                startActivity(navIntent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(networkChangeReceiver);
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReloadReceiver);
    }

    @Override
    public void onRenderSuccess(WXSDKInstance wxsdkInstance, int width, int height) {
        super.onRenderSuccess(wxsdkInstance,width,height);
        mProgressBar.setVisibility(View.GONE);
        mTipView.setVisibility(View.GONE);
    }

    @Override
    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {

    }

    @Override
    public void onException(WXSDKInstance wxsdkInstance, String s, String s1) {
        super.onException(wxsdkInstance,s,s1);
        mProgressBar.setVisibility(View.GONE);
        mTipView.setVisibility(View.VISIBLE);
        mTipView.setText(R.string.index_tip);
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
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
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
            ActivityCompat.requestPermissions(this, permissions, 0x10);
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
