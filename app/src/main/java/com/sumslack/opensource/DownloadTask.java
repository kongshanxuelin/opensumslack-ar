package com.sumslack.opensource;

import android.os.AsyncTask;
import android.os.Environment;

import com.taobao.weex.bridge.JSCallback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/23/023.
 */

public class DownloadTask extends AsyncTask<String,Integer,Integer> {
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PARSED = 2;
    public static final int TYPE_CANCELED = 3;
    private DownloadListener listener;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private JSCallback callback;

    private void callJSFuncProgress(long bytesDownloaded,long totalSize ){
        Map paramMap = new HashMap();
        paramMap.put("status","progress");
        paramMap.put("bytesDownloaded",bytesDownloaded);
        paramMap.put("totalSize",totalSize);
        if(callback!=null)
            callback.invokeAndKeepAlive(paramMap);
    }
    private void callJSFuncSuccess(String info,String tempFilePath){
        Map paramMap = new HashMap();
        paramMap.put("status","success");
        paramMap.put("info",info);
        paramMap.put("tempFilePath",tempFilePath);
        if(callback!=null)
            callback.invokeAndKeepAlive(paramMap);
    }
    private void callJSFuncFail(String info){
        Map paramMap = new HashMap();
        paramMap.put("status","fail");
        paramMap.put("info",info);
        if(callback!=null)
            callback.invokeAndKeepAlive(paramMap);
    }

    public DownloadTask(DownloadListener listener, JSCallback callback){
        this.listener = listener;
        this.callback = callback;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if(progress>lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status){
            case TYPE_SUCCESS:
                listener.onSuccess();
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_PARSED:
                listener.onParsed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
        }
    }

    public void parseDownload(){
        isPaused = true;
    }

    public void cancelDownload(){
        isCanceled = true;
    }

    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile savedFile = null;
        File file = null;
        try{
            long downloadedLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            file = new File(directory + fileName);
            if(file.exists()){
                downloadedLength = file.length();
            }
            long contentLenth = getContentLength(downloadUrl);
            if(contentLenth ==0){
                return TYPE_FAILED;
            }else if(contentLenth == downloadedLength){
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().addHeader("RANGE","bytes="+downloadedLength+"-").url(downloadUrl).build();
            Response response = client.newCall(request).execute();
            if(response!=null){
                is = response.body().byteStream();
                savedFile = new RandomAccessFile(file,"rw");
                savedFile.seek(downloadedLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while((len=is.read(b))!=-1){
                    if(isCanceled){
                        callJSFuncFail("下载被取消");
                        return TYPE_CANCELED;
                    }else if(isPaused){
                        callJSFuncFail("下载被暂停");
                        return TYPE_PARSED;
                    }else{
                        total += len;
                        savedFile.write(b,0,len);
                        int progress = (int) ((total+downloadedLength)*100 / contentLenth);
                        publishProgress(progress);
                        callJSFuncProgress(downloadedLength,contentLenth);
                    }
                }
                response.body().close();
                callJSFuncSuccess("下载成功",directory + fileName);
                return TYPE_SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
            callJSFuncFail(e.getMessage());
        }finally {
            try{
                if(is!=null){
                    is.close();
                }
                if(savedFile!=null){
                    savedFile.close();
                }
                if(isCanceled && file!=null){
                    file.delete();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    private long getContentLength(String downloadUrl) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(downloadUrl).build();
        Response response = client.newCall(request).execute();
        if(response!=null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }
        return 0;
    }
}
