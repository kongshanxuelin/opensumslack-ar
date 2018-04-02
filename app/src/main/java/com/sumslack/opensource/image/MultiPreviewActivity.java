package com.sumslack.opensource.image;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.sumslack.opensource.R;
import com.sumslack.opensource.utils.StrUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import me.kareluo.intensify.image.IntensifyImage;
import me.kareluo.intensify.image.IntensifyImageView;

public class MultiPreviewActivity extends AppCompatActivity{
    private static final String TAG = "MultiPreviewActivity";

    private ViewPager mViewPager;

    private ImagePageAdapter mAdapter;

    private String[] urls;
    private String url;
    private int position = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_multi);

        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .build();
        File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),"imageloader/cache");
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))
                .threadPoolSize(3)
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        ImageLoader.getInstance().init(config);

        url = StrUtil.formatMullStr(getIntent().getExtras().getString("url"));
        urls = getIntent().getExtras().getStringArray("urls");
        for(int i=0;i<urls.length;i++){
            if(urls[i].equals(url)) {
                position = i;
                break;
            }
        }


        mViewPager = (ViewPager) findViewById(R.id.vp_pager);
        mAdapter = new ImagePageAdapter();
        mViewPager.setAdapter(mAdapter);
    }

    private class ImagePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return urls.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            IntensifyImageView imageView = new IntensifyImageView(container.getContext());
            imageView.setScaleType(IntensifyImage.ScaleType.FIT_AUTO);
            try {
                String cur_url = urls[position];
                Bitmap bm = ImageLoader.getInstance().getMemoryCache().get(cur_url);
                if(bm!=null){
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    InputStream inputstream = new ByteArrayInputStream(baos.toByteArray());
                    imageView.setImage(inputstream);
                }else{
                    ImageLoader.getInstance().loadImage(cur_url,new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String imageUri, View view) {
                                    Log.d(TAG,"onLoadingStarted");
                                }

                                @Override
                                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                    Log.d(TAG,"onLoadingFailed");
                                }

                                @Override
                                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                                    Log.d(TAG,"onLoadingComplete");
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    loadedImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    InputStream inputstream = new ByteArrayInputStream(baos.toByteArray());
                                    imageView.setImage(inputstream);
                                }

                                @Override
                                public void onLoadingCancelled(String imageUri, View view) {
                                    Log.d(TAG,"onLoadingCancelled");
                                }
                            });
                }

            } catch (Exception e) {
                Log.w(TAG, e);
            }
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
