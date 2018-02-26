package com.sumslack.opensource;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sumslack.opensource.BaseActivity;

public class WXFragmentActivity extends BaseActivity {

  private static final String EXAMPLE_URL="http://wxapps.sumslack.com/demo2/index.js";
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_fragment);

    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
      toolbar.setLogo(R.drawable.near);
    }

    WeexFragment weexFragment = WeexFragment.newInstance(EXAMPLE_URL);
    FragmentTransaction transaction = getFragmentManager().beginTransaction();
    transaction.add(R.id.content_fragment, weexFragment);
    transaction.commit();
  }
}
