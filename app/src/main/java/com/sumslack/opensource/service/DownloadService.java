package com.sumslack.opensource.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.sumslack.opensource.DownloadListener;
import com.sumslack.opensource.DownloadTask;
import com.sumslack.opensource.NetworkActivity;
import com.sumslack.opensource.R;
import com.taobao.weex.bridge.JSCallback;

import java.io.File;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private String downloadUrl;


    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotification("下载中...",progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载成功",-1));
            Toast.makeText(DownloadService.this,"Download Success",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotification("下载失败",-1));
            Toast.makeText(DownloadService.this,"Download Failed",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onParsed() {
            downloadTask = null;
            Toast.makeText(DownloadService.this,"下载被暂停",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this,"下载被取消",Toast.LENGTH_LONG).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();


    public class DownloadBinder extends Binder{

        public void startDownload(String url, JSCallback callback){
            if(downloadTask == null){
                downloadUrl = url;
                downloadTask = new DownloadTask(listener,callback);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotification("准备下载中...",0));
            }
        }

        public void parseDownload(){
            if(downloadTask!=null){
                downloadTask.parseDownload();
            }
        }
        public void cancelDownload(){
            if(downloadTask!=null){
                downloadTask.cancelDownload();
            }else{
                if(downloadUrl!=null){
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(dir + fileName);
                    if(file.exists()){
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this,"Canlled",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title,int progress){
        Intent intent = new Intent(this, NetworkActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.near);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if(progress>0){
            builder.setContentText(progress + " % ");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
//        return null;
    }
}
