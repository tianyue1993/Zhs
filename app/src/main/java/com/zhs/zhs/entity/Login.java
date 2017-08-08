package com.zhs.zhs.entity;


/**
 * Created by admin on 2017/4/17.
 */

public class Login extends Entity {
    public int code;
    public String msg;
    public Data data;

    public class Data {
        public String mpushKey;
        public String fullName;
        public String mobileNumber;
        public String email;
        public String token;
        public String mpushTags;


    }
}
