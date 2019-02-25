package com.jifan.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import java.io.IOException;

public class NetUtils {

	public static boolean pingIpAddress(String ipAddress) {
		try {
			if(ipAddress==null||ipAddress=="")
			{
				//ipAddress="223.5.5.5";//阿里云dns
				ipAddress="www.baidu.com";//百度
			}
			Process process = Runtime.getRuntime().exec("/system/bin/ping -c 2 -w 3 " + ipAddress);
			int status = process.waitFor();
			if (status == 0) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	// 判断网络连接状态
	public static boolean isNetworkConnected_no(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				boolean isconect=mNetworkInfo.isAvailable();
				if(isconect) {
					int tt = getConnectedType(context);
					if (tt == 1) {
						isconect = isWiFiActive(context);
					}
				}
				return isconect;
			}
		}
		return false;
	}

	public static boolean isWiFiActive(Context inContext) {
		Context context = inContext.getApplicationContext();
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getTypeName().equals("WIFI") && info[i].isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	//是否连接WIFI
	public static boolean isWifiConnected(Context context)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(wifiNetworkInfo.isConnected())
		{
			return true ;
		}

		return false ;
	}

	// 判断移动网络 是否可用
	public static boolean isMobileAvailable(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	// 判断移动网络 是否可用
	public static boolean isMobileConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mMobileNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mMobileNetworkInfo != null) {
				return mMobileNetworkInfo.isConnected();
			}
		}
		return false;
	}

	/**
	 * Unknown network class
	 */
	public static final int NETWORK_CLASS_UNKNOWN = 0;

	/**
	 * wifi net work
	 */
	public static final int NETWORK_WIFI = 1;

	/**
	 * "2G" networks
	 */
	public static final int NETWORK_CLASS_2_G = 2;

	/**
	 * "3G" networks
	 */
	public static final int NETWORK_CLASS_3_G = 3;

	/**
	 * "4G" networks
	 */
	public static final int NETWORK_CLASS_4_G = 4;
	public static int getNetWorkClass(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

		switch (telephonyManager.getNetworkType()) {
			case TelephonyManager.NETWORK_TYPE_GPRS:
			case TelephonyManager.NETWORK_TYPE_EDGE:
			case TelephonyManager.NETWORK_TYPE_CDMA:
			case TelephonyManager.NETWORK_TYPE_1xRTT:
			case TelephonyManager.NETWORK_TYPE_IDEN:
				return NETWORK_CLASS_2_G;

			case TelephonyManager.NETWORK_TYPE_UMTS:
			case TelephonyManager.NETWORK_TYPE_EVDO_0:
			case TelephonyManager.NETWORK_TYPE_EVDO_A:
			case TelephonyManager.NETWORK_TYPE_HSDPA:
			case TelephonyManager.NETWORK_TYPE_HSUPA:
			case TelephonyManager.NETWORK_TYPE_HSPA:
			case TelephonyManager.NETWORK_TYPE_EVDO_B:
			case TelephonyManager.NETWORK_TYPE_EHRPD:
			case TelephonyManager.NETWORK_TYPE_HSPAP:
				return NETWORK_CLASS_3_G;

			case TelephonyManager.NETWORK_TYPE_LTE:
				return NETWORK_CLASS_4_G;

			default:
				return NETWORK_CLASS_UNKNOWN;
		}
	}

	// 获取连接类型 0 无线 1 wifi -1联不上网
	public static int getConnectedType(Context context) {
		int netWorkType = NETWORK_CLASS_UNKNOWN;
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				int type = networkInfo.getType();

				if (type == ConnectivityManager.TYPE_WIFI) {
					netWorkType = NETWORK_WIFI;
				} else if (type == ConnectivityManager.TYPE_MOBILE) {
					int workType = networkInfo.getSubtype();
					String _strSubTypeName = networkInfo.getSubtypeName();
					switch (workType) {
						case TelephonyManager.NETWORK_TYPE_GPRS:
						case TelephonyManager.NETWORK_TYPE_EDGE:
						case TelephonyManager.NETWORK_TYPE_CDMA:
						case TelephonyManager.NETWORK_TYPE_1xRTT:
						case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
							netWorkType=NETWORK_CLASS_2_G;
							break;
						case TelephonyManager.NETWORK_TYPE_UMTS:
						case TelephonyManager.NETWORK_TYPE_EVDO_0:
						case TelephonyManager.NETWORK_TYPE_EVDO_A:
						case TelephonyManager.NETWORK_TYPE_HSDPA:
						case TelephonyManager.NETWORK_TYPE_HSUPA:
						case TelephonyManager.NETWORK_TYPE_HSPA:
						case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
						case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
						case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
							netWorkType=NETWORK_CLASS_3_G;
							break;
						case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
							netWorkType=NETWORK_CLASS_4_G;
							break;
						default:
							// http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
							if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000"))
							{
								netWorkType=NETWORK_CLASS_3_G;
							}
							else
							{
								netWorkType = NETWORK_CLASS_UNKNOWN;
							}
							break;
					}
				}
				return  netWorkType;
			}
		}
		return -1;
	}



}
