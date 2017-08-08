package com.zhs.zhs.entity;

/**
 * Created by admin on 2017/5/11.
 */

public class FamilyState extends Entity {
    public String msg;
    public int code;
    public StateData data;

    public class StateData {
        public String id;
        public String familyId;
        public boolean userId;
        public int state;
    }
}
