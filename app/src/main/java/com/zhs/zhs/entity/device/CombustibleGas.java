package com.zhs.zhs.entity.device;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/8/2.
 */

public class CombustibleGas extends Client {

    //电池电压过低报警标记，-1正常，0报警
    public Integer LowerAlarm_Voltage = -1;
    //可燃气体浓度
    public BigDecimal GasStrength = new BigDecimal(0);
    //休眠使能
    public Integer SleepSwitch = -1;

}
