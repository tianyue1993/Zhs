package com.zhs.zhs.handler;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhs.zhs.entity.Familyer;
import com.zhs.zhs.exception.BaseException;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class MemberListHandler extends BaseResponseHandler {

    public MemberListHandler() {
        this(DEFAULT_CHARSET);
    }

    public MemberListHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(Familyer familyer) {

    }

    public void onFailure(BaseException exception) {
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (response.has("code") && !response.isNull("code")) {
                                int code = response.getInt("code");
                                String messege = response.getString("msg");
                                if (code == 1) {
                                    onSuccess(JSON.parseObject(response.toString(), Familyer.class));
                                } else if (code == 0) {
                                    onFailure(new BaseException(messege, -1));
                                } else {
                                    onFailure(new BaseException("Unexpected response " + response, -1));
                                }
                            } else {
                                onFailure(new BaseException("Unexpected response " + response, -1));
                            }
                        } catch (JSONException e) {
                            onFailure(new BaseException(-1, e));
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.d("onFailure JSONObject", statusCode + "---" + throwable + "---" + errorResponse);
        onFailure(new BaseException(statusCode, throwable));
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d("onFailure String", statusCode + "---" + throwable + "---" + responseString);
        onFailure(new BaseException(statusCode, throwable));
    }
}
