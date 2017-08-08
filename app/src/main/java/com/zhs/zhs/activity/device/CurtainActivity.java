package com.zhs.zhs.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.device.detail.Curtain.CurtainBase;
import com.zhs.zhs.activity.device.detail.DeviceContext;
import com.zhs.zhs.entity.device.Device;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICERENAME;

public class CurtainActivity extends Baseactivity {
    @Bind(R.id.seek)
    public SeekBar seek;
    @Bind(R.id.off)
    public TextView off;
    @Bind(R.id.open)
    public TextView open;
    @Bind(R.id.open_percent)
    public TextView openPercent;
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.rl_open)
    public RelativeLayout rlOpen;
    @Bind(R.id.activity_curtain)
    public RelativeLayout activityCurtain;
    String type = "1";//1是场景设备，2是我的设备
    String clintId = "";
    String areaId = "";
    Device device;
    CurtainBase deviceBase;
    @Bind(R.id.code)
    public TextView code;
    @Bind(R.id.switch_sleep)
    public TextView switchSleep;
    @Bind(R.id.open_sleep)
    public LinearLayout openSleep;


    /**
     * 智能窗帘页面
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtain);
        ButterKnife.bind(this);
        type = getIntent().getStringExtra("type");
        Bundle bundle = getIntent().getExtras();
        if (bundle.getSerializable("device") != null) {
            device = (Device) bundle.getSerializable("device");
            if (device != null) {
                clintId = device.ClientId;
                areaId = device.areaId;
            }
        } else {
            clintId = getIntent().getStringExtra("ClientId");
        }
        deviceBase = DeviceContext.getCurtainBaseInstance(type, this);
        DeviceContext.Initial(deviceBase, clintId, areaId);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        deviceBase.dispose();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEVICERENAME) {
            if (data != null) {
                if (!data.getStringExtra("name").equals("")) {
                    name.setText("" + data.getStringExtra("name"));
                }
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            deviceBase.sysBack();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }


}
