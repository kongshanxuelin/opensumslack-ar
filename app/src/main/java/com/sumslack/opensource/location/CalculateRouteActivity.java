package com.sumslack.opensource.location;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviException;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.autonavi.tbt.TrafficFacilityInfo;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sumslack.opensource.BaseActivity;
import com.sumslack.opensource.R;
import com.sumslack.opensource.RouteNaviActivity;
import com.sumslack.opensource.location.Bean.StrategyBean;
import com.sumslack.opensource.utils.StrUtil;
import com.sumslack.opensource.utils.SumslackUtil;

public class CalculateRouteActivity extends AppCompatActivity implements AMapNaviListener, View.OnClickListener, AMap.OnMapLoadedListener {
    private static final String TAG = "CalculateRouteActivity";
    private StrategyBean mStrategyBean;
    private static final float ROUTE_UNSELECTED_TRANSPARENCY = 0.3F;
    private static final float ROUTE_SELECTED_TRANSPARENCY = 1F;

    /**
     * 导航对象(单例)
     */
    private AMapNavi mAMapNavi;

    private MapView mMapView;
    private AMap mAMap;
    private NaviLatLng startLatlng = null;
    private NaviLatLng endLatlng = null;

    private String address;
    private List<NaviLatLng> startList = new ArrayList<NaviLatLng>();
    /**
     * 途径点坐标集合
     */
    private List<NaviLatLng> wayList = new ArrayList<NaviLatLng>();
    /**
     * 终点坐标集合［建议就一个终点］
     */
    private List<NaviLatLng> endList = new ArrayList<NaviLatLng>();
    /**
     * 保存当前算好的路线
     */
    private SparseArray<RouteOverLay> routeOverlays = new SparseArray<RouteOverLay>();
    /*
            * strategyFlag转换出来的值都对应PathPlanningStrategy常量，用户也可以直接传入PathPlanningStrategy常量进行算路。
            * 如:mAMapNavi.calculateDriveRoute(mStartList, mEndList, mWayPointList,PathPlanningStrategy.DRIVING_DEFAULT);
            */
    int strategyFlag = 0;

    private Button mStartNaviButton;
    private Button mStartNaviGaodeButton;
    private LinearLayout mRouteLineLayoutOne, mRouteLinelayoutTwo, mRouteLineLayoutThree;
    private View mRouteViewOne, mRouteViewTwo, mRouteViewThree;
    private TextView mRouteTextStrategyOne, mRouteTextStrategyTwo, mRouteTextStrategyThree;
    private TextView mRouteTextTimeOne, mRouteTextTimeTwo, mRouteTextTimeThree;
    private TextView mRouteTextDistanceOne, mRouteTextDistanceTwo, mRouteTextDistanceThree;
    private TextView mCalculateRouteOverView;
    private ImageView mImageTraffic, mImageStrategy;

    private int routeID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_route);

        startLatlng =  new NaviLatLng(getIntent().getExtras().getDouble("lat"),getIntent().getExtras().getDouble("lot"));
        endLatlng = new NaviLatLng(getIntent().getExtras().getDouble("lat2"),getIntent().getExtras().getDouble("lot2"));
        address = StrUtil.formatMullStr(getIntent().getExtras().getString("addr"));

        mMapView = (MapView) findViewById(R.id.navi_view);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        initView();
        init();
        initNavi();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calculate_route_start_navi: //语音导航模拟
                startNavi();
                break;
            case R.id.calculate_route_start_navi2:  //唤起本地高德导航
                if (LocationUtils.isInstallByRead("com.autonavi.minimap")){
                    LocationUtils.goToNaviActivity(this,"OpenSumslack",null,endLatlng.getLatitude(),endLatlng.getLongitude(),"1","2");
                }else if (LocationUtils.isInstallByRead("com.baidu.BaiduMap")){ //打开百度地图
                    try {
                        Intent intent = Intent.getIntent("intent://map/direction?" +
                                "destination=latlng:" + endLatlng.getLatitude() + "," + endLatlng.getLongitude() + "|name:"+ address +    //终点
                                "&mode=driving&" +     //导航路线方式
                                "&src=appname#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
                        startActivity(intent); //启动调用
                    } catch (URISyntaxException e) {
                        Log.e(TAG, e.getMessage());
                    }
                }else{
                    //Toast.makeText(CalculateRouteActivity.this,"无法使用，本地未安装任何导航App",Toast.LENGTH_LONG).show();
                    String url = "http://m.amap.com/?from="
                            + startLatlng.getLatitude() + "," + startLatlng.getLongitude()
                            + "&to=" + endLatlng.getLatitude() + "," + endLatlng.getLongitude() + "&type=0&opt=1&dev=0";
                    SumslackUtil.openWebview(CalculateRouteActivity.this,url);
                }
                break;
            case R.id.route_line_one:
                focuseRouteLine(true, false, false);
                break;
            case R.id.route_line_two:
                focuseRouteLine(false, true, false);
                break;
            case R.id.route_line_three:
                focuseRouteLine(false, false, true);
                break;
            case R.id.map_traffic:
                setTraffic();
                break;
            case R.id.strategy_choose:
                strategyChoose();
                break;
            default:
                break;
        }

    }

    /**
     * 驾车路径规划计算
     */
    private void calculateDriveRoute() {
        try {
            strategyFlag = mAMapNavi.strategyConvert(mStrategyBean.isCongestion(), mStrategyBean.isCost(), mStrategyBean.isAvoidhightspeed(), mStrategyBean.isHightspeed(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mAMapNavi.calculateDriveRoute(startList, endList, wayList, strategyFlag);
    }

    /**
     * 接收驾车偏好设置项
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (LocationUtils.ACTIVITY_RESULT_CODE == resultCode) {
            boolean congestion = data.getBooleanExtra(LocationUtils.INTENT_NAME_AVOID_CONGESTION, false);
            mStrategyBean.setCongestion(congestion);
            boolean cost = data.getBooleanExtra(LocationUtils.INTENT_NAME_AVOID_COST, false);
            mStrategyBean.setCost(cost);
            boolean avoidhightspeed = data.getBooleanExtra(LocationUtils.INTENT_NAME_AVOID_HIGHSPEED, false);
            mStrategyBean.setAvoidhightspeed(avoidhightspeed);
            boolean hightspeed = data.getBooleanExtra(LocationUtils.INTENT_NAME_PRIORITY_HIGHSPEED, false);
            mStrategyBean.setHightspeed(hightspeed);
            calculateDriveRoute();
        }
    }

    /**
     * 导航初始化
     */
    private void initNavi() {
        mStrategyBean = new StrategyBean(false, false, false, false);
        startList.add(startLatlng);
        endList.add(endLatlng);
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);

    }

    private void initView() {
        mStartNaviButton = (Button) findViewById(R.id.calculate_route_start_navi);
        mStartNaviGaodeButton = (Button)findViewById(R.id.calculate_route_start_navi2);
        mStartNaviButton.setOnClickListener(this);
        mStartNaviGaodeButton.setOnClickListener(this);

        mImageTraffic = (ImageView) findViewById(R.id.map_traffic);
        mImageTraffic.setOnClickListener(this);
        mImageStrategy = (ImageView) findViewById(R.id.strategy_choose);
        mImageStrategy.setOnClickListener(this);

        mCalculateRouteOverView = (TextView) findViewById(R.id.calculate_route_navi_overview);

        mRouteLineLayoutOne = (LinearLayout) findViewById(R.id.route_line_one);
        mRouteLineLayoutOne.setOnClickListener(this);
        mRouteLinelayoutTwo = (LinearLayout) findViewById(R.id.route_line_two);
        mRouteLinelayoutTwo.setOnClickListener(this);
        mRouteLineLayoutThree = (LinearLayout) findViewById(R.id.route_line_three);
        mRouteLineLayoutThree.setOnClickListener(this);

        mRouteViewOne = (View) findViewById(R.id.route_line_one_view);
        mRouteViewTwo = (View) findViewById(R.id.route_line_two_view);
        mRouteViewThree = (View) findViewById(R.id.route_line_three_view);

        mRouteTextStrategyOne = (TextView) findViewById(R.id.route_line_one_strategy);
        mRouteTextStrategyTwo = (TextView) findViewById(R.id.route_line_two_strategy);
        mRouteTextStrategyThree = (TextView) findViewById(R.id.route_line_three_strategy);

        mRouteTextTimeOne = (TextView) findViewById(R.id.route_line_one_time);
        mRouteTextTimeTwo = (TextView) findViewById(R.id.route_line_two_time);
        mRouteTextTimeThree = (TextView) findViewById(R.id.route_line_three_time);

        mRouteTextDistanceOne = (TextView) findViewById(R.id.route_line_one_distance);
        mRouteTextDistanceTwo = (TextView) findViewById(R.id.route_line_two_distance);
        mRouteTextDistanceThree = (TextView) findViewById(R.id.route_line_three_distance);
    }


    /**
     * 初始化AMap对象
     */
    private void init() {
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            mAMap.setTrafficEnabled(false);
            mAMap.setOnMapLoadedListener(this);
            mImageTraffic.setImageResource(R.drawable.map_traffic_white);
            UiSettings uiSettings = mAMap.getUiSettings();
            uiSettings.setZoomControlsEnabled(false);
        }
    }

    /**
     * 绘制路径规划结果
     *
     * @param routeId 路径规划线路ID
     * @param path    AMapNaviPath
     */
    private void drawRoutes(int routeId, AMapNaviPath path) {
        mAMap.moveCamera(CameraUpdateFactory.changeTilt(0));
        RouteOverLay routeOverLay = new RouteOverLay(mAMap, path, this);
        try {
            routeOverLay.setWidth(60f);
        } catch (AMapNaviException e) {
            e.printStackTrace();
        }
        routeOverLay.setTrafficLine(true);
        routeOverLay.addToMap();
        routeOverlays.put(routeId, routeOverLay);
    }


    /**
     * 开始导航
     */
    private void startNavi() {
        if (routeID != -1){
            mAMapNavi.selectRouteId(routeID);
            Intent gpsintent = new Intent(getApplicationContext(), RouteNaviActivity.class);
            gpsintent.putExtra("gps", false); // gps 为true为真实导航，为false为模拟导航
            startActivity(gpsintent);
        }
    }

    /**
     * 路线tag选中设置
     *
     * @param lineOne
     * @param lineTwo
     * @param lineThree
     */
    private void focuseRouteLine(boolean lineOne, boolean lineTwo, boolean lineThree) {
        Log.d("LG", "lineOne:" + lineOne + " lineTwo:" + lineTwo + " lineThree:" + lineThree);
        setLinelayoutOne(lineOne);
        setLinelayoutTwo(lineTwo);
        setLinelayoutThree(lineThree);
    }

    /**
     * 地图实时交通开关
     */
    private void setTraffic() {
        if (mAMap.isTrafficEnabled()) {
            mImageTraffic.setImageResource(R.drawable.map_traffic_white);
            mAMap.setTrafficEnabled(false);
        } else {
            mImageTraffic.setImageResource(R.drawable.map_traffic_hl_white);
            mAMap.setTrafficEnabled(true);
        }
    }

    private void cleanRouteOverlay() {
        for (int i = 0; i < routeOverlays.size(); i++) {
            int key = routeOverlays.keyAt(i);
            RouteOverLay overlay = routeOverlays.get(key);
            overlay.removeFromMap();
            overlay.destroy();
        }
        routeOverlays.clear();
    }

    /**
     * 跳转到驾车偏好设置页面
     */
    private void strategyChoose() {
        Intent intent = new Intent(this, StrategyChooseActivity.class);
        intent.putExtra(LocationUtils.INTENT_NAME_AVOID_CONGESTION, mStrategyBean.isCongestion());
        intent.putExtra(LocationUtils.INTENT_NAME_AVOID_COST, mStrategyBean.isCost());
        intent.putExtra(LocationUtils.INTENT_NAME_AVOID_HIGHSPEED, mStrategyBean.isAvoidhightspeed());
        intent.putExtra(LocationUtils.INTENT_NAME_PRIORITY_HIGHSPEED, mStrategyBean.isHightspeed());
        startActivityForResult(intent, LocationUtils.START_ACTIVITY_REQUEST_CODE);
    }

    /**
     * @param paths 多路线回调路线
     * @param ints  多路线回调路线ID
     */
    private void setRouteLineTag(HashMap<Integer, AMapNaviPath> paths, int[] ints) {
        if (ints.length < 1) {
            visiableRouteLine(false, false, false);
            return;
        }
        int indexOne = 0;
        String stragegyTagOne = paths.get(ints[indexOne]).getLabels();
        setLinelayoutOneContent(ints[indexOne], stragegyTagOne);
        if (ints.length == 1) {
            visiableRouteLine(true, false, false);
            focuseRouteLine(true, false, false);
            return;
        }

        int indexTwo = 1;
        String stragegyTagTwo = paths.get(ints[indexTwo]).getLabels();
        setLinelayoutTwoContent(ints[indexTwo], stragegyTagTwo);
        if (ints.length == 2) {
            visiableRouteLine(true, true, false);
            focuseRouteLine(true, false, false);
            return;
        }

        int indexThree = 2;
        String stragegyTagThree = paths.get(ints[indexThree]).getLabels();
        setLinelayoutThreeContent(ints[indexThree], stragegyTagThree);
        if (ints.length >= 3) {
            visiableRouteLine(true, true, true);
            focuseRouteLine(true, false, false);
        }

    }

    private void visiableRouteLine(boolean lineOne, boolean lineTwo, boolean lineThree) {
        setLinelayoutOneVisiable(lineOne);
        setLinelayoutTwoVisiable(lineTwo);
        setLinelayoutThreeVisiable(lineThree);
    }

    private void setLinelayoutOneVisiable(boolean visiable) {
        if (visiable) {
            mRouteLineLayoutOne.setVisibility(View.VISIBLE);
        } else {
            mRouteLineLayoutOne.setVisibility(View.GONE);
        }
    }

    private void setLinelayoutTwoVisiable(boolean visiable) {
        if (visiable) {
            mRouteLinelayoutTwo.setVisibility(View.VISIBLE);
        } else {
            mRouteLinelayoutTwo.setVisibility(View.GONE);
        }
    }

    private void setLinelayoutThreeVisiable(boolean visiable) {
        if (visiable) {
            mRouteLineLayoutThree.setVisibility(View.VISIBLE);
        } else {
            mRouteLineLayoutThree.setVisibility(View.GONE);
        }
    }

    /**
     * 设置第一条线路Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutOneContent(int routeID, String strategy) {
        mRouteLineLayoutOne.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        overlay.zoomToSpan();
        AMapNaviPath path = overlay.getAMapNaviPath();
        mRouteTextStrategyOne.setText(strategy);
        String timeDes = LocationUtils.getFriendlyTime(path.getAllTime());
        mRouteTextTimeOne.setText(timeDes);
        String disDes = LocationUtils.getFriendlyDistance(path.getAllLength());
        mRouteTextDistanceOne.setText(disDes);
    }

    /**
     * 设置第二条路线Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutTwoContent(int routeID, String strategy) {
        mRouteLinelayoutTwo.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        AMapNaviPath path = overlay.getAMapNaviPath();
        mRouteTextStrategyTwo.setText(strategy);
        String timeDes = LocationUtils.getFriendlyTime(path.getAllTime());
        mRouteTextTimeTwo.setText(timeDes);
        String disDes = LocationUtils.getFriendlyDistance(path.getAllLength());
        mRouteTextDistanceTwo.setText(disDes);
    }

    /**
     * 设置第三条路线Tab 内容
     *
     * @param routeID  路线ID
     * @param strategy 策略标签
     */
    private void setLinelayoutThreeContent(int routeID, String strategy) {
        mRouteLineLayoutThree.setTag(routeID);
        RouteOverLay overlay = routeOverlays.get(routeID);
        AMapNaviPath path = overlay.getAMapNaviPath();
        mRouteTextStrategyThree.setText(strategy);
        String timeDes = LocationUtils.getFriendlyTime(path.getAllTime());
        mRouteTextTimeThree.setText(timeDes);
        String disDes = LocationUtils.getFriendlyDistance(path.getAllLength());
        mRouteTextDistanceThree.setText(disDes);
    }

    /**
     * 第一条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutOne(boolean focus) {
        if (mRouteLineLayoutOne.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            RouteOverLay overlay = routeOverlays.get((int)mRouteLineLayoutOne.getTag());
            if (focus) {
                routeID = (int) mRouteLineLayoutOne.getTag();
                mCalculateRouteOverView.setText(LocationUtils.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                mRouteViewOne.setVisibility(View.VISIBLE);
                mRouteTextStrategyOne.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextTimeOne.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextDistanceOne.setTextColor(getResources().getColor(R.color.colorBlue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                mRouteViewOne.setVisibility(View.INVISIBLE);
                mRouteTextStrategyOne.setTextColor(getResources().getColor(R.color.colorDark));
                mRouteTextTimeOne.setTextColor(getResources().getColor(R.color.colorBlack));
                mRouteTextDistanceOne.setTextColor(getResources().getColor(R.color.colorDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第二条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutTwo(boolean focus) {
        if (mRouteLinelayoutTwo.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            RouteOverLay overlay = routeOverlays.get((int) mRouteLinelayoutTwo.getTag());
            if (focus) {
                routeID = (int) mRouteLinelayoutTwo.getTag();
                mCalculateRouteOverView.setText(LocationUtils.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                mRouteViewTwo.setVisibility(View.VISIBLE);
                mRouteTextStrategyTwo.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextTimeTwo.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextDistanceTwo.setTextColor(getResources().getColor(R.color.colorBlue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                mRouteViewTwo.setVisibility(View.INVISIBLE);
                mRouteTextStrategyTwo.setTextColor(getResources().getColor(R.color.colorDark));
                mRouteTextTimeTwo.setTextColor(getResources().getColor(R.color.colorBlack));
                mRouteTextDistanceTwo.setTextColor(getResources().getColor(R.color.colorDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 第三条路线是否focus
     *
     * @param focus focus为true 突出颜色显示，标示为选中状态，为false则标示非选中状态
     */
    private void setLinelayoutThree(boolean focus) {
        if (mRouteLineLayoutThree.getVisibility() != View.VISIBLE) {
            return;
        }
        try {
            RouteOverLay overlay = routeOverlays.get((int) mRouteLineLayoutThree.getTag());
            if (overlay == null) {
                return;
            }
            if (focus) {
                routeID = (int) mRouteLineLayoutThree.getTag();
                mCalculateRouteOverView.setText(LocationUtils.getRouteOverView(overlay.getAMapNaviPath()));
                mAMapNavi.selectRouteId(routeID);
                overlay.setTransparency(ROUTE_SELECTED_TRANSPARENCY);
                mRouteViewThree.setVisibility(View.VISIBLE);
                mRouteTextStrategyThree.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextTimeThree.setTextColor(getResources().getColor(R.color.colorBlue));
                mRouteTextDistanceThree.setTextColor(getResources().getColor(R.color.colorBlue));
            } else {
                overlay.setTransparency(ROUTE_UNSELECTED_TRANSPARENCY);
                mRouteViewThree.setVisibility(View.INVISIBLE);
                mRouteTextStrategyThree.setTextColor(getResources().getColor(R.color.colorDark));
                mRouteTextTimeThree.setTextColor(getResources().getColor(R.color.colorBlack));
                mRouteTextDistanceThree.setTextColor(getResources().getColor(R.color.colorDark));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
        if (mAMapNavi != null) {
            mAMapNavi.destroy();
        }
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {

    }

    @Override
    public void onStartNavi(int i) {

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {

    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {
        Toast.makeText(this.getApplicationContext(),"错误码"+i,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        cleanRouteOverlay();
        HashMap<Integer, AMapNaviPath> paths = mAMapNavi.getNaviPaths();
        for (int i = 0; i < ints.length; i++) {
            AMapNaviPath path = paths.get(ints[i]);
            if (path != null) {
                drawRoutes(ints[i], path);
            }
        }
        setRouteLineTag(paths, ints);
        mAMap.setMapType(AMap.MAP_TYPE_NAVI);
    }

    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onMapLoaded() {
        calculateDriveRoute();
    }
}
