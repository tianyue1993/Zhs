package com.zhs.zhs.activity.device.detail;

/**
 * Created by admin on 2017/7/26.
 */

public interface DeviceBaseInterface extends BaseInterface {
    void getDetail(String clintId);

    void reName(String areaId);

    void registerReceiver();

}
