package pluto;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lort on 2017/11/2.
 * this class private some function to configuration and communicate with device
 *
 * configuration function like this:
 * @ static public native int init();
 * @ static private native int smartConfigStart(String ssid, String psw, int hideSSID, int timeout);//timeout int ms
 * @ static private native int smartConfigStop();
 *
 *communication function like this:
 * before communication you should set a callback section at first
 * @ public int setSectionListener(int aID, onSectionListener listener);
 * @ public int removeSectionListener(int aID, onSectionListener listener);
 * @ public int resetSectionListener();
 *
 */

public class Aps {

    public class SendOption {
        public static final int Default = 0x00;//no option
        public static final int SkipScene = 0x01;//send message and skip trigger scene
        public static final int SkipRoute = 0x02;//send message without route device
        public static final int NoEecord = 0x04;//send message no record
        public static final int NoDelay = 0x08;//send message no delay
        public static final int Qos_1 = 0x10;//send message and try 24 hour(Will be supported in the future)
        public static final int Qos_2 = 0x20;//send message and try 7 days(Will be supported in the future)
    }
    //初始化
    public static int reqSend(int keyID,byte[] dst,int seq, int port, long aID, int cmd, int option, byte[] pdata, int len){
        Log.d(Common.TAG_Debug, "reqSend 0 :" + Clib.bytes2Hex(dst, '\0')  + " port:"+port +" aID:"+aID+" cmd:"+cmd + Clib.bytes2Hex(pdata, '\0') );
        return NativeInterface.reqSend(keyID,dst,seq,port,aID,cmd,option,pdata,len,SendOption.Default);
    }
    public  static int reqSend(int keyID,byte[] dst,int seq, int port, long aID, int cmd, int option, byte[] pdata, int len, int send_option) {
        Log.d(Common.TAG_Debug, "reqSend 0 :" + Clib.bytes2Hex(dst, '\0')  + " port:"+port +" aID:"+aID+" cmd:"+cmd + Clib.bytes2Hex(pdata, '\0') );
        return NativeInterface.reqSend(keyID,dst,seq,port,aID,cmd,option,pdata,len,send_option);
    }
    public static int reqSend(int keyID,byte[] dst,int seq, int port, long aID, int cmd, int option){
        Log.d(Common.TAG_Debug, "reqSend 1:" + Clib.bytes2Hex(dst, '\0')  + " port:"+port +" aID:"+aID+" cmd:"+cmd );
        return NativeInterface.reqSendNoData(keyID,dst,seq,port,aID,cmd,option);
    }
    public static int addDeviceKey(byte[] addr, int keyID, byte[] key){
        return NativeInterface.addDeviceKey(addr,keyID,key);
    }
    public static int removeDeviceKey(byte[] addr,int keyID){
        return NativeInterface.removeDeviceKey(addr,keyID);
    }
    public static int resetKeyList(){
        return NativeInterface.resetKeyList();
    };

    public static int setSectionListener(int aID, onSectionListener listener)
    {
        boolean isHave = false;
        for (Section subsec : mySectionList) {
            if ((subsec.aID==aID)&&(subsec.listener==listener)) {
                isHave = true;
            }
        }
        if (!isHave) {
            Section sec = new Section();
            sec.aID = aID;
            sec.listener = listener;
            mySectionList.add(sec);
            return Common.op_succeed;
        }
        return Common.op_faile;
    }
    public static int setSectionListener(int minID,int maxID, onSectionListener listener)
    {
        boolean isHave = false;
        for (Section subsec : mySectionList) {
            if ((subsec.minID==minID)&&(subsec.maxID==maxID)&&(subsec.listener==listener)) {
                isHave = true;
            }
        }
        if (!isHave) {
            Section sec = new Section();
            sec.minID = minID;
            sec.maxID = maxID;
            sec.aID = 0;
            sec.listener = listener;
            mySectionList.add(sec);
            return Common.op_succeed;
        }
        return Common.op_faile;
    }
    public static int removeSectionListener(int aID, onSectionListener listener)
    {
        int ret = Common.op_faile;
        for (Section subsec : mySectionList) {
            if ((subsec.aID==aID)&&(subsec.listener==listener)) {
                mySectionList.remove(subsec);
                ret = Common.op_succeed;
            }
        }
        return ret;
    }
    public static int removeSectionListener(int minID,int maxID, onSectionListener listener)
    {
        int ret = Common.op_faile;
        for (Section subsec : mySectionList) {
            if ((subsec.minID==minID)&&(subsec.maxID==maxID)&&(subsec.listener==listener)) {
                mySectionList.remove(subsec);
                ret = Common.op_succeed;
            }
        }
        return ret;
    }
    public static int resetSectionListener(){
        mySectionList = new ArrayList<Section>();
        return Common.op_succeed;
    }

    public static void sendStateCB(byte[] src,int seq, int port, long aID, int cmd,int option, int state) {
        Log.d(Common.TAG_Debug, "apsSendStateCB:" + Clib.bytes2Hex(src, '\0') + " state:" + state + " port:"+port +" aID:"+aID+" cmd:"+cmd);
        for (Section subsec : mySectionList) {
            if(subsec.aID!=0) {
                if (subsec.aID == aID) {
                    if (subsec.listener != null)
                        subsec.listener.SendStatus(src,seq,port, (int) aID, cmd,option, state);
                }
            }
            else if((aID>=subsec.minID)&&(aID<=subsec.maxID))
            {
                if(subsec.listener!=null)
                    subsec.listener.SendStatus(src,seq,port,(int)aID,cmd,option,state);
            }
        }
    }
    //设备信息接收回调函数
    public static void receiveCB(int keyID, byte[] src,int seq, int port,long aID, int cmd, int option, byte[] pdata, int len) {
        if ((src == null) || pdata == null) {
            Log.d(Common.TAG_Error, "apsReceiveCB: message error ocur !");
            return;
        }
        Log.d(Common.TAG_Debug, "apsReceiveCB: permite: " + keyID + " port " + port + " aID " + Integer.toHexString((int)aID));
        for (Section subsec : mySectionList) {
            if(subsec.aID!=0) {
                if (subsec.aID == aID) {
                    if (subsec.listener != null)
                        subsec.listener.RecieveCB(keyID, src, seq, port, (int) aID, cmd, (int) option, pdata, len);
                }
            }
            else if((aID>=subsec.minID)&&(aID<=subsec.maxID))
            {
                if (subsec.listener != null)
                    subsec.listener.RecieveCB(keyID, src, seq, port, (int) aID, cmd, (int) option, pdata, len);
            }
        }
    }
    private static class Section{
        private int         aID;
        private int         minID;
        private int         maxID;
        onSectionListener     listener;
    }
    public  interface  onSectionListener{
        /**
         * @ RecieveCB() Recieve message from remote device
         * */
        void RecieveCB(int keyID, byte[] src, int seq, int port, int aID, int cmd, int option, byte[] pdata, int len);
        /** @ SendState() Report Send State from pluto stack
         */
        void SendStatus(byte[] src,int seq, int port, int aID, int cmd,int option, int state);
    }
    private static ArrayList<Section> mySectionList = new ArrayList<Section>();
}
