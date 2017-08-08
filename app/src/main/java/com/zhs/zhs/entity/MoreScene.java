package com.zhs.zhs.entity;

import com.zhs.zhs.entity.device.Device;

import java.util.ArrayList;

/**
 * Created by admin on 2017/4/14.
 */

public class MoreScene extends Entity {
    public int code;
    public String msg;
    public ArrayList<Scene> data;

    public class Scene extends Entity {
        public String id;
        public String deviceId;
        public String name;
        public String sceneId;
        public String open;
        public String state;
        public String image;
        public String frequency;
        public String actionTime;
        public int actionType;
        public ArrayList<Device> devices;
    }

}
