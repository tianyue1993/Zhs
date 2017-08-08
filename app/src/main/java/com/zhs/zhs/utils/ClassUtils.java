package com.zhs.zhs.utils;

import java.util.HashMap;

/**
 * Created by admin on 2017/7/20.
 */

public class ClassUtils {

    private static HashMap<String,Class> cache=new HashMap<>();

    public static Class getClass(String className)
    {
        if(cache.containsKey(className))
        {
            return cache.get(className);
        }
        else {
            try {
                Class cls = getclass(className);
                cache.put(className,cls);
            }
            catch (Exception e)
            {
                 new Exception("不存在这个类");
            }
        }
        return cache.get(className);
    }

    private static Class getclass(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException//className是类名
    {
        Class obj=Class.forName(className); //以String类型的className实例化类
        return obj;
    }
}
