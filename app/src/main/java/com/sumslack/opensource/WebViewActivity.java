package com.sumslack.opensource;

import android.support.annotation.Nullable;
import android.os.Bundle;

public class WebViewActivity extends BaseWebActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        String url = getIntent().getStringExtra("url");
        setUrl(url);
        super.onCreate(savedInstanceState);
    }

    @Override
    public String getUrl() {
        return super.getUrl();
    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();



        //测试Cookies
        /*try {
            String targetUrl="";
            Log.i("Info","cookies:"+ AgentWebConfig.getCookiesByUrl(targetUrl="http://www.jd.com"));
            AgentWebConfig.removeAllCookies(new ValueCallback<Boolean>() {
                @Override
                public void onReceiveValue(Boolean value) {
                    Log.i("Info","onResume():"+value);
                }
            });
            String tagInfo=AgentWebConfig.getCookiesByUrl(targetUrl);
            Log.i("Info","tag:"+tagInfo);
            AgentWebConfig.syncCookie("http://www.jd.com","ID=IDHl3NVU0N3ltZm9OWHhubHVQZW1BRThLdGhLaFc5TnVtQWd1S2g1REcwNVhTS3RXQVFBQEBFDA984906B62C444931EA0");
            String tag=AgentWebConfig.getCookiesByUrl(targetUrl);
            Log.i("Info","tag:"+tag);
            AgentWebConfig.removeSessionCookies();
            Log.i("Info","removeSessionCookies:"+AgentWebConfig.getCookiesByUrl(targetUrl));
        }catch (Exception e){
            e.printStackTrace();
        }*/

    }
}
