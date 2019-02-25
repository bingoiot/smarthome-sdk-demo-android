package com.jifan.model;

/**
 * Created by ludy on 2017/4/28 0028.  种子 aid类型
 */

public class aid_seed {

    /// <summary>
    ///   主键
    /// </summary>
    private int id;
    /// <summary>
    ///   值(唯一)
    /// </summary>
    private String values;
    /// <summary>
    ///   描述
    /// </summary>
    private String describe;
    /// <summary>
    ///   图标
    /// </summary>
    private String icon;
    /// <summary>
    ///   类型
    /// </summary>
    private String typeid;
    /// <summary>
    ///   数据（范围 |  隔开   或 开始值~结束值）
    /// </summary>
    private String data;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTypeid() {
        return typeid;
    }

    public void setTypeid(String typeid) {
        this.typeid = typeid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
