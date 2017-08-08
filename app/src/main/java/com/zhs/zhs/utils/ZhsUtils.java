package com.zhs.zhs.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.File;

import static android.os.Environment.getExternalStorageDirectory;

public class ZhsUtils {
    private static final String SDCARD_ROOT = getExternalStorageDirectory().getPath();
    private static final String DOCTOR_SDCARD_PATH = SDCARD_ROOT
            + "/zhsaq_app";

    public static final String APP_PACKAGE_NAME = "com.zhsaq.family";
    public static final String APP_CACHE_PHONE_PATH = "/data/data/"
            + APP_PACKAGE_NAME + "/files/cache/";

    public static String getTmpCachePath() {
        String cachePath;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) == false) {
            cachePath = APP_CACHE_PHONE_PATH;
        } else {
            cachePath = DOCTOR_SDCARD_PATH + "/cache/";
        }

        tryMakePath(cachePath);

        return cachePath;
    }

    public static int pmToPercent(int total, String pm) {
        if (StringUtils.isEmpty(pm)) {
            return total;
        }
        double tpm = Double.parseDouble(pm);
        if (tpm >= 250d) {
            return 0;
        }
        if (tpm == 0d) {
            return total;
        }
        return (int) (total - (tpm / (250d / (double) total)));
    }

    public static String level(String pm) {
        int source = pmToPercent(100, pm) / 10;
        String result = "";
        switch (source) {
            case 9:
                result = "优";
                break;
            case 8:
            case 7:
                result = "良";
                break;
            case 6:
                result = "中";
                break;
            default:
                result = "差";
                break;
        }
        return result;
    }

    public static int pmToPercent(String pm) {
        return pmToPercent(180, pm);
    }

    /**
     * try to make the path if not existing, and change the mode of path to 777
     *
     * @param pathName
     */
    private static void tryMakePath(String pathName) {
        if (!new File(pathName).exists()) {
            new File(pathName).mkdirs();

            Process p;
            int status;
            try {
                p = Runtime.getRuntime().exec("chmod 777 " + pathName);
                status = p.waitFor();
                if (status == 0) {
                } else {
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * @param @param  ctx
     * @param @return 设定文件
     * @return String 返回类型
     * @throws
     * @Title: readDeviceId
     * @Description: 获取手机信息
     */
    public static String readDeviceId(Context ctx) {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            String time = Long.toString((System.currentTimeMillis() / (1000 * 60 * 60)));
            deviceId = time + time;
        }
        return deviceId;
    }
}
