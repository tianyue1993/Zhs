package com.zhs.zhs.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class GlobalSetting {

    /**
     * 数据存储工具类
     */

    private static Context mContext;
    private static SharedPreferences mPrefs;
    private static SharedPreferences mPrefsLogin;
    private static GlobalSetting mSpInstance;

    private static final String USER_ID = "user_id";//用户ID
    private static final String USER_NAME = "user_name";//登录名
    private static final String USER_PHONE = "user_phone";
    private static final String USER_EMAIIL = "user_emaiil";
    private static final String USER_CUSTOM_NAME = "user_custom_name";//姓名
    private static final String PREFERENCE_NAME = "preference_name";
    private static final String PREFERENCE_NAME_LOGIN = "preference_name_login";
    private static final String USER_AVATAR = "user_avatar";// 是否第一次设置用户信息
    private static final String FAMILY_ID = "family_id";
    private static final String USER_ADDRESS = "user_address";
    private static final String USER_CURRENT_ADDRESS = "user_current_address";
    private static final String USER_LAN = "user_lan";
    private static final String USER_LON = "user_lon";

    private static final String CURRENT_LAN = "current_lan";
    private static final String CURRENT_LON = "current_lon";
    private static final String USER_CODE = "user_code";
    private static final String HOUSE_HOLDER = "house_holder";
    private static final String MPUSH_KEY = "mpush_key";
    private static final String MPUSH_TAG = "mpush_tag";
    private static final String WARN_COUNT = "warn_count";
    private static final String ADDRESS_DATA = "address_data";

    private GlobalSetting(Context context) {
        mContext = context;
    }

    // 单例
    public static synchronized GlobalSetting getInstance(Context context) {
        if (mSpInstance == null) {
            mSpInstance = new GlobalSetting(context);
        }
        return mSpInstance;
    }


    // 初始化sp对象
    public SharedPreferences getSharedPreferences() {
        if (mPrefs == null) {
            mPrefs = mContext.getSharedPreferences(PREFERENCE_NAME,
                    Context.MODE_PRIVATE);
        }
        return mPrefs;
    }

    public SharedPreferences getSharedPreferencesLogin() {
        if (mPrefsLogin == null) {
            mPrefsLogin = mContext.getSharedPreferences(PREFERENCE_NAME_LOGIN,
                    Context.MODE_PRIVATE);
        }
        return mPrefsLogin;
    }


    //下几项用户用户登录时使用
    //用户名
    public void saveLoginUserName(String value) {
        SharedPreferences.Editor editor = getSharedPreferencesLogin().edit();
        editor.putString(USER_NAME, value);
        editor.commit();
    }

    public String getLoginUserName() {
        return getSharedPreferencesLogin().getString(USER_NAME, "");
    }


    //用户头像
    public void saveLoginUserAvatar(String value) {
        SharedPreferences.Editor editor = getSharedPreferencesLogin().edit();
        editor.putString(USER_AVATAR, value);
        editor.commit();
    }

    public String getLoginUserAvatar() {
        return getSharedPreferencesLogin().getString(USER_AVATAR, "");
    }

    /**
     * 推出登陆清楚用户数据
     */
    public void clear() {
        getSharedPreferences().edit().clear().commit();
    }

    public void clear_Login() {
        getSharedPreferencesLogin().edit().clear().commit();
    }

    /**
     * 存储boolean类型数据
     *
     * @param key
     * @param value
     */
    private void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    private void saveFloat(String key, Float value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    private void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private void saveInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public void saveToken(String id) {
        saveString(USER_ID, id);
    }

    public String getToken() {
        return getSharedPreferences().getString(USER_ID, "");
    }


    public void saveMpushKey(String id) {
        saveString(MPUSH_KEY, id);
    }

    public String getMpushKey() {
        return getSharedPreferences().getString(MPUSH_KEY, "");
    }

    public void saveMpushTag(String id) {
        saveString(MPUSH_TAG, id);
    }

    public String getMpushTag() {
        return getSharedPreferences().getString(MPUSH_TAG, "");
    }

    /**
     * 用户ID
     *
     * @param id
     */
    public void saveFamilyId(String id) {
        saveString(FAMILY_ID, id);
    }

    public String getFamilyId() {
        return getSharedPreferences().getString(FAMILY_ID, "");
    }


    /**
     * 用户姓名
     *
     * @param id
     */
    public void saveCustomName(String id) {
        saveString(USER_CUSTOM_NAME, id);
    }

    public String getCustomName() {
        return getSharedPreferences().getString(USER_CUSTOM_NAME, "");
    }

    /**
     * 用户手机号
     *
     * @param id
     */
    public void saveUserPhone(String id) {
        saveString(USER_PHONE, id);
    }

    public String getUserPhone() {
        return getSharedPreferences().getString(USER_PHONE, "");
    }


    /**
     * 用户邮箱
     *
     * @param id
     */
    public void saveUserEmail(String id) {
        saveString(USER_EMAIIL, id);
    }

    public String getUserEmail() {
        return getSharedPreferences().getString(USER_EMAIIL, "");
    }

    public void saveAddress(String id) {
        saveString(USER_ADDRESS, id);
    }

    public String getAddress() {
        return getSharedPreferences().getString(USER_ADDRESS, "");
    }


    public void saveCurrentAddress(String id) {
        saveString(USER_CURRENT_ADDRESS, id);
    }

    public String getCurrentAddress() {
        return getSharedPreferences().getString(USER_CURRENT_ADDRESS, "");
    }


    public void saveLan(float id) {
        saveFloat(USER_LAN, id);
    }

    public float getLat() {
        return getSharedPreferences().getFloat(USER_LAN, 0);
    }


    public void saveCurrentLat(float id) {
        saveFloat(CURRENT_LAN, id);
    }

    public float getCurrentLat() {
        return getSharedPreferences().getFloat(CURRENT_LAN, 0);
    }

    public void saveCurrentLon(float id) {
        saveFloat(CURRENT_LON, id);
    }

    public float getCurrentLon() {
        return getSharedPreferences().getFloat(CURRENT_LON, 0);
    }

    public void saveLon(float id) {
        saveFloat(USER_LON, id);
    }

    public float getLon() {
        return getSharedPreferences().getFloat(USER_LON, 0);
    }

    public void savePhoneCode(String id) {
        saveString(USER_CODE, id);
    }

    public String getPhoneCode() {
        return getSharedPreferences().getString(USER_CODE, "");
    }

    public boolean getHouseHolder() {
//        return getSharedPreferences().getBoolean(HOUSE_HOLDER, false);
        return true;
    }

    public void saveHouseHolder(boolean householder) {
        saveBoolean(HOUSE_HOLDER, householder);
    }


    public void saveWarnCount(int id) {
        saveInt(WARN_COUNT, id);
    }

    public int getWarnCount() {
        return getSharedPreferences().getInt(WARN_COUNT, 0);
    }


    public void saveAddressData(String id) {
        saveString(ADDRESS_DATA, id);
    }

    public String getAddressData() {
        return getSharedPreferences().getString(ADDRESS_DATA, "[{\"name\":\"北京\",\"city\":[{\"name\":\"北京\",\"county\":[\"昌平\",\"朝阳\",\"大兴\",\"房山\",\"丰台\",\"海淀\",\"怀柔\",\"门头沟\",\"密云\",\"平谷\",\"石景山\",\"顺义\",\"通州\",\"宣武\",\"延庆\"]}]},{\"name\":\"安徽\",\"city\":[{\"name\":\"安庆\",\"county\":[\"大观\",\"怀宁\",\"潜山\",\"宿松\",\"太湖\",\"桐城\",\"望江\",\"宜秀\",\"迎江\",\"岳西\",\"枞阳\"]},{\"name\":\"蚌埠\",\"county\":[\"蚌山\",\"固镇\",\"淮上\",\"怀远\",\"龙子湖\",\"五河\",\"禹会\"]},{\"name\":\"亳州\",\"county\":[\"涡阳\",\"利辛\",\"蒙城\",\"谯城\"]},{\"name\":\"巢湖\",\"county\":[\"含山\",\"和县\",\"居巢\",\"庐江\",\"无为\"]},{\"name\":\"池州\",\"county\":[\"东至\",\"贵池\",\"青阳\",\"石台\"]},{\"name\":\"滁州\",\"county\":[\"定远\",\"凤阳\",\"来安\",\"琅玡\",\"明光\",\"南谯\",\"全椒\",\"天长\"]},{\"name\":\"阜阳\",\"county\":[\"阜南\",\"界首\",\"临泉\",\"太和\",\"颖东\",\"颖泉\",\"颍上\",\"颖州\"]},{\"name\":\"合肥\",\"county\":[\"包河\",\"长丰\",\"肥东\",\"肥西\",\"庐阳\",\"蜀山\",\"瑶海\"]},{\"name\":\"淮北\",\"county\":[\"杜集\",\"烈山\",\"濉溪\",\"相山\"]},{\"name\":\"淮南\",\"county\":[\"八公山\",\"大通\",\"凤台\",\"潘集\",\"田家庵\",\"谢家集\"]},{\"name\":\"黄山\",\"county\":[\"黄山\",\"徽州\",\"祁门\",\"歙县\",\"屯溪\",\"休宁\",\"黟县\"]},{\"name\":\"六安\",\"county\":[\"霍邱\",\"霍山\",\"金安\",\"金寨\",\"寿县\",\"舒城\",\"裕安\"]},{\"name\":\"马鞍山\",\"county\":[\"当涂\",\"花山\",\"金家庄\",\"雨山\"]},{\"name\":\"宿州\",\"county\":[\"砀山\",\"灵璧\",\"泗县\",\"萧县\",\"埇桥\"]},{\"name\":\"铜陵\",\"county\":[\"郊区\",\"狮子山\",\"铜官山\",\"铜陵\"]},{\"name\":\"芜湖\",\"county\":[\"繁昌\",\"镜湖\",\"鸠江\",\"南陵\",\"三山\",\"芜湖县\",\"弋江\"]},{\"name\":\"宣城\",\"county\":[\"广德\",\"绩溪\",\"旌德\",\"泾县\",\"郎溪\",\"宁国\",\"宣州\"]}]}]");
    }

}
