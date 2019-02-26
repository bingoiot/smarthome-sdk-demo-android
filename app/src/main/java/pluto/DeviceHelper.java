package pluto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by lort on 2018/4/29.
 */

public class DeviceHelper extends Thread {
    public static DeviceHelper instance = new DeviceHelper();
    private static boolean runFlag = false;

    public class KeyID {
        public static final byte Default = 0x00;
        public static final byte SN = 0x01;
        public static final byte Admin = 0x02;
        public static final byte Common = 0x03;
        public static final byte Guest = 0x04;
    }
    
    public class Type {
        public static final byte Invalide = 0x00;
        public static final byte Gateway = 0x01;
        public static final byte Router = 0x02;
        public static final byte Device = 0x03;
    }

    public static void initialization() {
        Aps.setSectionListener(AttributeID.PDO_Beacon, myListener);
        Aps.setSectionListener(AttributeID.PDO_Device_Indication, myListener);
        Aps.setSectionListener(AttributeID.PDO_Device_Info, myListener);
        Aps.setSectionListener(AttributeID.PDO_Port_List, myListener);
        Aps.setSectionListener(AttributeID.PDO_Port_Describe, myListener);
        Aps.setSectionListener(AttributeID.PDO_SubDevice, myListener);
        Aps.setSectionListener(AttributeID.PDO_LQI, myListener);
        if (runFlag == false) {
            DeviceHelper.instance.start();
            runFlag = true;
        }
    }

    public static void AttachDevice(byte[] addr, byte[] adminKey, byte[] comKey, byte[] guestKey) {

        Device_t dev = null;
        byte keyID = KeyID.Default;
        if (guestKey != null) {
            keyID = KeyID.Guest;
            Aps.addDeviceKey(addr, keyID, guestKey);
        }
        if (comKey != null) {
            keyID = KeyID.Common;
            Aps.addDeviceKey(addr, keyID, comKey);
        }
        if (adminKey != null) {
            keyID = KeyID.Admin;
            Aps.addDeviceKey(addr, keyID, adminKey);
        }
        synchronized (lock) {
            dev = getDeviceByAddr(addr);
            if (dev == null) {
                addDevice(addr, keyID);
                dev = getDeviceByAddr(addr);
            }
            dev.keyID = keyID;
            dev.state = state_invalid;
        }
    }

    public static int RemoveDevice(byte[] addr) {
        byte ret = Common.osFailed;
        synchronized (myDeviceList) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                myDeviceList.remove(dev);
                ret = Common.osSucceed;
            }
        }
        return ret;
    }

    public static int RemoveAllDevice() {
        myDeviceList = new ArrayList<Device_t>();
        return Common.osSucceed;
    }

    public static int setSectionListener(onSectionListener myListener) {
        boolean isHave = false;
        byte ret = Common.osFailed;
        synchronized (mySectionList) {
            for (onSectionListener subsec : mySectionList) {
                if (subsec == myListener) {
                    isHave = true;
                }
            }
            if (!isHave) {
                mySectionList.add(myListener);
                ret = Common.osSucceed;
            }
        }
        return ret;
    }

    public static int removeSectionListener(onSectionListener myListener) {
        int ret = Common.osFailed;
        synchronized (mySectionList) {
            for(int i=mySectionList.size() - 1; i >= 0; i--) {
                onSectionListener subsec=mySectionList.get(i);
                if (subsec == myListener) {
                    mySectionList.remove(subsec);
                    ret = Common.osSucceed;
                }
            }
        }
        return ret;
    }

    public static int resetSectionListener() {
        synchronized (mySectionList) {
            mySectionList = new ArrayList<onSectionListener>();
        }
        return Common.op_succeed;
    }

    public static int wifiSmartConfigStart(String ssid, String psw, boolean hideSsid, int timeout) {
        byte hb;
        if (ssid == null || psw == null)
            return Common.osFailed;
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        Common.deviceAirkissStartTime = curDate.getTime();
        if (hideSsid)
            hb = (int) 1;
        else
            hb = (int) 0;
        return NativeInterface.smartConfigStart(ssid, psw, hb, timeout);
    }

    public static int wifiSmartConfigStop() {
        Common.deviceAirkissStartTime = 0;
        return NativeInterface.smartConfigStop();
    }

    public static int reqDevEnableJoin(int keyID, byte[] dst, int seq, int duration) {
        Log.d(Common.TAG_Debug, "jdo_reqDevBeacon: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        byte[] buf = new byte[1 + 4];
        buf[0] = Common.osTrue;
        Clib.u32tob(buf, 1, duration);
        //public static int reqSend(int keyID,byte[] dst,int seq, int port, long aID, int cmd, int option, byte[] pdata, int len)
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0,AttributeID.PDO_SubDevice, AttributeID.Command.Write, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqDevDisableJoin(int keyID, byte[] dst, int seq) {
        Log.d(Common.TAG_Debug, "reqDevDisableJoin: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        byte[] buf = new byte[1];
        buf[0] = Common.osFalse;
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0, AttributeID.PDO_SubDevice, AttributeID.Command.Write, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqReadPorts(int keyID, byte[] dst, int seq) {
        Log.d(Common.TAG_Debug, "reqReadPoints: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0, AttributeID.PDO_Port_List, AttributeID.Command.Read, AttributeID.Option.Default);
    }

    public static int reqReadDeviceInfo(int keyID, byte[] dst, int seq) {
        Log.d(Common.TAG_Debug, "reqReadDeviceInfo: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0, AttributeID.PDO_Device_Info, AttributeID.Command.Read, AttributeID.Option.Default);
    }

    public static int reqReadPortDescribe(int keyID, byte[] dst, int seq, int port) {
        Log.d(Common.TAG_Debug, "reqReadPointDescribe: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        byte[] buf = new byte[1];
        buf[0] = (byte) port;
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0, AttributeID.PDO_Port_Describe,AttributeID.Command.Read, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqWriteDevInfo(int keyID, byte[] dst, int seq, byte[] pdata) {
        Log.d(Common.TAG_Debug, "reqWriteDevInfo: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID + "len:" + pdata.length);
        Log.d(Common.TAG_Debug, "reqWriteDevInfo: " + Clib.bytes2Hex(pdata, ' '));
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0,AttributeID.PDO_Device_Info, AttributeID.Command.Write, AttributeID.Option.Default, pdata, pdata.length);
    }

    public static int reqWritePortDescribe(int keyID, byte[] dst, int seq, byte[] pdata) {
        Log.d(Common.TAG_Debug, "reqWritePointDescribe: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID + "len:" + pdata.length);
        Log.d(Common.TAG_Debug, "reqWritePointDescribe: " + Clib.bytes2Hex(pdata, ' '));
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0, AttributeID.PDO_Port_Describe, AttributeID.Command.Write, AttributeID.Option.Default, pdata, pdata.length);
    }

    public static int reqSendBeacon(int keyID, byte[] dst, int seq) {
        Log.d(Common.TAG_Debug, "reqSendBeacon: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        byte[] buf = new byte[1];
        buf[0] = (byte) (Clib.getUnixTime() / 60000);
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0, AttributeID.PDO_Beacon, AttributeID.Command.Beacon, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqGetLqi(int keyID, byte[] dst, int seq) {
        Log.d(Common.TAG_Debug, "reqGetLqi: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        return Aps.reqSend(keyID, dst, Common.getSeq(), 0, AttributeID.PDO_LQI, AttributeID.Command.Read, AttributeID.Option.Default);
    }

    public static int reqGetLqi(int keyID, byte[] dst, int seq, int port) {
        Log.d(Common.TAG_Debug, "reqGetLqi: " + Clib.bytes2Hex(dst, ':') + " keyID:" + keyID);
        return Aps.reqSend(keyID, dst, Common.getSeq(), port, AttributeID.PDO_LQI, AttributeID.Command.Read, AttributeID.Option.Default);
    }

    public static boolean updateDeviceInfo(byte[] addr, byte keyID) {
        synchronized (lock) {
            if (keyID == KeyID.Admin) {
                Device_t dev = getDeviceByAddr(addr);
                if (dev != null) {
                    if (dev.devInfo != null) {
                        int encFlag = 0;
                        try {
                            encFlag = dev.devInfo.getInt("encrypt");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (encFlag == 0)//no encrypt
                        {
                            try {
                                dev.devInfo.remove("encrypt");
                                dev.devInfo.put("encrypt", 1);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        String str = dev.devInfo.toString();
                        if (str != null) {
                            byte[] pdata = str.getBytes();
                            Log.d(Common.TAG_Info, "updateDeviceInfo: slen:" + str.length() + " blen:" + pdata.length + "info:" + str);
                            reqWriteDevInfo(keyID, dev.addr, Common.getSeq(), pdata);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public static boolean updatePortInfo(byte[] addr, byte port) {
        synchronized (lock) {
            if (port == 0x00) return false;
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jp = getPortDescribe(dev, port);
                if (jp != null) {
                    byte[] pdata = jp.toString().getBytes();
                    reqWritePortDescribe(dev.keyID, addr, Common.getSeq(), pdata);
                    return true;
                }
            }
        }
        return false;
    }

    public static JSONObject getDeviceInfo(String mac) {
        byte[] addr = Clib.hexToBytes(mac);
        return getDeviceInfo(addr);
    }

    public static JSONObject getDeviceInfo(byte[] addr) {
        Device_t dev = getDeviceByAddr(addr);
        if (dev != null) {
            return dev.devInfo;
        }
        return null;
    }

    public static byte[] getDevicePortList(byte[] addr) {
        Device_t dev = getDeviceByAddr(addr);
        if (dev != null) {
            return dev.portList;
        }
        return null;
    }

    public static JSONObject getDevicePortDescribe(byte[] addr, byte port) {
        Device_t dev = getDeviceByAddr(addr);
        if (dev != null) {
            JSONObject jp = getPortDescribe(dev, port);
            return jp;
        }
        return null;
    }

    public static String getDeviceName(byte[] addr) {
        String str;
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    try {
                        str = (String) jdev.getString("name");
                        return str;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static boolean setDeviceName(byte[] addr, String name) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    jdev.remove("name");
                    try {
                        jdev.put("name", name);
                        return true;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    public static byte[] getDeviceKey(byte[] addr, byte keyID) {
        byte[] pkey;
        String str = null;
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    try {
                        if (keyID == KeyID.Admin)
                            str = (String) jdev.getString("admin_key");
                        else if (keyID == KeyID.Common) {
                            str = (String) jdev.getString("com_key");
                        } else if (keyID == KeyID.Guest)
                            str = (String) jdev.getString("guest_key");
                        if (str != null) {
                            pkey = Clib.hexToBytes(str);
                            return pkey;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static int getDevicetype(byte[] addr) {
        int res = 0;
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    try {
                        res = jdev.getInt("dev_type");
                        return res;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return res;
    }

    public static String getDeviceMFG(byte[] addr) {
        String str;
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    try {
                        str = (String) jdev.getString("MFG");
                        return str;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static int getDeviceVerSion(byte[] addr) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    try {
                        int val = jdev.getInt("version");
                        return val;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return -1;
    }

    public static String getDeviceTime(byte[] addr) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    try {
                        long val = jdev.getInt("date") + 946656000L;//+2000.1.1-0:0:0
                        val = val * 1000;//expire million second
                        Log.d(Common.TAG_Debug, "getDeviceTime:" + String.format("%d,%x", val, val));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String str = simpleDateFormat.format(val);
                        return str;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static byte getDeviceLqi(byte[] addr) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jdev = (JSONObject) dev.devInfo;
                if (jdev != null) {
                    try {
                        int lqi = jdev.getInt("lqi");
                        return (byte) (lqi & 0x00FF);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return 0;
    }

    public static boolean getRemoteLockFlag(byte[] addr) {
        synchronized (lock) {
            int flag = getDeviceFlag(addr, "remote_lock");
            if (flag == 1)
                return true;
            else
                return false;
        }
    }

    public static void setRemoteLockFlag(byte[] addr, boolean flag) {
        synchronized (lock) {
            setDeviceFlag(addr, "remote_lock", flag);
        }
    }

    public static boolean getLocalLockFlag(byte[] addr) {
        synchronized (lock) {
            int flag = getDeviceFlag(addr, "local_lock");
            if (flag == 1)
                return true;
            else
                return false;
        }
    }

    public static boolean getGuestLockFlag(byte[] addr) {
        synchronized (lock) {
            int flag = getDeviceFlag(addr, "guest_lock");
            if (flag == 1)
                return true;
            else
                return false;
        }
    }

    public static void setGuestLockFlag(byte[] addr, boolean flag) {
        synchronized (lock) {
            setDeviceFlag(addr, "guest_lock", flag);
        }
    }

    public static void setLocalLockFlag(byte[] addr, boolean flag) {
        synchronized (lock) {
            setDeviceFlag(addr, "local_lock", flag);
        }
    }

    public static boolean getShareLockFlag(byte[] addr) {
        synchronized (lock) {
            int flag = getDeviceFlag(addr, "share_lock");
            if (flag == 1)
                return true;
            else
                return false;
        }
    }

    public static void setShareLockFlag(byte[] addr, boolean flag) {
        synchronized (lock) {
            setDeviceFlag(addr, "share_lock", flag);
        }
    }

    public static boolean getEncryptFlag(byte[] addr) {
        synchronized (lock) {
            int flag = getDeviceFlag(addr, "encrypt");
            if (flag == 1)
                return true;
            else
                return false;
        }
    }

    public static void setEncryptFlag(byte[] addr, boolean flag) {
        synchronized (lock) {
            setDeviceFlag(addr, "encrypt", flag);
        }
    }
    public static byte[] getPortList(byte[] addr) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev == null)
                return null;
            else {
                if (dev.portList != null) {
                   /* byte[] buf = new byte[dev.portList.length + 1];
                    buf[0] = 0x00;
                    System.arraycopy(dev.portList, 0, buf, 1, dev.portList.length);
                    return buf;
                    */
                    return dev.portList;
                } else {
                /*    byte[] buf = new byte[1];
                    buf[0] = 0x00;
                    return buf;
                    */
                    return new byte[0];
                }
            }
        }
    }

    public static String getPortName(byte[] addr, byte port) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jp = getPortDescribe(dev, port);
                if (jp != null) {
                    String name = null;
                    try {
                        name = jp.getString("name");
                        return name;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static void setPortName(byte[] addr, byte port, String name) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                JSONObject jp = getPortDescribe(dev, port);
                if (jp != null) {
                    jp.remove("name");
                    try {
                        jp.put("name", name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    //port=0获取设备属性
    public static int getPortAppID(byte[] addr, byte port) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                if (port != 0x00) {
                    JSONObject jp = getPortDescribe(dev, port);
                    if (jp != null) {
                        try {
                            return jp.getInt("app_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return ApplicationID.Unkown;
    }
    //port=0获取设备属性
    public static int[] getPortAttributeList(byte[] addr, byte port) {
        synchronized (lock) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                if (port == 0x00) {
                    JSONObject jp = dev.devInfo;
                    if (jp != null) {
                        int[] aIDs = null;
                        try {
                            JSONArray ja = jp.getJSONArray("attr_id");
                            if (ja != null) {
                                aIDs = new int[ja.length()];
                                for (int i = 0; i < ja.length(); i++) {
                                    aIDs[i] = ja.getInt(i);
                                }
                            }
                            return aIDs;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    JSONObject jp = getPortDescribe(dev, port);
                    if (jp != null) {
                        int[] aIDs = null;
                        try {
                            JSONArray ja = jp.getJSONArray("attr_id");
                            if (ja != null) {
                                aIDs = new int[ja.length()];
                                for (int i = 0; i < ja.length(); i++) {
                                    aIDs[i] = ja.getInt(i);
                                }
                            }
                            return aIDs;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (lock) {
                poll();
            }
            try {
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void poll() {
        for (Device_t dev : myDeviceList) {
            long temp = Clib.loopSub(Clib.getUnixTime(), dev.runtime);
            if (temp > pollDevcieTimeout) {
                dev.runtime = Clib.getUnixTime();
                if ((dev.stry < 5) && (dev.state != state_device_ready)) {
                    dev.stry++;
                    if (dev.devInfo == null) {
                        reqReadDeviceInfo(KeyID.Default, dev.addr, Common.osDisable);
                    }
                    else {
                        if (dev.keyID == KeyID.Admin) {
                            int enflag = Clib.toInt(dev.devInfo, "encrypt");
                            if (enflag == 0) {
                                Clib.setInt(dev.devInfo, "encrypt", 1);
                                String str = dev.devInfo.toString();
                                if (str != null) {
                                    byte[] pdata = str.getBytes();
                                    Log.d(Common.TAG_Info, "updateDeviceInfo: slen:" + str.length() + " blen:" + pdata.length + "info:" + str);
                                    reqWriteDevInfo(dev.keyID, dev.addr, Common.getSeq(), pdata);
                                }
                            }
                        }
                        if (dev.portList == null) {
                            dev.state = state_reading_port_list;
                            reqReadPorts(dev.keyID, dev.addr, Common.osDisable);
                        } else if (dev.portList.length != dev.portDescribe.size()) {
                            for (int i = 0; i < dev.portList.length; i++) {
                                reqReadPortDescribe(dev.keyID, dev.addr, Common.getSeq(), dev.portList[i]);
                            }
                            dev.state = state_reading_port_describe;
                        }
                        //reqReadPorts(dev.keyID, dev.addr, Common.osDisable);
                    }
                    if (checkDeviceInfoComplete(dev) == true) {//device info incomplete
                        for (onSectionListener subsec : mySectionList) {//search every section listener to callback
                            subsec.CompleteDevice(dev.addr, dev.devInfo, dev.portList, dev.portDescribe);
                        }
                        dev.state = state_device_ready;
                    }
                }
            }
        }
    }

    private static Aps.onSectionListener myListener = new Aps.onSectionListener() {
        @Override
        public void RecieveCB(int keyID, byte[] src, int seq, int port, int aID, int cmd, int option, byte[] pdata, int len) {
            Device_t dev;
            synchronized (lock) {
                switch (aID) {
                    case AttributeID.PDO_Beacon:
                        dev = getDeviceByAddr(src);
                        if (dev == null) {
                            addDevice(src);
                            dev = getDeviceByAddr(src);
                            if (dev != null) {
                                reqReadDeviceInfo(dev.keyID, dev.addr, Common.osDisable);
                                dev.runtime = Clib.getUnixTime();
                                dev.state = state_reading_device_info;
                            }
                        }
                        break;
                    case AttributeID.PDO_Device_Indication://new device joined
                        if(cmd == AttributeID.Command.Notify) {
                            dev = getDeviceByAddr(src);
                            if ((dev != null) && (dev.key_word != pdata[0])) {
                                RemoveDevice(src);
                                dev = null;
                            }
                            if (dev == null) {
                                addDevice(src);
                                dev = getDeviceByAddr(src);
                                if (dev != null) {
                                    reqReadDeviceInfo(dev.keyID, dev.addr, Common.osDisable);
                                    dev.runtime = Clib.getUnixTime();
                                    dev.state = state_reading_device_info;
                                    dev.key_word = pdata[0];
                                }
                            }
                        }
                        break;
                    case AttributeID.PDO_Device_Info://got device info
                        if (cmd == (AttributeID.Command.Response | AttributeID.Command.Read)) {
                            if (pdata[0] == Common.osSucceed) {
                                String devInfo = Clib.btostr(pdata, 1, (len - 1));
                                dev = getDeviceByAddr(src);
                                if (dev == null) {
                                    addDevice(src);
                                    dev = getDeviceByAddr(src);
                                }
                                if ((dev != null) && (devInfo != null)) {
                                    addDeviceInfo(src, devInfo);
                                    int devtype = Clib.toInt(dev.devInfo, "dev_type");
                                    if (devtype == Type.Gateway) {
                                        for (onSectionListener mylistener : mySectionList) {//search every section listener to callback
                                            mylistener.CompleteDevice(dev.addr, dev.devInfo, dev.portList, dev.portDescribe);
                                            dev.state = state_device_ready;
                                        }
                                    }
                                }
                                if ((dev != null) && (dev.portList == null)) {
                                    dev.state = state_reading_port_list;
                                    reqReadPorts(dev.keyID, dev.addr, Common.osDisable);
                                }
                            }
                        }
                        break;
                    case AttributeID.PDO_Port_List://got port list
                        if (cmd == (AttributeID.Command.Response | AttributeID.Command.Read)) {
                            if (pdata[0] == Common.osSucceed) {
                                byte[] buf = Clib.subbytes(pdata, 1, len - 1);
                                dev = getDeviceByAddr(src);
                                if (dev == null) {
                                    addDevice(src);
                                    dev = getDeviceByAddr(src);
                                }
                                if ((dev != null) && (buf != null)) {
                                    addDevciePortList(src, buf);
                                }
                                if (dev.portList.length != dev.portDescribe.size()) {
                                    for (int i = 0; i < buf.length; i++) {
                                        reqReadPortDescribe(dev.keyID, dev.addr, Common.getSeq(), buf[i]);
                                    }
                                    dev.state = state_reading_port_describe;
                                }
                            }
                        }
                        break;
                    case AttributeID.PDO_Port_Describe://got port Info
                        if (cmd == (AttributeID.Command.Response | AttributeID.Command.Read)) {
                            if (pdata[0] == Common.osSucceed) {
                                String desc = Clib.btostr(pdata, 1, (len - 1));
                                dev = getDeviceByAddr(src);
                                if (dev == null) {
                                    addDevice(src);
                                    dev = getDeviceByAddr(src);
                                }
                                if ((dev != null) && (desc != null)) {
                                    addDevicePortDescribe(src, desc);
                                    if ((dev.state == state_reading_port_describe) && (checkDeviceInfoComplete(dev) == true)) {
                                        for (onSectionListener subsec : mySectionList) {//search every section listener to callback
                                            subsec.CompleteDevice(dev.addr, dev.devInfo, dev.portList, dev.portDescribe);
                                        }
                                        dev.state = state_device_ready;
                                    }
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
                for (onSectionListener subsec : mySectionList) {//search every section listener to callback
                    switch (aID) {
                        case AttributeID.PDO_Beacon:
                            if (cmd == AttributeID.Command.Beacon)
                                subsec.ReceiveBeacon(src, Common.osSucceed);
                            break;
                        case AttributeID.PDO_Device_Indication://new device joined
                            if (cmd == AttributeID.Command.Notify)
                                subsec.DeviceJoinIndicates(src);
                            break;
                        case AttributeID.PDO_Device_Info://got device info
                            if (cmd == (AttributeID.Command.Response | AttributeID.Command.Read)) {
                                if (pdata[0] == Common.osSucceed) {
                                    String devInfo = Clib.btostr(pdata, 1, (len - 1));
                                    JSONObject info = Clib.toJSON(devInfo);
                                    subsec.ReceiveDeviceInfo(src, info, pdata[0]);
                                } else {
                                    subsec.ReceiveDeviceInfo(src, null, pdata[0]);
                                }
                            }
                            break;
                        case AttributeID.PDO_Port_List://got port list
                            if (cmd == (AttributeID.Command.Response | AttributeID.Command.Read)) {
                                if (pdata[0] == Common.osSucceed) {
                                    byte[] portList = Clib.subbytes(pdata, 1, (len - 1));
                                    subsec.ReceivePortList(src, portList, pdata[0]);
                                } else {
                                    subsec.ReceivePortList(src, null, pdata[0]);
                                }
                            }
                            break;
                        case AttributeID.PDO_Port_Describe://got port Info
                            if (cmd == (AttributeID.Command.Response | AttributeID.Command.Read)) {
                                if (pdata[0] == Common.osSucceed) {
                                    String describe = Clib.btostr(pdata, 1, (len - 1));
                                    JSONObject desc = Clib.toJSON(describe);
                                    subsec.ReceivePortDescribe(src, desc, pdata[0]);
                                } else {
                                    subsec.ReceivePortDescribe(src, null, pdata[0]);
                                }
                            }
                            break;
                        case AttributeID.PDO_SubDevice://new port joined
                            if (cmd == AttributeID.Command.Notify) {
                                String descibe = Clib.btostr(pdata, 0, len);
                                JSONObject desc = Clib.toJSON(descibe);
                                subsec.NewPortIndicates(src, desc);
                            }
                            break;
                        case AttributeID.PDO_LQI:
                            if (cmd == (AttributeID.Command.Response | AttributeID.Command.Read)) {
                                int lqi = (pdata[0] & 0xff);
                                subsec.ReceiveLqi(src, (byte) port, lqi, Common.op_succeed);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        @Override
        public void SendStatus(byte[] src,int seq, int port, int aID, int cmd, int option, int state) {
            for (onSectionListener subsec : mySectionList) {//search every section listener to callback
                subsec.SendStatusCB(src,seq, port, aID, cmd, option,state);
            }
        }
    };

    private static int addDevice(byte[] addr) {
        Device_t dev;
        dev = getDeviceByAddr(addr);
        if (dev == null) {
            dev = new Device_t();
            dev.runtime = Clib.getUnixTime();
            dev.stry = 0;
            dev.keyID = 0;
            dev.state = state_invalid;
            dev.devInfo = null;
            dev.portList = null;
            dev.addr = new byte[8];
            dev.portDescribe = new ArrayList<JSONObject>();
            System.arraycopy(addr, 0, dev.addr, 0, 8);
            myDeviceList.add(dev);
            return Common.osSucceed;
        }
        return Common.osFailed;
    }

    private static int addDevice(byte[] addr, byte keyID) {
        Device_t dev;
        dev = getDeviceByAddr(addr);
        if (dev == null) {
            dev = new Device_t();
            dev.runtime = 0;
            dev.stry = 0;
            dev.keyID = keyID;
            dev.state = state_invalid;
            dev.devInfo = null;
            dev.portList = null;
            dev.addr = new byte[8];
            dev.portDescribe = new ArrayList<JSONObject>();
            System.arraycopy(addr, 0, dev.addr, 0, 8);
            myDeviceList.add(dev);
            return Common.osSucceed;
        }
        return Common.osFailed;
    }

    private static int addDeviceInfo(byte[] addr, String devInfo) {
        JSONObject info = Clib.toJSON(devInfo);
        if (info != null) {
            Device_t dev = getDeviceByAddr(addr);
            if (dev != null) {
                String str;
                dev.devInfo = info;
                str = (String) Clib.toString(dev.devInfo, "guest_key");
                if (str != null) {
                    dev.keyID = KeyID.Guest;
                    byte[] key = Clib.hexToBytes(str);
                    Aps.addDeviceKey(dev.addr, dev.keyID, key);
                }
                str = (String) Clib.toString(dev.devInfo, "com_key");
                if (str != null) {
                    dev.keyID =  KeyID.Common;
                    byte[] key = Clib.hexToBytes(str);
                    Aps.addDeviceKey(dev.addr, dev.keyID, key);
                }
                str = (String) Clib.toString(dev.devInfo, "admin_key");
                if (str != null) {
                    dev.keyID =  KeyID.Admin;
                    byte[] key = Clib.hexToBytes(str);
                    Aps.addDeviceKey(dev.addr, dev.keyID, key);
                }
                return Common.osSucceed;
            }
        }
        return Common.osFailed;
    }

    private static int addDevciePortList(byte[] addr, byte[] ports) {
        Device_t dev = getDeviceByAddr(addr);
        if (dev != null) {
            dev.portList = ports;
            return Common.osSucceed;
        }
        return Common.osFailed;
    }

    private static int addDevicePortDescribe(byte[] addr, String desc) {
        boolean have = false;
        JSONObject json;
        Device_t dev = getDeviceByAddr(addr);
        if (dev != null) {
            json = Clib.toJSON(desc);
            if (json != null) {
                int port = Clib.toInt(json, "port");
                for (JSONObject js : dev.portDescribe) {
                    if (js != null) {
                        int sp = Clib.toInt(js, "port");
                        if (sp == port)
                            have = true;
                    }
                }
                if (!have) {
                    dev.portDescribe.add(json);
                    return Common.osSucceed;
                }
            }
        }
        return Common.osFailed;
    }

    private static boolean checkDeviceInfoComplete(Device_t dev) {
        boolean flag = false;
        if (dev != null) {
            if (dev.devInfo != null) {
                if (dev.portList != null) {
                    if (dev.portDescribe != null) {
                        if (dev.portDescribe.size() == dev.portList.length)
                            flag = true;
                    }
                }
            }
        }
        return flag;
    }

    private static Device_t getDeviceByAddr(byte[] addr) {
        Device_t dev = null;
        if (myDeviceList != null) {
            for (int i = 0; i < myDeviceList.size(); i++) {
                dev = (Device_t) myDeviceList.get(i);
                if (Clib.arraryCmp(dev.addr, addr) == 0)//找到对应设备
                {
                    return dev;
                }
            }
        }
        return null;
    }

    private static int getDeviceFlag(byte[] addr, String obj) {
        int val = -1;
        Device_t dev = getDeviceByAddr(addr);
        if (dev != null) {
            JSONObject jdev = (JSONObject) dev.devInfo;
            if (jdev != null) {
                try {
                    val = jdev.getInt(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return val;
    }

    private static int setDeviceFlag(byte[] addr, String obj, boolean flag) {
        int val = -1;
        Device_t dev = getDeviceByAddr(addr);
        if (dev != null) {
            JSONObject jdev = (JSONObject) dev.devInfo;
            if (jdev != null) {
                jdev.remove(obj);
                if (flag == true)
                    try {
                        jdev.put(obj, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                else
                    try {
                        jdev.put(obj, 0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
            }
        }
        return val;
    }

    private static JSONObject getPortDescribe(Device_t dev, byte port) {
        int i;
        byte sp;
        JSONObject node;
        if ((dev != null) && (port != 0x00) && (dev.portDescribe != null)) {
            for (i = 0; i < dev.portDescribe.size(); i++) {
                node = (JSONObject) dev.portDescribe.get(i);
                try {
                    sp = (byte) node.getInt("port");
                    if (sp == port)
                        return node;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /* local variable declaration ****/
    public interface onSectionListener {
        void DeviceJoinIndicates(byte[] addr);

        void NewPortIndicates(byte[] addr, JSONObject describe);

        void ReceiveDeviceInfo(byte[] addr, JSONObject devInfo, int state);

        void ReceivePortList(byte[] addr, byte[] ports, int state);

        void ReceivePortDescribe(byte[] addr, JSONObject describe, int state);

        void CompleteDevice(byte[] addr, JSONObject devInfo, byte[] ports, ArrayList<JSONObject> descList);

        void ReceiveLqi(byte[] addr, byte port, int lqi, int state);

        void ReceiveBeacon(byte[] addr, int state);

        void SendStatusCB(byte[] addr,int seq, int port, int aID,int cmd, int option, int state);
    }

    public static class Device_t {
        byte state;
        byte key_word;
        long runtime;
        byte stry;
        byte keyID;
        byte[] addr;
        JSONObject devInfo;
        byte[] portList;
        ArrayList<JSONObject> portDescribe;
    }

    private static final int state_invalid = 0;
    private static final int state_reading_device_info = 1;
    private static final int state_got_device_info = 2;
    private static final int state_reading_port_list = 3;
    private static final int state_got_port_list = 4;
    private static final int state_reading_port_describe = 5;
    private static final int state_got_port_describe = 6;
    private static final int state_device_ready = 7;

    private static ArrayList<onSectionListener> mySectionList = new ArrayList<onSectionListener>();
    private static ArrayList<Device_t> myDeviceList = new ArrayList<Device_t>();
    private static final int pollDevcieTimeout = 10000;
    private static Object lock = new Object();//多线程序保护锁

}
