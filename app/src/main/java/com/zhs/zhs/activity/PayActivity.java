package com.zhs.zhs.activity;

import android.os.Bundle;

import com.zhs.zhs.R;


/**
 * 在线缴费页面
 */
public class PayActivity extends Baseactivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        setTitleTextView(getString(R.string.pay), null);
    }
}
