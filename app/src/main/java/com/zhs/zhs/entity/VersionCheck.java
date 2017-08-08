package com.zhs.zhs.entity;

/**
 * Created by admin on 2017/6/7.
 */

public class VersionCheck extends Entity {
    public String msg;
    public int code;
    public VersionData data;

    public class VersionData {
        public String versionId;
        public String downloadUrl;
        public boolean updateState;
        public String describe;
    }
}
