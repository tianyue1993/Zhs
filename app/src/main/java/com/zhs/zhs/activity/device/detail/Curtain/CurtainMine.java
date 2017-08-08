package com.zhs.zhs.activity.device.detail.Curtain;

import android.view.View;
import android.widget.SeekBar;

import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.entity.device.CurtainData;
import com.zhs.zhs.handler.device.CurtainHandler;

/**
 * Created by admin on 2017/7/19.
 */

public class CurtainMine extends CurtainBase {


    public CurtainMine(Baseactivity baseactivity) {
        super(baseactivity);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        activity.showProgress(0, true);
        activity.client.getDeviceDetail(activity, clintId, new CurtainHandler() {
            @Override
            public void onSuccess(CurtainData commen) {
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
        activity.seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                activity.openPercent.setText("窗帘打开程度：" + progress + "%");
                object.put("CurtainOpening", "1");
                command.clear();
                command.add(object);
                controlDevice(command);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        activity.off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openPercent.setText("窗帘打开程度：" + 0 + "%");
                activity.seek.setProgress(0);
                object.put("CurtainOpening", "0");
                command.clear();
                command.add(object);
                controlDevice(command);
            }
        });


        activity.open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.openPercent.setText("窗帘打开程度：" + 100 + "%");
                activity.seek.setProgress(100);
                object.put("CurtainOpening", "100");
                command.clear();
                command.add(object);
                controlDevice(command);
            }
        });


    }

}
