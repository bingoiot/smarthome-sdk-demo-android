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
    //数据类型
    public static final byte data_type_unkown       = 0x00;
    public static final byte data_type_byte	      = 0x01;
    public static final byte data_type_int          = 0x02;
    public static final byte data_type_float        = 0x03;
    public static final byte data_type_string	    = 0x04;

    //根据aid 获取数据类型
    public static byte getDtypeByaID(int aID) {
        switch (aID) {
            case AttributeID.Gen_Key.Value:
            case AttributeID.Gen_Switch.Value:
            case AttributeID.Gen_WindowSwitch.Value:
            case AttributeID.Gen_IRCode.IRCode:
            case AttributeID.Gen_Locker.Value:
            case AttributeID.Gen_LockerPassword.Value:
            case AttributeID.Gen_LockerRFIDCard.Value:
            case AttributeID.Gen_Fan.Switch:
            case AttributeID.Gen_FanHorizontalDirection.Switch:
            case AttributeID.Gen_FanHorizontalDirection.Percent:
            case AttributeID.Gen_Alarm.FireAlarm:
            case AttributeID.Gen_Alarm.SmokeAlarm:
            case AttributeID.Gen_Alarm.PM2_5Alarm :
            case AttributeID.Gen_Alarm.FloodAlarm:
            case AttributeID.Gen_Alarm.WaterAlarm:
            case AttributeID.Gen_Alarm.PoisonGasAlarm:
            case AttributeID.Gen_Alarm.CarbonMonoxideAlarm:
            case AttributeID.Gen_Alarm.CombustibleAlarm:
            case AttributeID.Gen_Alarm.MenciAlarm:
            case AttributeID.Gen_Alarm.DisassembleAlarm:
            case AttributeID.Gen_Alarm.HumenCloseAlarm:
            case AttributeID.Gen_Alarm.EmergenciAlarm:
            case AttributeID.Gen_IRCode.HXDCode:
            case AttributeID.Gen_Color.Red:
            case AttributeID.Gen_Color.Green:
            case AttributeID.Gen_Color.Blue:
            case AttributeID.Gen_Color.Yellow:
            case AttributeID.Gen_Color.White:
            case AttributeID.Gen_Color.RGBW:
                return data_type_byte;
            case AttributeID.Gen_CommonLevel.Value:
            case AttributeID.Gen_Window.Percent:
            case AttributeID.Gen_Window.BlindsAngle:
            case AttributeID.Gen_Fan.Percent:
                return data_type_int;
            case AttributeID.Gen_Temperature.Value:
            case AttributeID.Gen_Humidity.Value:
            case AttributeID.Gen_KilowattHour.Value:
                return data_type_float;
            default:
                return data_type_unkown;
        }
    }
    //根据aid 获取 数据类型长度
    public static int getLenByaID(int aID) {
        switch (aID) {
            case AttributeID.Gen_Key.Value:
            case AttributeID.Gen_Switch.Value:
            case AttributeID.Gen_WindowSwitch.Value:
                return 1;
            case AttributeID.Gen_IRCode.IRCode:
                return -1;
            case AttributeID.Gen_Locker.Value:
            case AttributeID.Gen_LockerPassword.Value:
            case AttributeID.Gen_LockerRFIDCard.Value:
            case AttributeID.Gen_Fan.Switch:
            case AttributeID.Gen_FanHorizontalDirection.Switch:
            case AttributeID.Gen_FanHorizontalDirection.Percent:
            case AttributeID.Gen_Alarm.FireAlarm:
            case AttributeID.Gen_Alarm.SmokeAlarm:
            case AttributeID.Gen_Alarm.PM2_5Alarm :
            case AttributeID.Gen_Alarm.FloodAlarm:
            case AttributeID.Gen_Alarm.WaterAlarm:
            case AttributeID.Gen_Alarm.PoisonGasAlarm:
            case AttributeID.Gen_Alarm.CarbonMonoxideAlarm:
            case AttributeID.Gen_Alarm.CombustibleAlarm:
            case AttributeID.Gen_Alarm.MenciAlarm:
            case AttributeID.Gen_Alarm.DisassembleAlarm:
            case AttributeID.Gen_Alarm.HumenCloseAlarm:
            case AttributeID.Gen_Alarm.EmergenciAlarm:
                return 1;
            case AttributeID.Gen_IRCode.HXDCode:
                return -1;
            case AttributeID.Gen_Color.Red:
            case AttributeID.Gen_Color.Green:
            case AttributeID.Gen_Color.Blue:
            case AttributeID.Gen_Color.Yellow:
            case AttributeID.Gen_Color.White:
                return 1;
            case AttributeID.Unkown:
                return -1;
            case AttributeID.Gen_CommonLevel.Value:
            case AttributeID.Gen_Window.Percent:
            case AttributeID.Gen_Window.BlindsAngle:
            case AttributeID.Gen_Fan.Percent:
                return 1;
            case AttributeID.Gen_Temperature.Value:
            case AttributeID.Gen_Humidity.Value:
            case AttributeID.Gen_KilowattHour.Value:
                return -1;
            default:
                return -1;
        }}
}
