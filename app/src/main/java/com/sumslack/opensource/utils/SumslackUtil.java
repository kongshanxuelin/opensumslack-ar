package com.sumslack.opensource.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.sumslack.opensource.WXApplication;
import com.sumslack.opensource.WebViewActivity;

/**
 * Created by Administrator on 2018/3/5/005.
 */

public class SumslackUtil {
    public static void toast(String msg){
        Toast.makeText(WXApplication.getContext(),msg,Toast.LENGTH_LONG).show();
    }
    public static void openWebview(Context context, String url){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
