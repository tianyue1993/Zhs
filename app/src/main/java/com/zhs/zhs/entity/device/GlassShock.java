package com.zhs.zhs.entity.device;

/**
 * Created by admin on 2017/8/2.
 */

public class GlassShock extends Client {


    //门开关状态，01门关，00门开
    public Integer SwitchInput = -1;
    //电池电压过低报警标记，-1正常，0报警
    public Integer LowerAlarm_Voltage = -1;
    //休眠使能
    public Integer SleepSwitch = -1;
    //玻璃震荡报警标记，-1正常，0报警
    public Integer UpperAlarm_SwitchInput = -1;

}
