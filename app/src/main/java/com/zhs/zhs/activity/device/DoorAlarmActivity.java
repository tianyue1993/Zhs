package com.zhs.zhs.activity.device;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.device.detail.DeviceContext;
import com.zhs.zhs.activity.device.detail.DoorAlarm.DoorAlarmBase;
import com.zhs.zhs.entity.device.Device;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DoorAlarmActivity extends Baseactivity {

    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.code)
    public TextView code;
    @Bind(R.id.image)
    public ImageView image;
    @Bind(R.id.state)
    public TextView state;
    @Bind(R.id.switch_sleep)
    public TextView switchSleep;
    @Bind(R.id.open_sleep)
    public LinearLayout openSleep;
    @Bind(R.id.text1)
    public TextView text1;
    @Bind(R.id.text2)
    public TextView text2;
    @Bind(R.id.ll_warns)
    public LinearLayout llWarns;
    @Bind(R.id.activity_gas_induction)
    public RelativeLayout activityGasInduction;


    private String type;
    String clintId = "";
    String areaId = "";
    Device device;
    DoorAlarmBase deviceBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_door_alarm);
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
        deviceBase = DeviceContext.getDoorAlarmBaseInstance(type, this);
        DeviceContext.Initial(deviceBase, clintId, areaId);
    }
}
