package com.sumslack.opensource;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.LogUtils;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.sumslack.opensource.webview.JSServiceManager;
import com.sumslack.opensource.webview.WebLayout;

import java.util.HashMap;
import java.util.Map;

public class BaseWebActivity extends BaseActivity{
    private static final String TAG = "BaseWebActivity";
    protected AgentWeb mAgentWeb;
    private LinearLayout mLinearLayout;
    private Toolbar mToolbar;
    private TextView mTitleTextView;
    private AlertDialog mAlertDialog;

    private String url;

    //JSBridge
    private BridgeWebView mBridgeWebView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_view);

        mBridgeWebView=new BridgeWebView(this);

        mLinearLayout = (LinearLayout) this.findViewById(R.id.container);
        mToolbar = (Toolbar) this.findViewById(R.id.toolbar);
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setTitle("");
        mTitleTextView = (TextView) this.findViewById(R.id.toolbar_title);
        this.setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            // Enable the Up button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        long p = System.currentTimeMillis();

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebViewClient(new BridgeWebViewClient(mBridgeWebView))
                .setWebChromeClient(mWebChromeClient)
                .setWebView(mBridgeWebView)
                //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                //.setWebLayout(new WebLayout(this))
                //.setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                //.interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(getUrl());

        //JS Bridge
        JSServiceManager.getInstance().registerJSFunctions(this,mBridgeWebView);

        mBridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });
        Map user = new HashMap();
        user.put("test","abc");
        mBridgeWebView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Log.i(TAG,"data:"+data);
            }
        });

        mBridgeWebView.send("hello");

        //mAgentWeb.getUrlLoader().loadUrl(getUrl());

        long n = System.currentTimeMillis();
        Log.i("Info", "init used time:" + (n - p));


    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };

    public String getUrl() {
        if(url!=null)
            return url;
        else
            return "https://m.jd.com/";
    }

    protected  void setUrl(String url){
        this.url = url;
    }


    private void showDialog() {

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage("您确定要关闭该页面吗?")
                    .setNegativeButton("再逛逛", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    })//
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            BaseWebActivity.this.finish();
                        }
                    }).create();
        }
        mAlertDialog.show();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();

    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("Info", "result:" + requestCode + " result:" + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //  super.onProgressChanged(view, newProgress);
            Log.i(TAG, "onProgressChanged:" + newProgress + "  view:" + view);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitleTextView != null && !TextUtils.isEmpty(title)) {
                if (title.length() > 10) {
                    title = title.substring(0, 10).concat("...");
                }
            }
            mTitleTextView.setText(title);
        }
    };
}
