package com.jifan.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jifan.model.aid_value;
import com.jifan.model.dev_hardware;
import com.jifan.model.dev_port;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pluto.*;

/**
 * Created by Administrator on 2017/4/29.
 */

public class DevManage {
    // //获取端口信息
    public static List<dev_port> getdevport(String mac) {
        byte[] devAddr = Clib.hexToBytes(mac);
        return getdevport(devAddr);
    }

    //获取设备信息
    public static dev_hardware getDevInfo(String mac) {
        byte[] devAddr = Clib.hexToBytes(mac);
        return getDevInfo(devAddr);
    }

    //获取设备信息
    public static dev_hardware getDevInfo(byte[] devAddr) {
        dev_hardware item = new dev_hardware();
        String name = DeviceHelper.getDeviceName(devAddr);
        if (name == null) {
            name = "";
        }
        String MFG = DeviceHelper.getDeviceMFG(devAddr);
        if (MFG == null) {
            MFG = "";
        }
        int ver = DeviceHelper.getDeviceVerSion(devAddr);
        int jdoVer = 0;//ApsHelper.getJdoVerSion(devAddr);
        String unixTime = DeviceHelper.getDeviceTime(devAddr);
        if (unixTime == null) {
            unixTime = "";
        }
        boolean encrypte = DeviceHelper.getEncryptFlag(devAddr);
        boolean remoteLock = DeviceHelper.getRemoteLockFlag(devAddr);
        boolean localLock = DeviceHelper.getLocalLockFlag(devAddr);
        boolean guestLock = DeviceHelper.getGuestLockFlag(devAddr);
        boolean ShareLock = DeviceHelper.getShareLockFlag(devAddr);
        int devtype = DeviceHelper.getDevicetype(devAddr);
        byte keyID = DeviceHelper.KeyID.Default;
        String guestey = Clib.bytes2Hex(DeviceHelper.getDeviceKey(devAddr, DeviceHelper.KeyID.Guest), '\0');
        if (guestey == null) {
            guestey = "";
        } else {
            keyID = DeviceHelper.KeyID.Guest;
        }
        String comkey = Clib.bytes2Hex(DeviceHelper.getDeviceKey(devAddr, DeviceHelper.KeyID.Common), '\0');
        if (comkey == null) {
            comkey = "";
        } else {
            keyID = DeviceHelper.KeyID.Common;
        }
        String adkey = Clib.bytes2Hex(DeviceHelper.getDeviceKey(devAddr, DeviceHelper.KeyID.Admin), '\0');
        if (adkey == null) {
            adkey = "";
        } else {
            keyID = DeviceHelper.KeyID.Admin;
        }

        item.setName(name);
        item.setMac(Clib.bytes2Hex(devAddr, '\0'));
        item.setMFG(MFG);
        item.setVer(ver);
        item.setJdoVer(jdoVer);
        item.setUnixTime(unixTime);
        item.setEncrypte(encrypte);
        item.setRemoteLock(remoteLock);
        item.setLocalLock(localLock);
        item.setGuestLock(guestLock);
        item.setDevtype(devtype);
        item.setShareLock(ShareLock);
        item.setAdkey(adkey);
        item.setComkey(comkey);
        item.setGuestey(guestey);
        item.setKeyid(keyID);
        item.setAids(getdevIid(devAddr));

        List<Integer> GD_list = new ArrayList<Integer>();
        if(item.getAids()!=null&&item.getAids()!="") {
            String[] aids = item.getAids().split(",");
            for (String aid : aids) {
                if (aid != "") {
                    int a_id = Integer.parseInt(aid);
                    GD_list.add(a_id);
                }
            }
        }
        item.setAids_gd(GD_list);

        return item;
    }

    // //获取端口信息
    public static List<dev_port> getdevport(byte[] devAddr) {//获取 端口列表
        List<dev_port> list = new ArrayList<dev_port>();
        byte[] port = DeviceHelper.getPortList(devAddr);
        if (port != null) {
            for (int i = 0; i < port.length; i++) {//输出每个point的信息，逐个循环
                {
                    dev_port item = getdevportInit(devAddr, port[i]);
                    list.add(item);
                }
            }
        }
        return list;
    }

    //获取设备属性aid
    public static String getdevIid(byte[] devAddr) {
        int[] aID = getdevIidArry(devAddr);//获取当前point的aID列表，转成字符串
        String aid_Gens = "";
        if (aID != null) {
            for (int j = 0; j < aID.length; j++) {
                aid_Gens += aID[j] + ",";
            }
            if (aid_Gens.length() > 0) {
                aid_Gens = aid_Gens.substring(0, aid_Gens.length() - 1);
            }
        }
        return aid_Gens;
    }

    //获取设备属性aid
    public static int[] getdevIidArry(byte[] devAddr) {
        int[] aID = DeviceHelper.getPortAttributeList(devAddr, (byte) 0);//获取当前point的aID列表，转成字符串
        return aID;
    }


    public static dev_port getdevportInit(byte[] devAddr, byte port) {
        dev_hardware devHardware = DevManage.getDevInfo(devAddr);

        int[] aID = DeviceHelper.getPortAttributeList(devAddr, port);//获取当前point的aID列表，转成字符串
        int appID = DeviceHelper.getPortAppID(devAddr,port);
        dev_port item = new dev_port();
        item.setAid_dev_type(appID);
        String aid_Gens = "";
        if (aID != null)
            for (int j = 0; j < aID.length; j++) {
                aid_Gens += aID[j] + ",";
                /*2018.10.9开始，application ID 与attribute ID 不放在同一个数组内，并且不在同一个空间中定义
                if (aID[j] >= 4096 && aID[j] < 32768) {//小于0x008000   大于等于0x001000 是设备类型  小于1000是设备属性  大于等于8000是 数据属性
                    item.setAid_dev_type(aID[j]);
                } else {
                    aid_Gens += aID[j] + ",";
                }*/
            }
        if (aid_Gens.length() > 0) {
            aid_Gens = aid_Gens.substring(0, aid_Gens.length() - 1);
        }
        item.setAids(aid_Gens);
        item.setPort(port);
        item.setMac(Clib.bytes2Hex(devAddr, '\0'));
        item.setDevAddr(devAddr);
        item.setPortname(DeviceHelper.getPortName(devAddr, port));
        item.setKeyid(devHardware.getKeyid());
        return item;
    }

    public static dev_port getdevportInit(byte[] devAddr, String portInfoStr) {
        try {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            Type clz = new TypeToken<portInfo>() {
            }.getType();
            portInfo _model = gson.fromJson(portInfoStr, clz);
            dev_port item = new dev_port();
            String aid_Gens = "";
            int app_id = _model.getApp_id();
            item.setAid_dev_type(app_id);
            for (int j = 0; j < _model.getAttr_id().size(); j++) {//获取当前point的aID列表，转成字符串
                aid_Gens += _model.getAttr_id().get(j) + ",";
                /*2018.9.10开始不再按此规则，attribute ID 与 application ID 不在同一空间中定义
                if (_model.getaID().get(j) >= 4096 && _model.getaID().get(j) < 32768) {//小于0x008000   大于等于0x001000 是设备类型  小于1000是设备属性  大于等于8000是 数据属性
                    item.setAid_dev_type(_model.getaID().get(j));
                } else {
                    aid_Gens += _model.getaID().get(j) + ",";
                }*/
            }
            aid_Gens = aid_Gens.substring(0, aid_Gens.length() - 1);
            item.setAids(aid_Gens);
            item.setPort(_model.getPort());
            item.setMac(Clib.bytes2Hex(devAddr, '\0'));
            item.setDevAddr(devAddr);
            item.setPortname(_model.getName());
            return item;
        } catch (Exception ex) {
            Log.e("devmanage", ex.getMessage(), ex);
            return null;
        }

    }

    private class portInfo {
        private int port;
        private String name;
      //  private List<Integer> attr;
        private List<Integer> attr_id;
        private int app_id;

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
/*
        public List<Integer> getAttr() {
            return attr;
        }

        public void setAttr(List<Integer> attr) {
            this.attr = attr;
        }
*/
        public int getApp_id() {
            return app_id;
        }

        public void setApp_id(int app_id) {
            this.app_id = app_id;
        }

        public List<Integer> getAttr_id() {
            return attr_id;
        }

        public void setAttr_id(List<Integer> attr_id) {
            this.attr_id = attr_id;
        }
    }



    //初始化 端口 所有端口 AID_Gen_Dev  区分出来
    public static List<dev_port> InitDev_port(List<dev_port> devPortList) {
        for (int i = devPortList.size() - 1; i >= 0; i--) {
            devPortList.get(i).setDevAddr(Clib.hexToBytes(devPortList.get(i).getMac()));

            if (devPortList.get(i).getAid_valueList() == null) {

                List<aid_value> aidValueList = new ArrayList<aid_value>();
                List<Integer> GD_list = new ArrayList<Integer>();
                if(devPortList.get(i).getAids()!=null&&devPortList.get(i).getAids()!="") {
                    String[] aids = devPortList.get(i).getAids().split(",");
                    for (String aid : aids) {
                        int a_id = Integer.parseInt(aid);//小于0x008000   大于等于0x001000 是设备类型  小于1000是设备属性  大于等于8000是 数据属性
                        if (a_id >= 32768)// 大于等于8000是 数据属性
                        {
                            aid_value av = new aid_value();
                            av.setAid(a_id);
                            av.setValues("00");
                            aidValueList.add(av);
                        } else if (a_id < 4096) {//小于1000是设备属性
                            GD_list.add(a_id);
                        }
                    }
                }
                devPortList.get(i).setAid_valueList(aidValueList);
                devPortList.get(i).setAids_gd(GD_list);
            }
        }

        return devPortList;
    }

    /**
     * 是否有信号值
     *
     * @param GD_list
     * @return
     */
    public static boolean IsHaveLqi(List<Integer> GD_list) {
        boolean ish = false;
        if (GD_list != null)
            for (int aid : GD_list) {
                if (aid == AttributeID.PDO_LQI) {
                    ish = true;
                    break;
                }
            }
        return ish;
    }

    /**
     * 是否有 引允 入网  网关用
     *
     * @param GD_list
     * @return
     */
    public static boolean IsEnableJoin(List<Integer> GD_list) {
        boolean ish = false;
        if (GD_list != null)
            for (int aid : GD_list) {
                if (aid == AttributeID.PDO_SubDevice) {
                    ish = true;
                    break;
                }
            }
        return ish;
    }
}
