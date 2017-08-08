package com.zhs.zhs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.utils.GlobalSetting;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OfflineActivity extends Activity {
    @Bind(R.id.content)
    TextView content;
    @Bind(R.id.sure)
    TextView sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_offline);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("msg") != null) {
            content.setText(getIntent().getStringExtra("msg"));
        }

    }

    // 实现onTouchEvent触屏函数但点击屏幕时不销毁本Activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @OnClick({R.id.sure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sure:
                GlobalSetting.getInstance(ZhsApplication.getContext()).clear();
                ZhsApplication.getContext().startActivity(new Intent(ZhsApplication.getContext(), LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalSetting.getInstance(ZhsApplication.getContext()).clear();
        ZhsApplication.pausePush();
        ZhsApplication.getContext().startActivity(new Intent(ZhsApplication.getContext(), LoginActivity.class));
    }
}
