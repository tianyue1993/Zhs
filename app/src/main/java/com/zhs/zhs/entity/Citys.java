package com.zhs.zhs.entity;


import java.util.ArrayList;

/**
 * Created by admin on 2017/7/31.
 */

public class Citys extends Entity {
    public ArrayList<D> d;
    public String c;
    public String n;

    @Override
    public String toString() {
        return "Citys{" +
                "d=" + d +
                ", c='" + c + '\'' +
                ", n='" + n + '\'' +
                '}';
    }
}
