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

    /** as fallow constant you should use DeviceHelper.Type.xx to replace*/
    @Deprecated public static final byte dev_type_invalide = 0x00;
    @Deprecated public static final byte dev_type_gateway = 0x01;
    @Deprecated public static final byte dev_type_router = 0x02;
    @Deprecated public static final byte dev_type_device = 0x03;
    @Deprecated public static final byte dev_type_lowenergy = 0x04;

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

    /** as fallow constant you should use Scene.State.xx to replace*/
    @Deprecated public static final byte op_vmInvalid = 10;
    @Deprecated public static final byte op_vmRun = 11;
    @Deprecated public static final byte op_vmPause = 12;
    @Deprecated public static final byte op_vmStop = 13;
    @Deprecated public static final byte op_vmFinished = 14;
    @Deprecated public static final byte op_vmNoTask = 15;
    @Deprecated public static final byte op_vmSysTick = 16;
    @Deprecated public static final byte op_vmTest = 17;
    @Deprecated public static final byte op_vmJoined = 18;
    @Deprecated public static final byte op_vm_param=19;//19

    public static final byte op_upgrade_stop  = 0x20;
    public static final byte op_upgrade_start = 0x21;
    public static final byte op_upgrade_finished = 0x22;


    public static byte  deviceDiscoverKeyWord = 0;
    public static long  deviceAirkissStartTime = 0;

    public static final int airkissScanTimeout = (30*1000*1000);//

    /** as fallow constant you should use Pluto.LoginState.xx to replace*/
    @Deprecated public static final byte login_state_stop = 0;
    @Deprecated public static final byte login_state_start = 1;
    @Deprecated public static final byte login_state_online = 2;
    @Deprecated public static final byte login_state_offline = 3;
    @Deprecated public static final byte login_state_login_failed = 4;
    @Deprecated public static final byte login_state_logout_failed = 5;

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

    /** as fallow constant you should use AttributeID.Command.xx to replace*/
    @Deprecated public static final int cmd_invalide		=  	0x00;
    @Deprecated public static final int cmd_read			=	0x01;
    @Deprecated public static final int cmd_write 			= 	0x02;
    @Deprecated public static final int cmd_del				= 	0x03;
    @Deprecated public static final int cmd_toggle			= 	0x04;
    @Deprecated public static final int cmd_alarm			= 	0x05;
    @Deprecated public static final int cmd_readname		= 	0x06;
    @Deprecated public static final int cmd_rename 			= 	0x07;
    @Deprecated public static final int cmd_readstate		=   0x08;
    @Deprecated public static final int cmd_writestate		=	0x09;
    @Deprecated public static final int cmd_readCfg			= 	0x0A;
    @Deprecated public static final int cmd_writeCfg		= 	0x0B;
    @Deprecated public static final int cmd_beacon			= 	0x0C;
    @Deprecated public static final int cmd_notify			=	0x0D;
    @Deprecated public static final int cmd_return			=   0x80;

    /** we do not need data type again*/
    @Deprecated public static final byte data_type_unkown       = 0x00;
    @Deprecated public static final byte data_type_byte	        = 0x01;
    @Deprecated public static final byte data_type_int          = 0x02;
    @Deprecated public static final byte data_type_float        = 0x03;
    @Deprecated public static final byte data_type_string	    = 0x04;

    /** we do not need read or write attribute in future */
    @Deprecated public static final byte attr_read  	= 0x01;
    @Deprecated public static final byte attr_write	= 0x02;

    /** as fallow constant you should use DeviceHelp.KeyID.xx to replace*/
    @Deprecated public static final byte keyID_none 		= 0x00;
    @Deprecated public static final byte keyID_sn		    = 0x01;
    @Deprecated public static final byte keyID_admin		= 0x02;
    @Deprecated public static final byte keyID_common	    = 0x03;
    @Deprecated public static final byte keyID_guest		= 0x04;

    //public  static  final  int aID_GEN_OperationState=0xffffff;
    /*************** 通用设备类型**************************/
    /** as fallow constant you should use ApplicationID.xx to replace*/
    @Deprecated public static final int Application_ID_Key						    =0x001000;
    @Deprecated public static final int Application_ID_Switch						=0x001010;

    @Deprecated public static final int Application_ID_Light						    =0x001020;
    @Deprecated public static final int Application_ID_DimmerLight				    =0x001021;
    @Deprecated public static final int Application_ID_LED_Light					=0x001022;
    @Deprecated public static final int Application_ID_ColorLight				=0x001023;

    @Deprecated public static final int Application_ID_86Socket					    =0x001030;
    @Deprecated public static final int Application_ID_LineSocket					=0x001031;

    @Deprecated public static final int Application_ID_WidowCovering		        =0x001041;
    @Deprecated public static final int Application_ID_BlindWidowCovering		    =0x001043;
    @Deprecated public static final int Application_ID_PercentWidowCovering		=0x001045;

    @Deprecated public static final int Application_ID_Locker						    =0x001080;

    @Deprecated public static final int Application_ID_IRBox						    =0x001100;
    ////////////////////////////////////////////////////////
    @Deprecated public static final int Application_ID_Fan						    =0x002100;
    @Deprecated public static final int Application_ID_WatterRefrigeration		=0x002101;
    @Deprecated public static final int Application_ID_Television		            =0x002102;

    @Deprecated public static final int Application_ID_TempController				=0x002200;
    @Deprecated public static final int Application_ID_AirConditioning			    =0x002201;
    @Deprecated public static final int Application_ID_FanMachineController		=0x002202;
    ///////////////////////////////////////////////////////////
    @Deprecated public static final int Application_ID_FireAlarm					=0x004000;
    @Deprecated public static final int Application_ID_SmokeAlarm					=0x004001;
    @Deprecated public static final int Application_ID_PM25Alarm					=0x004002;

    @Deprecated public static final int Application_ID_FloodAlarm					=0x004010;
    @Deprecated public static final int Application_ID_WaterAlarm					=0x004011;

    @Deprecated public static final int Application_ID_PoisonGasAlarm				=0x004020;
    @Deprecated public static final int Application_ID_CarbonMonoxideAlarm		=0x004021;

    @Deprecated public static final int Application_ID_CombustibelAlarm			=0x004030;

    @Deprecated public static final int Application_ID_DoorMagneticAlarm			=0x004100;
    @Deprecated public static final int Application_ID_BodyInfraredAlarm			=0x004110;
    @Deprecated public static final int Application_ID_BodyMicroWaveAlarm			=0x004111;
    @Deprecated public static final int Application_ID_EmergencyButtonAlarm		=0x004120;
    @Deprecated public static final int Application_ID_SecurityAlarm		        =0x004FFF;
    ///////////////////////////////////////////////////////////
    @Deprecated public static final int Application_ID_HumidityAndTemSensor		=0x005000;
    @Deprecated public static final int Application_ID_TemperatureSensor			=0x005001;
    @Deprecated public static final int Application_ID_HumiditySensor				=0x005002;

    @Deprecated public static final int Application_ID_CarbonMonoxideSensor		=0x005010;
    @Deprecated public static final int Application_ID_CarbonDioxideSensor		=0x005011;
    @Deprecated public static final int Application_ID_OxygenSensor				    =0x005012;
    @Deprecated public static final int Application_ID_NitrogenSensor				=0x005013;
    @Deprecated public static final int Application_ID_OxygenAndNitrogenSensor	=0x005014;
    @Deprecated public static final int Application_ID_OzoneSensor				    =0x005015;

    @Deprecated public static final int Application_ID_NaturalGasSensor			=0x005020;
    @Deprecated public static final int Application_ID_MethaneSensor				=0x005021;

    @Deprecated public static final int Application_ID_FormaldehydeSensor			=0x005030;

    @Deprecated public static final int Application_ID_LocationSensor				=0x005100;
    @Deprecated public static final int Application_ID_DistanceSensor				=0x005101;
    @Deprecated public static final int Application_ID_HeightSensor				    =0x005102;
    @Deprecated public static final int Application_ID_AltitudeSensor				=0x005104;

    @Deprecated public static final int Application_ID_AirPressureSensor			=0x005110;
    @Deprecated public static final int Application_ID_WatterPressureSensor		=0x005111;

    @Deprecated public static final int Application_ID_GravitySensor				=0x005120;

    @Deprecated public static final int Application_ID_LightSensor				    =0x005130;

    @Deprecated public static final int Application_ID_SpeedSensor				    =0x005140;
    @Deprecated public static final int Application_ID_AccelerationSensor			=0x005141;

    @Deprecated public static final int Application_ID_WindSpeedSensor			    =0x005150;

    @Deprecated public static final int Application_ID_FlowSensor					=0x005160;

    @Deprecated public static final int Application_ID_KilowattHourSensor			=0x005170;
    @Deprecated public static final int Application_ID_WattHourSensor				=0x005171;
    @Deprecated public static final int Application_ID_VoltageSensor				=0x005172;
    @Deprecated public static final int Application_ID_AmmeterSensor				=0x005173;

    ///////////////////////////////////////////////////////////
    @Deprecated public static final int Application_ID_RFASKRemoteCtrl			    =0x007000;
    @Deprecated public static final int Application_ID_UART						    =0x007010;

    /*************通用数据属性*********************************/
    /**use AttributeID.Option.Default to instead*/
    @Deprecated public static final int aID_Common_Option                    = 0x00;

    /**use AttributeID.xx.Default to instead*/
    @Deprecated public static final int aID_PDO_Type_Factory                 =0x000000; //
    @Deprecated public static final int aID_PDO_Type_Device_Info             =0x000001; //
    @Deprecated public static final int aID_PDO_Type_Port_List	            =0x000002;
    @Deprecated public static final int aID_PDO_Type_Port_Describe          =0x000003;
    @Deprecated public static final int aID_PDO_Type_Scene                   =0x000004;
    @Deprecated public static final int aID_PDO_Type_File                     =0x000005;
    @Deprecated public static final int aID_PDO_Type_White_Table	          =0x000006;
    @Deprecated public static final int aID_PDO_Type_Alarm_Record			 =0x000007;
    @Deprecated public static final int aID_PDO_Type_History_Record	         =0x000008;
    @Deprecated public static final int aID_PDO_Type_Beacon                  =0x00000F;
    @Deprecated public static final int aID_PDO_Type_Device_Indication        =0x000010;
    @Deprecated public static final int aID_PDO_Type_Time                   =0x000011;
    @Deprecated public static final int aID_PDO_Type_Upgrade				  =0x000012;
    @Deprecated public static final int aID_PDO_Type_ManufactureID		  =0x0000FF;

    @Deprecated public static final int aID_PDO_Type_Port_Indication      =0x000100;
    @Deprecated public static final int aID_PDO_Type_Permite_Join           =0x000101;
    @Deprecated public static final int aID_Gen_Type_LQI                    =0x000102;
    ////////////////////////////////////////////////////////////
    @Deprecated public static final int aID_Gen_Type_KeyValue					=0x009000;
    @Deprecated public static final int aID_Gen_Type_KeyShift					=0x009001;

    @Deprecated public static final int aID_Gen_Type_Switch					=0x009020;
    @Deprecated public static final int aID_Gen_Type_SocketSwitch		    =0x009021;

    @Deprecated public static final int aID_Gen_Type_CommonPersent		    =0x009030;
    @Deprecated public static final int aID_Gen_Type_Red				        =0x009031;
    @Deprecated public static final int aID_Gen_Type_Green				    =0x009032;
    @Deprecated public static final int aID_Gen_Type_Blue				        =0x009033;
    @Deprecated public static final int aID_Gen_Type_Yellow				    =0x009034;
    @Deprecated public static final int aID_Gen_Type_White				    =0x00903F;

    @Deprecated public static final int aID_Gen_Type_WindowSwitch			=0x009050;
    @Deprecated public static final int aID_Gen_Type_WindowPercent			=0x009051;
    @Deprecated public static final int aID_Gen_Type_BlindsAngle				=0x009052;

    @Deprecated public static final int aID_Gen_Type_IRCode					=0x009055;
    @Deprecated public static final int aID_Gen_Type_IRHXD					=0x009056;
    @Deprecated public static final int aID_Gen_Type_RFCode					=0x00905A;
    @Deprecated public static final int aID_Gen_Type_RFLearn					=0x00905B;

    @Deprecated public static final int aID_Gen_Type_Locker						=0x009080;
    @Deprecated public static final int aID_Gen_Type_Locker_RFID				    =0x009081;
    @Deprecated public static final int aID_Gen_Type_Locker_PSW					=0x009082;
    @Deprecated public static final int aID_Gen_Type_Locker_Finger				=0x009083;

    @Deprecated public static final int aID_Gen_Type_FanSwitch					=0x009060;
    @Deprecated public static final int aID_Gen_Type_FanSpeedPercent			=0x009061;
    @Deprecated public static final int aID_Gen_Type_FanForwardSwitch			=0x009062;
    @Deprecated public static final int aID_Gen_Type_FanWaterSwitch				=0x009063;

    @Deprecated public static final int aID_Gen_Type_FireAlarm					=0x00C000;
    @Deprecated public static final int aID_Gen_Type_SmokeAlarm					=0x00C001;
    @Deprecated public static final int aID_Gen_Type_PM25Alarm 					=0x00C002;

    @Deprecated public static final int aID_Gen_Type_FloodAlarm					=0x00C010;
    @Deprecated public static final int aID_Gen_Type_WaterAlarm					=0x00C011;

    @Deprecated public static final int aID_Gen_Type_PoisonGasAlarm				=0x00C020;
    @Deprecated public static final int aID_Gen_Type_CarbonMonoxideAlarm		=0x00C021;

    @Deprecated public static final int aID_Gen_Type_CombustibleAlarm			=0x00C030;

    @Deprecated public static final int aID_Gen_Type_MenciAlarm					=0x00C100;
    @Deprecated public static final int aID_Gen_Type_HumenIRAlarm				=0x00C110;
    @Deprecated public static final int aID_Gen_Type_HumenWaveAlarm				=0x00C111;
    @Deprecated public static final int aID_Gen_Type_EmergencyButton			=0x00C120;

    @Deprecated public static final int aID_Gen_Type_DisassembleAlarm			=0x00C400;
    @Deprecated public static final int aID_Gen_Type_SecurityAlarm			    =0x00CFFF;

    @Deprecated public static final int aID_Gen_Type_TemperatureSensor			=0x00D000;
    @Deprecated public static final int aID_Gen_Type_HumiditySensor				=0x00D001;

    @Deprecated public static final int aID_Gen_Type_PushPowerSensor			=0x00D130;

    @Deprecated public static final int aID_Gen_Type_KilowattHourSensor			=0x00D170;
    @Deprecated public static final int aID_Gen_Type_WattHourSensor				=0x00D171;

    @Deprecated public static final int aID_Gen_Type_KilowattSensor				=0x00D174;
    @Deprecated public static final int aID_Gen_Type_WattSensor					=0x00D175;
    @Deprecated public static final int aID_Gen_Type_VoltageSensor				=0x00D178;
    @Deprecated public static final int aID_Gen_Type_AmpSensor					=0x00D17C;
    @Deprecated public static final int aID_Gen_Type_Unkown						=0x00FFFF;

    @Deprecated public static byte getDtypeByaID(int aID) {
        switch (aID) {
            case aID_Gen_Type_KeyValue:
            case aID_Gen_Type_KeyShift:
            case aID_Gen_Type_Switch:
            case aID_Gen_Type_SocketSwitch:
            case aID_Gen_Type_WindowSwitch:
            case aID_Gen_Type_IRCode:
            case aID_Gen_Type_RFCode:
            case aID_Gen_Type_Locker:
            case aID_Gen_Type_Locker_PSW:
            case aID_Gen_Type_Locker_RFID:
            case aID_Gen_Type_Locker_Finger:
            case aID_Gen_Type_FanSwitch:
            case aID_Gen_Type_FanForwardSwitch:
            case aID_Gen_Type_FanWaterSwitch:
            case aID_Gen_Type_FireAlarm:
            case aID_Gen_Type_SmokeAlarm:
            case aID_Gen_Type_PM25Alarm :
            case aID_Gen_Type_FloodAlarm:
            case aID_Gen_Type_WaterAlarm:
            case aID_Gen_Type_PoisonGasAlarm:
            case aID_Gen_Type_CarbonMonoxideAlarm:
            case aID_Gen_Type_CombustibleAlarm:
            case aID_Gen_Type_MenciAlarm:
            case aID_Gen_Type_DisassembleAlarm:
            case aID_Gen_Type_HumenIRAlarm:
            case aID_Gen_Type_HumenWaveAlarm:
            case aID_Gen_Type_EmergencyButton:
            case aID_Gen_Type_IRHXD:
            case aID_Gen_Type_RFLearn:
            case aID_Gen_Type_Red:
            case aID_Gen_Type_Green:
            case aID_Gen_Type_Blue:
            case aID_Gen_Type_Yellow:
            case aID_Gen_Type_White:
            case aID_Gen_Type_Unkown:
                return data_type_byte;
            case aID_Gen_Type_CommonPersent:
            case aID_Gen_Type_WindowPercent:
            case aID_Gen_Type_BlindsAngle:
            case aID_Gen_Type_FanSpeedPercent:
                return data_type_int;
            case aID_Gen_Type_TemperatureSensor:
            case aID_Gen_Type_HumiditySensor:
            case aID_Gen_Type_PushPowerSensor:
            case aID_Gen_Type_KilowattHourSensor:
            case aID_Gen_Type_WattHourSensor:
            case aID_Gen_Type_KilowattSensor:
            case aID_Gen_Type_WattSensor:
            case aID_Gen_Type_VoltageSensor:
            case aID_Gen_Type_AmpSensor:
                return data_type_float;
            default:
                return data_type_unkown;
        }
    }
    //根据aid 获取 数据类型长度
    @Deprecated public static int getLenByaID(int aID) {
        switch (aID) {
            case aID_Gen_Type_KeyValue:
            case aID_Gen_Type_KeyShift:
            case aID_Gen_Type_Switch:
            case aID_Gen_Type_SocketSwitch:
            case aID_Gen_Type_WindowSwitch:
                return 1;
            case aID_Gen_Type_IRCode:
            case aID_Gen_Type_RFCode:
                return -1;
            case aID_Gen_Type_Locker:
            case aID_Gen_Type_Locker_PSW:
            case aID_Gen_Type_Locker_RFID:
            case aID_Gen_Type_Locker_Finger:
            case aID_Gen_Type_FanSwitch:
            case aID_Gen_Type_FanForwardSwitch:
            case aID_Gen_Type_FanWaterSwitch:
            case aID_Gen_Type_FireAlarm:
            case aID_Gen_Type_SmokeAlarm:
            case aID_Gen_Type_PM25Alarm :
            case aID_Gen_Type_FloodAlarm:
            case aID_Gen_Type_WaterAlarm:
            case aID_Gen_Type_PoisonGasAlarm:
            case aID_Gen_Type_CarbonMonoxideAlarm:
            case aID_Gen_Type_CombustibleAlarm:
            case aID_Gen_Type_MenciAlarm:
            case aID_Gen_Type_DisassembleAlarm:
            case aID_Gen_Type_HumenIRAlarm:
            case aID_Gen_Type_HumenWaveAlarm:
            case aID_Gen_Type_EmergencyButton:
                return 1;
            case aID_Gen_Type_IRHXD:
            case aID_Gen_Type_RFLearn:
                return -1;
            case aID_Gen_Type_Red:
            case aID_Gen_Type_Green:
            case aID_Gen_Type_Blue:
            case aID_Gen_Type_Yellow:
            case aID_Gen_Type_White:
                return 1;
            case aID_Gen_Type_Unkown:
                return -1;
            case aID_Gen_Type_CommonPersent:
            case aID_Gen_Type_WindowPercent:
            case aID_Gen_Type_BlindsAngle:
            case aID_Gen_Type_FanSpeedPercent:
                return 1;
            case aID_Gen_Type_TemperatureSensor:
            case aID_Gen_Type_HumiditySensor:
            case aID_Gen_Type_PushPowerSensor:
            case aID_Gen_Type_KilowattHourSensor:
            case aID_Gen_Type_WattHourSensor:
            case aID_Gen_Type_KilowattSensor:
            case aID_Gen_Type_WattSensor:
            case aID_Gen_Type_VoltageSensor:
            case aID_Gen_Type_AmpSensor:
                return -1;
            default:
                return -1;
        }
    }

}
