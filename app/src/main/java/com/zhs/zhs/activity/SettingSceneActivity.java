package com.zhs.zhs.activity;

import android.os.Bundle;

import com.zhs.zhs.R;

public class SettingSceneActivity extends Baseactivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_scene2);
        setTitleTextView(getString(R.string.scene_setting), null);
    }
}
