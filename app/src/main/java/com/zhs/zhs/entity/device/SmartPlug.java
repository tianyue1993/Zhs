package com.zhs.zhs.entity.device;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/7/6.
 */

public class SmartPlug extends Client {
    //220V输出状态，1开关闭合，0开关断开
    public Integer SwitchOutput_A = -1;
    //USB输出状态，1开关闭合，0开关断开
    public Integer SwitchOutput_B = -1;
    //休眠使能，0休眠，1不休眠
    public Integer SleepSwitch = -1;
    //插座温度
    public BigDecimal Temperature = new BigDecimal(0);
}