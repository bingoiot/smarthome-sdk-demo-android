package com.jifan.utils;

/**
 * Created by ludy on 2017/4/28 0028.
 */

import java.lang.reflect.Field;

public class ResourceMan {

    // 根据字符，获取id文件 int id = ResourceMan.getResId("icon", R.drawable.class);
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}