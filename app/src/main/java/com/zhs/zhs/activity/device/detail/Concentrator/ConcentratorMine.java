package com.zhs.zhs.activity.device.detail.Concentrator;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.CnncentratorData;
import com.zhs.zhs.handler.device.ConcentratorHandler;

/**
 * Created by admin on 2017/7/19.
 */

public class ConcentratorMine extends ConcentratorBase {

    public ConcentratorMine(Baseactivity baseactivity) {
        super(baseactivity);
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

    @Override
    public void switchEvent() {
        super.switchEvent();
        object.put("Switch", "0");
        command.clear();
        command.add(object);
    }

}
