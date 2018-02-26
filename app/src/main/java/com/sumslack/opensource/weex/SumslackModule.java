package com.sumslack.opensource.weex;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.sumslack.opensource.NetworkActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/26/026.
 */

public class SumslackModule extends WXModule{
    private static final String TAG = "SumslackModule";
    @JSMethod(uiThread = false)
    public String test(String a){
        return "return " + a;
    }

    @JSMethod(uiThread = true)
    public void scan(){
        Intent cameraIntent = new Intent(mWXSDKInstance.getContext(), CaptureActivity.class);
        mWXSDKInstance.getContext().startActivity(cameraIntent);
    }

    @JSMethod(uiThread = true)
    public void layoutNaviBar(){

    }
    @JSMethod(uiThread = false)
    public void navigateTo(String url){
        if(url.startsWith("file://assets")){
            url = url.substring("file://assets".length());
        }
        Intent intent = new Intent(mWXSDKInstance.getContext(), NetworkActivity.class);
        url = getBaseUrl() + url;
        Log.d(TAG,"url:" +url);
        intent.putExtra("url",url);
        mWXSDKInstance.getContext().startActivity(intent);
    }
    @JSMethod(uiThread = false)
    public void redirectTo(String url){
        if(url.startsWith("file://assets")){
            url = url.substring("file://assets".length());
        }
        Intent intent = new Intent(mWXSDKInstance.getContext(), NetworkActivity.class);
        url = getBaseUrl() + url;
        intent.putExtra("url",url);
        mWXSDKInstance.getContext().startActivity(intent);
    }

    private String getBaseUrl(){
        return mWXSDKInstance.getBundleUrl().substring(0,mWXSDKInstance.getBundleUrl().lastIndexOf("/"));
    }
}
