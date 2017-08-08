package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.AddDeviceAdapter;
import com.zhs.zhs.entity.device.Device;
import com.zhs.zhs.entity.MyDevice;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.DeviceListHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AddDeviceActivity extends Baseactivity {

    @Bind(R.id.device_grid)
    GridView deviceGrid;
    private ArrayList<Device> adaptList = new ArrayList<>();
    private ArrayList<Device> addDeviceList = new ArrayList<>();
    private ArrayList<Device> unAddDeviceList = new ArrayList<>();
    AddDeviceAdapter adapter;
    private static final int DEVICE = 11;
    /**
     * 列表中已加入上一页的设备
     */
    private ArrayList<Device> hadAddDevice = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.add_device), null);
        hadAddDevice = (ArrayList<Device>) getIntent().getExtras().getSerializable("device");
        getList();
        setRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("addDeviceList", addDeviceList);
                intent.putExtras(bundle);
                // 将数据根据特定键值的意图事件导入
                setResult(DEVICE, intent);
                //关闭后返回主Activity
                finish();
            }
        });
        deviceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setBackgroundResource(R.color.base_color);
                for (int i = 0; i < addDeviceList.size(); i++) {
                    if (addDeviceList.get(i).deviceId == adapter.getItem(position).deviceId) {
                        return;
                    }
                }
                addDeviceList.add(adapter.getItem(position));
            }
        });


        leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("addDeviceList", addDeviceList);
                intent.putExtras(bundle);
                setResult(10000, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("addDeviceList", addDeviceList);
            intent.putExtras(bundle);
            // 将数据根据特定键值的意图事件导入
            setResult(10000, intent);
            //关闭后返回主Activity
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }

    /**
     * 获取当前用户下添加到场景的设备列表
     */
    public void getList() {
        showProgress(0, true);
        client.getDeviceNotInScene(mContext, new DeviceListHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(MyDevice myDevice) {
                super.onSuccess(myDevice);
                cancelmDialog();
                adaptList = myDevice.data;
                unAddDeviceList.clear();
                unAddDeviceList = adaptList;
                if (hadAddDevice.size() > 0 && myDevice.data.size() > 0) {
                    /**
                     * 移除列表中已加入上一页的设备
                     */
                    for (int i = 0; i < adaptList.size(); i++) {
                        for (int j = 0; j < hadAddDevice.size(); j++) {
                            if (hadAddDevice.get(j).deviceId == adaptList.get(i).deviceId) {
                                unAddDeviceList.remove(adaptList.get(i));
                            }
                        }

                    }
                } else {
                    unAddDeviceList = adaptList;
                }

                adapter = new AddDeviceAdapter(mContext, unAddDeviceList);
                deviceGrid.setAdapter(adapter);
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }

}
