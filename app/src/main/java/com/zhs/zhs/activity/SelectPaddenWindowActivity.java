package com.zhs.zhs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.zhs.zhs.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectPaddenWindowActivity extends Activity {

    @Bind(R.id.only_one)
    Button onlyOne;
    @Bind(R.id.one_five)
    Button oneFive;
    @Bind(R.id.weekend)
    Button weekend;
    @Bind(R.id.everyday)
    Button everyday;
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.pop_layout)
    LinearLayout popLayout;
    private static final int FREQUENCY = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_padden_window);
        ButterKnife.bind(this);
    }


//    // 实现onTouchEvent触屏函数但点击屏幕时销毁本Activity
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        finish();
//        return true;
//    }

    @OnClick({R.id.only_one, R.id.one_five, R.id.weekend, R.id.everyday, R.id.btn_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.only_one:
                backWithData("frequency", onlyOne.getText().toString());
                break;
            case R.id.one_five:
                backWithData("frequency", oneFive.getText().toString());
                break;
            case R.id.weekend:
                backWithData("frequency", weekend.getText().toString());
                break;
            case R.id.everyday:
                backWithData("frequency", everyday.getText().toString());
                break;
            case R.id.btn_cancel:
                backWithData("frequency", onlyOne.getText().toString());
                break;

        }
    }

    private void backWithData(String k, String v) {
        Intent data = new Intent();
        data.putExtra(k, v);
        setResult(FREQUENCY, data);
        finish();
    }

}
