package com.zhs.zhs.activity.device.detail.Concentrator;

import android.content.Intent;
import android.view.View;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.CnncentratorData;
import com.zhs.zhs.handler.device.ConcentratorHandler;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICEDETAIL;

/**
 * Created by admin on 2017/7/19.
 */

public class ConcentratorScence extends ConcentratorBase {
    public ConcentratorScence(Baseactivity baseactivity) {
        super(baseactivity);
        activity.listview.setVisibility(View.GONE);
        activity.blueTooth.setVisibility(View.GONE);
        activity.line.setVisibility(View.GONE);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        activity.showProgress(0, true);
        activity.client.getDeviceDetail(activity, clintId, new ConcentratorHandler() {
            @Override
            public void onSuccess(CnncentratorData commen) {
                super.onSuccess(commen);
                showMsg(commen.data);
                activity.cancelmDialog();
            }

            @Override
            public void onFailure() {
                super.onFailure();
                activity.cancelmDialog();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                activity.cancelmDialog();
            }
        });
    }

    /**
     * 重写父类，只存指令，不传服务
     */
    @Override
    public void switchEvent() {
        super.switchEvent();
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
