package pluto;

/**
 * Created by lort on 2019/2/20.
 */

public class AttributeID {

    public class Command
    {
        public static final int Invalide = 0x00;
        public static final int Read = 0x01;
        public static final int Write = 0x02;
        public static final int Del = 0x03;
        public static final int Toggle = 0x04;
        public static final int Alarm = 0x05;
        public static final int ReadName = 0x06;
        public static final int Rename = 0x07;
        public static final int ReadState = 0x08;
        public static final int WriteState = 0x09;
        public static final int ReadCfg = 0x0A;
        public static final int WriteCfg = 0x0B;
        public static final int Beacon = 0x0C;
        public static final int Notify = 0x0D;
        public static final int Response = 0x80;
    }

    public class Option {
        public static final int Default = 0x00;
    }

    public static final int PDO_Factory                 =0x000000; //
    public static final int PDO_Device_Info             =0x000001; //
    public static final int PDO_Port_List	            =0x000002;
    public static final int PDO_Port_Describe           =0x000003;
    public static final int PDO_Scene                   =0x000004;
    public static final int PDO_File                     =0x000005;
    public static final int PDO_User_Table	             =0x000006;
    public static final int PDO_Alarm_Record			 =0x000007;
    public static final int PDO_History_Record	         =0x000008;
    public static final int PDO_Beacon                  =0x00000F;
    public static final int PDO_Device_Indication        =0x000010;
    public static final int PDO_Time                   =0x000011;
    public static final int PDO_Upgrade				  =0x000012;
    public static final int PDO_ManufactureID		  =0x0000FF;

    public static final int PDO_SubDevice              =0x000100;

    public static final int PDO_LQI                    =0x000102;
    ////////////////////////////////////////////////////////////

    public class Gen_Key{
        public static final int Value = 0x009000;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int KeyDown = 0x80;//key down
            public static final int KeyKeep = 0x81;//key down
            public static final int KeyUp = 0x82;//key down
        }
    }

    public class Gen_Switch{
        public static final int Value = 0x009020;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Write = 0x02;
            public static final int WriteResponse = 0x82;
            public static final int Toggle = 0x04;
            public static final int ToggleResponse = 0x84;
        }
    }

    public class Gen_CommonLevel{
        public static final int Value = 0x009030;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Write = 0x02;
            public static final int WriteResponse = 0x82;
        }
    }

    public class Gen_Color{
        public static final int Red				        =0x009031;
        public static final int Green				    =0x009032;
        public static final int Blue				    =0x009033;
        public static final int Yellow				    =0x009034;
        public static final int White				    =0x00903F;
        public static final int RGBW				    =0x009040;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Write = 0x02;
            public static final int WriteResponse = 0x82;
        }
    }

    public class Gen_WindowSwitch{
        public static final int Value = 0x009050;
        public class Option{
            public static final int Default = 0x00;
            public static final int Percent = 0x01;
        }
        public class Command{
            /** support to command option with Default and percent;*/
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Set = 0x02;
            public static final int SetResponse = 0x82;
            /**only support Default command option*/
            public static final int SetWithDuration = 0x03;
            public static final int SetWithDurationResponse = 0x83;
            public static final int ReadForward = 0x0A;
            public static final int ReadForwordResponse = 0x8A;
            public static final int SetForward = 0x0B;
            public static final int SetForwardResponse = 0x8B;
            public static final int ReadTravelTime = 0x0C;
            public static final int ReadTravelTimeResponse = 0x8C;
            public static final int SetTravelTime = 0x0D;
            public static final int SetTraveTimeResponse = 0x8D;
        }
    }
    public class Gen_Window{
        public static final int Percent = 0x009051;
        public static final int BlindsAngle = 0x009052;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Set = 0x02;
            public static final int SetResponse = 0x82;

            public static final int ReadForward = 0x0A;
            public static final int ReadForwordResponse = 0x8A;
            public static final int SetForward = 0x0B;
            public static final int SetForwardResponse = 0x8B;
        }
    }
    public class Gen_IRCode{
        public static final int IRCode					=0x009055;
        public static final int HXDCode                 =0x009056;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Send = 0x02;
            public static final int SendResponse = 0x82;
        }
    }
    public class Gen_Fan{
        public static final int Switch					=0x009060;
        public static final int Percent                 =0x009061;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Set = 0x02;
            public static final int SetResponse = 0x82;
        }
    }
    public class Gen_FanHorizontalDirection{
        public static final int Switch					=0x009062;
        public static final int Percent                 =0x009063;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Set = 0x02;
            public static final int SetResponse = 0x82;
        }
    }
    public class Gen_FanVerticalDirection{
        public static final int Switch					=0x009064;
        public static final int Percent                 =0x009065;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Set = 0x02;
            public static final int SetResponse = 0x82;
        }
    }

    public class Gen_Locker{
        public static final int Value					=0x009080;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int Set = 0x02;
            public static final int SetResponse = 0x82;
        }
    }
    public class Gen_LockerRFIDCard{
        public static final int Value					=0x009081;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int ReadNum = 0x01;
            public static final int ReadNumResponse = 0x81;
            public static final int ReadIDCard = 0x02;
            public static final int ReadIDCardResponse = 0x82;
            public static final int DelIDCardByID = 0x03;
            public static final int DelIDCardByIDResponse = 0x83;
            public static final int DelIDCardByUser = 0x04;
            public static final int DelIDCardByUserResponse = 0x84;
            public static final int AddIDCard = 0x05;
            public static final int AddIDCardResponse = 0x85;
        }
    }
    public class Gen_LockerPassword{
        public static final int Value					=0x009082;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int ReadNum = 0x01;
            public static final int ReadNumResponse = 0x81;
            public static final int ReadPassword = 0x02;
            public static final int ReadPasswordResponse = 0x82;
            public static final int DelPasswordByID = 0x03;
            public static final int DelPasswordByIDResponse = 0x83;
            public static final int DelPasswordByUser = 0x04;
            public static final int DelPasswordByUserResponse = 0x84;
            public static final int AddPassword = 0x05;
            public static final int AddPasswordResponse = 0x85;
        }
    }
    public class Gen_LockerFinger{
        public static final int Value					=0x009082;
        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Recieve = 0x80;
            public static final int ReadNum = 0x01;
            public static final int ReadNumResponse = 0x81;
            public static final int ReadFinger = 0x02;
            public static final int ReadFingerResponse = 0x82;
            public static final int DelFingerByID = 0x03;
            public static final int DelFingerByIDResponse = 0x83;
            public static final int DelFingerByUser = 0x04;
            public static final int DelFingerByUserResponse = 0x84;
            public static final int AddFinger = 0x05;
            public static final int AddFingerResponse = 0x85;
        }
    }
    public class Gen_Alarm{

        public class Option{
            public static final int Default = 0x00;
        }
        public class Command{
            public static final int Alram = 0x85;
        }
        public static final int FireAlarm					=0x00C000;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int SmokeAlarm					=0x00C001;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int PM2_5Alarm 					=0x00C002;/**state[1] = 0:disalarm, 1: Alarm*/

        public static final int FloodAlarm					=0x00C010;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int WaterAlarm					=0x00C011;/**state[1] = 0:disalarm, 1: Alarm*/

        public static final int PoisonGasAlarm				=0x00C020;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int CarbonMonoxideAlarm		    =0x00C021;/**state[1] = 0:disalarm, 1: Alarm*/

        public static final int CombustibleAlarm			=0x00C030;/**state[1] = 0:disalarm, 1: Alarm*/

        public static final int MenciAlarm					=0x00C100;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int HumenCloseAlarm				=0x00C110;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int EmergenciAlarm				=0x00C120;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int LowPowerAlarm				=0x00C130;/**state[1] = 0:disalarm, 1: Alarm*/
        public static final int DisassembleAlarm			=0x00C400;/**state[1] = 0:disalarm, 1: Alarm*/

        public static final int Gen_AlarmLight			    =0x00CFFD;/**mode[1], mode=0:default,1:,mode1,2:mode2,3:mode3*/
        public static final int Gen_AlarmVoice			    =0x00CFFE;/**mode[1]+level[1], mode=0:default,1:,mode1,2:mode2,3:mode3, level=0:off,1:L,2:M,3:H*/
        public static final int Gen_AlarmZone			    =0x00CFFF;/**state[1] = 0:disalarm, 1: Alarm*/
    }
    public class Gen_Temperature {

        public class Option {
            public static final int Temp = 0x00;/**Unit ℃ ,Humidity = Ascii float code,example: 12.4*/
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D000;
    }
    public class Gen_Humidity {

        public class Option {
            public static final int RH = 0x00;/**Unit RH ,Humidity = Ascii float code,example: 12.4*/
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D001;/**Unit RH ,Humidity = Ascii float code,example: 12.4*/
    }
    public class Gen_Illuminance {

        public class Option {
            public static final int Lex = 0x00;/**Unit:Lex, Illuminance Ascii float code,example: 12.4*/
            public static final int Lm = 0x01;/**Unit:lm Illuminance Ascii float code,example: 12.4*/
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D003;/** Illuminance = Ascii float code,example: 12.4*/
    }
    public class Gen_Presure {

        public class Option {
            public static final int Pa = 0x00;
            public static final int KPa = 0x01;
            public static final int MPa = 0x02;
            public static final int GPa = 0x03;
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D105;/** Presure = Ascii float code,example: 12.4*/
    }
    public class Gen_Flowmeter {

        public class Option {
            public static final int mL = 0x00;
            public static final int L = 0x01;
            public static final int ME3 = 0x02;
            public static final int KME3 = 0x03;
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D160;/** Flowmeter = Ascii float code,example: 12.4*/
    }
    public class Gen_KilowattHour {
        public class Option {
            public static final int KWH = 0x00;
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D170;/** Kilowatt = Ascii float code,example: 12.4*/
    }
    public class Gen_Watt {
        public class Option {
            public static final int W = 0x00;//Unit : Watt
            public static final int mW = 0x01;
            public static final int uW = 0x02;
            public static final int nW = 0x03;
            public static final int pW = 0x04;
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D170;/** Watt = Ascii float code,example: 12.4*/
    }
    public class Gen_Voltage {
        public class Option {
            public static final int V = 0x00;//Unit : V
            public static final int mV = 0x01;
            public static final int uV = 0x02;
            public static final int nV = 0x03;
            public static final int pV = 0x04;
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D170;/** Valtage = Ascii float code,example: 12.4*/
    }
    public class Gen_Amp {
        public class Option {
            public static final int A = 0x00;//Unit : V
            public static final int mA = 0x01;
            public static final int uA = 0x02;
            public static final int nA = 0x03;
            public static final int pA = 0x04;
        }
        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }
        public static final int Value = 0x00D170;/** Amp = Ascii float code,example: 12.4*/
    }
    public class Gen_Frequecy {
        public class Option {
            public static final int Hz = 0x00;//Unit : V
            public static final int KHz = 0x01;
            public static final int MHz = 0x02;
            public static final int GHz = 0x03;
        }

        public class Command {
            public static final int Report = 0x80;
            public static final int Read = 0x01;
            public static final int ReadResponse = 0x81;
            public static final int ReadSampleRate = 0x0A;
            public static final int ReadSampleRateResponse = 0x8A;
            public static final int SetSampleRate = 0x0B;
            public static final int SetSampleRateResponse = 0x8B;
        }

        public static final int Value = 0x00D180;/** Frequecy = Ascii float code,example: 12.4*/
    }
    public static final int Unkown = 0x00FFFF;
}
