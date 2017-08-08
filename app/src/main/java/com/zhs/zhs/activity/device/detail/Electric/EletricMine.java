package com.zhs.zhs.activity.device.detail.Electric;

import android.widget.CompoundButton;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.EletricData;
import com.zhs.zhs.handler.device.EletricHandler;

/**
 * Created by admin on 2017/7/19.
 */

public class EletricMine extends ElectricBase {

    public EletricMine(Baseactivity baseactivity) {
        super(baseactivity);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        activity.showProgress(0, true);
        activity.client.getDeviceDetail(activity, clintId, new EletricHandler() {
            @Override
            public void onSuccess(EletricData commen) {
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

    @Override
    public void switchEvent() {
        super.switchEvent();
        activity.SwitchOutputFCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    object.put("SwitchOutput_F", "1");
                } else {
                    object.put("SwitchOutput_F", "0");
                }
                command.clear();
                command.add(object);
                controlDevice(command);
            }
        });

        activity.SwitchOutputGCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    object.put("SwitchOutput_G", "1");
                } else {
                    object.put("SwitchOutput_G", "0");
                }
                command.clear();
                command.add(object);
                controlDevice(command);
            }
        });

        activity.SwitchOutputHCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    object.put("SwitchOutput_H", "1");
                } else {
                    object.put("SwitchOutput_H", "0");
                }
                command.clear();
                command.add(object);
                controlDevice(command);
            }
        });
    }

}
