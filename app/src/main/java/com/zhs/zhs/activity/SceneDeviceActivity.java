package com.zhs.zhs.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.adapter.AddDeviceAdapter;
import com.zhs.zhs.entity.device.Device;
import com.zhs.zhs.entity.MoreScene;
import com.zhs.zhs.entity.MyDevice;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.DeviceListHandler;
import com.zhs.zhs.utils.ClassUtils;
import com.zhs.zhs.views.DialogFactory;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;
import static com.zhs.zhs.ZhsApplication.getActivityData;
import static com.zhs.zhs.utils.GlobalPrefrence.DEVICEDETAIL;


public class SceneDeviceActivity extends Baseactivity {
    @Bind(R.id.device_grid)
    GridView deviceGrid;
    @Bind(R.id.commit)
    RelativeLayout commit;
    private ArrayList<Device> adaptList = new ArrayList<>();
    AddDeviceAdapter adapter;
    private Dialog delete;
    private static final int DEVICE = 11;
    MoreScene.Scene scene;
    ArrayList<JSONObject> command = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_device);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.scene_device), null);
        if (getIntent().getStringExtra("type").equals("2")) {
            //修改场景获取已添加设备
            scene = (MoreScene.Scene) getIntent().getExtras().getSerializable("scene");
            getDevice(scene.sceneId);
        } else {
            adaptList = (ArrayList<Device>) getIntent().getExtras().getSerializable("device");
        }

        setRightImage(R.mipmap.ic_add_scene, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("device", adaptList);
                intent.putExtras(bundle);
                if (scene != null) {
                    intent.putExtra("sceneId", scene.sceneId);
                }
                intent.setClass(mContext, AddDeviceActivity.class);
                startActivityForResult(intent, DEVICE);
            }
        });

        adapter = new AddDeviceAdapter(mContext, adaptList);
        deviceGrid.setAdapter(adapter);
        /**
         * 长按删除设备
         */
        deviceGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                delete = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要删除此设备？", "取消", "确定", new View.OnClickListener() {
                    @SuppressWarnings("unused")
                    @Override
                    public void onClick(View v) {
                        delete.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (delete != null && delete.isShowing()) {
                            delete.dismiss();
                            showToast("删除成功");
                            adaptList.remove(adapter.getItem(position));
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                return true;
            }
        });
        deviceGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("type", "1");
                Bundle bundle = new Bundle();
                bundle.putSerializable("device", adapter.getItem(position));
                intent.putExtras(bundle);
                if (getActivityData().containsKey(adapter.getItem(position).productType)) {
                    intent.setClass(mContext, ClassUtils.getClass(getActivityData().get(adapter.getItem(position).productType)));
                    startActivityForResult(intent, DEVICEDETAIL);
                } else {
                    showToast("敬请期待！");
                }
                return;

            }
        });

        leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("device", adaptList);
                intent.putExtras(bundle);
                setResult(DEVICE, intent);
                finish();
            }
        });
    }


    /**
     * 从上一个页面返回，携带已选中设备并显示
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case DEVICE:
                Bundle bundle = data.getExtras();
                ArrayList<Device> list = (ArrayList<Device>) bundle.getSerializable("addDeviceList");
                for (Iterator<Device> iterator = list.iterator(); iterator.hasNext(); ) {
                    Device device = (Device) iterator.next();
                    adaptList.add(device);
                }
                adapter = new AddDeviceAdapter(mContext, adaptList);
                deviceGrid.setAdapter(adapter);
                break;
            case DEVICEDETAIL:
                //从设备详情返回，列表里替换修改后的设备预设信息
                String object = data.getStringExtra("object");
                JSONObject jsonObject = JSONObject.parseObject(object);
                if (jsonObject != null) {
                    for (Iterator<JSONObject> iterator = command.iterator(); iterator.hasNext(); ) {
                        JSONObject device = iterator.next();
                        if (device.get("ClientId").equals(jsonObject.get("ClientId"))) {
                            command.remove(device);
                        }
                    }
                    command.add(jsonObject);
                }
                Log.d(TAG, "DEVICEDETAILcommand--" + command.toString());
                break;
        }

    }

    @OnClick(R.id.commit)
    public void onViewClicked() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("device", adaptList);
        intent.putExtras(bundle);
        setResult(DEVICE, intent);
        finish();
    }

    public void getDevice(String id) {
        showProgress(0, true);
        client.getDeviceList(mContext, id, new DeviceListHandler() {
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
                adapter = new AddDeviceAdapter(mContext, adaptList);
                deviceGrid.setAdapter(adapter);
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("device", adaptList);
            intent.putExtras(bundle);
            setResult(DEVICE, intent);
            finish();
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }

    }
}
