package pluto;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lort on 2018/5/28.
 */

public class Record{
    private static final String alarmFileName = "alarm.rd";
    private static final String historyFileName = "history.rd";
    public static void initialization()
    {
        Aps.setSectionListener(AttributeID.PDO_Alarm_Record,recordListener);
        Aps.setSectionListener(AttributeID.PDO_History_Record,recordListener);
    }
    public static int setAlarmRecordSectionListener(onSectionListener listener){
        boolean isHave = false;
        for (onSectionListener subsec : myAlarmRecordSectionList) {
            if (subsec==listener) {
                isHave = true;
            }
        }
        if (!isHave) {
            myAlarmRecordSectionList.add(listener);
            return Common.op_succeed;
        }
        return Common.op_faile;
    }
    public static int removeAlarmSectionListener(onSectionListener myListener)
    {
        int ret = Common.op_faile;
        for (onSectionListener subsec : myAlarmRecordSectionList) {
            if (subsec==myListener){
                myAlarmRecordSectionList.remove(subsec);
                ret = Common.op_succeed;
            }
        }
        return ret;
    }
    public static int resetAlarmRecordSectionListener(){
        myAlarmRecordSectionList = new ArrayList<onSectionListener>();
        return Common.op_succeed;
    }
    public static int setHistroryRecordSectionListener(onSectionListener listener){
        boolean isHave = false;
        for (onSectionListener subsec : myAlarmRecordSectionList) {
            if (subsec==listener) {
                isHave = true;
            }
        }
        if (!isHave) {
            myAlarmRecordSectionList.add(listener);
            return Common.op_succeed;
        }
        return Common.op_faile;
    }
    public static int removeHistorySectionListener(onSectionListener myListener)
    {
        int ret = Common.op_faile;
        for (onSectionListener subsec : myAlarmRecordSectionList) {
            if (subsec==myListener){
                myAlarmRecordSectionList.remove(subsec);
                ret = Common.op_succeed;
            }
        }
        return ret;
    }
    public static int resetHistoryRecordSectionListener(){
        myAlarmRecordSectionList = new ArrayList<onSectionListener>();
        return Common.op_succeed;
    }
    public static int reqReadHistory(byte keyID,byte[] addr,int seq)
    {
        return Aps.reqSend(keyID,addr,seq,(byte)0x00, AttributeID.PDO_History_Record, AttributeID.Command.Read, AttributeID.Option.Default);
    }
    public static int reqDeleteHistory(byte keyID,byte[] addr,int seq)
    {
        return Aps.reqSend(keyID,addr,seq,(byte)0x00, AttributeID.PDO_History_Record, AttributeID.Command.Del, AttributeID.Option.Default);
    }
    public static int reqReadHistoryEnableFlag(byte keyID, byte[] addr, int seq)
    {
        return Aps.reqSend(keyID,addr,seq,0,AttributeID.PDO_History_Record, AttributeID.Command.ReadState,AttributeID.Option.Default);
    }
    public static int reqWriteHistoryEnableFlag(byte keyID, byte[] addr, int seq, boolean enable)
    {
        byte[] buf = new byte[1];
        if(enable==false)
            buf[0] = 0;
        else
            buf[0] = 1;
        return Aps.reqSend(keyID,addr,seq,0,AttributeID.PDO_History_Record, AttributeID.Command.WriteState,AttributeID.Option.Default,buf,1);
    }
    public static int reqReadAlarm(byte keyID,byte[] addr,int seq)
    {
        return Aps.reqSend(keyID,addr,seq,(byte)0x00, AttributeID.PDO_Alarm_Record, AttributeID.Command.Read, AttributeID.Option.Default);
    }
    public static int reqDeleteAlarm(byte keyID,byte[] addr,int seq)
    {
        return Aps.reqSend(keyID,addr,seq,(byte)0x00, AttributeID.PDO_Alarm_Record, AttributeID.Command.Del, AttributeID.Option.Default);
    }
    private static Aps.onSectionListener recordListener = new Aps.onSectionListener() {
        @Override
        public void RecieveCB(int keyID, byte[] src,int seq, int port,int aID, int cmd, int option,  byte[] pdata, int len) {
            if(aID== AttributeID.PDO_Alarm_Record) {
                switch(cmd&0x7F) {
                    case AttributeID.Command.Read://read scene respone]
                        ProcessCallBack(myAlarmRecordSectionList,keyID,src,seq,pdata);
                        break;
                    case AttributeID.Command.Del://delect scene respone
                        int state = FileSystem.getState(pdata);
                        String fname = FileSystem.getName(null);
                        Log.d(Common.TAG_Debug, "deleteCB: "+fname+" state:"+(int)state);
                        for (onSectionListener subsec : myAlarmRecordSectionList) {//search every section listener to callback
                            subsec.DeleteCB(keyID,src,seq,state);
                        }
                        break;
                }
            }
            else if(aID== AttributeID.PDO_History_Record) {
                switch(cmd&0x7F) {
                    case AttributeID.Command.Read://read scene respone]
                        ProcessCallBack(myHistoryRecordSectionList,keyID,src,seq,pdata);
                        break;
                    case AttributeID.Command.Del://delect scene respone
                        int state = FileSystem.getState(pdata);
                        String fname = FileSystem.getName(null);
                        Log.d(Common.TAG_Debug, "deleteCB: "+fname+" state:"+(int)state);
                        for (onSectionListener subsec : myHistoryRecordSectionList) {//search every section listener to callback
                            subsec.DeleteCB(keyID,src,seq,state);
                        }
                        break;
                    case AttributeID.Command.ReadState:
                        for (onSectionListener subsec : myHistoryRecordSectionList) {//search every section listener to callback
                            subsec.ReadStateCB(keyID,src,seq,pdata[0],pdata[1]);
                        }
                        break;
                    case AttributeID.Command.WriteState:
                        for (onSectionListener subsec : myHistoryRecordSectionList) {//search every section listener to callback
                            subsec.WriteStateCB(keyID,src,seq,pdata[0]);
                        }
                        break;
                }
            }
        }
        @Override
        public void SendStatus(byte[] src,int seq,int port,int aID, int cmd, int option,int state) {
            if(aID== AttributeID.PDO_Alarm_Record) {
                for (onSectionListener subsec : myAlarmRecordSectionList) {//search every section listener to callback
                    subsec.SendState(src,seq,state);
                }
            }
            else if(aID== AttributeID.PDO_History_Record) {
                for (onSectionListener subsec : myHistoryRecordSectionList) {//search every section listener to callback
                    subsec.SendState(src,seq,state);
                }
            }
        }
    };
    private static void ProcessCallBack(ArrayList<onSectionListener> sectionList, int keyID, byte[] src, int seq, byte[] pdata)
    {
        int state = pdata[0];
        if(state == Common.op_succeed) {
            String str = Clib.btostr(pdata,1,pdata.length-1);
            if (str != null) {
                try {
                    JSONArray arrayList = new JSONArray(str);
                    for (int i = 0; i < arrayList.length(); i++) {
                        JSONObject root = (JSONObject) arrayList.get(i);
                        String tagStr = root.getString("tag");
                        long unix_time = root.getLong("date");
                        String cmdStr = root.getString("cmd");
                        String addStr = root.getString("addr");
                        String dataStr = root.getString("data");
                        byte[] toaddr = Clib.hexToBytes(addStr);
                        byte[] todata = Clib.hexToBytes(dataStr);
                        byte[] tocmd = Clib.hexToBytes(cmdStr);
                        int sport = get_port(toaddr);
                        int skeyID = get_keyID(toaddr);
                        byte[] dev_addr = get_addr(toaddr);
                        long saID = get_aID(tocmd);
                        int scmd = get_cmd(tocmd);
                        int soption = get_option(tocmd);
                        for (onSectionListener subsec : sectionList) {//search every section listener to callback
                            subsec.ReadCB(keyID, src, seq, state, tagStr, unix_time, dev_addr, sport, scmd, soption, saID, todata);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else{
            for (onSectionListener subsec : sectionList) {//search every section listener to callback
                subsec.ReadCB(keyID,src,seq, state,null, 0,null,0,(byte)0,0,0,null);
            }
        }
    }
    private static byte[] get_addr(byte[] data)
    {
        byte[] addr = new byte[8];
        System.arraycopy(data,0,addr,0,8);
        return addr;
    }
    private static int get_port(byte[] data)
    {
        return data[8]&255;
    }
    private static int get_keyID(byte[] data)
    {
        return data[9]&255;
    }
    private static int get_cmd(byte[] data)
    {
        return data[0]&255;
    }
    private static int get_option(byte[] data)
    {
        return data[1]&255;
    }
    private static long get_aID(byte[] data)
    {
        long aid = Clib.btoi(data,2,4);
        return aid;
    }
    public interface onSectionListener{
        public void ReadCB(int keyID, byte[] src, int seq, int state,String tag, long unix_time, byte[] dev_addr, int port, int cmd, int option, long aID, byte[] pdata);
        public void DeleteCB(int keyID, byte[] addr, int seq, int state);
        public void ReadStateCB(int keyID, byte[] src, int seq, int state, int enable);
        public void WriteStateCB(int keyID, byte[] src, int seq, int  state);
        public void SendState(byte[] src, int seq, int state);
    }
    private static ArrayList<onSectionListener> myAlarmRecordSectionList = new ArrayList<onSectionListener>();
    private static ArrayList<onSectionListener> myHistoryRecordSectionList = new ArrayList<onSectionListener>();
}
