package com.zhs.zhs.entity.device;

import com.zhs.zhs.entity.device.Client;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/7/25.
 */

public class Concentrator extends Client {
    //PM2.5
    public BigDecimal PM = new BigDecimal(0);
    //温度
    public BigDecimal Temperature = new BigDecimal(0);
    //湿度
    public BigDecimal Humidity = new BigDecimal(0);
    //甲醛气体浓度
    public BigDecimal GasStrength = new BigDecimal(0);
}
