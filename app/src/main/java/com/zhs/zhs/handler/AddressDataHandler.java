package com.zhs.zhs.handler;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.entity.AdressData;
import com.zhs.zhs.utils.GlobalSetting;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class AddressDataHandler extends BaseResponseHandler {

    public AddressDataHandler() {
        this(DEFAULT_CHARSET);
    }

    public AddressDataHandler(String encoding) {
        super(encoding);
    }

    public void onSuccess(AdressData adressData) {

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
                                String messege = response.getString("data");
                                if (code == 1) {
                                    onSuccess(JSON.parseObject(messege, AdressData.class));
                                    GlobalSetting.getInstance(ZhsApplication.getContext()).saveAddressData(messege);
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
        Log.d("onFailure String", statusCode + "---" + throwable + "---");
        onFailure();
    }
}
