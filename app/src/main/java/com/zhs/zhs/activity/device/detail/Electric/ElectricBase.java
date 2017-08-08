package com.zhs.zhs.activity.device.detail.Electric;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.ResetDeviceNameActivity;
import com.zhs.zhs.activity.device.ElectricMonitorActivity;
import com.zhs.zhs.activity.device.detail.DeviceBase;
import com.zhs.zhs.activity.device.detail.DeviceBaseInterface;
import com.zhs.zhs.entity.device.Electric;
import com.zhs.zhs.utils.GlobalPrefrence;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/24.
 */

public class ElectricBase extends DeviceBase implements DeviceBaseInterface {
    protected ElectricMonitorActivity activity;

    public String clintId = "";
    public JSONObject object = new JSONObject();
    public ArrayList<JSONObject> command = new ArrayList<>();
    public String name;

    public void getDetail(String clintId) {
        if (IsrName == false) {
            hideRname();
        }
    }


    public ElectricBase(Baseactivity baseactivity) {
        super(baseactivity);
        title = "气表";
        activity = (ElectricMonitorActivity) baseactivity;
        switchEvent();
    }

    public void sysBack() {
        activity.finish();
    }


    public void dispose() {
        if (IsRegisterMpush) {
            if (receiver != null) {
                baseactivity.unregisterReceiver(receiver);
            }
        }
    }

    public void switchEvent() {
        object.put("ClientId", clintId);
        object.put("MessageType", "switch");
    }


    private void hideRname() {
        activity.setRightText("", null);
        Drawable drawable = activity.getResources().getDrawable(R.mipmap.ic_write);
        drawable.setBounds(0, 0, 0, 0);
        activity.name.setCompoundDrawables(null, null, drawable, null);
        activity.name.setClickable(false);
    }

    /**
     * 显示页面信息
     *
     * @param commen
     */
    public void showMsg(Electric commen) {
        activity.name.setText(commen.MonitorAreaName);
        name = commen.MonitorAreaName;
        activity.code.setText(commen.ClientId);
        if (commen.SwitchInput_F == 1) {
            activity.SwitchOutputFCheck.setChecked(true);
        } else {
            activity.SwitchOutputFCheck.setChecked(false);
        }

        if (commen.SwitchInput_G == 1) {
            activity.SwitchOutputGCheck.setChecked(true);
        } else {
            activity.SwitchOutputGCheck.setChecked(false);
        }

        if (commen.SwitchInput_H == 1) {
            activity.SwitchOutputHCheck.setChecked(true);
        } else {
            activity.SwitchOutputHCheck.setChecked(false);
        }

        activity.pressure.setText(commen.ResidualCurrent + "");
        activity.flow.setText(commen.ElectricCurrent_A + "");
        activity.trueFlow.setText(commen.Voltage_A + "");
        activity.trueTemp.setText(commen.Temperature_A + "");

    }

    /**
     * 实时数据更新
     */
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IsRegisterMpush) {
                if (intent.getAction().equals(GlobalPrefrence.DEVICEDATA)) {
                    String string = intent.getStringExtra("message");
                    Electric electric = JSON.parseObject(string.toString(), Electric.class);
                    clintId = electric.ClientId;
                    showMsg(electric);
                }
            }
        }
    };



    /**
     * 重命名设备
     */
    public void reName(final String areaId) {
        if (IsrName) {
            activity.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("areaId", areaId);
                    intent.putExtra("name", name);
                    intent.setClass(activity, ResetDeviceNameActivity.class);
                    activity.startActivityForResult(intent, GlobalPrefrence.DEVICERENAME);
                }
            });
        }
    }
}