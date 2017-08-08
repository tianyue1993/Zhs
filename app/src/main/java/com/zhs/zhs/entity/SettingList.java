package com.zhs.zhs.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/6/2.
 */

public class SettingList extends Entity {
    public String msg;
    public int code;
    public SettingData data;

    public static class SettingData extends Entity {
        public ArrayList<NewsSettingData> list;
        public ArrayList<Template> template;
    }

    public static class Template extends Entity {
        public String id;
        public String name;
    }
}
