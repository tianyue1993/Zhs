package com.zhs.zhs.entity;

/**
 * Created by admin on 2017/5/4.
 */

public class AboutUs extends Entity {

    public int code;
    public String msg;
    public AboutUsData data;

    public class AboutUsData {
        public String website;//公司网址
        public String phone;//公司的电话
        public String version;//当前版本号
    }
}
