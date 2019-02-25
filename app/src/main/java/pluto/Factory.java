package pluto;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by lort on 2017/11/2.
 */

public class Factory {
    static JSONObject FactoryInfo;
    public static void initialization(){
        Aps.setSectionListener(AttributeID.PDO_Factory,myListener);
    }
    public static int setSectionListener(onSectionListener myListener){
        boolean isHave = false;
        for (onSectionListener subsec : mySectionList) {
            if (subsec==myListener) {
                isHave = true;
            }
        }
        if (!isHave) {
            mySectionList.add(myListener);
            return Common.op_succeed;
        }
        return Common.op_faile;
    }
    public static int removeSectionListener(onSectionListener myListener)
    {
        int ret = Common.op_faile;
        for (onSectionListener subsec : mySectionList) {
            if (subsec==myListener){
                mySectionList.remove(subsec);
                ret = Common.op_succeed;
            }
        }
        return ret;
    }
    public static int resetSectionListener(){
        mySectionList = new ArrayList<onSectionListener>();
        return Common.op_succeed;
    }
    public static int reqReadInfo(int keyID, byte[] addr, int seq)
    {
        return Aps.reqSend(keyID,addr,seq,0,AttributeID.PDO_Factory,AttributeID.Command.Read,AttributeID.Option.Default);
    }
    public static int reqSaveInfo(int keyID, byte[] addr, int seq, String info)
    {
        byte[] buf = info.getBytes();
        return Aps.reqSend(keyID,addr,seq,0,AttributeID.PDO_Factory,AttributeID.Command.Write,AttributeID.Option.Default,buf,buf.length);
    }
    public static String output()
    {
        return FactoryInfo.toString();
    }
    public static int creat(String server_domain, String server_ipv4, int server_udp_port, int server_tcp_port, int local_udp_port)
    {
        int ret = Common.osFailed;
         FactoryInfo = new JSONObject();
        try {
            FactoryInfo.put("server_url",server_domain);
            FactoryInfo.put("server_ipv4",server_ipv4);
            FactoryInfo.put("server_udp_port",server_udp_port);
            FactoryInfo.put("server_tcp_port",server_tcp_port);
            FactoryInfo.put("local_udp_port",local_udp_port);
            ret = Common.osSucceed;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ret;
    }
    public static int putExtraNum(String name, int num)
    {
        int ret = Common.osFailed;
        if(FactoryInfo!=null)
        {
            try {
                FactoryInfo.put(name,num);
                ret = Common.osSucceed;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    public static int putExtraString(String name, String str)
    {
        int ret = Common.osFailed;
        if(FactoryInfo!=null)
        {
            try {
                FactoryInfo.put(name,str);
                ret = Common.osSucceed;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }
    static Aps.onSectionListener myListener = new Aps.onSectionListener() {
        @Override
        public void RecieveCB(int keyID, byte[] src, int seq, int port, int aID, int cmd, int option, byte[] pdata, int len) {
            switch(cmd&0x7F)
            {
                case AttributeID.Command.Read:
                    if(pdata[0]==Common.op_succeed)
                    {
                        String substr = Clib.btostr(pdata,1,pdata.length-1);
                        JSONObject info = Clib.toJSON(substr);
                        for (onSectionListener subsec : mySectionList) {
                           subsec.ReadCB(keyID,src,pdata[0],info);
                        }
                    }
                    else
                    {
                        for (onSectionListener subsec : mySectionList) {
                            subsec.ReadCB(keyID,src,Common.op_faile,null);
                        }
                    }
                break;
                case AttributeID.Command.Write:
                    for (onSectionListener subsec : mySectionList) {
                        subsec.WriteCB(keyID,src,pdata[0]);
                    }
                break;
            }
        }

        @Override
        public void SendStatus(byte[] src,int seq, int port, int aID, int cmd,int option, int state) {

        }
    };
    private static onSectionListener mySection;
    /*********** 出厂设置***********************/
    private static ArrayList<onSectionListener> mySectionList = new ArrayList<onSectionListener>();
    public interface onSectionListener{
        void ReadCB(int keyID, byte[] addr, int state, JSONObject info);
        void WriteCB(int keyID, byte[] addr, int state);
    }
}
