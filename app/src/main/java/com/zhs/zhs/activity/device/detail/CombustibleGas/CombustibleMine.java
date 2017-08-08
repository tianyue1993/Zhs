package com.zhs.zhs.activity.device.detail.CombustibleGas;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.CombustibleGasData;
import com.zhs.zhs.handler.device.CombustibleHandler;

/**
 * Created by admin on 2017/7/19.
 */

public class CombustibleMine extends CombustibleBase {


    public CombustibleMine(Baseactivity baseactivity) {
        super(baseactivity);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        activity.showProgress(0, true);
        activity.client.getDeviceDetail(activity, clintId, new CombustibleHandler() {
            @Override
            public void onSuccess(CombustibleGasData commen) {
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


}
