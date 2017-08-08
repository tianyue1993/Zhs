package com.zhs.zhs.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.device.Device;
import com.zhs.zhs.entity.MoreScene;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddSceneActivity extends Baseactivity {

    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.start)
    TextView start;
    @Bind(R.id.sure)
    Button sure;
    @Bind(R.id.device)
    RelativeLayout device;
    @Bind(R.id.frequency)
    TextView tvFrequency;
    @Bind(R.id.time)
    LinearLayout time;
    @Bind(R.id.open_check)
    CheckBox openCheck;
    TextView ifPadden;
    String frequency = "只重复一次";
    String actionTime;
    int actionType = 1;

    private ArrayList<Device> adaptList = new ArrayList<>();
    private static final int FREQUENCY = 10;
    private static final int DEVICE = 11;

    public MoreScene.Scene scene;
    int type = 1;//1是创建，2是修改
    String sceneId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_scene);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("type").equals("1")) {
            setTitleTextView(getString(R.string.add_scene), null);
            sure.setText(getString(R.string.add_sure));
            type = 1;
        } else {
            type = 2;
            setTitleTextView(getString(R.string.scene_setting), null);
            sure.setText(getString(R.string.revise_sure));
            scene = (MoreScene.Scene) getIntent().getExtras().getSerializable("scene");
            editName.setText(scene.name);
            start.setText(scene.actionTime);
            actionTime = scene.actionTime;
            actionType = scene.actionType;
            sceneId = scene.sceneId;
            adaptList = scene.devices;
            if (scene.actionType == 0) {
                tvFrequency.setText(getString(R.string.type1));
            } else if (scene.actionType == 1) {
                tvFrequency.setText(getString(R.string.type2));
            } else if (scene.actionType == 2) {
                tvFrequency.setText(getString(R.string.type3));
            } else if (scene.actionType == 3) {
                tvFrequency.setText(getString(R.string.type4));
            }


        }

    }

    @OnClick({R.id.sure, R.id.device, R.id.time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.time:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View dataview = View.inflate(this, R.layout.set_sleep_time_layout, null);
                final TimePicker timePicker = (TimePicker) dataview.findViewById(R.id.time_picker);
                final Button commit = (Button) dataview.findViewById(R.id.commit);
                ifPadden = (TextView) dataview.findViewById(R.id.if_padden);
                builder.setView(dataview);
                builder.setTitle("选择开始时间");
                final Dialog dialog = builder.create();
                timePicker.setIs24HourView(true);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    //这里传入的hourOfDay为小时值，minute为原分钟值
                    public void onTimeChanged(TimePicker view, final int hourOfDay, final int minute) {
                        start.setText("" + hourOfDay + " 时 " + minute + "分");
                        actionTime = "" + hourOfDay + " 时 " + minute + "分";
                    }
                });
                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                ifPadden.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivityForResult(new Intent(mContext, SelectPaddenWindowActivity.class), FREQUENCY);
                    }
                });
                dialog.show();
                break;
            case R.id.sure:
                if (editName.getText().length() < 1) {
                    showToast("场景名称不能为空");
                } else if (adaptList.size() < 1) {
                    showToast("您还没有设置场景设备");
                } else if (actionTime == null) {
                    showToast("您还没有设置场景时间");
                } else {
                    updataScene();
                }

                break;
            case R.id.device:
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("device", adaptList);
                intent.putExtras(bundle);
                intent.setClass(mContext, SceneDeviceActivity.class);
                startActivityForResult(intent, DEVICE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FREQUENCY:
                if (data.getStringExtra("frequency") != null) {
                    frequency = data.getStringExtra("frequency");
                    tvFrequency.setText(frequency);
                    ifPadden.setText(frequency);
                    if (frequency.equals("只重复一次")) {
                        actionType = 0;
                    } else if (frequency.equals("只重复一次")) {
                        actionType = 1;
                    } else if (frequency.equals("只重复一次")) {
                        actionType = 2;
                    } else if (frequency.equals("只重复一次")) {
                        actionType = 3;
                    }

                }
                break;
            case DEVICE:
                if (data != null) {
                    if (data.getExtras() != null) {
                        adaptList = (ArrayList<Device>) data.getExtras().getSerializable("device");
                    }
                }
                break;
        }


    }

    public void updataScene() {
        JSONObject object = new JSONObject();
        object.put("sceneName", editName.getText().toString());
        object.put("type", 1);
        object.put("actionTime", actionTime);
        object.put("actionType", actionType);
        object.put("sceneDetailList", adaptList);
        if (type == 2) {
            object.put("sceneId", sceneId);
        }

        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            client.getSceneSave(this, entity, commentHandler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public CommentHandler commentHandler = new CommentHandler() {

        @Override
        public void onStart() {
            super.onStart();
            cancelmDialog();
            showProgress(0, true);
        }

        @Override
        public void onCancel() {
            super.onCancel();
            cancelmDialog();
        }

        @Override
        public void onSuccess(Commen commen) {
            super.onSuccess(commen);
            showToast(commen.msg);
            cancelmDialog();
            sendBroadcast(new Intent("delete"));
            sendBroadcast(new Intent("homerefresh"));
            sendBroadcast(new Intent(" updatestate"));
            finish();
        }

        @Override
        public void onFailure(BaseException exception) {
            super.onFailure(exception);
            cancelmDialog();
        }
    };
}
