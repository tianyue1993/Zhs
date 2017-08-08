package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ResetDeviceNameActivity extends Baseactivity {

    @Bind(R.id.feedback_contact_tv)
    EditText name;
    @Bind(R.id.commit)
    Button commit;
    private static final int DEVICERENAME = 15;
    public String areaId;
    public String tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_device_name);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.change_device_name), null);
        areaId = getIntent().getStringExtra("areaId");
        tvName = getIntent().getStringExtra("name");
        name.setText(tvName);
        name.setSelection(tvName.length());
        setRightText("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().length() < 2) {
                    showToast("请输入不少于2个字设备名称");
                } else {
                    updateDevice();
                }
            }
        });
        setLeftTextView(0, null);
    }


    /**
     * 修改设备状态和名称
     */
    public void updateDevice() {
        cancelmDialog();
        showProgress(0, true);
        JSONObject params = new JSONObject();
        params.put("areaId", areaId);
        params.put("name", name.getText().toString());
        try {
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            showProgress(0, true);
            client.getUpdateProduct(mContext, entity, new CommentHandler() {
                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }

                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    Intent intent = new Intent();
                    intent.putExtra("name", name.getText().toString());
                    setResult(DEVICERENAME, intent);
                    finish();
                }

                @Override
                public void onFailure(BaseException exception) {
                    super.onFailure(exception);
                    cancelmDialog();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

}
