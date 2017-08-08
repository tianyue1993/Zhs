package com.zhs.zhs.activity.device.detail.CombustibleGas;

import android.content.Intent;
import android.view.View;

import com.zhs.zhs.activity.Baseactivity;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICEDETAIL;

/**
 * Created by admin on 2017/7/19.
 */

public class CombustibleScence extends CombustibleBase {
    public CombustibleScence(Baseactivity baseactivity) {
        super(baseactivity);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        //显示指令信息
    }


    @Override
    public void sysBack() {
        Intent intent = new Intent();
        intent.putExtra("object", object.toString());
        activity.setResult(DEVICEDETAIL, intent);
        activity.finish();
    }

    @Override
    public void backEvent() {
        activity.leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sysBack();
            }
        });
    }
}
