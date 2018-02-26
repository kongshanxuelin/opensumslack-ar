package com.sumslack.opensource.extend.module;

import android.widget.Toast;

import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.WXModule;

/**
 * Created by Administrator on 2018/2/1/001.
 */

public class TestModule extends WXModule {
    //run ui thread
    @JSMethod(uiThread = true)
    public void printLog(String msg) {
        Toast.makeText(mWXSDKInstance.getContext(),msg, Toast.LENGTH_SHORT).show();
    }
}
