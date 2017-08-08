package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
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

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPswdActivity extends Baseactivity {
    //旧密码
    @Bind(R.id.old_pwd)
    EditText oldPwd;
    //新密码
    @Bind(R.id.new_pwd)
    EditText newPwd;
    //再次确认
    @Bind(R.id.renew_pwd)
    EditText renewPwd;
    //眼睛
    @Bind(R.id.forget_show_pwd_check)
    CheckBox forgetShowPwdCheck;
    //修改
    @Bind(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pswd);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        setTitleTextView("修改密码", null);
        forgetShowPwdCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // true 为隐藏密码 false 为显示密码
                if (isChecked) {
                    //如果选中，隐藏密码
                    oldPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    newPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    renewPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则显示密码
                    oldPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    renewPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

    }


    @OnClick({R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                //修改密码
                String oldPass = oldPwd.getText().toString();
                String newPass = newPwd.getText().toString();
                String renewPass = renewPwd.getText().toString();
                if (oldPass.length() < 6 || newPass.length() < 6 || newPass.length() < 6) {
                    showToast(R.string.password_length_error);
                } else if (!newPass.trim().equalsIgnoreCase(renewPass.trim())) {
                    showToast("请检查密码是否一致");
                } else if (oldPass.trim().length() > 12 || newPass.trim().length() > 12 || renewPass.trim().length() > 12) {
                    showToast(R.string.password_length_error);
                } else {

                    JSONObject params = new JSONObject();
                    params.put("oldPwd", Md5Util.getMD5(oldPwd.getText().toString()));
                    params.put("newPwd", Md5Util.getMD5(newPwd.getText().toString()));
                    try {
                        StringEntity entity = new StringEntity(params.toString(), "UTF-8");
                        showProgress(0, true);
                        client.getResetpwd(this, entity, new CommentHandler() {

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
                                if (commen.code == 1) {
                                    exist();
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
                break;
        }
    }

    /**
     * 退出登录
     */
    private void exist() {
        cancelmDialog();
        showProgress(0, true);
        client.invokeExit(mContext, new CommentHandler() {
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
                if (commen.code == 1) {
                    finish();
                    prefs.clear();
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }
}
