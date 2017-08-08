package com.zhs.zhs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.Familyer;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.utils.Md5Util;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddFamify extends Baseactivity {

    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.sure)
    Button sure;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.get_code)
    TextView getCode;
    @Bind(R.id.et_code)
    EditText etCode;

    public int type;//1为修改2是添加
    public Familyer.MyFamilyer familyer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_famify);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra("rivise").equals("1")) {
            type = 1;
            setTitleTextView(getString(R.string.revise_family), null);
            sure.setText(getString(R.string.revise_sure));
            Bundle bundle = getIntent().getExtras();
            familyer = (Familyer.MyFamilyer) bundle.getSerializable("member");
            editName.setText(familyer.name);
            etPhone.setText(familyer.phone);

        } else {
            type = 2;
            setTitleTextView(getString(R.string.add_family), null);
            sure.setText(getString(R.string.add_sure));
        }


    }

    @OnClick({R.id.sure, R.id.get_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sure:
                if (editName.getText().length() < 1) {
                    showToast(getString(R.string.add_family_name));
                } else if (etPhone.getText().length() < 1) {
                    showToast(getString(R.string.add_family_phone));
                } else if (etCode.getText().length() < 1) {
                    showToast(getString(R.string.code_null));
                } else {
                    if (Md5Util.getMD5(Md5Util.getMD5(etCode.getText().toString().trim())).equals(prefs.getPhoneCode())) {
                        saveMember();
                    } else {
                        showToast(getString(R.string.incorrect_input_code));
                    }

                }
                break;
            case R.id.get_code:
                if (etPhone.getText().length() == 11) {
                    getCode();
                } else {
                    showToast(getString(R.string.input_current_phone));
                }
                break;
        }
    }


    public void saveMember() {
        JSONObject object = new JSONObject();
        object.put("name", editName.getText().toString());
        object.put("phone", etPhone.getText().toString());
        object.put("verifyCode", etCode.getText().toString());
        if (type == 1) {
            object.put("memberId", familyer.memberId);
        }
        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            showProgress(0, true);
            client.getMemberSave(this, entity, new CommentHandler() {
                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }

                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    showToast(commen.msg);
                    sendBroadcast(new Intent("member_refresh"));
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

    public void getCode() {
        showProgress(0, true);
        client.getPhoneCode(mContext, etPhone.getText().toString().trim() + "?handleType=2", new CommentHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(Commen getCode) {
                super.onSuccess(getCode);
                cancelmDialog();
                prefs.savePhoneCode(getCode.msg);
                showToast(getString(R.string.send_success));
                timer.start();
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }

    // 注册 刷新验证码 倒计时
    CountDownTimer timer = new CountDownTimer(60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            getCode.setText(millisUntilFinished / 1000 + getString(R.string.second_resend));
            getCode.setClickable(false);
            getCode.setTextColor(Color.parseColor("#A8A8A8"));
        }

        @Override
        public void onFinish() {
            getCode.setClickable(true);
            getCode.setText(R.string.reget_check_code);
            getCode.setTextColor(Color.parseColor("#F7B23f"));
        }
    };
}
