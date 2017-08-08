package com.zhs.zhs.activity.device.detail.Curtain;

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
import com.zhs.zhs.activity.device.CurtainActivity;
import com.zhs.zhs.activity.device.detail.DeviceBase;
import com.zhs.zhs.activity.device.detail.DeviceBaseInterface;
import com.zhs.zhs.entity.device.Curtain;
import com.zhs.zhs.utils.GlobalPrefrence;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/24.
 */

public class CurtainBase extends DeviceBase implements DeviceBaseInterface {
    protected CurtainActivity activity;
    public boolean IsRegisterMpush = true;
    public String clintId = "";
    public JSONObject object = new JSONObject();
    public ArrayList<JSONObject> command = new ArrayList<>();
    public String name;

    public void getDetail(String clintId) {
        if (IsrName == false) {
            hideRname();
        }
    }


    public CurtainBase(Baseactivity baseactivity) {
        super(baseactivity);
        title = "智能窗帘";
        activity = (CurtainActivity) baseactivity;
        switchEvent();
    }

    public void sysBack() {
        activity.finish();
    }


    public void switchEvent() {
        object.put("ClientId", clintId);
        object.put("MessageType", "switch");
        object.put("CurtainOpening", "0");
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
    public void showMsg(Curtain commen) {
        activity.name.setText(commen.MonitorAreaName);
        name = commen.MonitorAreaName;
        activity.code.setText(commen.ClientId);
//        if (commen.SwitchOutput_A == 1) {
//            activity.openCheck.setChecked(true);
//        } else {
//            activity.openCheck.setChecked(false);
//        }

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
                    Curtain gas = JSON.parseObject(string.toString(), Curtain.class);
                    clintId = gas.ClientId;
                    showMsg(gas);
                }
            }
        }
    };

    @Override
    public void registerReceiver() {
        if (IsRegisterMpush) {
            super.registerReceiver();
            baseactivity.registerReceiver(receiver, filter);
        }
    }

    public void dispose() {
        if (IsRegisterMpush) {
            if (receiver != null) {
                baseactivity.unregisterReceiver(receiver);
            }
        }

    }

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
