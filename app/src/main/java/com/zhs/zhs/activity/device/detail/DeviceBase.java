package com.zhs.zhs.activity.device.detail;

import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.SetSleepTimeActivity;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.utils.GlobalPrefrence;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by admin on 2017/7/19.
 */

public class DeviceBase implements BaseInterface {
    public static Baseactivity baseactivity;
    public String title;
    protected IntentFilter filter;
    public boolean IsrName;
    public boolean IsSet = false;
    public boolean IsRegisterMpush = true;

    public DeviceBase(Baseactivity activity) {
        this.baseactivity = activity;
    }

    public void setTitle() {
        baseactivity.setTitleTextView(title, null);
    }


    public void startPush(String key, String tags) {
        if (IsRegisterMpush) {
            ZhsApplication.startDevicePush(key, tags);
        }
    }

    public void registerReceiver() {
        filter = new IntentFilter();
        filter.addAction(GlobalPrefrence.DEVICEDATA);
    }

    public void backEvent() {
        baseactivity.leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseactivity.finish();
            }
        });
    }


    public void setRightText() {
        if (IsSet) {
            baseactivity.setRightText("设置", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseactivity.startActivity(new Intent(baseactivity, SetSleepTimeActivity.class));
                }
            });
        }
    }

    public static void controlDevice(ArrayList<JSONObject> command) {
        JSONObject parms = new JSONObject();
        parms.put("command", JSON.toJSONString(command));
        try {
            baseactivity.cancelmDialog();
            baseactivity.showProgress(0, true);
            StringEntity entity = new StringEntity(parms.toString(), "UTF-8");
            baseactivity.client.getDeviceControl(baseactivity, entity, new CommentHandler() {
                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    baseactivity.showToast("ok");
                    baseactivity.cancelmDialog();
                }

                @Override
                public void onFailure(BaseException exception) {
                    super.onFailure(exception);
                    baseactivity.cancelmDialog();
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    baseactivity.cancelmDialog();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
