package com.jifan.model;
import java.io.Serializable;
import java.util.List;

/**
 * Created by ludy on 2017/4/19 0019. 设备端口
 */
public class dev_port implements Serializable {
    private static final long serialVersionUID = -7060210544600464391L;

    //所属设备的 mac
    private String mac;

    //所属设备的 mac 16进制
    private byte[] devAddr;

    //端口index
    private int port;

    //端口名
    private String portname;

    //设备类型 aid 1开头
    private int aid_dev_type;

    // 通用设备属性 AID(多个, 号隔开) 数据属性
    private String aids;

    // 设备属性 Gen_Dev(多个, 号隔开) 设备属性 端口0
    private List<Integer> aids_gd;



   private  int area_id;
private  int dev_modelid;
    private  int keyid;

    private  String key;

    private  String regionname;
private  int dp_attenid;


    //信号值
    private  int signal;
   //通用设备属性与值
    List<aid_value> aid_valueList;

    private  String icon;


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public byte[] getDevAddr() {
        return devAddr;
    }

    public void setDevAddr(byte[] devAddr) {
        this.devAddr = devAddr;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPortname() {
        return portname;
    }

    public void setPortname(String portname) {
        this.portname = portname;
    }

    public int getAid_dev_type() {
        return aid_dev_type;
    }

    public void setAid_dev_type(int aid_dev_type) {
        this.aid_dev_type = aid_dev_type;
    }

    public String getAids() {
        return aids;
    }

    public void setAids(String aids) {
        this.aids = aids;
    }


    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getArea_id() {
        return area_id;
    }

    public void setArea_id(int area_id) {
        this.area_id = area_id;
    }



    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getDp_attenid() {
        return dp_attenid;
    }

    public void setDp_attenid(int dp_attenid) {
        this.dp_attenid = dp_attenid;
    }

    public int getSignal() {
        return signal;
    }

    public void setSignal(int signal) {
        this.signal = signal;
    }

    public List<Integer> getAids_gd() {
        return aids_gd;
    }

    public void setAids_gd(List<Integer> aids_gd) {
        this.aids_gd = aids_gd;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public int getDev_modelid() {
        return dev_modelid;
    }

    public void setDev_modelid(int dev_modelid) {
        this.dev_modelid = dev_modelid;
    }

    public List<aid_value> getAid_valueList() {
        return aid_valueList;
    }

    public void setAid_valueList(List<aid_value> aid_valueList) {
        this.aid_valueList = aid_valueList;
    }
}
