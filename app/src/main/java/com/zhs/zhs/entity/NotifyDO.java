package com.zhs.zhs.entity;

import com.zhs.zhs.entity.device.Client;

import java.util.Map;

/**
 * Created by admin on 2017/7/6.
 */

public class NotifyDO extends Client {
    public String msgId;
    public String title;
    public String content;
    public Integer nid; //主要用于聚合通知，非必填
    public Byte flags; //特性字段。 0x01:声音   0x02:震动 0x03:闪灯
    public String largeIcon; // 大图标
    public String ticker; //和title一样
    public Integer number;
    public Map<String, String> extras;
}
