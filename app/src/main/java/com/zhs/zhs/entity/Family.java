package com.zhs.zhs.entity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by admin on 2017/4/6.
 * 我的家庭信息实体类
 */

public class Family extends Entity {
    public Address address;
    public ArrayList<MyFamilys> familys;
    public ArrayList<Scenes> scenes;
    public boolean protectState;
    public boolean houseHolder;//是否是户主
    public int warnCount;
    public Map<String, Object> enviromentData;
    public Enviroment enviroment;

    public class Address extends Entity {
        public String address;
        public float lon;
        public float lat;

    }

    public class MyFamilys extends Entity {
        public String familyId;
        public String name;
        public boolean checked;
    }


    public class Scenes extends Entity {
        public String id;
        public String name;
        public boolean use;
    }

    public class FamilyData extends Entity {
        public int total;
        public String temperature;
        public String humidity;
        public String pm;
        public String hcho;//甲醛
        public String voc;
    }


    public class Enviroment extends Entity {
        public int areaId;
        public int deviceId;
        public String name;
        public String productType;
        public String clientId;
    }
}
