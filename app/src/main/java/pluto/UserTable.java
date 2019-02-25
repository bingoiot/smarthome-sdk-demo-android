package pluto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lort on 2017/11/7.
 *          file name: "usrtable.tb"
 *         [
 *      	{
 *      	user:xxxxx
 *      	port:xxxxx
 *      	},
 *      	{
 *      	user:xxxxx
 *      	port:xxxxx
 *      	}
 *      ]
 */

public class UserTable {
    public static void initialization(){
        Aps.setSectionListener(AttributeID.PDO_User_Table,myListener);
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

    public static void reqRead(int keyID, byte[] addr, int seq)
    {
        Aps.reqSend(keyID,addr,seq,0x00, AttributeID.PDO_User_Table, AttributeID.Command.Read, AttributeID.Option.Default);
    }
    public static void reqDelete(int keyID, byte[] addr, int seq)
    {
        Aps.reqSend(keyID, addr, seq,0x00, AttributeID.PDO_User_Table, AttributeID.Command.Del, AttributeID.Option.Default);
    }
    public static int create(boolean white_list)
    {
        jUserTable = new JSONObject();
        JSONArray user_list = new JSONArray();
        try {
            int flag;
            if(white_list==false)
                flag = 0;
            else
                flag = 1;
            jUserTable.put("list_flag",flag);
            jUserTable.put("user_list",user_list);
        } catch (JSONException e) {
            e.printStackTrace();
            return Common.op_faile;
        }
        return Common.op_succeed;
    }
    public static int put(byte[] addr,byte[] port_list)
    {
        if(jUserTable==null) return Common.op_faile;
        JSONArray uer_list = null;
        try {
            uer_list = jUserTable.getJSONArray("user_list");
        } catch (JSONException e) {
            e.printStackTrace();
            return Common.op_faile;
        }
        String user = Clib.bytes2Hex(addr,'\0');
        byte[] portMap = convertBitmapFromPort(port_list);
        String portStr = Clib.bytes2Hex(portMap,'\0');
        JSONObject node = new JSONObject();
        try {
            node.put("user",user);
            node.put("port",portStr);
            uer_list.put(node);
        } catch (JSONException e) {
            e.printStackTrace();
            return Common.op_faile;
        }
        return Common.op_succeed;
    }
    public static int reqSave(int keyID, byte[] dst, int seq,String table)
    {
        byte[] buf = table.getBytes();
        Aps.reqSend(keyID,dst, seq, 0x00, AttributeID.PDO_User_Table, AttributeID.Command.Write, AttributeID.Option.Default,  buf, buf.length);
        return Common.op_succeed;
    }
    public static String output()
    {
        return jUserTable.toString();
    }
    private static Aps.onSectionListener myListener = new Aps.onSectionListener() {
        @Override
        public void RecieveCB(int keyID, byte[] src, int seq, int port, int aID,int cmd, int option, byte[] pdata, int len) {
            switch(cmd&0x7F) {
                case AttributeID.Command.Read://read scene respone]
                    readCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.Write://write scene respone
                    writeCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.Del://delect scene respone
                    deleteCB(keyID, src, pdata);
                    break;
            }
        }
        @Override
        public void SendStatus(byte[] src,int seq, int port, int aID, int cmd,int option, int state) {
            for (onSectionListener subsec : mySectionList){
                subsec.SendStatusCB(src,state);
            }
        }
    };
    private static void readCB(int keyID, byte[] addr,byte[] pdata)
    {
        /**pdata structure as floww:
         char 	crc[2];
         byte	nlen[2];
         byte	dlen[4];
         byte	pdata[0];*/
        int state = pdata[0];
        String logstr;
        String str = null;
        boolean white_list = false;
        if(state == Common.op_succeed) {
            str = Clib.btostr(pdata,1,pdata.length-1);
            logstr = "UserTable recieveCB: " + "data:" + str;
            JSONObject root = null;
            try {
                root = new JSONObject(str);
                int wl = root.getInt("list_flag");
                JSONArray user_list = root.getJSONArray("user_list");
                for (int i = 0; i < user_list.length(); i++) {
                    JSONObject node = (JSONObject) user_list.get(i);
                    String user = node.getString("user");
                    String port = node.getString("port");

                    if(wl==0)
                        white_list = false;//disable control
                    else
                        white_list = true;//enable control
                    byte[] uaddr = Clib.hexToBytes(user);
                    byte[] portMap = Clib.hexToBytes(port);
                    byte[] uport = convertPortFromBitmap(portMap);
                    for(onSectionListener subsec : mySectionList){
                        subsec.ReadCB(keyID, addr, state, white_list,uaddr, uport);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            logstr = "UserTable recieveCB: null";
            for(onSectionListener subsec : mySectionList){
                subsec.ReadCB(keyID, addr, Common.op_faile,white_list, null, null);
            }
        }
        Log.d(Common.TAG_Debug, logstr);
    }
    private static void writeCB(int keyID, byte[] addr, byte[] pdata)
    {
        int state = FileSystem.getState(pdata);
        Log.d(Common.TAG_Debug, "UserTable writeCB: "+"data:"+(int)state);
        for(onSectionListener subsec : mySectionList){
            subsec.WriteCB(keyID, addr,state);
        }
    }
    private static void deleteCB(int keyID, byte[] addr, byte[] pdata)
    {
        int state = FileSystem.getState(pdata);
        Log.d(Common.TAG_Debug, "UserTable deleteCB: "+" state:"+(int)state);
        for(onSectionListener subsec : mySectionList){
            subsec.DeleteCB(keyID, addr, state);
        }
    }
    private static int totalBitNum(byte[] bitmap)
    {
        int     num = 0;
        int    temp;
        for(int i=0;i<bitmap.length;i++)
        {
            temp = ((int)bitmap[i])&255;
            for(int j=0;j<8;j++)
            {
                if((temp&1)>0)
                    num++;
                temp>>=1;
            }
        }
        return num;
    }
    private static byte[] convertPortFromBitmap(byte[] bitmap)
    {
        int len = totalBitNum(bitmap);
        if(len==0)return null;
        byte[]  portList = new byte[len];
        int    temp;
        int     port = 255;
        int     portid = 0;
        for(int i=0;i<bitmap.length;i++)
        {
            temp = ((int)bitmap[i])&255;
            for(int j=0;j<8;j++)
            {
                if((temp&0x80)>0)
                    portList[portid++] = (byte)port;
                temp<<=1;
                port--;
            }
        }
        return portList;
    }
    private static byte[] convertBitmapFromPort(byte[] portList)
    {
        byte[]  bitmap = new byte[32];
        for(int i=0;i<portList.length;i++)
        {
            int temp = (int)portList[i]&255;
            byte offset = (byte)(31-(temp/8));
            byte bit = (byte)(temp%8);
            bitmap[offset] |= (byte)(1<<bit);
        }
        return bitmap;
    }
    private static ArrayList<onSectionListener> mySectionList = new ArrayList<onSectionListener>();
    private static JSONObject jUserTable = null;
    public interface onSectionListener{
        void ReadCB(int keyID, byte[] addr, int state,boolean white_list, byte[] userAddr, byte[] portlist);
        void WriteCB(int keyID, byte[] addr, int state);
        void DeleteCB(int keyID, byte[] addr, int state);
        void SendStatusCB(byte[] addr, int state);
    }

}
