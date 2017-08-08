package com.zhs.zhs.exception;

import android.content.Intent;
import android.widget.Toast;

import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.activity.LoginActivity;
import com.zhs.zhs.utils.GlobalSetting;

import static com.zhs.zhs.R.id.code;

/**
 * 异常信息处理Exception基类封装
 */
public class BaseException extends Exception {

    private static final long serialVersionUID = 1L;

    private int statusCode = -1;

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(Exception cause) {
        super(cause);
    }

    public BaseException(String msg, int statusCode) {
        super(msg);
        this.statusCode = statusCode;
        if (code == 401) {
            exceptionCode("您的账号在其他设备登录，请重新登陆");
            ZhsApplication.unbindUser();
        } else if (code == 405) {
            exceptionCode("对不起，该功能暂时为对您开放");
        } else {
            Toast.makeText(ZhsApplication.getContext(), msg, Toast.LENGTH_LONG).show();
        }

    }

    public BaseException(String msg, Exception cause) {
        super(msg, cause);
    }

    public BaseException(String msg, Exception cause, int statusCode) {
        super(msg, cause);
        this.statusCode = statusCode;

    }

    public BaseException(int code, String msg, Throwable throwable) {
        super(msg, throwable);
        this.statusCode = code;
    }

    public BaseException(int code, Throwable throwable) {
        super(throwable);
        this.statusCode = code;

        if (code == 401) {
            exceptionCode("您的账号在其他设备登录，请重新登陆");
            ZhsApplication.unbindUser();
        } else if (code == 405) {
            exceptionCode("对不起，该功能暂时为对您开放");
        } else {
            Toast.makeText(ZhsApplication.getContext(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public int getStatusCode() {
        return this.statusCode;
    }


    /**
     * 表示当前账号被异地登录，返回登录页面
     */
    protected synchronized void exceptionCode(String string) {
        Toast.makeText(ZhsApplication.getContext(), string, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ZhsApplication.getContext(), LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ZhsApplication.getContext().startActivity(intent);
        GlobalSetting.getInstance(ZhsApplication.getContext()).clear();
    }

}
