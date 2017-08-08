package com.zhs.zhs.entity.device;


import com.zhs.zhs.entity.device.Client;

import java.math.BigDecimal;

/**
 * Created by xiangshiquan on 2017/6/13.
 */
public class Gas extends Client {

    //累计流量
    public BigDecimal TotalFlow = new BigDecimal(0);
    //压力值
    public BigDecimal PressureValue = new BigDecimal(0);
    //瞬时流量
    public BigDecimal CurrentFlow = new BigDecimal(0);
    //高压预警或报警标记，0报警，1预警
    public Integer UpperAlarm_PressureValue = -1;
    //低压预警或报警标记，0报警，1预警
    public Integer LowerAlarm_PressureValue = -1;
    //漏气报警标记，0无漏气，1漏气
    public Integer GasLeakFlag = -1;
    //电池电压过低报警标记
    public Integer LowerAlarm_Voltage = -1;
    //压力传感器异常码，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_PressureValue = -1;
    //流量传感器异常码，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_CurrentFlow = -1;
    //气阀输出状态，1开关闭合，0开关断开
    public Integer SwitchOutput = -1;
    //气阀输入状态，1开关闭合状态，0开关断开状态
    public Integer SwitchInput = -1;
    //休眠使能，0休眠，1不休眠
    public Integer SleepSwitch = -1;

    @Override
    public String toString() {
        return "Gas{" +
                "TotalFlow=" + TotalFlow +
                ", PressureValue=" + PressureValue +
                ", CurrentFlow=" + CurrentFlow +
                ", UpperAlarm_PressureValue=" + UpperAlarm_PressureValue +
                ", LowerAlarm_PressureValue=" + LowerAlarm_PressureValue +
                ", GasLeakFlag=" + GasLeakFlag +
                ", LowerAlarm_Voltage=" + LowerAlarm_Voltage +
                ", ExceptionCode_PressureValue=" + ExceptionCode_PressureValue +
                ", ExceptionCode_CurrentFlow=" + ExceptionCode_CurrentFlow +
                ", SwitchOutput=" + SwitchOutput +
                ", SwitchInput=" + SwitchInput +
                ", SleepSwitch=" + SleepSwitch +
                '}';
    }
}
