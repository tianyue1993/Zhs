package com.zhs.zhs.activity;

import android.os.Bundle;

import com.zhs.zhs.R;


/**
 * 常见问题页面
 */
public class QuestionActivity extends Baseactivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        setTitleTextView(getString(R.string.question),null);


    }
}
