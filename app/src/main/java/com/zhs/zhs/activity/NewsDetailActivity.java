package com.zhs.zhs.activity;

import android.os.Bundle;

import com.zhs.zhs.R;

public class NewsDetailActivity extends Baseactivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        setTitleTextView(getString(R.string.news_detail), null);
    }
}
