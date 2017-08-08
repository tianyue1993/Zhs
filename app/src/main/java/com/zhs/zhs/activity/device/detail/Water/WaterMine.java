package com.zhs.zhs.activity.device.detail.Water;

import android.widget.CompoundButton;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.WaterData;
import com.zhs.zhs.handler.device.WaterHandler;

/**
 * Created by admin on 2017/7/19.
 */

public class WaterMine extends WaterBase {


    public WaterMine(Baseactivity baseactivity) {
        super(baseactivity);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        activity.showProgress(0, true);
        activity.client.getDeviceDetail(activity, clintId, new WaterHandler() {
            @Override
            public void onSuccess(WaterData commen) {
                super.onSuccess(commen);
                activity.cancelmDialog();
                showMsg(commen.data);
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

    @Override
    public void switchEvent() {
        super.switchEvent();
        activity.openCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    object.put("SwitchOutput", "1");
                } else {
                    object.put("SwitchOutput", "0");
                }
                command.clear();
                command.add(object);
                controlDevice(command);
            }
        });
    }

    @Override
    public void setRightText() {
        super.setRightText();
        super.switchEvent();
    }
}
