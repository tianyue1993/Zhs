package com.zhs.zhs.activity.device.detail.Gas;

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
import com.zhs.zhs.activity.device.GasActivity;
import com.zhs.zhs.activity.device.detail.DeviceBase;
import com.zhs.zhs.activity.device.detail.DeviceBaseInterface;
import com.zhs.zhs.entity.device.Gas;
import com.zhs.zhs.utils.GlobalPrefrence;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/19.
 */

public class GasBase extends DeviceBase implements DeviceBaseInterface {
    protected GasActivity activity;
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


    public GasBase(Baseactivity baseactivity) {
        super(baseactivity);
        title = "气表";
        activity = (GasActivity) baseactivity;
        switchEvent();
    }

    public void sysBack() {
        activity.finish();
    }


    public void switchEvent() {
        object.put("ClientId", clintId);
        object.put("MessageType", "switch");
        if (activity.openCheck.isChecked()) {
            object.put("SwitchOutput", "1");
        } else {
            object.put("SwitchOutput", "0");
        }
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
    public void showMsg(Gas commen) {
        activity.flow.setText(commen.TotalFlow + "");
        activity.name.setText(commen.MonitorAreaName);
        name = commen.MonitorAreaName;
        activity.code.setText(commen.ClientId);
        if (commen.GasLeakFlag == 1) {
            activity.text1.setText("你家漏气了！！！！");
            activity.text1.setVisibility(View.VISIBLE);

        } else {
            activity.text1.setVisibility(View.GONE);
        }
        if (commen.LowerAlarm_Voltage == 0) {
            activity.text2.setText("你家电池没电了！！！！");
            activity.text2.setVisibility(View.VISIBLE);
        } else {
            activity.text2.setVisibility(View.GONE);
        }

        if (commen.SwitchInput == 1) {
            activity.openCheck.setChecked(true);
        } else {
            activity.openCheck.setChecked(false);
        }

        if (commen.GasLeakFlag != 1 && commen.LowerAlarm_Voltage != 0) {
            activity.llWarns.setVisibility(View.GONE);
            activity.state.setTextColor(activity.getResources().getColor(R.color.black));
        } else {
            activity.llWarns.setVisibility(View.VISIBLE);
            activity.state.setTextColor(activity.getResources().getColor(R.color.red));
            activity.state.setText("设备异常");
        }
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
                    Gas gas = JSON.parseObject(string.toString(), Gas.class);
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
