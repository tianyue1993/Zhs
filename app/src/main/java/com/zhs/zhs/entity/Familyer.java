package com.zhs.zhs.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/4/18.
 */

public class Familyer extends Entity {

    public int code;
    public String msg;
    public ArrayList<MyFamilyer> data;

    public class MyFamilyer extends Entity {
        public String name;
        public String phone;
        public int accredit;//是否授权：1授权，0未授权
        public int memberId;
        public String familyId;
        public boolean impowerState;
        public int authType;
        public boolean expire;
    }

}
