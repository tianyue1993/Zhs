package com.zhs.zhs.activity;

import android.os.Bundle;

import com.zhs.zhs.R;

public class MyMallActivity extends Baseactivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mall);
        setTitleTextView(getString(R.string.my_mall), null);
    }
}
