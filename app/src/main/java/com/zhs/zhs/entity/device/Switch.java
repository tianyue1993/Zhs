package com.zhs.zhs.entity.device;

import com.zhs.zhs.entity.device.Client;

public class Switch extends Client {
    //第一路开关，1开关闭合，0开关断开
    public Integer SwitchOutput_A=-1;
    //第二路开关，1开关闭合，0开关断开
    public Integer SwitchOutput_B=-1;
    //第三路开关，1开关闭合，0开关断开
    public Integer SwitchOutput_C=-1;
    //休眠使能，0休眠，1不休眠
    public Integer SleepSwitch=-1;
}