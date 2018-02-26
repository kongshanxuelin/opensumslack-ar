package com.sumslack.opensource.weex.module;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baoyz.actionsheet.ActionSheet;
import com.google.gson.Gson;
import com.google.zxing.common.StringUtils;
import com.sumslack.opensource.BaseActivity;
import com.sumslack.opensource.IndexActivity;
import com.sumslack.opensource.eventbus.MsgEvent;
import com.sumslack.opensource.qrcode.QrCodeIndexActivity;
import com.sumslack.opensource.utils.StrUtil;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.sumslack.opensource.NetworkActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Administrator on 2018/2/26/026.
 */

public class SumslackModule extends WXModule{
    private static final String TAG = "SumslackModule";

    final public static int TAKE_PHOTO = 100;
    final public static int CHOOSE_PHOTO = 101;

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

    @JSMethod(uiThread = true)
    public void refresh(){

    }

    @JSMethod(uiThread = true)
    public void scan(JSCallback callback){
        Intent cameraIntent = new Intent(mWXSDKInstance.getContext(), QrCodeIndexActivity.class);
        mWXSDKInstance.getContext().startActivity(cameraIntent);

        Map<String,Object> param = new HashMap<>();
        param.put("ret","hello,测试");
        callback.invoke(param);
    }

    @JSMethod(uiThread = true)
    public void layoutNaviBar(){

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
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            getActivity().startActivityForResult(intent,CHOOSE_PHOTO);
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
            getActivity().startActivityForResult(intent,TAKE_PHOTO);
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
    @JSMethod(uiThread = false)
    public void redirectTo(String url){
        if(url.startsWith("file://assets")){
            url = url.substring("file://assets".length());
        }
        Intent intent = new Intent(mWXSDKInstance.getContext(), NetworkActivity.class);
        url = getBaseUrl() + url;
        intent.putExtra("url",url);
        mWXSDKInstance.getContext().startActivity(intent);
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
        switch(requestCode){
            case TAKE_PHOTO:
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
            case CHOOSE_PHOTO:
                if(resultCode == getActivity().RESULT_OK){
                    if(Build.VERSION.SDK_INT >= 19){
                        handleImageOnKitKat(data);
                    }else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
        }
        if(!uuid.equals("")){
            callbackMap.remove(uuid);
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


}
