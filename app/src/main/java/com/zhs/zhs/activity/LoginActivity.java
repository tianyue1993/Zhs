package com.zhs.zhs.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhs.zhs.ApiClient;
import com.zhs.zhs.MainActivity;
import com.zhs.zhs.R;
import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.entity.Login;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.LoginHandler;
import com.zhs.zhs.utils.GlobalSetting;
import com.zhs.zhs.utils.Md5Util;
import com.zhs.zhs.utils.StringUtils;
import com.zhs.zhs.utils.ZhsUtils;
import com.zhs.zhs.views.ProgressDialog;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {


    @Bind(R.id.user)
    EditText user;
    @Bind(R.id.pwd)
    EditText pwd;
    @Bind(R.id.remember)
    CheckBox remember;
    @Bind(R.id.forget)
    TextView forget;
    @Bind(R.id.login)
    Button login;
    @Bind(R.id.activity_login)
    LinearLayout activityLogin;
    @Bind(R.id.avator)
    ImageView avator;

    Context mContext;
    @Bind(R.id.login_showh_pwd)
    CheckBox loginShowhPwd;
    @Bind(R.id.register)
    TextView register;


    //对话框
    private ProgressDialog mProgress;
    //存储
    private GlobalSetting prefs;
    //网络
    private ApiClient client;


    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    prefs.saveCurrentLat((float) aMapLocation.getLatitude());
                    prefs.saveCurrentLon((float) aMapLocation.getLongitude());
                    prefs.saveCurrentAddress(aMapLocation.getAddress());

                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
//                    adress.setText("定位失败,请检查网络或定位权限");

                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //让布局向上移来显示软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ZhsApplication.pausePush();
        mContext = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        prefs = GlobalSetting.getInstance(this);
        client = ApiClient.getInstance();
        if (!prefs.getLoginUserAvatar().equals("")) {
            ImageLoader.getInstance().displayImage(prefs.getLoginUserAvatar(), avator);
        }
        if (!prefs.getLoginUserName().equals("")) {
            user.setText(prefs.getLoginUserName());
        }


        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //开启权限访问对话框
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10003);

        } else {
            initLocation();
            mLocationOption.setOnceLocation(true);
            mLocationOption.setOnceLocationLatest(true);
            mLocationClient.setLocationOption(mLocationOption);
            mLocationClient.startLocation();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        initLocation();
        mLocationOption.setOnceLocation(true);
        mLocationOption.setOnceLocationLatest(true);
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @OnClick({R.id.remember, R.id.forget, R.id.login, R.id.login_showh_pwd, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget:
                startActivity(new Intent(mContext, ForgetPswdActivity.class));
                break;
            case R.id.login:
                toLogin();
                break;
            case R.id.login_showh_pwd:
                toShowHidePwd();
                break;
            case R.id.register:
                startActivity(new Intent(mContext, RegisterActivity.class));
                break;
        }
    }

    /**
     * 显示隐藏密码
     */
    private void toShowHidePwd() {
        if (loginShowhPwd.isChecked()) {
            //如果选中，显示密码
            pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            //否则隐藏密码
            pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }


    private void showProgress(int resId, boolean cancel) {
        mProgress = new ProgressDialog(this);
        if (resId <= 0) {
            mProgress.setMessage(R.string.loading_data, cancel);
        } else {
            mProgress.setMessage(resId, cancel);
        }
        mProgress.show();
    }

    private void cancelmDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }

    /**
     * 登录
     */
    private void toLogin() {
        if (!StringUtils.isEmpty(user.getText().toString().trim()) && !StringUtils.isEmpty(pwd.getText().toString().trim())) {
            JSONObject object = new JSONObject();
            object.put("uid", user.getText().toString().trim());
            object.put("pwd", Md5Util.getMD5(pwd.getText().toString().trim()));
            object.put("deviceId", ZhsUtils.readDeviceId(mContext));
            try {
                StringEntity entity = new StringEntity(object.toString(), "UTF-8");
                showProgress(0, true);
                client.getLogin(this, entity, loginHandler);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
    }


    public LoginHandler loginHandler = new LoginHandler() {
        @Override
        public void onCancel() {
            super.onCancel();
            cancelmDialog();
        }

        @Override
        public void onSuccess(Login login) {
            super.onSuccess(login);
            if (login.code == 1) {
                prefs.saveLoginUserName(user.getText().toString().trim());
                prefs.saveUserPhone(login.data.mobileNumber);
                prefs.saveToken(login.data.token);
                prefs.saveUserEmail(login.data.email);
                prefs.saveCustomName(login.data.fullName);
                prefs.saveMpushKey(login.data.mpushKey);
                prefs.saveMpushTag(login.data.mpushTags);
                ApiClient.getInstance().asyncHttpClient.addHeader("Authorization", login.data.token);
                ZhsApplication.resumePush();
                startActivity(new Intent(mContext, MainActivity.class));
                finish();
            } else {
                Toast.makeText(mContext, login.msg, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onFailure(BaseException exception) {
            cancelmDialog();
            super.onFailure(exception);

        }
    };

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(mContext);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);

    }

}
