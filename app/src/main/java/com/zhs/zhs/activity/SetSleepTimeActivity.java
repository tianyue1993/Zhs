package com.zhs.zhs.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhs.zhs.activity.device.detail.DeviceBase.controlDevice;

public class SetSleepTimeActivity extends Baseactivity {

    @Bind(R.id.commit)
    Button commit;
    @Bind(R.id.start)
    TextView start;
    @Bind(R.id.start_time)
    RelativeLayout startTime;
    @Bind(R.id.end)
    TextView end;
    @Bind(R.id.end_time)
    RelativeLayout endTime;

    public JSONObject object = new JSONObject();
    public ArrayList<JSONObject> command = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_sleep_time);
        ButterKnife.bind(this);
        setTitleTextView("设置休眠", null);

    }

    @OnClick({R.id.commit, R.id.start_time, R.id.end_time})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.commit:
                object.put("SleepBeginTime", start.getText().toString());
                object.put("SleepEndTime", end.getText().toString());
                command.clear();
                command.add(object);
                controlDevice(command);
                finish();
                break;
            case R.id.start_time:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View dataview = View.inflate(this, R.layout.set_time_layout, null);
                final TimePicker timePicker = (TimePicker) dataview.findViewById(R.id.time_picker);
                final Button commit = (Button) dataview.findViewById(R.id.commit);
                builder.setView(dataview);
                builder.setTitle("选择开始时间");
                final Dialog dialog = builder.create();
                timePicker.setIs24HourView(true);
                timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, final int hourOfDay, final int minute) {
                        start.setText("" + hourOfDay + " 时 " + minute + "分" + "00秒");
                    }
                });
                commit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.end_time:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                View dataview1 = View.inflate(this, R.layout.set_time_layout, null);
                final TimePicker timePicker1 = (TimePicker) dataview1.findViewById(R.id.time_picker);
                final Button commit1 = (Button) dataview1.findViewById(R.id.commit);
                builder1.setView(dataview1);
                builder1.setTitle("选择结束时间");
                final Dialog dialog1 = builder1.create();
                timePicker1.setIs24HourView(true);
                timePicker1.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    //这里传入的hourOfDay为小时值，minute为原分钟值
                    public void onTimeChanged(TimePicker view, final int hourOfDay, final int minute) {
                        end.setText("" + hourOfDay + " 时 " + minute + "分" + "00秒");
                    }
                });
                commit1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
        }
    }
}
