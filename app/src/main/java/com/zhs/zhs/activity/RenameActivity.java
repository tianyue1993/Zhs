package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
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
import butterknife.OnClick;


public class RenameActivity extends Baseactivity {

    @Bind(R.id.rename)
    EditText rename;
    @Bind(R.id.btn_next)
    Button btnNext;
    private static final int RENAME = 38;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.change_name), null);
    }

    private void updataUser() {
        JSONObject params = new JSONObject();
        params.put("username", rename.getText().toString());
        try {
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            showProgress(0, true);
            client.getChangeInfo(this, entity, new CommentHandler() {
                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }

                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    showToast("修改成功");
                    if (commen.code == 1) {
                        prefs.saveCustomName(rename.getText().toString());
                        Intent data = new Intent();
                        data.putExtra("name", rename.getText().toString());
                        setResult(RENAME, data);
                        sendBroadcast(new Intent("rename"));
                        finish();
                    }

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

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        if (rename.getText().toString().length() == 0) {
            showToast("请输入不少于1个字姓名");
        } else {
            updataUser();
        }
    }
}
