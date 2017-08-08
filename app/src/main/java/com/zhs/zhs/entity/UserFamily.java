package com.zhs.zhs.entity;

import java.math.BigDecimal;

/**
 * Created by admin on 2017/7/31.
 */

public class UserFamily extends Entity {
    public Integer id;
    public String name;
    public String address;
    public String areaCode;
    public boolean isDefault;
    public BigDecimal lat;
    public BigDecimal lon;

}
