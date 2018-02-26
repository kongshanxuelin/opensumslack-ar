package com.sumslack.opensource;

import android.app.Application;
import android.content.Context;

import com.taobao.weex.InitConfig;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.common.WXException;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.sumslack.opensource.weex.compontent.RichText;
import com.sumslack.opensource.weex.module.PhoneInfoModule;
import com.sumslack.opensource.weex.module.SumslackModule;

/**
 * 注意要在Manifest中启用
 * 参考manifest，否则会抛出ExceptionInInitializerError
 * 要实现ImageAdapter 否则图片不能下载
 * gradle 中一定要添加一些依赖，否则初始化会失败。
 * compile 'com.android.support:recyclerview-v7:23.1.1'
 * compile 'com.android.support:support-v4:23.1.1'
 * compile 'com.android.support:appcompat-v7:23.1.1'
 * compile 'com.alibaba:fastjson:1.1.45'
 */
public class WXApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        InitConfig config = new InitConfig.Builder().setImgAdapter(new ImageAdapter()).build();
        WXSDKEngine.initialize(this, config);
        try {
            //Sumslack扩展的Weex JSBridge
            WXSDKEngine.registerModule("event", SumslackModule.class);
            //手机基本信息
            WXSDKEngine.registerModule("poneInfo", PhoneInfoModule.class);
            //扩展组件测试
            WXSDKEngine.registerComponent("rich", RichText.class, false);
        } catch (WXException e) {
            e.printStackTrace();
        }
        //扫描二维码
        ZXingLibrary.initDisplayOpinion(this);
    }

    public static Context getContext() {
        return context;
    }
}
