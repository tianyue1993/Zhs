package com.zhs.zhs.entity.device;

import com.zhs.zhs.entity.device.Client;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/7/25.
 */

public class Water extends Client {
    //累计流量
    public BigDecimal TotalFlow = new BigDecimal(0);
    //预留 压力值
    public BigDecimal PressureValue = new BigDecimal(0);
    //预留 瞬时流量
    public BigDecimal CurrentFlow = new BigDecimal(0);
    //预留 高压预警或报警标记，0报警，1预警
    public Integer UpperAlarm_PressureValue = -1;
    //预留 低压预警或报警标记，0报警，1预警
    public Integer LowerAlarm_PressureValue = -1;
    //漏水报警标记，0无漏水，1漏水
    public Integer WaterLeakFlag = -1;
    //电池电压过低报警标记，-1正常，0报警
    public Integer LowerAlarm_Voltage = -1;
    //预留 压力传感器异常码，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_PressureValue = -1;
    //预留 流量传感器异常码，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_TotalFlow = -1;
    //水阀输出状态，1开关闭合，0开关断开
    public Integer SwitchOutput = -1;
    //水阀输入状态，1开关闭合状态，0开关断开状态
    public Integer SwitchInput = -1;
    //休眠使能，0休眠，1不休眠
    public Integer SleepSwitch = -1;
}
