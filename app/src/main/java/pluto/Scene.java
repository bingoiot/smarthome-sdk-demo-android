package pluto;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by lort on 2017/11/2.
 */

/**
 * pdata structure as floww:
 * char 	crc[2];
 * byte	nlen[2];
 * byte	dlen[4];
 * byte	pdata[0];
 */
public class Scene {

    public class State {
        public static final int Run = 11;
        public static final int Pause = 12;
        public static final int Stop = 13;
        public static final int Finished = 14;
        public static final int Notask = 15;
        public static final int Systick = 16;
        public static final int Test = 17;
        public static final int Joined = 18;
        public static final int Param = 19;
    }

    public static void initialization() {
        Aps.setSectionListener(AttributeID.PDO_Scene,myListener);
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
    public static int reqRead(int keyID,byte[] addr,int seq, String fname) {
        byte[] buf = FileSystem.genPackage(fname);
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.Read, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqWrite(int keyID, byte[] addr, int seq, String fname, byte[] pdata) {
        byte[] buf = FileSystem.genPackage(fname, pdata);
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.Write, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqDel(int keyID, byte[] addr,int seq, String fname) {
        byte[] buf = FileSystem.genPackage(fname);
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.Del, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqReadAllName(int keyID, byte[] addr,int seq)//read all scence name
    {
        byte[] buf = FileSystem.genPackage("*.vm");
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.ReadName, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqRenameScene(int keyID, byte[] addr, int seq, String oldname, String newname)//rename scence
    {
        byte[] buf = FileSystem.genPackage(oldname, newname);
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.Rename, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqJoin(int keyID, byte[] addr, int seq, String tsk0, String tsk1)//join another scene and set the scene run util other's run finished
    {
        byte[] buf = FileSystem.genPackage(tsk0, tsk1);
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.WriteState, AttributeID.Option.Default, buf, buf.length);
    }

    public static int reqRun(int keyID, byte[] addr, int seq, String name, String arg)//run a scene in device by name, if no argument pls set null
    {
        byte[] buf = null;
        if (arg != "" && arg != null) {
            buf = FileSystem.genPackage(name, arg);

        } else {
            buf = FileSystem.genPackage(name, Common.op_vmTest);
        }
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.WriteState, AttributeID.Option.Default, buf, buf.length);
    }
    /**
     * read scene current state as flow:
     * 1 op_invalid = 9;
     * 2 op_vmRun = 10;
     * 3 op_vmPause = 11;
     * 4 op_vmStop = 12;
     * 5 op_vmFinished = 13;
     * 6 op_vmNoTask = 14;
     * 7 op_vmSysTick = 15;
     * 8 op_vmTest = 16;
     *
     * @param addr
     * @param keyID
     * @param name
     * @return
     */
    public static int reqReadState(int keyID, byte[] addr,int seq, String name) {
        byte[] buf = FileSystem.genPackage(name);
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.ReadState, AttributeID.Option.Default, buf, buf.length);
    }

    /**
     * set scene state as flow:
     * 2 op_vmRun = 10;
     * 3 op_vmPause = 11;
     * 4 op_vmStop = 12;
     *
     * @param addr
     * @param keyID
     * @param name
     * @param state
     * @return
     */
    public static int reqSetState(int keyID, byte[] addr, int seq, String name, int state) {
        byte[] buf = FileSystem.genPackage(name, (byte)state);
        return Aps.reqSend(keyID, addr,seq, (byte) 0x00, AttributeID.PDO_Scene, AttributeID.Command.WriteState, AttributeID.Option.Default, buf, buf.length);
    }
    public static int creatBody(){
        return NativeInterface.creatBody();
    }
    public static int addWhileBlock(int root, String reason){
        return NativeInterface.addWhileBlock(root,reason);
    }
    public static int addIfBlock(int root, String ifType, String reason){
        return NativeInterface.addIfBlock(root,ifType,reason);
    }
    public static int addAction(int root, String what, String param){
        return NativeInterface.addAction(root,what,param);
    }
    public static int addElseBlock(int root){
        return NativeInterface.addElseBlock(root);
    }
    public static String output(int root){
        return NativeInterface.output(root);
    }
    public static int release(){
        return NativeInterface.release();
    }
    public static  boolean isSceneState(byte state)
    {
        switch(state)
        {
            case Common.op_succeed:
            case Common.op_faile:
            case Common.op_vmInvalid:
            case Common.op_vmRun:
            case Common.op_vmPause:
            case Common.op_vmStop:
            case Common.op_vmFinished:
            case Common.op_vmNoTask:
            case Common.op_vmSysTick:
            case Common.op_vmTest:
            case Common.op_vmJoined:
            case Common.op_vm_param:
                return true;
            default:
                return false;
        }
    }
    private static Aps.onSectionListener myListener = new Aps.onSectionListener() {
        @Override
        public void RecieveCB(int keyID, byte[] src, int seq, int port, int aID,int cmd, int option, byte[] pdata, int len) {
            switch (cmd & 0x7F) {
                case AttributeID.Command.Read://read scene respone]
                    readCB(keyID,src, pdata);
                    break;
                case AttributeID.Command.Write://write scene respone
                    writeCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.Del://delect scene respone
                    deleteCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.Alarm://scene alarm
                    alarmCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.ReadName:
                    readNameCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.Rename:
                    renameCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.ReadState:
                    readStateCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.WriteState:
                    writeStateCB(keyID, src, pdata);
                    break;
                case AttributeID.Command.ReadCfg://no Commonine
                    break;
                case AttributeID.Command.WriteCfg://no Commonine
                    break;
            }
        }

        @Override
        public void SendStatus(byte[] src,int seq, int port, int aID, int cmd,int option, int state) {

        }
    };
    private static void readCB(int keyID, byte[] addr, byte[] pdata) {
        /**pdata structure as floww:
         char 	crc[2];
         byte	nlen[2];
         byte	dlen[4];
         byte	pdata[0];*/
        int state = FileSystem.getState(pdata);
        String fname = FileSystem.getName(null);
        byte[] pbuf = FileSystem.getData(null);
        String logstr = null;
        String str = null;
        if (pbuf != null) {
            str = new String(pbuf);
            logstr = "scene recieveCB: " + fname + "data:" + str;
        } else
            logstr = "scene recieveCB: " + fname;
        Log.d(Common.TAG_Debug, logstr);
        for(onSectionListener subsec: mySectionList){
            subsec.ReadCB(keyID, addr, fname, str);
        }
    }

    private static void writeCB(int keyID, byte[] addr, byte[] pdata) {
        int state = FileSystem.getState(pdata);
        String fname = FileSystem.getName(null);
        Log.d(Common.TAG_Debug, "scene writeCB: " + fname + "data:" + (int) state);
        for(onSectionListener subsec: mySectionList){
            subsec.WriteCB(keyID, addr, fname, state);
        }
    }

    private static void deleteCB(int keyID, byte[] addr, byte[] pdata) {
        int state = FileSystem.getState(pdata);
        String fname = FileSystem.getName(null);
        Log.d(Common.TAG_Debug, "scene deleteCB: " + fname + " state:" + (int) state);
        for(onSectionListener subsec: mySectionList){
            subsec.DeleteCB(keyID, addr, fname, state);
        }
    }

    private static void renameCB(int keyID, byte[] addr, byte[] pdata) {
        int state = FileSystem.getState(pdata);
        String fname = FileSystem.getName(null);
        Log.d(Common.TAG_Debug, "scene renameCB: " + fname + " state:" + (int) state);
        for(onSectionListener subsec: mySectionList){
            subsec.RenameCB(keyID, addr, fname, state);
        }
    }

    private static void readNameCB(int keyID, byte[] addr, byte[] pdata) {
        byte state = FileSystem.getState(pdata);
        if(isSceneState(state)==true) {
            String fname = "";
            fname = FileSystem.getName(null);
            Log.d(Common.TAG_Debug, "scene readNameCB: " + fname + " state:" + (int) state);
            for (onSectionListener subsec : mySectionList) {
                subsec.ReadNameCB(keyID, addr, fname, state);
            }
        }
    }

    private static void readStateCB(int keyID, byte[] addr, byte[] pdata) {
        int state = FileSystem.getState(pdata);
        String fname = FileSystem.getName(null);
        Log.d(Common.TAG_Debug, "scene readStateCB: " + fname + " state:" + (int) state);
        for(onSectionListener subsec: mySectionList){
            subsec.ReadStateCB(keyID, addr, fname, state);
        }
    }

    private static void writeStateCB(int keyID, byte[] addr, byte[] pdata) {
        int state = FileSystem.getState(pdata);
        String fname = FileSystem.getName(null);
        Log.d(Common.TAG_Debug, "scene writeStateCB: " + fname + " state:" + (int) state);
        for(onSectionListener subsec: mySectionList){
            subsec.WriteStateCB(keyID, addr, fname, state);
        }
    }

    private static void alarmCB(int keyID, byte[] addr, byte[] pdata) {
        int state = FileSystem.getState(pdata);
        String fname = FileSystem.getName(null);
        Log.d(Common.TAG_Debug, "scene alarmCB: " + fname + "data:" + (int) state);
        for(onSectionListener subsec: mySectionList){
            subsec.Alarm(keyID, addr, fname, state);
        }
    }

    public static void test(byte[] addr, byte keyID) {
        int jbody = creatBody();
        //addAction(jbody,"state","auto");
        addAction(jbody, "do", "x=0");
        int jwhile = addWhileBlock(jbody, "x < 10");
        int jif = addIfBlock(jwhile, "if", "wait(point=04,cmd=08,dtype=01,aID=008010)");
        addAction(jif, "do", "k=0");
        int jwhile1 = addWhileBlock(jif, "k < 5");
        addAction(jwhile1, "send", "addr:0100010100000019,keyID:00,point:01,cmd:02,dtype:01,aID:008010,dlen:0001,data:01");
        addAction(jwhile1, "delay", "200");
        addAction(jwhile1, "send", "addr:0100010100000019,keyID:00,point:02,cmd:02,dtype:01,aID:008010,dlen:0001,data:01");
        addAction(jwhile1, "delay", "200");
        addAction(jwhile1, "send", "addr:0100010100000019,keyID:00,point:03,cmd:02,dtype:01,aID:008010,dlen:0001,data:01");
        addAction(jwhile1, "delay", "200");
        addAction(jwhile1, "send", "addr:0100010100000019,keyID:00,point:03,cmd:02,dtype:01,aID:008010,dlen:0001,data:00");
        addAction(jwhile1, "delay", "200");
        addAction(jwhile1, "send", "addr:0100010100000019,keyID:00,point:02,cmd:02,dtype:01,aID:008010,dlen:0001,data:00");
        addAction(jwhile1, "delay", "200");
        addAction(jwhile1, "send", "addr:0100010100000019,keyID:00,point:01,cmd:02,dtype:01,aID:008010,dlen:0001,data:00");
        addAction(jwhile1, "delay", "200");
        addAction(jwhile1, "do", "k++");
        addAction(jif, "do", "x++");
        String out = output(jbody);
        Log.d(Common.TAG_Debug, "Test1: " + out);
        Scene.reqWrite(keyID, addr, Common.getSeq(),"Test1.vm", out.getBytes());
    }
    public interface onSectionListener{
        void ReadStateCB(int keyID, byte[] addr, String name, int state);
        void WriteStateCB(int keyID, byte[] addr, String name, int state);
        void ReadNameCB(int keyID, byte[] addr, String name, int state);
        void RenameCB(int keyID, byte[] addr, String fname, int state);
        void ReadCB(int keyID, byte[] addr, String fname, String pdata);
        void WriteCB(int keyID, byte[] addr, String fname, int state);
        void DeleteCB(int keyID, byte[] addr, String fname, int state);
        void Alarm(int keyID, byte[] addr, String fname, int state);
    }
    private static ArrayList<onSectionListener> mySectionList = new ArrayList<onSectionListener>();
}
