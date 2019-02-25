package pluto;

import android.util.Log;

/**
 * Created by lort on 2017/11/2.
 * pluto device service object
 */

public class Pluto {//pluto device service object
    //static byte[] 	user=new byte[]{0x01,0x00,0x01,0x01,0x00,0x00,0x00,0x1A};
    //static byte[] 	psw = new byte[]{0x5F,(byte)0xC8,(byte)0x86,(byte)0xF3,0x4F,(byte)0xDE,0x53,0x04,0x58,0x52,0x66,0x60,0x7B,(byte)0xB7,0x09,(byte)0xDC};
    static byte[] 	user=new byte[]{(byte)0x80,(byte)0x86,(byte)0x18,(byte)0x37,0x67,0x63,(byte)0x81,0x00};
    static byte[] 	psw = new byte[]{0x5F,(byte)0xC8,(byte)0x86,(byte)0xF3,0x4F,(byte)0xDE,0x53,0x04,0x58,0x52,0x66,0x60,0x7B,(byte)0xB7,0x09,(byte)0xDC};

    public class LoginState {
        public static final int Stop = 0;
        public static final int Start = 1;
        public static final int OnLine = 2;
        public static final int OffLine = 3;
        public static final int LoginFailed = 4;
        public static final int LogoutFailed = 5;
    }

    static {
        System.loadLibrary("Pluto");

        NativeInterface.init();
        DeviceHelper.initialization();
        Record.initialization();
        Scene.initialization();
        Upgrade.initialization();
        UserTable.initialization();
        Factory.initialization();

        NativeInterface.reqSetServerUrl("www.glalaxy.com");
        NativeInterface.reqSetServerIP("119.23.8.181");
        //NativeInterface.reqSetServerIP("192.168.1.101");
        NativeInterface.reqSetServerTcpPort(16729);
        NativeInterface.reqSetServerUDPPort(16729);
        NativeInterface.reqSetLocalIP("192.168.1.2");
        NativeInterface.reqSetLocalUDPPort(16729);
    }
    //用户登录
    public static  int reqLogin(byte[] user, byte[] psw){
        return NativeInterface.reqLogin(user,psw);
    }
    public static  int stopLogin(){
        return NativeInterface.stopLogin();
    }
    public static  int reqLogout(){
        return NativeInterface.reqLogout();
    }
    public static  int getLoginState(){
        return NativeInterface.getLoginState();
    }
    public static  int skipRoute(int state){
        return NativeInterface.skipRoute(state);
    }
    public static  int getSkipRouteState(){
        return NativeInterface.getSkipRouteState();
    }

    private     static  onLoginStateListener         onLoginStateCB = null;
    public static void setOnLoginStateListener(onLoginStateListener o)
    {
        onLoginStateCB = o;
    }
    public static void loginStateCB(int state)
    {
        Log.d(Common.TAG_Debug, "jdoLoginStateCB: state "+state);
        if(onLoginStateCB!=null)
            onLoginStateCB.recieve(state);
    }
    public interface onLoginStateListener
    {
        public void recieve(int state);
    }
}
