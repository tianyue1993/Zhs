package com.zhs.zhs.activity.device.detail.GlassShock;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.GlassShockData;
import com.zhs.zhs.handler.device.GlassShockHandler;

/**
 * Created by admin on 2017/7/19.
 */

public class GlassShockMine extends GlassShockBase {


    public GlassShockMine(Baseactivity baseactivity) {
        super(baseactivity);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        activity.showProgress(0, true);
        activity.client.getDeviceDetail(activity, clintId, new GlassShockHandler() {
            @Override
            public void onSuccess(GlassShockData commen) {
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

//    @Override
//    public void switchEvent() {
//        activity.openCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    object.put("SwitchOutput", "1");
//                } else {
//                    object.put("SwitchOutput", "0");
//                }
//                command.clear();
//                command.add(object);
//                controlDevice(command);
//            }
//        });
//    }

}
