package com.sumslack.opensource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.sumslack.opensource.location.TipAdapter;
import com.sumslack.opensource.utils.NavUtil;
import com.sumslack.opensource.utils.StrUtil;
import com.sumslack.opensource.utils.SumslackUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements TextWatcher,Inputtips.InputtipsListener,AdapterView.OnItemClickListener {
    MapView mMapView = null;
    private double lat;
    private double lot;
    private String uuid; //回调函数的uuid
    private AMap aMap;
    private AutoCompleteTextView searchText;// 输入搜索关键字
    private List<Tip> listString = new ArrayList<Tip>();

    private List<LocListviewItem> searchResultList = new ArrayList<>();
    private ListView searchResultListView;
    private LocListviewItem selectLocItem;

    class LocListviewItem {
        private String name;
        private String address;
        private PoiItem poiItem;

        public LocListviewItem(String name,String address,PoiItem poiItem){
            this.name = name;
            this.address = address;
            this.poiItem = poiItem;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public PoiItem getPoiItem() {
            return poiItem;
        }

        public void setPoiItem(PoiItem poiItem) {
            this.poiItem = poiItem;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        lat = getIntent().getExtras().getDouble("lat");
        lot = getIntent().getExtras().getDouble("lot");
        uuid = StrUtil.formatMullStr(getIntent().getExtras().get("uuid"));
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        init();
    }
    MyAdapter myAdapter = null;
    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        searchResultListView = (ListView)findViewById(R.id.locListView);
        myAdapter = new MyAdapter(this,searchResultList);

        searchResultListView.setAdapter(myAdapter);
        searchResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //获取选中的参数
                selectLocItem = searchResultList.get(position);
                myAdapter.setSelectedPosition(position);
                //选中某个定位到那里
                if(selectLocItem!=null) {
                    locLocation(selectLocItem.getPoiItem().getLatLonPoint().getLatitude(), selectLocItem.getPoiItem().getLatLonPoint().getLongitude());
                    searchText.setText(selectLocItem.getName());
                }
            }
        });

        Button btnSelected = (Button)findViewById(R.id.keyWordBtn);
        Button btnCancelled = (Button)findViewById(R.id.keyWordCancel);
        searchText = (AutoCompleteTextView)findViewById(R.id.keyWord);
        searchText.addTextChangedListener(this);// 添加文本输入框监听事件

        btnCancelled.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSelected.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent result = new Intent();
                if(selectLocItem!=null) {
                    result.putExtra("lat", selectLocItem.getPoiItem().getLatLonPoint().getLatitude());
                    result.putExtra("lot", selectLocItem.getPoiItem().getLatLonPoint().getLongitude());
                    result.putExtra("province", selectLocItem.getPoiItem().getProvinceName());
                    result.putExtra("city", selectLocItem.getPoiItem().getCityName());
                    result.putExtra("address", selectLocItem.getAddress());
                    result.putExtra("uuid",uuid);
                    MapActivity.this.setResult(RESULT_OK,result);
                    MapActivity.this.finish();
                }else{
                    SumslackUtil.toast("请选择一个位置");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
        locLocation(this.lat,this.lot);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {
        aMap.moveCamera(update);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        BaseActivity index = (BaseActivity)getIntent();
//        if(index!=null){
//            index.onActivityResult(requestCode,resultCode,data);
//        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    //TextWatch
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String newText = s.toString().trim();
        if (!StrUtil.formatMullStr(newText).equals("")) {
            InputtipsQuery inputquery = new InputtipsQuery(newText,"宁波" );
            Inputtips inputTips = new Inputtips(MapActivity.this, inputquery);
            inputTips.setInputtipsListener(this);
            inputTips.requestInputtipsAsyn();
        }
    }

    @Override
    public void onGetInputtips(List<Tip> tipList, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {// 正确返回
            listString.clear();
            for (int i = 0; i < tipList.size(); i++) {
                listString.add(tipList.get(i));
            }
            TipAdapter aAdapter = new TipAdapter(listString,getApplicationContext());
            searchText.setAdapter(aAdapter);
            //searchText.setThreshold(1);
            searchText.setOnItemClickListener(this);
            aAdapter.notifyDataSetChanged();
        } else {
            NavUtil.showerror(MapActivity.this, rCode);

        }
    }
    private int currentPage = 0;
    private LatLonPoint latLonPoint;
    private PoiSearch.SearchBound searchBound;
    private PoiResult poiResults;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Tip tip = listString.get(position);
        if(tip!=null){
            //Toast.makeText(MapActivity.this,tip.getName(),Toast.LENGTH_LONG).show();
            myAdapter.setSelectedPosition(0);
            searchText.setText(tip.getName());
            if(tip.getPoint() == null) return;
            locLocation(tip.getPoint().getLatitude(),tip.getPoint().getLongitude());
            if(latLonPoint == null){
                latLonPoint = tip.getPoint();
            }else{
                latLonPoint.setLatitude(tip.getPoint().getLatitude());
                latLonPoint.setLongitude(tip.getPoint().getLongitude());
            }

            //根据当前选定的位置查找周边地址备选

            currentPage = 0;
            PoiSearch.Query query=new PoiSearch.Query(searchText.getText().toString(),"");
            query.setPageNum(currentPage);
            query.setPageSize(10);
            PoiSearch poiSearch = new PoiSearch(this,query);
            poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener(){
                @Override
                public void onPoiItemSearched(PoiItem poiItem, int i) {

                }

                @Override
                public void onPoiSearched(PoiResult poiResult, int i) {
                    if(i == 1000){
                        if (poiResult != null && poiResult.getQuery() != null) {
                            if (poiResult.getQuery().equals(query)) {
                                poiResults = poiResult;
                                List<PoiItem> poiItems = poiResult.getPois();
                                List<SuggestionCity> suggestionCities = poiResult
                                        .getSearchSuggestionCitys();
                                if (poiItems != null && poiItems.size() > 0) {
                                    aMap.clear();// 清理之前的图标
                                    searchResultList.clear();
                                    for(PoiItem item : poiItems){
                                        String _addr = item.getCityName() + item.getAdName() + item.getSnippet();
                                        searchResultList.add(new LocListviewItem(item.getTitle(),_addr,item));
                                    }
                                    myAdapter.notifyDataSetChanged();
                                } else if (suggestionCities != null
                                        && suggestionCities.size() > 0) {
                                    //showSuggestCity(suggestionCities);
                                    Toast.makeText(MapActivity.this, "当前城市下未找到结果",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MapActivity.this, "未找到结果",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }else{
                        Toast.makeText(MapActivity.this,"未找到结果",Toast.LENGTH_LONG).show();
                    }
                }
            });//设置回调数据的监听器
            if(latLonPoint!=null){
                searchBound = new PoiSearch.SearchBound(latLonPoint,2000);
                poiSearch.setBound(searchBound);
            }
            poiSearch.searchPOIAsyn();//开始搜索


        }
    }

    private void locLocation(double _lat, double _lot){
        //设定当前位置
        changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(_lat, _lot),18,20,0)));
        aMap.clear();
        aMap.addMarker(new MarkerOptions().position(new LatLng(_lat, _lot)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
    }


    public class MyAdapter extends BaseAdapter {
        Context context;
        List<LocListviewItem> mList;
        private int selectedPosition = 0;
        LayoutInflater mInflater;

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        public MyAdapter(Context context,List<LocListviewItem> mList){
            this.context = context;
            this.mList = mList;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder = null;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.listview_loc_nearresult,parent,false);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.bindView(position);
            return convertView;
        }


        public class ViewHolder{
            TextView name;
            TextView address;
            ImageView imageCheck;

            public ViewHolder(View view) {
                name = (TextView) view.findViewById(R.id.listview_loc_nearresult_name);
                address = (TextView) view.findViewById(R.id.listview_loc_nearresult_address);
                imageCheck = (ImageView) view.findViewById(R.id.id_gaode_location_search_confirm_icon);
            }

            public void bindView(int position) {
                if (position >= searchResultList.size())
                    return;
                LocListviewItem item = searchResultList.get(position);
                PoiItem poiItem = item.getPoiItem();
                name.setText(poiItem.getTitle());
                address.setText(poiItem.getCityName() + poiItem.getAdName() + poiItem.getSnippet());
                imageCheck.setVisibility(position == selectedPosition ? View.VISIBLE : View.INVISIBLE);
                address.setVisibility(View.VISIBLE);
            }
        }
    }


}
