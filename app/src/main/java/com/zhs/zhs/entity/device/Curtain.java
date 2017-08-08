package com.zhs.zhs.entity.device;

import com.zhs.zhs.entity.device.Client;

/**
 * Created by admin on 2017/8/2.
 */

public class Curtain extends Client {
    //窗帘开度
    public Integer CurtainOpening = 0;
    //休眠使能，0休眠，1不休眠
    public Integer SleepSwitch = -1;
}
