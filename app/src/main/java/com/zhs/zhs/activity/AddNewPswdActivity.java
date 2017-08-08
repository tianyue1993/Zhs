package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.utils.Md5Util;
import com.zhs.zhs.utils.StringUtils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddNewPswdActivity extends Baseactivity implements TextWatcher {
    //跳转返回判断
    @Bind(R.id.forget_new_pwd)
    EditText forgetNewPwd;
    @Bind(R.id.forget_confirm_pwd)
    EditText forgetConfirmPwd;
    @Bind(R.id.forget_show_pwd_check)
    CheckBox forgetShowPwdCheck;
    @Bind(R.id.forget_commit)
    Button forgetCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_pswd);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.forget_pswd), null);
        forgetNewPwd.addTextChangedListener(this);
        forgetConfirmPwd.addTextChangedListener(this);
        forgetShowPwdCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，隐藏密码
                    forgetNewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    forgetConfirmPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则显示密码
                    forgetNewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    forgetConfirmPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @OnClick({R.id.forget_commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_commit:
                toCommitPwd();
                break;
        }
    }

    //确认提交
    private void toCommitPwd() {
        if (forgetNewPwd.getText().toString().trim().length() < 6 || forgetConfirmPwd.getText().toString().trim().length() < 6) {
            //判断长度
            showToast(R.string.password_length_error);
            return;
        } else if (!forgetNewPwd.getText().toString().trim().trim().equalsIgnoreCase(forgetConfirmPwd.getText().toString().trim().trim())) {
            //判断前后一致
            showToast(R.string.password_equals_error);
            return;
        } else if (forgetNewPwd.getText().toString().trim().trim().length() > 12 || forgetConfirmPwd.getText().toString().trim().trim().length() > 12) {
            showToast(R.string.password_length_error);
            return;
        }


        JSONObject object = new JSONObject();
        object.put("phone", getIntent().getStringExtra("input"));
        object.put("newPwd", Md5Util.getMD5(forgetConfirmPwd.getText().toString().trim()));
        object.put("verifyCode", getIntent().getStringExtra("code"));
        if (getIntent().getStringExtra("type").equals("phone")) {
            object.put("resetType", "0");
        } else {
            object.put("resetType", "1");
        }

        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            showProgress(0, false);
            client.getForgotpwd(this, entity, new CommentHandler() {

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
                    startActivity(new Intent(mContext, LoginActivity.class));
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //修改Button背景
        if (StringUtils.isEmpty(forgetNewPwd.getText().toString().trim()) || StringUtils.isEmpty(forgetConfirmPwd.getText().toString().trim())) {
            forgetCommit.setBackgroundResource(R.drawable.bg_base_unchecked);
            forgetCommit.setClickable(false);
        } else {
            forgetCommit.setBackgroundResource(R.drawable.bg_base);
            forgetCommit.setClickable(true);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
