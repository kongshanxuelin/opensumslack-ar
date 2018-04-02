package com.sumslack.opensource.weex.module;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.baoyz.actionsheet.ActionSheet;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.bean.ImageItem;
import com.sumslack.opensource.BaseActivity;
import com.sumslack.opensource.MapActivity;
import com.sumslack.opensource.NetworkActivity;
import com.sumslack.opensource.WXApplication;
import com.sumslack.opensource.image.MultiPreviewActivity;
import com.sumslack.opensource.location.CalculateRouteActivity;
import com.sumslack.opensource.location.LocationUtils;
import com.sumslack.opensource.qrcode.QrCodeIndexActivity;
import com.sumslack.opensource.utils.StrUtil;
import com.sumslack.opensource.utils.SumslackUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

import static com.taobao.weex.WXSDKInstance.requestUrl;

/**
 * Created by Administrator on 2018/2/26/026.
 */

public class SumslackModule extends WXModule{
    private static final String TAG = "SumslackModule";

    final public static int REQ_RESULT_TAKE_PHOTO = 100;
    final public static int REQ_RESULT_CHOOSE_PHOTO = 101;
    final public static int REQ_RESULT_SCAN = 102;
    final public static int REQ_RESULT_LOCSELECTED=103;

    final public static Map<String,JSCallback> callbackMap = new HashMap<>();

    public BaseActivity getActivity(){
        return (BaseActivity)mWXSDKInstance.getContext();
    }



    private Uri imageUri;//拍照
    /*
        关闭当前页
     */
    @JSMethod(uiThread = true)
    public void popViewController()
    {
        getActivity().finish();
    }

    /**
     * 刷新Weex页面
     */
    @JSMethod(uiThread = true)
    public void refresh(){
        BaseActivity act = getActivity();
        if(act!=null && act instanceof NetworkActivity){
            ((NetworkActivity)act).refresh();
        }
    }

    /**
     * 扫一扫
     */
    @JSMethod(uiThread = true)
    public void scan(JSCallback callback){
        Intent cameraIntent = new Intent(mWXSDKInstance.getContext(), QrCodeIndexActivity.class);
        getActivity().startActivityForResult(cameraIntent,REQ_RESULT_SCAN);

        uuid = UUID.randomUUID().toString();
        callbackMap.put(uuid,callback);

    }

    /**
     * 获取当前位置
     * @param callback
     */
    @JSMethod(uiThread = true)
    public void getLocation(JSCallback callback){
        LocationUtils.getCurrentLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                double lat = aMapLocation.getLatitude();
                double lot = aMapLocation.getLongitude();
                String subLocality = aMapLocation.getDistrict();
                String country = aMapLocation.getCountry();
                String city = aMapLocation.getCity();
                String thoroughface = aMapLocation.getAddress();
                String state = aMapLocation.getProvince();
                JSONObject json = new JSONObject();
                json.put("lat",lat);
                json.put("lot",lot);
                json.put("subLocality",subLocality);
                json.put("country",country);
                json.put("city",city);
                json.put("state",state);
                json.put("thoroughface",thoroughface);
                callback.invoke(json);

                LocationUtils.locationDestroy();

            }
        });
    }

    /**
     * 选择地理位置
     * @param callback
     */
    @JSMethod(uiThread = true)
    public void chooseLocation(JSCallback callback){
        LocationUtils.getCurrentLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                double lat = aMapLocation.getLatitude();
                double lot = aMapLocation.getLongitude();
                Intent intent = new Intent(getActivity(), MapActivity.class);
                intent.putExtra("lat",lat);
                intent.putExtra("lot",lot);
                uuid = UUID.randomUUID().toString();
                intent.putExtra("uuid",uuid);
                callbackMap.put(uuid,callback);
                getActivity().startActivityForResult(intent,REQ_RESULT_LOCSELECTED);

                LocationUtils.locationDestroy();
            }
        });


    }

    /*
    * 根据目的地导航
    * param:json(lat,lot,building,address
    * */
    @JSMethod(uiThread = true)
    public void openLocation(String param){
        LocationUtils.getCurrentLocation(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                double lat = aMapLocation.getLatitude();
                double lot = aMapLocation.getLongitude();
                String addr = StrUtil.formatMullStr(aMapLocation.getAddress());
                final JSONObject p = JSON.parseObject(param);
                try {
                    double lat2 = Double.parseDouble(StrUtil.formatMullStr(p.get("lat")));
                    double lot2 = Double.parseDouble(StrUtil.formatMullStr(p.get("lot")));
                    Intent navIntent = new Intent(getActivity(), CalculateRouteActivity.class);
                    navIntent.putExtra("lat",lat);
                    navIntent.putExtra("lot",lot);
                    navIntent.putExtra("lat2",lat2);
                    navIntent.putExtra("lot2",lot2);
                    navIntent.putExtra("addr",addr);

                    getActivity().startActivity(navIntent);

                    LocationUtils.locationDestroy();
                }catch(Exception ex){
                    ex.printStackTrace();
                    SumslackUtil.toast("传入参数不对!");
                }
            }
        });

    }

    @JSMethod(uiThread = true)
    public void showActionSheet(String param,final JSCallback callback){
        JSONObject json = JSON.parseObject(param);
        if(json!=null){
            JSONArray items = json.getJSONArray("itemList");
            if(items!=null && items.size()>0){
                ActionSheet.createBuilder(mWXSDKInstance.getContext(),getActivity().getSupportFragmentManager())
                        .setCancelButtonTitle("Cancel")
                        .setOtherButtonTitles("Item1", "Item2", "Item3", "Item4")
                        .setCancelableOnTouchOutside(true)
                        .setListener(new ActionSheet.ActionSheetListener(){
                            @Override
                            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
                                JSONObject ret = new JSONObject();
                                ret.put("ret",-1);
                                callback.invoke(ret);
                            }
                            @Override
                            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                                JSONObject ret = new JSONObject();
                                ret.put("ret",index);
                                callback.invoke(ret);
                            }
                        }).show();
            }
        }
    }

    private String uuid;

    @JSMethod(uiThread = true)
    public void chooseImage(int count,int sourceType,JSCallback callback){
        uuid = UUID.randomUUID().toString();
        callbackMap.put(uuid,callback);
        if(sourceType == 0){ //相册选择
//            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//            intent.setType("image/*");
//            getActivity().startActivityForResult(intent,REQ_RESULT_CHOOSE_PHOTO);

            AndroidImagePicker.getInstance().setSelectLimit(count).pickMulti(getActivity(), true, new AndroidImagePicker.OnImagePickCompleteListener() {
                @Override
                public void onImagePickComplete(List<ImageItem> items) {
                    List<String> imageList = new ArrayList<>();
                    if(items != null && items.size() > 0){
                        for(ImageItem item : items){
                            imageList.add(item.path);
                        }
                    }
                    Map retMap = new HashMap();
                    retMap.put("list",imageList);
                    callback.invoke(retMap);
                }
            });

        }else if(sourceType == 1){ //拍照选择
            File outputImage = new File(getActivity().getExternalCacheDir(),"temp.jpg");
            try{
                if(outputImage.exists()){
                    outputImage.delete();
                }
                outputImage.createNewFile();
            }catch(IOException e){
                e.printStackTrace();
            }
            if(Build.VERSION.SDK_INT>=24){
                imageUri = FileProvider.getUriForFile(getActivity(),"com.weex.sample.fileprovider",outputImage);
            }else{
                imageUri = Uri.fromFile(outputImage);
            }
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            getActivity().startActivityForResult(intent,REQ_RESULT_TAKE_PHOTO);
        }

    }

    @JSMethod(uiThread = true)
    public void makePhoneCall(String param){
        JSONObject json = JSON.parseObject(param);
        if(json!=null){
            String phoneNumber = json.getString("phoneNumber");
            if(phoneNumber!=null){
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+phoneNumber));
                try{
                    mWXSDKInstance.getContext().startActivity(intent);
                }catch(SecurityException sec){
                    sec.printStackTrace();
                }
            }
        }
    }

    @JSMethod(uiThread = false)
    public void navigateTo(String url){
        if(url.startsWith("file://assets")){
            url = url.substring("file://assets".length());
        }
        Intent intent = new Intent(mWXSDKInstance.getContext(), NetworkActivity.class);
        url = getBaseUrl() + url;
        Log.d(TAG,"url:" +url);
        intent.putExtra("url",url);
        mWXSDKInstance.getContext().startActivity(intent);
    }


    @JSMethod(uiThread = true)
    public void layoutNaviBar(String param){
        BaseActivity baseActivity = getActivity();
        if(baseActivity instanceof  NetworkActivity){
            NetworkActivity act = (NetworkActivity)baseActivity;
            if(act!=null){
                act.setLayoutMenus(param);
            }
        }
    }

    @JSMethod(uiThread = true)
    public void setNavigationBarTitle(String title){
        BaseActivity baseActivity = getActivity();
        if(baseActivity instanceof  NetworkActivity){
            NetworkActivity act = (NetworkActivity)baseActivity;
            if(act!=null){
                act.setNavigationBarTitle(title);
            }
        }
    }

    @JSMethod(uiThread = false)
    public void redirectTo(String url){
        if(url.startsWith("file://assets")){
            url = url.substring("file://assets".length());
        }
        if(getActivity()!=null && getActivity() instanceof  NetworkActivity)
            getActivity().finish();
        Intent intent = new Intent(WXApplication.getContext(), NetworkActivity.class);
        url = getBaseUrl() + url;
        intent.putExtra("url",url);
        WXApplication.getContext().startActivity(intent);
    }

    @JSMethod(uiThread = true)
    public void downloadFile(String url,JSCallback callback){
        BaseActivity baseActivity = getActivity();
        if(baseActivity instanceof  NetworkActivity){
            NetworkActivity act = (NetworkActivity)baseActivity;
            if(act!=null){
                act.downloadFile(url,callback);
            }
        }
    }

    private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/xml;charset=utf-8");
    @JSMethod(uiThread = true)
    public void uploadFile(String filePath,JSCallback callback){
        OkHttpClient mOkHttpClient = new OkHttpClient();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        /*如果需要追加参数
        builder.addFormDataPart(key, object.toString());
        */
        File file = new File(filePath);
        builder.addFormDataPart("file", file.getName(), createProgressRequestBody(MEDIA_OBJECT_STREAM, file, callback));

        RequestBody body = builder.build();
        final Request request = new Request.Builder().url("http://www.sumslack.com/dfs-api/file/upload").post(body).build();
        final Call call = mOkHttpClient.newBuilder().writeTimeout(50, TimeUnit.SECONDS).build().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Map progressMap = new HashMap();
                progressMap.put("status","fail");
                progressMap.put("info","上传失败："+e.getMessage());
                callback.invoke(progressMap);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String string = response.body().string();
                    Log.e(TAG, "response ----->" + string);
                    Map progressMap = new HashMap();
                    progressMap.put("status","success");
                    progressMap.put("info","上传成功");
                    String resp = StrUtil.formatMullStr(response.body().string());
                    progressMap.put("remoteUrl","");
                    progressMap.put("remoteThumbUrl","");
                    callback.invoke(progressMap);
                } else {
                    Map progressMap = new HashMap();
                    progressMap.put("status","fail");
                    progressMap.put("info","上传失败!");
                    callback.invoke(progressMap);
                }
            }
        });
    }

    @JSMethod(uiThread = true)
    public void previewImage(String param){
        JSONObject json = JSON.parseObject(param);
        String url = StrUtil.formatMullStr(json.get("current"));
        JSONArray a = json.getJSONArray("urls");
        String[] urls = new String[a.size()];
        for(int i=0;i<a.size();i++){
            urls[i] = a.getString(i);
        }
        Intent intent = new Intent(getActivity(), MultiPreviewActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("urls",urls);
        getActivity().startActivity(intent);
    }

    private String getBaseUrl(){
        return mWXSDKInstance.getBundleUrl().substring(0,mWXSDKInstance.getBundleUrl().lastIndexOf("/"));
    }

    @Override
    public void onActivityStart() {
        super.onActivityStart();
    }

    @Override
    public void onActivityStop() {
        super.onActivityStop();
    }
//    @Subscribe(threadMode = ThreadMode.POSTING)
//    public void onMessageEvent(MsgEvent event)
//    {
//        if(event!=null){
//            switch (event.getAction()){
//                case MsgEvent.Action.QrCode_Camera:
//                    Map<String,Object> params = new HashMap<>();
//                    params.put("bundle",event.getParams());
//                    mWXSDKInstance.fireModuleEvent("test",this,params);
//                    break;
//            }
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK ){
            switch(requestCode){
                case REQ_RESULT_LOCSELECTED:
                    String cuuid = StrUtil.formatMullStr(data.getExtras().get("uuid"));
                    Map addressMap = new HashMap();
                    addressMap.put("lat",data.getExtras().getDouble("lat"));
                    addressMap.put("lot",data.getExtras().getDouble("lot"));
                    addressMap.put("province",StrUtil.formatMullStr(data.getExtras().getString("province")));
                    addressMap.put("city",StrUtil.formatMullStr(data.getExtras().getString("city")));
                    addressMap.put("address",StrUtil.formatMullStr(data.getExtras().getString("address")));
                    callbackInvoke(cuuid,addressMap);
                    break;
                case REQ_RESULT_TAKE_PHOTO:
                    if(resultCode == getActivity().RESULT_OK){
                        try{
                            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                            List<String> imageList = new ArrayList<>();
                            imageList.add(imageUri.getPath());
                            Map retMap = new HashMap();
                            retMap.put("list",imageList);
                            callbackInvoke(uuid,retMap);
                        }catch(FileNotFoundException ex){
                            ex.printStackTrace();
                        }
                    }
                    break;
                case REQ_RESULT_CHOOSE_PHOTO:
                    if(resultCode == getActivity().RESULT_OK){
                        if(Build.VERSION.SDK_INT >= 19){
                            handleImageOnKitKat(data);
                        }else{
                            handleImageBeforeKitKat(data);
                        }
                    }
                    break;
                case REQ_RESULT_SCAN:
                    Bundle bundle = data.getExtras();
                    if(bundle == null){
                        return;
                    }
                    if(bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                        String result = bundle.getString(CodeUtils.RESULT_STRING);
                        if(result!=null){
                            Map retMap = new HashMap();
                            retMap.put("ret",result);
                            callbackInvoke(uuid,retMap);
                        }else{
                            Map retMap = new HashMap();
                            retMap.put("ret","SCAN CANCELLED.");
                            callbackInvoke(uuid,retMap);
                        }
                    }else{
                        Map retMap = new HashMap();
                        retMap.put("ret","SCAN FAILED.");
                        callbackInvoke(uuid,retMap);
                    }
                    break;
            }
            if(!StrUtil.formatMullStr(uuid).equals("")){
                callbackMap.remove(uuid);
            }
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(getActivity(),uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }
        Log.d(TAG,"imagePath19:"+imagePath);
        List<String> imageList = new ArrayList<>();
        imageList.add(imagePath);
        Map retMap = new HashMap();
        retMap.put("list",imageList);
        callbackInvoke(uuid,retMap);
    }

    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        Log.d(TAG,"imagePath:"+imagePath);
        List<String> imageList = new ArrayList<>();
        imageList.add(imagePath);
        Map retMap = new HashMap();
        retMap.put("list",imageList);
        callbackInvoke(uuid,retMap);
    }

    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getActivity().getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void callbackInvoke(String cb_id,Object param){
        if(cb_id == null || cb_id.equals("")) return;
        JSCallback callback = callbackMap.get(cb_id);
        if(callback!=null){
            callback.invoke(param);
            callbackMap.remove(cb_id);
        }
    }

    private RequestBody createProgressRequestBody(final MediaType contentType, final File file, final JSCallback callBack) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return contentType;
            }

            @Override
            public long contentLength() {
                return file.length();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                Source source;
                try {
                    source = Okio.source(file);
                    Buffer buf = new Buffer();
                    long remaining = contentLength();
                    long current = 0;
                    for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
                        sink.write(buf, readCount);
                        current += readCount;
                        Log.e(TAG, "current------>" + current);
                        Map progressMap = new HashMap();
                        progressMap.put("status","progress");
                        progressMap.put("bytesWritten",current);
                        progressMap.put("totalSize",remaining);
                        callBack.invoke(progressMap);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    @Override
    public void onActivityCreate() {
        super.onActivityCreate();
    }

    @Override
    public void onActivityDestroy() {
        AndroidImagePicker.getInstance().onDestroy();
        super.onActivityDestroy();

    }
}
