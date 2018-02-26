package com.sumslack.opensource.webview;

import android.content.Context;
import android.content.Intent;

import com.alibaba.fastjson.JSONObject;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.google.gson.Gson;
import com.taobao.weex.ui.component.WXA;
import com.uuzuche.lib_zxing.activity.CaptureActivity;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/24/024.
 */

public class JSServiceManager {
    private static final String TAG = "JSServiceManager";
    private static JSServiceManager instance = new JSServiceManager();
    public static synchronized JSServiceManager getInstance(){
        if(instance == null){
            instance = new JSServiceManager();
        }
        return instance;
    }
    private JSServiceManager(){}
    private Context context;
    private BridgeWebView mBridgeWebView;
    public void registerJSFunctions(Context context,BridgeWebView mBridgeWebView){
        this.context = context;
        this.mBridgeWebView = mBridgeWebView;
        scan();
    }
    public void scan(){
        if(mBridgeWebView!=null){
            mBridgeWebView.registerHandler("scan", new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    Intent cameraIntent = new Intent(context, CaptureActivity.class);
                    context.startActivity(cameraIntent);
                    Gson json = new Gson();
                    Map retMap = new HashMap();
                    retMap.put("ret",true);
                    retMap.put("data",data);
                    function.onCallBack(json.toJson(retMap).toString());
                }
            });
        }
    }

    public void chooseImage(){
        if(mBridgeWebView!=null){
            mBridgeWebView.registerHandler("chooseImage", new BridgeHandler() {
                @Override
                public void handler(String data, CallBackFunction function) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    context.startActivity(intent);
                    Gson json = new Gson();
                    Map retMap = new HashMap();
                    retMap.put("ret",true);
                    retMap.put("data",data);
                    function.onCallBack(json.toJson(retMap).toString());
                }
            });
        }
    }

}
