package com.zhs.zhs;


import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.zhs.zhs.handler.BaseResponseHandler;
import com.zhs.zhs.utils.GlobalSetting;

import org.apache.http.entity.StringEntity;

import static com.zhs.zhs.ZhsApplication.ADDCUSTOM;
import static com.zhs.zhs.ZhsApplication.ADDRESSDATA;
import static com.zhs.zhs.ZhsApplication.ALLREAD;
import static com.zhs.zhs.ZhsApplication.CHANGENUMBER;
import static com.zhs.zhs.ZhsApplication.CHANGEUSERINFO;
import static com.zhs.zhs.ZhsApplication.CUSTOMERLIST;
import static com.zhs.zhs.ZhsApplication.DEFAULTFAMILY;
import static com.zhs.zhs.ZhsApplication.DELETE;
import static com.zhs.zhs.ZhsApplication.DEVICECONTROL;
import static com.zhs.zhs.ZhsApplication.DEVICEDETAIL;
import static com.zhs.zhs.ZhsApplication.DEVICEINSCENE;
import static com.zhs.zhs.ZhsApplication.EDITCUSTOM;
import static com.zhs.zhs.ZhsApplication.EXIT;
import static com.zhs.zhs.ZhsApplication.FAMILYCHANGE;
import static com.zhs.zhs.ZhsApplication.FEEDBACK;
import static com.zhs.zhs.ZhsApplication.FORGOTPWD;
import static com.zhs.zhs.ZhsApplication.GETCODE;
import static com.zhs.zhs.ZhsApplication.GETEMAILCODE;
import static com.zhs.zhs.ZhsApplication.IMPOWERAPPLY;
import static com.zhs.zhs.ZhsApplication.IMPOWERLIST;
import static com.zhs.zhs.ZhsApplication.ISREAD;
import static com.zhs.zhs.ZhsApplication.LOGIN;
import static com.zhs.zhs.ZhsApplication.MEMBERLIST;
import static com.zhs.zhs.ZhsApplication.MEMBERREMOVE;
import static com.zhs.zhs.ZhsApplication.MEMBERSAVE;
import static com.zhs.zhs.ZhsApplication.MSGCOUNT;
import static com.zhs.zhs.ZhsApplication.MSGLIST;
import static com.zhs.zhs.ZhsApplication.REGISTER;
import static com.zhs.zhs.ZhsApplication.RESETPWD;
import static com.zhs.zhs.ZhsApplication.SCANADD;
import static com.zhs.zhs.ZhsApplication.SCENEAPPLY;
import static com.zhs.zhs.ZhsApplication.SCENEDEL;
import static com.zhs.zhs.ZhsApplication.SCENELIST;
import static com.zhs.zhs.ZhsApplication.SCENESAVE;
import static com.zhs.zhs.ZhsApplication.SETTINGADD;
import static com.zhs.zhs.ZhsApplication.SETTINGEDIT;
import static com.zhs.zhs.ZhsApplication.SETTINGLIST;
import static com.zhs.zhs.ZhsApplication.SETTINGREMOVE;
import static com.zhs.zhs.ZhsApplication.UPDATEPRODUCT;
import static com.zhs.zhs.ZhsApplication.UPDATESTATE;
import static com.zhs.zhs.ZhsApplication.UPDATEVERSION;
import static com.zhs.zhs.ZhsApplication.getContext;


/**
 * 网络请求封装
 */
public class ApiClient {
    public AsyncHttpClient asyncHttpClient;

    private volatile static ApiClient instance;

    public static ApiClient getInstance() {
        if (instance == null) {
            synchronized (ApiClient.class) {
                if (instance == null) {
                    instance = new ApiClient();
                    if (GlobalSetting.getInstance(getContext()).getToken() != null && !GlobalSetting.getInstance(getContext()).getToken().equals("")) {
                        instance.asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(getContext()).getToken());
                    }
                }
            }
        } else {
            if (GlobalSetting.getInstance(getContext()).getToken() != null && !GlobalSetting.getInstance(getContext()).getToken().equals("")) {
                instance.asyncHttpClient.addHeader("Authorization", GlobalSetting.getInstance(getContext()).getToken());
            }
        }
        return instance;
    }

    /**
     * AsyncHttpClient初始化
     */
    private ApiClient() {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setURLEncodingEnabled(false);
        asyncHttpClient.setTimeout(30000);
        asyncHttpClient.setMaxRetriesAndTimeout(0, 10000);
        asyncHttpClient.addHeader("Content-Type", "application/json;charset=UTF-8");
    }

    /**
     * 登录接口
     */
    public void getLogin(Context context, StringEntity entity, BaseResponseHandler handler) {
        String url = LOGIN();
        asyncHttpClient.post(context, url, entity, "application/json", handler);
    }

    /**
     * 获取验证码
     */
    public void getPhoneCode(Context context, String url, BaseResponseHandler handler) {
        asyncHttpClient.get(context, GETCODE() + url, handler);
    }


    /**
     * 修改用户个人信息
     */

    public void getUpdateNews(Context context, StringEntity entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, CHANGENUMBER(), entity, "application/json", handler);
    }

    public void getForgotpwd(Context context, StringEntity entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, FORGOTPWD(), entity, "application/json", handler);
    }

    public void getResetpwd(Context context, StringEntity entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, RESETPWD(), entity, "application/json", handler);
    }

    /**
     * 退出登录
     *
     * @param context
     * @param handler
     */
    public void invokeExit(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, EXIT(), handler);
    }

    /**
     * 保存场景
     *
     * @param context
     * @param entity
     * @param handler
     */
    public void getSceneSave(Context context, StringEntity entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, SCENESAVE(), entity, "application/json", handler);
    }

    /**
     * 获取未在场景里的设备
     *
     * @param context
     * @param handler
     */
    public void getDeviceNotInScene(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, IMPOWERLIST(), handler);
    }

    /**
     * 获取邮箱验证码
     *
     * @param context
     * @param entity
     * @param handler
     */
    public void getEmailcode(Context context, RequestParams entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, GETEMAILCODE(), entity, handler);
    }

    public void getSceneDelet(Context context, String url, BaseResponseHandler handler) {
        asyncHttpClient.get(context, SCENEDEL() + url, handler);
    }

    public void getSceneList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, SCENELIST(), handler);
    }

    public void getSceneApply(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, SCENEAPPLY() + id, handler);
    }

    public void getDeviceList(Context context, String sceneId, BaseResponseHandler handler) {
        asyncHttpClient.get(context, DEVICEINSCENE() + sceneId, handler);
    }

    public void getDefaultFamily(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, DEFAULTFAMILY(), handler);
    }

    public void getFeedBack(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, FEEDBACK(), params, "application/json", handler);
    }


    public void getFamilyChange(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, FAMILYCHANGE() + "?familyId=" + id, handler);
    }


    public void getUptateState(Context context, RequestParams params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, UPDATESTATE(), params, handler);
    }

    public void getSettingList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, SETTINGLIST(), handler);
    }

    public void getSettingAdd(Context context, StringEntity entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, SETTINGADD(), entity, "application/json", handler);
    }

    public void getSettingEdit(Context context, StringEntity entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, SETTINGEDIT(), entity, "application/json", handler);
    }

    public void getSettingRemove(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, SETTINGREMOVE() + id, handler);
    }


    public void getMemberList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, MEMBERLIST() + "?familyId=" + GlobalSetting.getInstance(context).getFamilyId(), handler);
    }

    public void getMemberSave(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, MEMBERSAVE(), params, "application/json", handler);
    }

    public void getChangeInfo(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, CHANGEUSERINFO(), params, "application/json", handler);
    }

    public void getMemberRemove(Context context, String params, BaseResponseHandler handler) {
        asyncHttpClient.get(context, MEMBERREMOVE() + params, handler);
    }

    public void getImpowerApply(Context context, StringEntity entity, BaseResponseHandler handler) {
        asyncHttpClient.post(context, IMPOWERAPPLY(), entity, "application/json", handler);
    }

    public void getImpowerList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, IMPOWERLIST(), handler);
    }

    public void getUpdateProduct(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, UPDATEPRODUCT(), params, "application/json", handler);
    }

    public void getUpdateVersion(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, UPDATEVERSION(), params, "application/json", handler);
    }


    public void getDeviceControl(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, DEVICECONTROL(), params, "application/json", handler);
    }

    public void getMsgList(Context context, String index, BaseResponseHandler handler) {
        asyncHttpClient.get(context, MSGLIST(index), handler);
    }

    public void getIsRead(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, ISREAD(id), handler);
    }


    public void getAllRead(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, ALLREAD(), handler);
    }

    public void getMsgCount(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, MSGCOUNT(), handler);
    }


    public void getDeviceDetail(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, DEVICEDETAIL(id), handler);
    }

    public void getRegester(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, REGISTER(), params, "application/json", handler);
    }

    public void getCustomerList(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, CUSTOMERLIST(), handler);
    }

    public void getAddressData(Context context, BaseResponseHandler handler) {
        asyncHttpClient.get(context, ADDRESSDATA(), handler);
    }

    public void getAddCustom(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, ADDCUSTOM(), params, "application/json", handler);
    }

    public void getEditCustom(Context context, StringEntity params, BaseResponseHandler handler) {
        asyncHttpClient.post(context, EDITCUSTOM(), params, "application/json", handler);
    }

    public void getScanAdd(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, SCANADD() + id, handler);
    }


    public void getDeleteDevice(Context context, String id, BaseResponseHandler handler) {
        asyncHttpClient.get(context, DELETE() + id, handler);
    }
}
