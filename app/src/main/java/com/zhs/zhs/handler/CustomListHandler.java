package com.zhs.zhs.handler;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhs.zhs.entity.CustomData;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class CustomListHandler extends BaseResponseHandler {

    public CustomListHandler() {
        this(DEFAULT_CHARSET);
    }

    public CustomListHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(CustomData userFamily) {

    }

    public void onFailure() {
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
                                    onSuccess(JSON.parseObject(response.toString(), CustomData.class));
                                } else if (code == 0) {
                                    onFailure();
                                } else {
                                    onFailure();
                                }
                            } else {
                                onFailure();
                            }
                        } catch (JSONException e) {
                            onFailure();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
        Log.d("onFailure JSONObject", statusCode + "---" + throwable + "---" + errorResponse);
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
        Log.d("onFailure String", statusCode + "---" + throwable + "---" + responseString);
        onFailure();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable, errorResponse);
        Log.d("onFailure String", statusCode + "---" + throwable + "---" );
        onFailure();
    }
}
