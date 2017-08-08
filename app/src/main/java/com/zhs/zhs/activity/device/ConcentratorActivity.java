package com.zhs.zhs.activity.device;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.Baseactivity;
import com.zhs.zhs.activity.ResetDeviceNameActivity;
import com.zhs.zhs.activity.device.detail.Concentrator.ConcentratorBase;
import com.zhs.zhs.activity.device.detail.DeviceContext;
import com.zhs.zhs.adapter.BlueAdapter;
import com.zhs.zhs.entity.BlueDevice;
import com.zhs.zhs.entity.device.Device;
import com.zhs.zhs.utils.BlueUtils;
import com.zhs.zhs.utils.GlobalPrefrence;
import com.zhs.zhs.views.ListViewForScrollView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICERENAME;

public class ConcentratorActivity extends Baseactivity {

    /**
     * 集中器
     */
    @Bind(R.id.name)
    public TextView name;
    @Bind(R.id.code)
    public TextView code;
    @Bind(R.id.image)
    public ImageView image;
    @Bind(R.id.state)
    public TextView state;
    @Bind(R.id.check1)
    public CheckBox check1;
    @Bind(R.id.listview)
    public ListViewForScrollView listview;
    @Bind(R.id.search_device)
    public Button search_device;
    @Bind(R.id.temperature)
    public TextView temperature;
    @Bind(R.id.humidity)
    public TextView humidity;
    @Bind(R.id.pm)
    public TextView pm;
    @Bind(R.id.hcho)
    public TextView hcho;
    @Bind(R.id.data)
    LinearLayout data;
    @Bind(R.id.blue_tooth)
    public LinearLayout blueTooth;

    private static final int ENABLE_BLUE = 100;
    private static final String DEVICE = "deviceActivity";
    @Bind(R.id.line)
    public ImageView line;


    private BluetoothAdapter mBluetoothAdapter;
    private Set<BluetoothDevice> devices;
    private Set<BlueDevice> setDevices = new HashSet<>();
    private BlueAdapter blueAdapter;
    private IntentFilter mFilter;

    String type = "1";//1是场景设备，2是我的设备
    /**
     * 蓝牙音频传输协议
     */
    private BluetoothA2dp a2dp;
    /**
     * 需要连接的蓝牙设备
     */
    private BluetoothDevice currentBluetoothDevice;


    String clintId = "";
    String areaId = "";
    Device device;
    ConcentratorBase deviceBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concentrator);
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
        deviceBase = DeviceContext.getConcentratorBaseInstance(type, this);
        DeviceContext.Initial(deviceBase, clintId, areaId);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            //开启权限访问对话框
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH}, 10003);
        }
        initDate();

        /**如果本地蓝牙没有开启，则开启*/
        if (!mBluetoothAdapter.isEnabled()) {
            check1.setChecked(false);
            search_device.setVisibility(View.GONE);
        } else {
            check1.setChecked(true);
            search_device.setVisibility(View.VISIBLE);
        }

        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    search_device.setVisibility(View.VISIBLE);
                    Intent mIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(mIntent, ENABLE_BLUE);
                    getBondedDevices();
                } else {
                    search_device.setVisibility(View.GONE);
                    /**关闭蓝牙*/
                    if (mBluetoothAdapter.isEnabled()) {
                        mBluetoothAdapter.disable();
                    }
                }
            }
        });

        search_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressDailog();
                // 如果正在搜索，就先取消搜索
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
                mBluetoothAdapter.startDiscovery();
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                List<BlueDevice> listDevices = blueAdapter.getListDevices();
                final BlueDevice blueDevice = listDevices.get(i);
                String msg = "";
                /**还没有配对*/
                if (blueDevice.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                    msg = "是否与设备" + blueDevice.getName() + "配对并连接？";
                } else {
                    msg = "是否与设备" + blueDevice.getName() + "连接？";
                }
                showDailog(msg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /**当前需要配对的蓝牙设备*/
                        currentBluetoothDevice = blueDevice.getDevice();
                        /**还没有配对*/
                        if (blueDevice.getDevice().getBondState() != BluetoothDevice.BOND_BONDED) {
                            startPariBlue(blueDevice);
                        } else {
                            /**完成配对的,直接连接*/
                            contectBuleDevices();
                        }
                        showProgressDailog();
                    }
                });
            }
        });


    }


    private void initDate() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        /**注册搜索蓝牙receiver*/
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver, mFilter);
        /**注册配对状态改变监听器*/
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        mFilter.addAction(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter);
        getBondedDevices();

    }


    /**
     * 获取所有已经绑定的蓝牙设备并显示
     */
    private void getBondedDevices() {
        if (!setDevices.isEmpty())
            setDevices.clear();
        devices = mBluetoothAdapter.getBondedDevices();
        for (BluetoothDevice bluetoothDevice : devices) {
            BlueDevice blueDevice = new BlueDevice();
            blueDevice.setName(bluetoothDevice.getName());
            blueDevice.setAddress(bluetoothDevice.getAddress());
            blueDevice.setDevice(bluetoothDevice);
            if (bluetoothDevice.getBondState() == 12) {
                blueDevice.setStatus("已配对");
            } else if (bluetoothDevice.getBondState() == 11) {
                blueDevice.setStatus("正在配对...");
            } else if (bluetoothDevice.getBondState() == 10) {
                blueDevice.setStatus("未匹配");
            } else {
                blueDevice.setStatus("已配对");
            }
            setDevices.add(blueDevice);
        }
        if (blueAdapter == null) {
            blueAdapter = new BlueAdapter(this, setDevices);
            listview.setAdapter(blueAdapter);
        } else {
            blueAdapter.setSetDevices(setDevices);
            blueAdapter.notifyDataSetChanged();
        }
    }


    /**
     * 开始配对蓝牙设备
     *
     * @param blueDevice
     */
    private void startPariBlue(BlueDevice blueDevice) {
        BlueUtils blueUtils = new BlueUtils(blueDevice);
        blueUtils.doPair();
    }

    /**
     * 取消配对蓝牙设备
     *
     * @param blueDevice
     */
    private void stopPariBlue(BlueDevice blueDevice) {
        BlueUtils.unpairDevice(blueDevice.getDevice());
    }

    /**
     * 开始连接蓝牙设备
     */
    private void contectBuleDevices() {
        /**使用A2DP协议连接设备*/
        mBluetoothAdapter.getProfileProxy(this, mProfileServiceListener, BluetoothProfile.A2DP);
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

        if (requestCode == ENABLE_BLUE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙开启成功", Toast.LENGTH_SHORT).show();
                getBondedDevices();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "蓝牙开始失败", Toast.LENGTH_SHORT).show();
            }
        } else {

        }

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


    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            /** 搜索到的蓝牙设备*/
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // 搜索到的不是已经配对的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    BlueDevice blueDevice = new BlueDevice();
                    blueDevice.setName(device.getName());
                    blueDevice.setAddress(device.getAddress());
                    blueDevice.setDevice(device);
                    setDevices.add(blueDevice);
                    blueAdapter.setSetDevices(setDevices);
                    blueAdapter.notifyDataSetChanged();
                }

                /**当绑定的状态改变时*/
            } else if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        hideProgressDailog();
                        /**开始连接*/
                        contectBuleDevices();
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Toast.makeText(mContext, "成功取消配对", Toast.LENGTH_SHORT).show();
                        getBondedDevices();
                        break;
                    default:
                        break;
                }

                /**搜索完成*/
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminateVisibility(false);
                hideProgressDailog();
            }
        }
    };

    /**
     * 连接蓝牙设备（通过监听蓝牙协议的服务，在连接服务的时候使用BluetoothA2dp协议）
     */
    private BluetoothProfile.ServiceListener mProfileServiceListener = new BluetoothProfile.ServiceListener() {

        @Override
        public void onServiceDisconnected(int profile) {

        }

        @Override
        public void onServiceConnected(int profile, BluetoothProfile proxy) {
            try {
                if (profile == BluetoothProfile.HEADSET) {

                } else if (profile == BluetoothProfile.A2DP) {
                    /**使用A2DP的协议连接蓝牙设备（使用了反射技术调用连接的方法）*/
                    a2dp = (BluetoothA2dp) proxy;
                    if (a2dp.getConnectionState(currentBluetoothDevice) != BluetoothProfile.STATE_CONNECTED) {
                        a2dp.getClass()
                                .getMethod("connect", BluetoothDevice.class)
                                .invoke(a2dp, currentBluetoothDevice);
                        Toast.makeText(mContext, "请播放音乐", Toast.LENGTH_SHORT).show();
                        getBondedDevices();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    protected ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    public void showProgressDailog() {
        if (progressDialog == null)
            progressDialog = new ProgressDialog(mContext);
        progressDialog.setMessage("正在搜索...");
        progressDialog.show();

    }

    public void hideProgressDailog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void showDailog(String msg, DialogInterface.OnClickListener listenter) {
        alertDialog = new AlertDialog.Builder(mContext).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确认", listenter);
        alertDialog.show();
    }

    @OnClick(R.id.name)
    public void onViewClicked() {
        Intent intent = new Intent();
        intent.setClass(mContext, ResetDeviceNameActivity.class);
        startActivityForResult(intent, GlobalPrefrence.DEVICERENAME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }

        deviceBase.dispose();
    }
}
