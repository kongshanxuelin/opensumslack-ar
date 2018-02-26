package com.sumslack.opensource;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/22/022.
 */

public class ActivityCollectorManager {
    public static List<Activity> activities = new ArrayList<>();
    public static void addActivity(Activity activity){
        activities.add(activity);
    }
    public static void removeActivity(Activity act){
        activities.remove(act);
    }
    public static void finishAll(){
        for(Activity act : activities){
            if(!act.isFinishing()){
                act.finish();
            }
        }
    }
}
