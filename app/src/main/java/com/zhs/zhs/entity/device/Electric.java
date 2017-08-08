package com.zhs.zhs.entity.device;

import com.zhs.zhs.entity.device.Client;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/7/6.
 */

public class Electric extends Client {
    //剩余电流
    public Integer ResidualCurrent = 0;
    //电流
    public BigDecimal ElectricCurrent_A = new BigDecimal(0);
    //电压
    public BigDecimal Voltage_A = new BigDecimal(0);
    //温度
    public BigDecimal Temperature_A = new BigDecimal(0);
    //环境温度
    public BigDecimal Temperature_E = new BigDecimal(0);
    //环境湿度
    public BigDecimal Humidity_E = new BigDecimal(0);
    //剩余电流预警或报警标记，0报警，1预警
    public Integer UpperAlarm_ResidualCurrent = -1;
    //A相电流预警或报警标记，0报警，1预警
    public Integer UpperAlarm_ElectricCurrent_A = -1;
    //A相低压预警或报警标记，0报警，1预警
    public Integer LowerAlarm_Voltage_A = -1;
    //A相高压预警或报警标记，0报警，1预警
    public Integer UpperAlarm_Voltage_A = -1;
    //A相温度预警或报警标记，0报警，1预警
    public Integer UpperAlarm_Temperature_A = -1;
    //环境温度预警或报警标记，0报警，1预警
    public Integer UpperAlarm_Temperature_E = -1;
    //环境湿度预警或报警标记，0报警，1预警
    public Integer UpperAlarm_Humidity_E = -1;
    //烟雾报警标记，0正常，1报警
    public Integer SmokeFlag = -1;
    //剩余电流传感器异常，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_ResidualCurrent = -1;
    //A相电流传感器异常，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_ElectricCurrent_A = -1;
    //A相电压传感器异常，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_Voltage_A = -1;
    //A相温度传感器异常，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_Temperature_A = -1;
    //环境温度传感器异常，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_Temperature_E = -1;
    //环境湿度传感器异常，0正常，1短路，2断路，3数据异常
    public Integer ExceptionCode_Humidity_E = -1;
    //脱扣器开关输出状态，1开关闭合，0开关断开
    public Integer SwitchOutput_F = -1;
    //脱扣器开关输入状态，1开关闭合状态，0开关断开状态
    public Integer SwitchInput_F = -1;
    //灭火器开关输出状态，1开关闭合，0开关断开
    public Integer SwitchOutput_G = -1;
    //灭火器开关输入状态，1开关闭合状态，0开关断开状态
    public Integer SwitchInput_G = -1;
    //报警器开关输出状态，1开关闭合，0开关断开
    public Integer SwitchOutput_H = -1;
    //报警器开关输入状态，1开关闭合状态，0开关断开状态
    public Integer SwitchInput_H = -1;
    //关联状态
    public String AssociatedStatus = "";
}
