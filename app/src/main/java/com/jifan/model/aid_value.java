package com.jifan.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/4/29.
 */

public class aid_value implements Serializable {
    private static final long serialVersionUID = -7060211454600464391L;
    //
private  int aid;
    //数据类型
    private byte dtype;

    private  String values;
    public aid_value()
    {


    }
    public aid_value(int _aid,byte _dtype,String _values)
    {
        this.aid=_aid;
        this.dtype=_dtype;
        this.values=_values;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public byte getDtype() {
        return dtype;
    }

    public void setDtype(byte dtype) {
        this.dtype = dtype;
    }
}
