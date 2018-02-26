package com.sumslack.opensource;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private RelativeLayout container;
    private boolean canJump = true; //当有广告加载时设置为false

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        container = (RelativeLayout)findViewById(R.id.container);

        //进行运行时权限处理
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }else{
            requestAds();
        }
    }

    private void requestAds(){
        String appId = "1105585573";
        String adId = "4010212448179536";
        /*
        new SplashAD(this,container,appId,adId,new SplashADListener(){
            @Override
            public void onADDismissed(){
                forward();
            }
            @Override
            public void onNoAD(int i){
                forward();
            }
            @Override
            public void onADPresent(){
                //广告加载成功
            }
            @Override
            public void onADClicked(){
                //广告被点击
            }
        });
         */
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(canJump){
            forward();
        }
        canJump = true;
    }

    private void forward(){
        if(canJump){
            Intent intent = new Intent(this,IndexActivity.class);
            startActivity(intent);
            finish();
        }else{
            canJump = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"必须同意所有权限才能使用本程序",Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestAds();
                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }
    }
}
