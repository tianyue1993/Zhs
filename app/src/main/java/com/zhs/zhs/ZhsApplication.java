package com.zhs.zhs;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import com.amap.api.navi.AMapNavi;
import com.mpush.client.ClientConfig;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zhs.zhs.mpush.MPush;
import com.zhs.zhs.utils.GlobalPrefrence;
import com.zhs.zhs.utils.ZhsUtils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by tianyue on 2017/3/29.
 * 全局Application
 */
public class ZhsApplication extends Application {


    public static Context mCon;

    public static ZhsApplication application;
    /**
     * 接口跟Url
     */
    public static String BASE_URL;

    /**
     * 添加的打开过的Activity
     */
    public static ArrayList<Activity> activities = new ArrayList<Activity>();

    /**
     * 添加Activity  一键退出调用这个方法
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    /**
     * Activity 的一键退出
     */
    public static void finishActivity() {
        for (int i = 0; i < activities.size(); i++) {
            activities.get(i).finish();
        }
    }

    /**
     * 初始化域名信息
     */
    private void initDoMain() {
        //获取本应用程序信息
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = getPackageManager().getApplicationInfo(this.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //读取gradle信息
        if (null != applicationInfo) {
            this.BASE_URL = applicationInfo.metaData.getString("serverDoMain");
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mCon = getApplicationContext();
        application = this;
        initDoMain();
        initImageLoader();
        AMapNavi.setApiKey(this, "545508bc258eb7fc98a00a1b0494ca3a");
    }


    private static void initPush(String allocServer, String userId, String tag) {
        //公钥有服务端提供和私钥对应
        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";
        ClientConfig cc = ClientConfig.build()
                .setPublicKey(publicKey)
                .setAllotServer(allocServer)
                .setDeviceId(ZhsUtils.readDeviceId(mCon))
                .setClientVersion(BuildConfig.VERSION_NAME)
                .setEnableHttpProxy(true)
                .setUserId(userId)
                .setTags(tag);
        MPush.I.checkInit(mCon).setClientConfig(cc);
    }


    public static void startPush(String key, String tag) {
        initPush(GlobalPrefrence.MushServiceAddress, key, tag);
        MPush.I.checkInit(mCon).startPush();
        MPush.I.bindAccount(key, tag);
    }


    public static void unbindUser() {
        MPush.I.unbindAccount();
    }


    public static void pausePush() {
        MPush.I.pausePush();
    }

    public static void resumePush() {
        MPush.I.resumePush();
    }


//    private static void initDevicePush(String allocServer, String userId, String tagd) {
//        //公钥有服务端提供和私钥对应
//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCghPCWCobG8nTD24juwSVataW7iViRxcTkey/B792VZEhuHjQvA3cAJgx2Lv8GnX8NIoShZtoCg3Cx6ecs+VEPD2fBcg2L4JK7xldGpOJ3ONEAyVsLOttXZtNXvyDZRijiErQALMTorcgi79M5uVX9/jMv2Ggb2XAeZhlLD28fHwIDAQAB";
//        ClientConfig cc = ClientConfig.build()
//                .setPublicKey(publicKey)
//                .setAllotServer(allocServer)
//                .setDeviceId(ZhsUtils.readDeviceId(mCon))
//                .setClientVersion(BuildConfig.VERSION_NAME)
//                .setEnableHttpProxy(true)
//                .setUserId(userId);
//        com.zhs.zhs.devicempush.MPush.I.checkInit(mCon).setClientConfig(cc);
//    }


    public static void startDevicePush(String key, String tag) {
        initPush(GlobalPrefrence.MushServiceAddress, key, tag);
        com.zhs.zhs.devicempush.MPush.I.checkInit(mCon).startPush();
        com.zhs.zhs.devicempush.MPush.I.bindAccount(key, tag);
    }


    public static void pauseDevicePush() {
        com.zhs.zhs.devicempush.MPush.I.pausePush();
    }

    public static void resumeDevicePush() {
        com.zhs.zhs.devicempush.MPush.I.resumePush();
    }

    /**
     * 初始化图片加载框架
     */
    private void initImageLoader() {
        DisplayImageOptions defaultDisplayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_INT).bitmapConfig(Bitmap.Config.RGB_565).build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).defaultDisplayImageOptions(defaultDisplayImageOptions).threadPriority(Thread.NORM_PRIORITY).discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.FIFO).memoryCache(new WeakMemoryCache()).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 获取上下文对象
     *
     * @return
     */
    public static Context getContext() {
        return application;
    }

    /**
     * @return 服务器域名
     */
    public static String getServerDoMain() {
        return BASE_URL;
    }

    public static String LOGIN() {
        return BASE_URL + "user/login";
    }

    /**
     * 获取验证码
     *
     * @return
     */
    public static String GETCODE() {
        return BASE_URL + "user/code/";
    }


    /**
     * 邮箱获取验证码
     *
     * @return
     */
    public static String GETEMAILCODE() {
        return BASE_URL + "user/send/mail";
    }


    /**
     * 忘记密码
     *
     * @return
     */
    public static String FORGOTPWD() {
        return BASE_URL + "user/forgot/pwd";
    }

    /**
     * 修改密码
     *
     * @return
     */
    public static String RESETPWD() {
        return BASE_URL + "user/update/pwd";
    }

    /**
     * 退出登录
     *
     * @return
     */
    public static String EXIT() {
        return BASE_URL + "user/logout";
    }

    /**
     * 场景创建、修改
     *
     * @return
     */
    public static String SCENESAVE() {
        return BASE_URL + "scene/save";
    }

    public static String SCENEDEL() {
        return BASE_URL + "scene/delete/";
    }

    public static String SCENELIST() {
        return BASE_URL + "scene/list?familyId=";
    }

    public static String SCENEAPPLY() {
        return BASE_URL + "scene/apply/cancel?Id=";
    }

    public static String DEVICEINSCENE() {
        return BASE_URL + "user/device/inScenelist?sceneId=";
    }


    public static String DEFAULTFAMILY() {
        return BASE_URL + "home/index";
    }


    public static String FEEDBACK() {
        return BASE_URL + "feedback/user/feedback";
    }


    public static String FAMILYCHANGE() {
        return BASE_URL + "home/family/change";
    }


    public static String UPDATESTATE() {
        return BASE_URL + "protect/update/state";
    }

    public static String SETTINGLIST() {
        return BASE_URL + "settings/list";
    }

    public static String SETTINGEDIT() {
        return BASE_URL + "settings/edit";
    }

    public static String SETTINGREMOVE() {
        return BASE_URL + "settings/remove?id=";
    }

    public static String SETTINGADD() {
        return BASE_URL + "settings/add";
    }


    public static String MEMBERLIST() {
        return BASE_URL + "customer/member/list";
    }

    public static String MEMBERSAVE() {
        return BASE_URL + "customer/member/save";
    }

    public static String MEMBERREMOVE() {
        return BASE_URL + "customer/member/remove?id=";
    }


    public static String IMPOWERAPPLY() {
        return BASE_URL + "customer/member/authorization?memberId=";
    }

    public static String IMPOWERLIST() {
        return BASE_URL + "user/device/list";
    }

    public static String UPDATEPRODUCT() {
        return BASE_URL + "user/device/update";
    }

    public static String UPDATEVERSION() {
        return BASE_URL + "version/get";
    }

    public static String CHANGENUMBER() {
        return BASE_URL + "user/change/number";
    }

    public static String CHANGEUSERINFO() {
        return BASE_URL + "user/change/userInfo";
    }

    public static String DEVICECONTROL() {
        return BASE_URL + "user/device/Control?control";
    }

    public static String MSGLIST(String index) {
        return BASE_URL + "msg/list?rows=10&index=" + index;
    }

    public static String ISREAD(String index) {
        return BASE_URL + "msg/isRead?id=" + index;
    }

    public static String ALLREAD() {
        return BASE_URL + "msg/all/read";
    }


    public static String MSGCOUNT() {
        return BASE_URL + "msg/count";
    }

    public static String DEVICEDETAIL(String id) {
        return BASE_URL + "user/device/last/data?clientId=" + id;
    }

    public static String CUSTOMERLIST() {
        return BASE_URL + "customer/list";
    }


    public static String REGISTER() {
        return BASE_URL + "user/register";
    }


    public static String ADDRESSDATA() {
        return BASE_URL + "";
    }

    public static String ADDCUSTOM() {
        return BASE_URL + "customer/add";
    }

    public static String EDITCUSTOM() {
        return BASE_URL + "customer/edit";
    }


    public static String SCANADD() {
        return BASE_URL + "user/device/scan?clientId=";
    }


    public static String DELETE() {
        return BASE_URL + "user/device/delete?clientId=";
    }

    private static HashMap<String, String> typeCache = new HashMap<>();

    static {
//        typeCache.put("0000", "com.zhs.zhs.activity.device.ConcentratorActivity");//环境监测仪
//        typeCache.put("0001", "com.zhs.zhs.activity.device.WaterInductionActivity");//水表
//        typeCache.put("0002", "com.zhs.zhs.activity.device.GasActivity");//气表
//        typeCache.put("0005", "com.zhs.zhs.activity.device.DoorAlarmActivity");//门磁报警
//        typeCache.put("0006", "com.zhs.zhs.activity.device.GlassShockActivity");//玻璃震荡器
//        typeCache.put("0009", "com.zhs.zhs.activity.device.CombustibleGasActivity");//可燃气体探测器
//        typeCache.put("0015", "com.zhs.zhs.activity.device.ElectricMonitorActivity");//单相电电气火灾探测器
//        typeCache.put("0016", "com.zhs.zhs.activity.device.SmartSwitchActivity");//智能开关
//        typeCache.put("0017", "com.zhs.zhs.activity.device.SmartPlugActivity");//智能插座
//        typeCache.put("0018", "com.zhs.zhs.activity.device.CurtainActivity");//窗帘

        typeCache.put("0001", "com.zhs.zhs.activity.device.ConcentratorActivity");//环境监测仪“无”
        typeCache.put("0801", "com.zhs.zhs.activity.device.WaterInductionActivity");//水表
        typeCache.put("0701", "com.zhs.zhs.activity.device.GasActivity");//气表
        typeCache.put("0401", "com.zhs.zhs.activity.device.DoorAlarmActivity");//门磁报警
        typeCache.put("0501", "com.zhs.zhs.activity.device.GlassShockActivity");//玻璃震荡器
        typeCache.put("0601", "com.zhs.zhs.activity.device.CombustibleGasActivity");//可燃气体探测器
        typeCache.put("0901", "com.zhs.zhs.activity.device.ElectricMonitorActivity");//单相电电气火灾探测器“无”
        typeCache.put("0201", "com.zhs.zhs.activity.device.SmartSwitchActivity");//智能开关
        typeCache.put("0101", "com.zhs.zhs.activity.device.SmartPlugActivity");//智能插座
        typeCache.put("0301", "com.zhs.zhs.activity.device.CurtainActivity");//窗帘
    }

    public static HashMap<String, String> getActivityData() {
        return typeCache;
    }

}
