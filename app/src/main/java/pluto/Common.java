package pluto;

/**
 * Created by lort on 2017/11/2.
 */

public class Common {

    public static String TAG = "Pluto";
    static int jseq = 0;
    public static final String TAG_Info = "Pluto_Info";
    public static final String TAG_Debug = "Pluto_Debug";
    public static final String TAG_Error = "Pluto_Error";

    static public int getSeq()
    {
        return jseq++;
    }

    public static final byte op_succeed = 0x00;
    public static final byte op_faile = 1;
    public static final byte op_error = 2;
    public static final byte op_invalid= 3;
    public static final byte op_permit_denied = 4;
    public static final byte op_repeat = 5;
    public static final byte op_no_port = 6;
    public static final byte op_no_file = 7;
    public static final byte op_read_error = 8;
    public static final byte op_write_error = 9;

    public static byte  deviceDiscoverKeyWord = 0;
    public static long  deviceAirkissStartTime = 0;

    public static final int airkissScanTimeout = (30*1000*1000);//

    public static final byte osFalse = 0x00;
    public static final byte osTrue = 0x01;
    public static final byte osDisable = 0x00;
    public static final byte osEnable = 0x01;
    public static final byte osError = (byte)0xFF;
    public static final byte osSucceed = 0x00;
    public static final byte osFailed = 0x01;
    public static final byte osParam_error = 0x02;
    public static final byte osPassword_error = 0x04;
    public static final byte osVersion_error = 0x06;

}
