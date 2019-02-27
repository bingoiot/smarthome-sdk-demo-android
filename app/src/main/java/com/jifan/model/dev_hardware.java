package com.jifan.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ludy on 2017/4/19 0019.  硬件设备信息
 */

public class dev_hardware implements Serializable {
    private static final long serialVersionUID = -7060210542600464481L;


    private String mac;
    //名称
    private String name;
    //生产日期
    private String MFG;
    //设备版本号
    private int ver;
    //JDO 版本号
    private int jdoVer;
    //设备实时时间
    private String unixTime;
    //设备加密标志
    private boolean encrypte;
    // 远程操作锁
    private boolean remoteLock;
    //设备本地锁
    private boolean localLock;

    private boolean guestLock;
    //
    private String comkey;
    private String adkey;

    //
    private String guestey;

    private boolean shareLock;

    private List<dev_port> dev_portList;
    /**
     * 2管理员 3 一般用户 4 体验用户
     */
    private int keyid;

    //设备类型 dev_invalide= 0x00, dev_gateway = 0x01,dev_route		= 0x02,dev_device 03 ,dev_endDevice 04
    private int Devtype;


    // 通用设备属性 AID(多个, 号隔开) 数据属性
    private String aids;

    // 设备属性 Gen_Dev(多个, 号隔开) 设备属性 端口0
    private List<Integer> aids_gd;

    public List<Integer> getAids_gd() {
        return aids_gd;
    }

    public void setAids_gd(List<Integer> aids_gd) {
        this.aids_gd = aids_gd;
    }

    //设备体验用户锁
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMFG() {
        return MFG;
    }

    public void setMFG(String MFG) {
        this.MFG = MFG;
    }

    public int getVer() {
        return ver;
    }

    public void setVer(int ver) {
        this.ver = ver;
    }

    public int getJdoVer() {
        return jdoVer;
    }

    public void setJdoVer(int jdoVer) {
        this.jdoVer = jdoVer;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public boolean getEncrypte() {
        return encrypte;
    }

    public void setEncrypte(boolean encrypte) {
        this.encrypte = encrypte;
    }

    public boolean isRemoteLock() {
        return remoteLock;
    }

    public void setRemoteLock(boolean remoteLock) {
        this.remoteLock = remoteLock;
    }

    public boolean isLocalLock() {
        return localLock;
    }

    public void setLocalLock(boolean localLock) {
        this.localLock = localLock;
    }

    public boolean isGuestLock() {
        return guestLock;
    }

    public void setGuestLock(boolean guestLock) {
        this.guestLock = guestLock;
    }

    public String getComkey() {
        return comkey;
    }

    public void setComkey(String comkey) {
        this.comkey = comkey;
    }

    public String getGuestey() {
        return guestey;
    }

    public void setGuestey(String guestey) {
        this.guestey = guestey;
    }


    public int getKeyid() {
        return keyid;
    }

    public void setKeyid(int keyid) {
        this.keyid = keyid;
    }

    public int getDevtype() {
        return Devtype;
    }

    public void setDevtype(int devtype) {
        Devtype = devtype;
    }

    public boolean getShareLock() {
        return shareLock;
    }

    public void setShareLock(boolean shareLock) {
        this.shareLock = shareLock;
    }

    public String getkeys() {
        if (adkey != null && adkey != "") {

            return adkey;
        } else if (comkey != null && comkey != "") {

            return comkey;
        } else if (guestey != null && guestey != "") {
            return guestey;

        } else {
            return "";

        }

    }

    public String getAdkey() {
        return adkey;
    }

    public void setAdkey(String adkey) {
        this.adkey = adkey;
    }

    public String getAids() {
        return aids;
    }

    public void setAids(String aids) {
        this.aids = aids;
    }

    public List<dev_port> getDev_portList() {
        return dev_portList;
    }

    public void setDev_portList(List<dev_port> dev_portList) {
        this.dev_portList = dev_portList;
    }
}
