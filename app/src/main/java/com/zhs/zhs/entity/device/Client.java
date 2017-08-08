package com.zhs.zhs.entity.device;

import com.zhs.zhs.entity.Entity;

/**
 * Created by xiangshiquan on 2017/6/13.
 */
public class Client extends Entity {
    //终端Id
    public String ClientId = "";
    //集中器Id
    public String ConcentratorId = "";
    //终端类型
    public Integer ClientType = 0;
    //服务器IP地址
    public String ServerIP = "";
    //域名
    public String DomainName = "";
    //服务器端口
    public Integer ServerPort = 0;
    //终端IP地址
    public String ClientIP = "";
    //终端端口
    public Integer ClientPort = 0;
    //最近一次上传数据时间
    public String CurrentTime = "";
    //物联网手机卡号
    public String IoTPhoneNumber = "";
    //单位ID
    public Integer CustomerId = 0;
    //单位名称
    public String CustomerName = "";
    //监控区域Id
    public Integer MonitorAreaId = 0;
    //监控区域名称
    public String MonitorAreaName = "";
    //终端状态
    public String ClientStatus = "";
    //报警数据标记
    public Integer AlarmDataFlag = 0;
}
