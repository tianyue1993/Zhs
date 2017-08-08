package com.zhs.zhs.entity;

import java.util.ArrayList;

/**
 * Created by admin on 2017/4/15.
 */

public class News extends Entity {
    public int code;
    public String msg;
    public ArrayList<NewsData> data;

    public class NewsData extends Entity {

        public String _id;

        public boolean checked;

        public String clientId;

        public String clientType;

        public String createdTime;

        public int level;

        public String title;

        public int type;

        public int userId;


    }
}
