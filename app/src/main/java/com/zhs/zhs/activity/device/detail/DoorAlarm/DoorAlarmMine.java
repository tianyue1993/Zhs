package com.zhs.zhs.activity.device.detail.DoorAlarm;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.GlassShockData;
import com.zhs.zhs.handler.device.GlassShockHandler;

/**
 * Created by admin on 2017/7/19.
 */

public class DoorAlarmMine extends DoorAlarmBase {

    public DoorAlarmMine(Baseactivity baseactivity) {
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


}
