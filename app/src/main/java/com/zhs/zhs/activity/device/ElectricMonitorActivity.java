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
import com.zhs.zhs.activity.device.detail.Electric.ElectricBase;
import com.zhs.zhs.entity.device.Device;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICERENAME;

public class ElectricMonitorActivity extends Baseactivity {


    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.code)
    public TextView code;
    @Bind(R.id.image)
    public ImageView image;
    @Bind(R.id.switch_one)
    public LinearLayout switchOne;
    @Bind(R.id.switch_two)
    public LinearLayout switchTwo;
    @Bind(R.id.switch_three)
    public LinearLayout switchThree;
    @Bind(R.id.switch_four)
    public LinearLayout switchFour;
    @Bind(R.id.switch_five)
    public LinearLayout switchFive;
    @Bind(R.id.switch_six)
    public LinearLayout switchSix;
    @Bind(R.id.switch_seven)
    public LinearLayout switchSeven;
    @Bind(R.id.switch_eleven)
    public LinearLayout switchEleven;
    @Bind(R.id.ll_branch_switch)
    public LinearLayout llBranchSwitch;
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
    @Bind(R.id.true_temp)
    public TextView trueTemp;
    @Bind(R.id.rl_true_temp)
    public RelativeLayout rlTrueTemp;
    @Bind(R.id.data)
    public LinearLayout data;
    @Bind(R.id.SwitchOutput_F_check)
    public CheckBox SwitchOutputFCheck;
    @Bind(R.id.SwitchOutput_G_check)
    public CheckBox SwitchOutputGCheck;
    @Bind(R.id.SwitchInput_H_check)
    public CheckBox SwitchOutputHCheck;
    private String type;
    String clintId = "";
    String areaId = "";
    Device device;
    ElectricBase deviceBase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electric_monitor);
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
        deviceBase = DeviceContext.getElectricBaseInstance(type, this);
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
