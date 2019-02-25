package pluto;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lort on 2017/11/10.
 */

public class Upgrade {

    public static void initialization(){
        Aps.setSectionListener(AttributeID.PDO_Upgrade,myListener);
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
    public static int reqUpdate(byte keyID,byte[] addr,  String server_ip, int server_port, String server_url, int stry)
    {
        JSONObject root = new JSONObject();
        if(root!=null)
        {
            try {
                root.put("server_ip",server_ip);
                root.put("server_port",server_port);
                root.put("server_url",server_url);
                root.put("try",stry);
                root.put("state",Common.op_upgrade_start);
                String out = root.toString();
                byte[] buf = out.getBytes();
                return Aps.reqSend(keyID,addr, Common.getSeq(),(byte)0x00, AttributeID.PDO_Upgrade, AttributeID.Command.Write, Common.aID_Common_Option,buf,buf.length);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
    public static int reqReadInfo(byte keyID,byte[] addr)
    {
        return Aps.reqSend(keyID,addr, Common.getSeq(),(byte)0x00, AttributeID.PDO_Upgrade, AttributeID.Command.Read, Common.aID_Common_Option);
    }
    private static Aps.onSectionListener myListener = new Aps.onSectionListener() {
        @Override
        public void RecieveCB(int keyID, byte[] src, int seq, int port, int aID,int cmd, int option, byte[] pdata, int len) {
            switch(cmd&0x7F) {
                case AttributeID.Command.Read://read scene respone]
                    readCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.Write:
                    for(onSectionListener subsec : mySectionList) {
                        subsec.UpdateStateCB(keyID, src, pdata[0], 0);
                    }
                    break;
                case AttributeID.Command.Notify:
                    for(onSectionListener subsec : mySectionList) {
                        subsec.UpdateStateCB(keyID, src, pdata[0], pdata[1]);
                    }
                    break;
            }
        }

        @Override
        public void SendStatus(byte[] src,int seq,int port, int aID, int cmd,int option, int state) {
        }
    };
    private static void readCB(int keyID, byte[] addr, byte[] pdata)
    {
        if(pdata==null||pdata.length==0)
            return ;
        int state = pdata[0];
        if(state==Common.op_succeed) {
            String str = Clib.btostr(pdata, 1, pdata.length - 1);
            if (str != null) {
                JSONObject root = null;
                try {
                    root = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (onSectionListener subsec : mySectionList) {
                    subsec.ReadInfoCB(keyID, addr, state, root);
                }
            }
        }
    }
    private static void updateCB(int keyID, byte[] addr, byte[] pdata)
    {
        if(pdata==null||pdata.length==0)
            return ;
        int state = pdata[0];
        int percent = 0;
        if(pdata.length==2)
            percent = pdata[1];

    }
    private static int getcmd(byte[] pdata)
    {
        int cmd = ((int)pdata[5])&255;
        return cmd;
    }
    private static byte getDtype(byte[] pdata)
    {
        byte dtype = (pdata[6]);
        return dtype;
    }
    private static byte getPort(byte[] pdata)
    {
        return pdata[0];
    }
    private static int getDatalen(byte[] pdata)
    {
        int len;
        len = (int) Clib.btoi(pdata,7,4);
        return len;
    }
    private static long getaID(byte[] pdata)
    {
        int aID;
        aID = (int) Clib.btoi(pdata,1,4);
        return aID;
    }
    private static byte[] getData(byte[] pdata)
    {
        int len = getDatalen(pdata);
        byte[] pbuf = new byte[len];
        if(pbuf!=null)
            System.arraycopy(pdata,11,pbuf,0,len);
        return pbuf;
    }

    private static ArrayList<onSectionListener> mySectionList = new ArrayList<onSectionListener>();
    public interface onSectionListener{
        void ReadInfoCB(int keyID, byte[] addr, int state, JSONObject info);
        void UpdateStateCB(int keyID, byte[] addr, int state, int percent);
    }
}
