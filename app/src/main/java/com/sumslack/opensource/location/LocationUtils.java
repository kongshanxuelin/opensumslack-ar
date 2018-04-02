package com.sumslack.opensource.location;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviStep;
import com.sumslack.opensource.WXApplication;
import com.sumslack.opensource.location.Bean.StrategyBean;


import java.io.File;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

public class LocationUtils {
    private static DecimalFormat fnum = new DecimalFormat("##0.0");
    public static final int AVOID_CONGESTION = 4;  // 躲避拥堵
    public static final int AVOID_COST = 5;  // 避免收费
    public static final int AVOID_HIGHSPEED = 6; //不走高速
    public static final int PRIORITY_HIGHSPEED = 7; //高速优先

    public static final int START_ACTIVITY_REQUEST_CODE = 1;
    public static final int ACTIVITY_RESULT_CODE = 2;

    public static final String INTENT_NAME_AVOID_CONGESTION = "AVOID_CONGESTION";
    public static final String INTENT_NAME_AVOID_COST = "AVOID_COST";
    public static final String INTENT_NAME_AVOID_HIGHSPEED = "AVOID_HIGHSPEED";
    public static final String INTENT_NAME_PRIORITY_HIGHSPEED = "PRIORITY_HIGHSPEED";

    //Location
    private static AMapLocationClient locClient = null;
    private static AMapLocationClientOption mLocOption = null;

    public static void locationDestroy(){
        if (null != locClient) {
            locClient.stopLocation();
            locClient.onDestroy();
            locClient=null;
        }
    }

    public static void getCurrentLocation(AMapLocationListener listener){
        if(ContextCompat.checkSelfPermission(WXApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            Toast.makeText(WXApplication.getContext(),"没给该应用授权位置获取权限",Toast.LENGTH_LONG).show();
        }else{
            if(locClient == null) {
                locClient = new AMapLocationClient(WXApplication.getContext());
                mLocOption = new AMapLocationClientOption();
            }
            mLocOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocOption.setOnceLocation(true);
            locClient.setLocationOption(mLocOption);
            locClient.stopLocation();
            locClient.startLocation();
            locClient.setLocationListener(listener);
        }
    }


    public static String getFriendlyTime(int s) {
        String timeDes = "";
        int h = s / 3600;
        if (h > 0) {
            timeDes += h + "小时";
        }
        int min = (int) (s % 3600) / 60;
        if (min > 0) {
            timeDes += min + "分";
        }
        return timeDes;
    }

    public static String getFriendlyDistance(int m) {
        float dis = m / 1000f;
        String disDes = fnum.format(dis) + "公里";
        return disDes;
    }

    /**
     * 计算path对应的标签
     *
     * @param paths        多路径规划回调的所有路径
     * @param ints         多路径线路ID
     * @param pathIndex    当前路径索引
     * @param strategyBean 封装策略bean
     * @return path对应标签描述
     */
    public static String getStrategyDes(HashMap<Integer, AMapNaviPath> paths, int[] ints, int pathIndex, StrategyBean strategyBean) {
        int StrategyTAGIndex = pathIndex + 1;
        String StrategyTAG = "方案" + StrategyTAGIndex;

        int minTime = Integer.MAX_VALUE;
        int minDistance = Integer.MAX_VALUE;
        int minTrafficLightNumber = Integer.MAX_VALUE;
        int minCost = Integer.MAX_VALUE;
        for (int i = 0; i < ints.length; i++) {
            if (pathIndex == i) {
                continue;
            }
            AMapNaviPath path = paths.get(ints[i]);
            if (path == null) {
                continue;
            }
            int trafficListNumber = getTrafficNumber(path);
            if (trafficListNumber < minTrafficLightNumber) {
                minTrafficLightNumber = trafficListNumber;
            }

            if (path.getTollCost() < minCost) {
                minCost = path.getTollCost();
            }

            if (path.getAllTime() < minTime) {
                minTime = path.getAllTime();
            }
            if (path.getAllLength() < minDistance) {
                minDistance = path.getAllLength();
            }
        }
        AMapNaviPath indexPath = paths.get(ints[pathIndex]);
        int indexTrafficLightNumber = getTrafficNumber(indexPath);
        int indexCost = indexPath.getTollCost();
        int indexTime = indexPath.getAllTime();
        int indexDistance = indexPath.getAllLength();
        if (indexTrafficLightNumber < minTrafficLightNumber) {
            StrategyTAG = "红绿灯少";
        }
        if (indexCost < minCost) {
            StrategyTAG = "收费较少";
        }

        if (Math.round(indexDistance / 100f) < Math.round(minDistance / 100f)) {   // 展示距离精确到千米保留一位小数，比较时按照展示数据处理
            StrategyTAG = "距离最短";
        }
        if (indexTime / 60 < minTime / 60) {    //展示时间精确到分钟，比较时按照展示数据处理
            StrategyTAG = "时间最短";
        }
        boolean isMulti = isMultiStrategy(strategyBean);
        if (strategyBean.isCongestion() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "躲避拥堵";
        }
        if (strategyBean.isAvoidhightspeed() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "不走高速";
        }
        if (strategyBean.isCost() && pathIndex == 0 && !isMulti) {
            StrategyTAG = "避免收费";
        }
        if (pathIndex == 0 && StrategyTAG.startsWith("方案")) {
            StrategyTAG = "推荐";
        }
        return StrategyTAG;
    }


    /**
     * 判断驾车偏好设置是否同时选中多个策略
     *
     * @param strategyBean 驾车策略bean
     * @return
     */
    public static boolean isMultiStrategy(StrategyBean strategyBean) {
        boolean isMultiStrategy = false;
        if (strategyBean.isCongestion()) {
            if (strategyBean.isAvoidhightspeed() || strategyBean.isCost() || strategyBean.isHightspeed()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isCost()) {
            if (strategyBean.isCongestion() || strategyBean.isAvoidhightspeed()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isAvoidhightspeed()) {
            if (strategyBean.isCongestion() || strategyBean.isCost()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        if (strategyBean.isHightspeed()) {
            if (strategyBean.isCongestion()) {
                isMultiStrategy = true;
            } else {
                isMultiStrategy = false;
            }
        }
        return isMultiStrategy;
    }

    public static Spanned getRouteOverView(AMapNaviPath path) {
        String routeOverView = "";
        if (path == null) {
            Html.fromHtml(routeOverView);
        }

        int cost = path.getTollCost();
        if (cost > 0) {
            routeOverView += "过路费约<font color=\"red\" >" + cost + "</font>元";
        }
        int trafficLightNumber = getTrafficNumber(path);
        if (trafficLightNumber > 0) {
            routeOverView += "红绿灯" + trafficLightNumber + "个";
        }
        return Html.fromHtml(routeOverView);
    }

    public static int getTrafficNumber(AMapNaviPath path) {
        int trafficLightNumber = 0;
        if (path == null) {
            return trafficLightNumber;
        }
        List<AMapNaviStep> steps = path.getSteps();
        for (AMapNaviStep step : steps) {
            trafficLightNumber += step.getTrafficLightNumber();
        }
        return trafficLightNumber;
    }

    //唤起高德地图进行导航
    /**
     * 启动高德App进行导航
     * <h3>Version</h3> 1.0
     * <h3>CreateTime</h3> 2016/6/27,13:58
     * <h3>UpdateTime</h3> 2016/6/27,13:58
     * <h3>CreateAuthor</h3>
     * <h3>UpdateAuthor</h3>
     * <h3>UpdateInfo</h3> (此处输入修改内容,若无修改可不写.)
     *
     * @param sourceApplication 必填 第三方调用应用名称。如 amap
     * @param poiname 非必填 POI 名称
     * @param lat 必填 纬度
     * @param lon 必填 经度
     * @param dev 必填 是否偏移(0:lat 和 lon 是已经加密后的,不需要国测加密; 1:需要国测加密)
     * @param style 必填 导航方式(0 速度快; 1 费用少; 2 路程短; 3 不走高速；4 躲避拥堵；5 不走高速且避免收费；6 不走高速且躲避拥堵；7 躲避收费和拥堵；8 不走高速躲避收费和拥堵))
     */
    public static  void goToNaviActivity(Context context, String sourceApplication , String poiname , double lat , double lon , String dev , String style){
        StringBuffer stringBuffer  = new StringBuffer("androidamap://navi?sourceApplication=")
                .append(sourceApplication);
        if (!TextUtils.isEmpty(poiname)){
            stringBuffer.append("&poiname=").append(poiname);
        }
        stringBuffer.append("&lat=").append(lat)
                .append("&lon=").append(lon)
                .append("&dev=").append(dev)
                .append("&style=").append(style);

        Intent intent = new Intent("android.intent.action.VIEW", android.net.Uri.parse(stringBuffer.toString()));
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

    public static boolean isInstallByRead(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }
}
