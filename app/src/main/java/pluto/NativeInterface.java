package pluto;

import android.util.Log;

/**
 * Created by lort on 2018/5/29.
 */

public class NativeInterface {
    /** init vm
     */
    public static native int init();
    /*
     * @ smartConfigStart() configuration wifi device
     * */
    public static native int smartConfigStart(String ssid, String psw, byte hideSSID, int timeout);//timeout int ms
    /**
     * @ smartConfigStop() stop configuration of wifi device
     * */
    public static native int smartConfigStop();
    //设备发送函数
    /*
     * @ reqSend() send a message to device
     */
    public static native int reqSend(int keyID,byte[] dst,int seq, int port, long aID, int cmd, int option, byte[] pdata, int len, int send_option);
    //设备发送函数
    /*
     * @ reqSend_NoData() send a message to device
     */
    public static native int reqSendNoData(int keyID,byte[] dst,int seq, int port, long aID, int cmd, int option);
    /*
     * @ reqAddDeviceKey() add key and keyID for remote device
     * */
    public static native int addDeviceKey(byte[] addr, int keyID, byte[] key);
    /*
     * @ removeDeviceKey() key key and keyID for remote device
     */
    public static native int removeDeviceKey(byte[] addr,int keyID);
    /*
     * @ resetKeyList() reset all device key on table list
     */
    public static native int resetKeyList();
    /*
        /* local variable definition */
    /*************
     * native funtion for create scene
     ********************************/
    public static native int creatBody();
    public static native int addWhileBlock(int root, String reason);
    public static native int addIfBlock(int root, String ifType, String reason);
    public static native int addAction(int root, String what, String param);
    public static native int addElseBlock(int root);
    public static native String output(int root);
    public static native int release();

    private static void sendStatusCB(byte[] src, int seq, int port, long aID, int cmd, int option,int state) {
        Aps.sendStateCB(src,seq,port,aID,cmd,option,state);
    }
    //设备信息接收回调函数
    private static void receiveMessageCB(int keyID, byte[] src,int seq, int port, long aID, int cmd, int option, byte[] pdata, int len) {
        Aps.receiveCB(keyID,src,seq,port,(int)aID,cmd,option,pdata,len);
    }

    //用户登录
    public static  native int reqSetServerUrl(String url);
    public static  native int reqSetServerIP(String ip);
    public static  native int reqSetServerTcpPort(int port);
    public static  native int reqSetServerUDPPort(int port);

    public static  native int reqSetLocalIP(String ip);
    public static  native int reqSetLocalUDPPort(int port);

    public static  native int reqLogin(byte[] user, byte[] psw);
    public static  native int reqCheckLogin();
    public static  native int stopLogin();
    public static  native int reqLogout();
    public static  native int getLoginState();
    public static  native int skipRoute(int state);
    public static  native int getSkipRouteState();
    //设备状态变更回调函数
    private static void loginStatusCB(int state)
    {
        Pluto.loginStateCB(state);
    }

}
