package com.zhs.zhs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
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

import static com.zhs.zhs.R.id.lost_code;

public class ResetPhoneActivity extends Baseactivity implements TextWatcher, View.OnClickListener {
    //输入手机号/邮箱
    @Bind(R.id.lost_phone)
    EditText lostPhone;
    //输入验证码
    @Bind(lost_code)
    EditText lostCode;
    //获取验证码
    @Bind(R.id.get_code)
    TextView getCode;
    //下一步
    @Bind(R.id.lost_commit)
    Button lostCommit;
    //找回方式
    @Bind(R.id.lost_type)
    TextView lostType;
    //清除
    @Bind(R.id.clear_input)
    ImageView clearInput;

    /**
     * 获取验证码方式 true 为手机验证码 false 为邮箱验证码
     */
    private boolean getCodeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pswd);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    //杂项
    private void initView() {
        if (getIntent().getStringExtra("type").equals("1")) {
            setTitleTextView("修改手机号", null);
            getCodeType = true;
            lostType.setText("手机号：");
            if (lostPhone.getText().toString().isEmpty()) {
                lostPhone.setHint("请输入手机号");
            } else {
                lostPhone.setText("");
            }
            if (!lostCode.getText().toString().isEmpty()) {
                lostCode.setText("");
            }
            getRightTextView().setText("邮箱找回");
        } else {
            setTitleTextView("修改邮箱", null);
            getCodeType = false;
            lostType.setText("邮　箱：");
            getCodeType = false;
            if (lostPhone.getText().toString().isEmpty()) {
                lostPhone.setHint("请输入邮箱");
            } else {
                lostPhone.setText("");
            }
            if (!lostCode.getText().toString().isEmpty()) {
                lostCode.setText("");
            }
        }

        lostCommit.setClickable(false);
    }

    //添加监听
    private void initListener() {
        getRightTextView().setOnClickListener(this);
        lostPhone.addTextChangedListener(this);
        lostCode.addTextChangedListener(this);
    }

    @OnClick({R.id.get_code, R.id.lost_commit, R.id.clear_input})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_code:
                //获取验证
                toGetCode();
                break;
            case R.id.lost_commit:
                //下一步
                toNext();
                break;
            case R.id.clear_input:
                //清除
                lostPhone.setText("");
                break;

        }
    }


    //下一步
    private void toNext() {
        if (!StringUtils.isEmpty(lostCode.getText().toString().trim())) {
            if (Md5Util.getMD5(Md5Util.getMD5(lostCode.getText().toString().trim())).equals(prefs.getPhoneCode())) {
                updataUser();
            } else {
                showToast("验证码输入有误");
            }


        } else {
            if (getCodeType) {
                showToast("请输入手机号！");
            } else {
                showToast("请输入邮箱！");
            }
        }
    }

    //获取验证码
    private void toGetCode() {
        if (!StringUtils.isEmpty(lostPhone.getText().toString().trim())) {
            if (getCodeType) {
                toGetPhoneCode();
            } else {
                toGetEmialCode();
            }
        } else {
            if (getCodeType) {
                showToast("请输入手机号！");
            } else {
                showToast("请输入邮箱！");
            }
        }
    }

    //获取短信
    private void toGetPhoneCode() {
        //true 为手机验证码 false 为邮箱验证码
        showProgress(0, true);
        client.getPhoneCode(mContext, lostPhone.getText().toString().trim() + "?handleType=1", new CommentHandler() {
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
                Log.d("Reqeust", "Reqeust: " + getCode.msg);
                showToast("发送成功");
                timer.start();

            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }


    //邮箱验证码
    private void toGetEmialCode() {
        //true 为手机验证码 false 为邮箱验证码
        showProgress(0, true);
        RequestParams object = new RequestParams();
        object.put("toAccount", lostPhone.getText().toString().trim());
        object.put("handleType", "3");
        client.getEmailcode(mContext, object, new CommentHandler() {
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
                showToast("发送成功");
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
            getCode.setText(millisUntilFinished / 1000 + "秒后重发");
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    /**
     * 根据输入框，显示删除图标
     * 下一步按钮是否可点击
     */

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!StringUtils.isEmpty(lostPhone.getText().toString().trim())) {
            clearInput.setVisibility(View.VISIBLE);
        } else {
            clearInput.setVisibility(View.GONE);
        }
        if (StringUtils.isEmpty(lostPhone.getText().toString().trim()) || StringUtils.isEmpty(lostCode.getText().toString().trim())) {
            lostCommit.setBackgroundResource(R.drawable.bg_base_unchecked);
            lostCommit.setClickable(false);
        } else {
            lostCommit.setBackgroundResource(R.drawable.bg_base);
            lostCommit.setClickable(true);
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /**
     * 更改用户信息
     */
    private void updataUser() {
        JSONObject params = new JSONObject();
        params.put("number", lostPhone.getText().toString());
        params.put("verifyCode", lostCode.getText().toString());
        if (getCodeType) {
            params.put("type", "1");//修改手机号
        } else {
            params.put("type", "2");//修改邮箱
        }
        try {
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            showProgress(0, true);
            client.getUpdateNews(this, entity, new CommentHandler() {

                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }

                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    showToast("修改成功，请重新登陆");
                    if (commen.code == 1) {
                        exist();
                        prefs.saveLoginUserName(lostPhone.getText().toString().trim());
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

    /**
     * 更改完成，退出登录，引导用户重新登陆
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
                prefs.clear();
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }
}
