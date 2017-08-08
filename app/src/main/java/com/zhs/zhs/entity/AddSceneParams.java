package com.zhs.zhs.entity;

import com.zhs.zhs.entity.device.Device;

import java.util.ArrayList;

/**
 * Created by admin on 2017/5/28.
 */

public class AddSceneParams extends Entity {
    public String sceneName;
    public String type;
    public String actionType;
    public String actionTime;
    public String protectState;
    public ArrayList<Device> sceneDetailList;
    public String familyId;
    public String sceneId;
}
