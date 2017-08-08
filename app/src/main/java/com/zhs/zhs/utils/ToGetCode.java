package com.zhs.zhs.utils;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;
import com.zhs.zhs.ApiClient;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.views.ProgressDialog;

/**
 * Created by admin on 2017/6/20.
 */

public class ToGetCode {
    public Context mContext;
    public ProgressDialog mProgress;
    ApiClient client;
    public boolean success = false;

    public ToGetCode(Context context) {
        mContext = context;
        client = ApiClient.getInstance();
    }

    public void showProgress(int resId, boolean cancel) {
        mProgress = new ProgressDialog(mContext);
        if (resId <= 0) {
            mProgress.setMessage(R.string.loading_data, cancel);
        } else {
            mProgress.setMessage(resId, cancel);
        }
        mProgress.show();
    }

    public void cancelmDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }


    //邮箱验证码
    private boolean toGetCode(String lostPhone) {
        //true 为手机验证码 false 为邮箱验证码
        showProgress(0, true);
        RequestParams object = new RequestParams();
        object.put("toAccount", lostPhone);
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
                Toast.makeText(mContext, getCode.msg, Toast.LENGTH_SHORT).show();
                success = true;
//                timer.start();
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
        return success;
    }

}
