package com.jifan;

import java.util.Date;


/**
 * Created by ludy on 2016/12/27 0027.
 */

public class Config {
    public static boolean isActive; //是否 在运行，进入后台后为false
    public static int flag=0;//是否被进程杀死 0是 1不是
    public static Date ActiveEndTime = new Date();//进入后台的时间
    public static int Request_Alldata_Stat=0;//是否需要加载数据 0 需要 -1不需要
    public static int Ini_Alldata_Stat=0;//是否需要设备数据初始化 0 需要 -1不需要

    public static int Ini_Quick_Dev=0;//是否需要初始化快捷方式的设备 0 需要 -1不需要

    public static boolean isAppLogin=false;//app 用户是否登录

    public static boolean JifanIsLogin = false;//socket 是否登录



    public final static String KEY_USER_SHARE = "key_user_share";
    public final static String KEY_SHARE_ALLDEV = "key_share_alldev";
    public final static String KEY_SHARE_Quick_Dev = "KEY_SHARE_Quick_Dev";


    public final static int notifyCode_LoadDataFinsh=1;//数据加载完成
    public final static int notifyCode_WeatherLoadDataFinsh=2;//天气获取完成
    public final static  int notifyCode_ChangeHome=3;//切换家
    public final static  int notifyCode_shownavigation=4;//显示测边






}
