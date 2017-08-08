package com.zhs.zhs.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.utils.Md5Util;
import com.zhs.zhs.utils.StringUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ForgetPswdActivity extends Baseactivity implements TextWatcher, View.OnClickListener {
    //跳转返回判断
    private static final int TO_REQUEST_CODE = 3;
    //输入手机号/邮箱
    @Bind(R.id.lost_phone)
    EditText lostPhone;
    //输入验证码
    @Bind(R.id.lost_code)
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
    private boolean getCodeType = true;

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
        setTitleTextView("找回密码", null);
//        getRightTextView().setText("邮箱找回");
//        getRightTextView().setVisibility(View.VISIBLE);
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
            case R.id.text_base_right:
                //切换找回方式
                toSetInputType();
                break;
            case R.id.clear_input:
                //清除
                lostPhone.setText("");
                break;

        }
    }

    //找回方式
    private void toSetInputType() {
        if (getRightTextView().getText().equals("邮箱找回")) {
            //切换到邮箱
            getRightTextView().setText("手机找回");
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
        } else {
            //切换到手机
            getRightTextView().setText("邮箱找回");
            lostType.setText("手机号：");
            getCodeType = true;
            if (lostPhone.getText().toString().isEmpty()) {
                lostPhone.setHint("请输入手机号");
            } else {
                lostPhone.setText("");
            }
            if (!lostCode.getText().toString().isEmpty()) {
                lostCode.setText("");
            }
            getRightTextView().setText("邮箱找回");
        }
    }

    //下一步
    private void toNext() {
        if (!StringUtils.isEmpty(lostCode.getText().toString().trim())) {

            if (Md5Util.getMD5(Md5Util.getMD5(lostCode.getText().toString().trim())).equals(prefs.getPhoneCode())) {
                Intent intent = new Intent(mContext, AddNewPswdActivity.class);
                intent.putExtra("input", lostPhone.getText().toString().trim());
                intent.putExtra("code", lostCode.getText().toString().trim());
                if (getCodeType) {
                    intent.putExtra("type", "phone");
                    startActivityForResult(intent, TO_REQUEST_CODE);
                } else {
                    intent.putExtra("type", "mail");
                    startActivityForResult(intent, TO_REQUEST_CODE);
                    startActivity(intent);
                }
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
        client.getPhoneCode(mContext, lostPhone.getText().toString().trim() + "?handleType=0", new CommentHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(Commen getCode) {
                super.onSuccess(getCode);
                cancelmDialog();
                showToast("发送成功");
                prefs.savePhoneCode(getCode.msg);
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
        object.put("handleType", "0");
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
                showToast("发送成功");
                prefs.savePhoneCode(getCode.msg);
                timer.start();
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //回到登录页面
        if (requestCode == TO_REQUEST_CODE) {
            finish();
        }
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
}
