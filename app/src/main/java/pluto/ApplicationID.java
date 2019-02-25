package pluto;

/**
 * Created by lort on 2019/2/20.
 */

public class ApplicationID {
    public static final int Key						    =0x001000;
    public static final int Switch						=0x001010;

    public static final int Light						=0x001020;
    public static final int DimmerLight				    =0x001021;
    public static final int LED_Light					=0x001022;
    public static final int ColorLight				    =0x001023;

    public static final int Socket					    =0x001030;
    public static final int LineSocket					=0x001031;

    public static final int WidowCovering		        =0x001041;
    public static final int BlindWidowCovering		    =0x001042;
    public static final int PercentWidowCovering		=0x001043;

    public static final int Locker						 =0x001080;

    public static final int IRBox						 =0x001100;
    ////////////////////////////////////////////////////////
    public static final int Fan						    =0x002100;
    public static final int WatterRefrigeration		    =0x002101;
    public static final int Television		            =0x002102;

    public static final int TempController				=0x002200;
    public static final int AirConditioning			    =0x002201;
    public static final int FanMachineController		=0x002202;
    ///////////////////////////////////////////////////////////
    public static final int FireAlarm					=0x004000;
    public static final int SmokeAlarm					=0x004001;
    public static final int PM25Alarm					=0x004002;

    public static final int FloodAlarm					=0x004010;
    public static final int WaterAlarm					=0x004011;

    public static final int PoisonGasAlarm				=0x004020;
    public static final int CarbonMonoxideAlarm		    =0x004021;

    public static final int CombustibelAlarm			=0x004030;

    public static final int DoorMagneticAlarm			=0x004100;
    public static final int BodyInfraredAlarm			=0x004110;
    public static final int BodyMicroWaveAlarm			=0x004111;
    public static final int EmergencyButtonAlarm		=0x004120;
    public static final int SecurityAlarm		        =0x004FFF;
    ///////////////////////////////////////////////////////////
    public static final int HumidityAndTemSensor		=0x005000;
    public static final int TemperatureSensor			=0x005001;
    public static final int HumiditySensor				=0x005002;

    public static final int CarbonMonoxideSensor		=0x005010;
    public static final int CarbonDioxideSensor		=0x005011;
    public static final int OxygenSensor				    =0x005012;
    public static final int NitrogenSensor				=0x005013;
    public static final int OxygenAndNitrogenSensor	=0x005014;
    public static final int OzoneSensor				    =0x005015;

    public static final int NaturalGasSensor			=0x005020;
    public static final int MethaneSensor				=0x005021;

    public static final int FormaldehydeSensor			=0x005030;

    public static final int LocationSensor				=0x005100;
    public static final int DistanceSensor				=0x005101;
    public static final int HeightSensor				    =0x005102;
    public static final int AltitudeSensor				=0x005104;

    public static final int AirPressureSensor			=0x005110;
    public static final int WatterPressureSensor		    =0x005111;

    public static final int GravitySensor				=0x005120;

    public static final int LightSensor				    =0x005130;

    public static final int SpeedSensor				    =0x005140;
    public static final int AccelerationSensor			=0x005141;

    public static final int WindSpeedSensor			    =0x005150;

    public static final int FlowSensor					=0x005160;

    public static final int KilowattHourSensor			=0x005170;
    public static final int WattHourSensor				=0x005171;
    public static final int VoltageSensor				=0x005172;
    public static final int AmmeterSensor				=0x005173;

    ///////////////////////////////////////////////////////////
    public static final int RFASKRemoteCtrl			    =0x007000;
    public static final int UART						    =0x007010;
}
