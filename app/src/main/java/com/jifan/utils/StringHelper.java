package com.jifan.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Created by ludy on 2016/12/30 0030.
 */

/**
 * 是否是手机号码
 */
public class StringHelper {


    public static String GetUUID() {

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    /**
     * 去掉前后空格
     */
    public static String rep_span(String str) {
        //去掉前后空格方法一
        //String regex = "^\\[(.*)\\]$";
        //String s1 = str.replaceAll(regex, "$1");
        //方法二,注：replace方法无正则匹配
        String regex = "^\\[|\\]$";
        String s1 = str.replaceAll(regex, "");
        return s1;
    }


    /// <summary>
    /// 字符串转byte ASCII编码 用于加密
    /// </summary>
    /// <param name="str">字符</param>
    /// <param name="leng">长度</param>
    /// <returns></returns>
    public static byte[] strToEncryptKey(String str, int leng) {
        byte[] nb = new byte[leng];
        try {
            byte[] byteArray = str.getBytes("US-ASCII");
            if (byteArray.length > leng) {
                System.arraycopy(byteArray, 0, nb, 0, leng);
            } else {
                System.arraycopy(byteArray, 0, nb, 0, byteArray.length);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return nb;
    }

    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }


    /**
     * Convert hex string to byte[]
     *
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * Convert char to byte
     *
     * @param c char
     * @return byte
     */
    static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }


    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    private static String string = "0123456789abcdef";

    public static String getRandomString(int length) {
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len - 1)));
        }
        return sb.toString();
    }


    static String mySessionId="";

    public  static  String GetmySessionId()
    {
        if(mySessionId=="")
        {

            mySessionId=getRandomString(11);
        }
        return  mySessionId;
    }

    /**
     * 字符串是否为空
     *
     * @param Str
     * @return
     */
    public static boolean StrIsEmpty(String Str) {
        if (Str == null) {
            return true;
        }
        if (Str.isEmpty()) {
            return true;
        }
        return false;
    }

}
