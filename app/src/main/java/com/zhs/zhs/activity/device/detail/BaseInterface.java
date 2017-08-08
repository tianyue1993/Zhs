package com.zhs.zhs.activity.device.detail;

/**
 * Created by admin on 2017/7/26.
 */

public interface BaseInterface {
    void setTitle();

    void setRightText();

    void backEvent();

    void startPush(String key, String tags);
}
