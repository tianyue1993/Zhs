package com.zhs.zhs.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/7/31.
 */

public class AdressData extends Entity {
    public ArrayList<Data> data;
    public String msg;
    public int code;

    public static class Data extends Entity {
        public ArrayList<Citys> d;
        public String c;
        public String n;
    }


}
