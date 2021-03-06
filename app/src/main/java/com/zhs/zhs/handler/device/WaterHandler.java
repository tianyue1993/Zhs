package com.zhs.zhs.handler.device;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhs.zhs.entity.device.WaterData;
import com.zhs.zhs.handler.BaseResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class WaterHandler extends BaseResponseHandler {

    public WaterHandler() {
        this(DEFAULT_CHARSET);
    }

    public WaterHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(WaterData commen) {

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
                                if (code == 1) {
                                    onSuccess(JSON.parseObject(response.toString(), WaterData.class));
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
        onFailure();
    }
}
