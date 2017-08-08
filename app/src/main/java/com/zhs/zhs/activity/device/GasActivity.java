package com.zhs.zhs.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.device.detail.DeviceContext;
import com.zhs.zhs.activity.device.detail.Gas.GasBase;
import com.zhs.zhs.entity.device.Device;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICERENAME;

public class GasActivity extends Baseactivity {
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.code)
    public TextView code;
    @Bind(R.id.image)
    public ImageView image;
    @Bind(R.id.state)
    public TextView state;
    @Bind(R.id.open_check)
    public CheckBox openCheck;
    @Bind(R.id.open)
    public LinearLayout open;
    @Bind(R.id.flow)
    public TextView flow;
    @Bind(R.id.rl_flow)
    public RelativeLayout rlFlow;
    @Bind(R.id.pressure)
    public TextView pressure;
    @Bind(R.id.rl_pressure)
    public RelativeLayout rlPressure;
    @Bind(R.id.true_flow)
    public TextView trueFlow;
    @Bind(R.id.rl_true_flow)
    public RelativeLayout rlTrueFlow;
    @Bind(R.id.data)
    public RelativeLayout data;
    @Bind(R.id.text1)
    public TextView text1;
    @Bind(R.id.text2)
    public TextView text2;
    @Bind(R.id.ll_warns)
    public LinearLayout llWarns;
    @Bind(R.id.activity_gas_induction)
    public RelativeLayout activityGasInduction;
    @Bind(R.id.switch_sleep)
    public TextView switchSleep;
    @Bind(R.id.open_sleep)
    public LinearLayout openSleep;
    private String type;
    String clintId = "";
    String areaId = "";
    Device device;
    GasBase deviceBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gas_induction);
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
        deviceBase = DeviceContext.getGasBaseInstance(type, this);
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
