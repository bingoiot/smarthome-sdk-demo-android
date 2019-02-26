package com.jifan.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jifan.model.aid_value;
import com.jifan.model.dev_port;

import java.util.List;

import pluto.*;

/**
 * Created by Administrator on 2017/5/30.
 */

public class ControlHelper {
    //是否有aid 是开关， 或者是锁的
    public static int get_Switch_aid(List<aid_value> list) {
        if (list != null && list.size() > 0) {

            for (aid_value aidValue : list) {
                int aid = aidValue.getAid();
                if (aid == AttributeID.Gen_Switch.Value ||
                        aid == AttributeID.Gen_Locker.Value ||
                        aid == AttributeID.Gen_LockerRFIDCard.Value ||
                        aid == AttributeID.Gen_LockerPassword.Value||
                        aid == AttributeID.Gen_LockerFinger.Value)
                {
                    return aid;
                }
            }
        }
        return 0;
    }

    //是否报警设备 1报警 0 不报警
    public static int get_Alarm_aid(int aid_dev_type, List<aid_value> list) {
        if (aid_dev_type < AttributeID.Gen_Alarm.FireAlarm || aid_dev_type > AttributeID.Gen_Alarm.Gen_AlarmZone)//不属于报警类的设备 范围
        {
            return 0;
        }

        if (list != null && list.size() > 0) {

            for (aid_value aidValue : list) {
                int aid = aidValue.getAid();
                if (aid == AttributeID.Gen_Alarm.HumenCloseAlarm ||
                        aid == AttributeID.Gen_Alarm.MenciAlarm ||
                        aid == AttributeID.Gen_Alarm.SmokeAlarm ||
                        aid == AttributeID.Gen_Alarm.PM2_5Alarm ||
                        aid == AttributeID.Gen_Alarm.FloodAlarm ||
                        aid == AttributeID.Gen_Alarm.WaterAlarm ||
                        aid == AttributeID.Gen_Alarm.PoisonGasAlarm ||
                        aid == AttributeID.Gen_Alarm.CarbonMonoxideAlarm ||
                        aid == AttributeID.Gen_Alarm.CombustibleAlarm ||
                        aid == AttributeID.Gen_Alarm.EmergenciAlarm ||
                        aid == AttributeID.Gen_Alarm.FireAlarm) {
                    return aid;
                } else if (aid >= AttributeID.Gen_Alarm.FireAlarm && aid <= AttributeID.Gen_Alarm.Gen_AlarmZone)//属于报警类的 范围
                {
                    return aid;
                }
            }
        }
        return 0;
    }


    public static void SendData(dev_port devPort, int aID, byte[] data) {

        byte[] mac = devPort.getDevAddr();
        byte port = (byte) devPort.getPort();
        Aps.reqSend((byte) devPort.getKeyid(), mac,Common.getSeq(), port,aID, AttributeID.Command.Write, AttributeID.Option.Default, data, data.length);

    }

    public static void SendDataSwithch(Context context, dev_port devPort, int SwitchGen_Type) {//默认状态 发送 （开关）
        List<aid_value> avlist = devPort.getAid_valueList();//所有aid和值

        aid_value avItem = null;
        for (aid_value av : avlist) {//获取主aid的值
            if (av.getAid() == SwitchGen_Type) {
                avItem = av;
                break;
            }
        }
        if (avItem == null) {
            return;
        }
        String value = avItem.getValues();
        if (value == null) {
            value = "00";
        }
        switch (value) {
            case "01":
                value = "00";
                break;
            case "00":
                value = "01";
                break;
            default:
                value = "01";
                break;
        }
        byte[] data = Clib.hexToBytes(value);
        SendData(devPort, SwitchGen_Type, data);
    }

    ///开关全开 或全关
    public static void SendDataAll(Context context, List<dev_port> list, byte[] pdata) {
        for (dev_port devPort : list) {
            int aid = 0;
            int switch_den_type = get_Switch_aid(devPort.getAid_valueList());//获取 开关
            if (switch_den_type != 0) {
                ControlHelper.SendData(devPort, switch_den_type, pdata);
            } else {

            }
        }
    }

    public static void SendDataRead(dev_port devPort) {//状态 读状态

        List<aid_value> avlist = devPort.getAid_valueList();//所有aid和值
        byte[] mac = devPort.getDevAddr();
        byte port = (byte) devPort.getPort();

        if(avlist!=null) {
            for (aid_value ad : avlist) {
                byte[] data = new byte[1];
                data[0] = 0;
                Aps.reqSend((byte) devPort.getKeyid(), mac, Common.getSeq(), port, ad.getAid(), AttributeID.Command.Read, AttributeID.Option.Default, data, 1);

            }
        }

    }



    //点击设备时的操作
    public static void clickDevPort(Context context, dev_port item, List<dev_port> deviceportlist) {

        int switch_den_type = ControlHelper.get_Switch_aid(item.getAid_valueList());
        int alark_den_type =ControlHelper.get_Alarm_aid(item.getAid_dev_type(),item.getAid_valueList());//是否有 报警
        if (switch_den_type != 0&&alark_den_type==0) {
            ControlHelper.SendDataSwithch(context, item, switch_den_type);
        } else {

            Intent it;
            Bundle mBundle;
            switch (item.getAid_dev_type()) {
                    /*    case JifanDef.aID_Dev_Type_DemmerLEDLight:
                            new c_demer_led(context, item).show();
                            break; */
                case AttributeID.Gen_WindowSwitch.Value://开关窗帘
                   // new c_curtains(context, item).show();
                    break;
                case AttributeID.Gen_Window.Percent://百分比 窗帘
                   // new c_curtains_percent(context, item).show();
                    break;
                case AttributeID.Gen_Window.BlindsAngle://百叶窗
                   // new c_curtains_venetian(context, item).show();
                    break;
                case AttributeID.Gen_Humidity.Value://温湿度
                   // new c_temp_humidity(context, item).show();
                    break;
                case AttributeID.Gen_Temperature.Value://温度
                    //new c_temperature(context, item).show();
                    break;
                case AttributeID.Gen_Alarm.EmergenciAlarm://紧急求救按钮
                case AttributeID.Gen_Alarm.Gen_AlarmZone:
                   // ControlHelper.c_EmergencyButtonAlarm(context, item, deviceportlist);
                    //updateAdpter();//刷新ui
                    break;
                case AttributeID.Gen_Fan.Switch://风扇

                  //  new c_fan(context, item).show();

                    break;
                default:
                    //  new c_curtains_venetian(context, item).show();
                    //    new c_demer_led(context, item).show();
                    break;
            }
        }
        //   Toast.makeText(context, "点击了第" + (arg2 + 1) + "张图片", Toast.LENGTH_LONG).show();

    }

    //长按设备时的操作
    public static void longclickDevPort(Context context, dev_port item) {

        Intent it;
        switch (item.getAid_dev_type()) {
            case AttributeID.Gen_CommonLevel.Value:
                //new c_demer_led(context, item).show();
                break;
            default:
                //  new c_curtains_venetian(context, item).show();
                //    new c_demer_led(context, item).show();

                break;
        }

    }

}
