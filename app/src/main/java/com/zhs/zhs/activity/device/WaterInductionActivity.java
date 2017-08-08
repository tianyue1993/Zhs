package com.zhs.zhs.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.device.detail.DeviceContext;
import com.zhs.zhs.activity.device.detail.Water.WaterBase;
import com.zhs.zhs.entity.device.Device;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICERENAME;

public class WaterInductionActivity extends Baseactivity {
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.flow)
    public TextView flow;
    @Bind(R.id.state)
    public TextView state;
    @Bind(R.id.open_check)
    public CheckBox openCheck;
    @Bind(R.id.text1)
    public TextView text1;
    @Bind(R.id.text2)
    public TextView text2;
    @Bind(R.id.ll_warns)
    public LinearLayout llWarns;
    @Bind(R.id.code)
    public TextView code;
    @Bind(R.id.rl_flow)
    RelativeLayout rlFlow;
    @Bind(R.id.pressure)
    TextView pressure;
    @Bind(R.id.rl_pressure)
    RelativeLayout rlPressure;
    @Bind(R.id.true_flow)
    TextView trueFlow;
    @Bind(R.id.rl_true_flow)
    RelativeLayout rlTrueFlow;
    @Bind(R.id.data)
    RelativeLayout data;
    @Bind(R.id.activity_leakage_induction)
    RelativeLayout activityLeakageInduction;
    private String type;
    String clintId = "";
    String areaId = "";
    Device device;
    WaterBase deviceBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leakage_induction);
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
        deviceBase = DeviceContext.getWaterBaseInstance(type, this);
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
