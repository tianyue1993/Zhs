package com.zhs.zhs.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.utils.ChooseCityInterface;
import com.zhs.zhs.utils.ChooseCityUtil;
import com.zhs.zhs.utils.Md5Util;
import com.zhs.zhs.utils.StringUtils;
import com.zhs.zhs.utils.ZhsUtils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhs.zhs.R.id.captcha_send;

public class RegisterActivity extends Baseactivity {

    @Bind(R.id.phone)
    EditText phone;
    @Bind(R.id.captcha)
    EditText captcha;
    @Bind(captcha_send)
    Button captchaSend;
    @Bind(R.id.pwd)
    EditText pwd;
    @Bind(R.id.pwd_comfirm)
    EditText pwdComfirm;
    @Bind(R.id.register)
    Button register;
    @Bind(R.id.et_address)
    TextView etAddress;
    @Bind(R.id.choose_address)
    RelativeLayout chooseAddress;
    String areaCode = "";
    @Bind(R.id.login_showh_pwd)
    CheckBox loginShowhPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        setTitleTextView("注册", null);
        loginShowhPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    pwdComfirm.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    pwdComfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @OnClick({captcha_send, R.id.register, R.id.choose_address})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case captcha_send:
                if (phone.getText().length() == 11) {
                    getCode();
                } else {
                    showToast(getString(R.string.input_current_phone));
                }
                break;
            case R.id.register:
                if (phone.getText().length() < 1) {
                    showToast("手机号不能为空！");
                    return;
                } else if (captcha.getText().length() < 1) {
                    showToast("验证码不能为空！");
                    return;
                } else if (pwd.getText().length() < 1) {
                    showToast("密码不能为空！");
                    return;
                } else if (!pwd.getText().toString().equals(pwdComfirm.getText().toString())) {
                    showToast("两次密码输入不一致！");
                    return;
                } else if (!Md5Util.getMD5(Md5Util.getMD5(captcha.getText().toString().trim())).equals(prefs.getPhoneCode())) {
                    showToast(getString(R.string.incorrect_input_code));
                    return;
                } else {
                    register();
                }
                break;
            case R.id.choose_address:
                chooseCityDialog(etAddress);
                break;
            default:
                break;

        }

    }


    //Choose Date 选择省市县
    public void chooseCityDialog(View view) {
        final ChooseCityUtil cityUtil = new ChooseCityUtil();
        String[] oldCityArray = etAddress.getText().toString().split("-");//将TextView上的文本分割成数组 当做默认值
        cityUtil.createDialog(this, oldCityArray, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                //oldCityArray为传入的默认值 newCityArray为返回的结果
                etAddress.setText(newCityArray[0] + "-" + newCityArray[1] + "-" + newCityArray[2]);
                areaCode = newCityArray[3];
            }
        });
    }

    public void getCode() {
        showProgress(0, true);
        client.getPhoneCode(mContext, phone.getText().toString().trim() + "?handleType=2", new CommentHandler() {
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
            captchaSend.setText(millisUntilFinished / 1000 + getString(R.string.second_resend));
            captchaSend.setClickable(false);
            captchaSend.setTextColor(Color.parseColor("#A8A8A8"));
        }

        @Override
        public void onFinish() {
            captchaSend.setClickable(true);
            captchaSend.setText(R.string.reget_check_code);
            captchaSend.setTextColor(Color.parseColor("#F7B23f"));
        }
    };


    public void register() {
        JSONObject object = new JSONObject();
        object.put("pwd", pwd.getText().toString());
        object.put("phone", phone.getText().toString());
        object.put("verifyCode", captcha.getText().toString());
        object.put("deviceId", ZhsUtils.readDeviceId(mContext));
        object.put("lat", prefs.getCurrentLat());
        object.put("lon", prefs.getCurrentLon());
        if (!StringUtils.isEmpty(areaCode)) {
            object.put("areaCode", areaCode);
            object.put("address", etAddress.getText().toString());
        } else {
            showToast("请选择地址！");
            return;
        }

        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            showProgress(0, true);
            client.getRegester(this, entity, new CommentHandler() {
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
