package com.sumslack.opensource;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.sumslack.opensource.weex.adapter.DefaultAccessibilityRoleAdapter;
import com.sumslack.opensource.weex.adapter.DefaultWebSocketAdapterFactory;
import com.sumslack.opensource.weex.adapter.ImageAdapter;
import com.sumslack.opensource.weex.adapter.InterceptWXHttpAdapter;
import com.sumslack.opensource.weex.adapter.JSExceptionAdapter;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.bridge.WXBridgeManager;
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

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(context));

        WXBridgeManager.updateGlobalConfig("wson_on");
        WXEnvironment.setOpenDebugLog(true);
        WXEnvironment.setApkDebugable(true);

        WXSDKEngine.addCustomOptions("appName", "WXOpenSumslack");
        WXSDKEngine.addCustomOptions("appGroup", "WXApp");

        WXSDKEngine.initialize(this,
                new InitConfig.Builder()
                        //.setImgAdapter(new FrescoImageAdapter())// use fresco adapter
                        .setImgAdapter(new ImageAdapter())
                        .setWebSocketAdapterFactory(new DefaultWebSocketAdapterFactory())
                        .setJSExceptionAdapter(new JSExceptionAdapter())
                        .setHttpAdapter(new InterceptWXHttpAdapter())
                        .build()
        );
        WXSDKManager.getInstance().setAccessibilityRoleAdapter(new DefaultAccessibilityRoleAdapter());

        try {
            Fresco.initialize(this);
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

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                if (false) {
                    // We assume that the application is on an idle time.
                    WXSDKManager.getInstance().notifyTrimMemory();
                }
                if (false) {
                    WXSDKManager.getInstance().notifySerializeCodeCache();
                }
            }
        });
    }

    public static Context getContext() {
        return context;
    }

    private void initDebugEnvironment(boolean connectable, boolean debuggable, String host) {
        if (!"DEBUG_SERVER_HOST".equals(host)) {
            WXEnvironment.sDebugServerConnectable = connectable;
            WXEnvironment.sRemoteDebugMode = debuggable;
            WXEnvironment.sRemoteDebugProxyUrl = "ws://" + host + ":8088/debugProxy/native";
        }
    }
}
