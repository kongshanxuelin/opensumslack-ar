package com.sumslack.opensource.eventbus;

import android.content.Intent;
import android.os.Bundle;

import java.util.Map;

/**
 * Created by Administrator on 2018/2/26/026.
 */

public class MsgEvent {
    public static final class Action {
        public static final int QrCode_Camera = 0x1000;
        public static final int QrCode_Album = 0x1001;

    }
    private int action;
    private Bundle params;
    private Intent intent;
    public MsgEvent(int action,Bundle params){
        this.action = action;
        this.params = params;
    }
    public MsgEvent(int action,Intent intent){
        this.action = action;
        this.intent = intent;
    }
    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public Bundle getParams() {
        return params;
    }

    public void setParams(Bundle params) {
        this.params = params;
    }

    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }
}
