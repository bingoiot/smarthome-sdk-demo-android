package pluto;

/**
 * Created by lort on 2017/11/2.
 */

/**pdata structure as floww:
 char 	crc[2];
 byte	nlen[2];
 byte	dlen[4];
 byte	pdata[0];*/

public class FileSystem {

/**pdata structure as floww:
 char 	crc[2];
 byte	nlen[2];
 byte	dlen[4];
 byte	pdata[0];*/
    public static byte[]  dbuf = null;
    public static byte  getState(byte[] pdata)
    {
        byte state = pdata[0];
        dbuf = new byte[pdata.length-1];
        System.arraycopy(pdata,1,dbuf,0,pdata.length-1);
        return state;
    }
    public static String getName(byte[] pdata)
    {
        byte[] pbuf;
        String name = "";
        if(pdata==null)
            pbuf = dbuf;
        else
            pbuf = pdata;
        int nlen = getNameLenth(pbuf);
        if(nlen<=pbuf.length) {
            byte[] buf = new byte[nlen];
            System.arraycopy(pbuf, 8, buf, 0, nlen);
            name = new String(buf);
        }
        return name;
    }
    public static int getNameLenth(byte[] pdata)
    {
        byte[] pbuf;
        if(pdata==null)
            pbuf = dbuf;
        else
            pbuf = pdata;
        byte[] buf = new byte[2];
        System.arraycopy(pbuf,2,buf,0,2);
        int nlen = (int) Clib.btoi(buf);
        return nlen;
    }
    public static int getDataLenth(byte[] pdata)
    {
        byte[] pbuf;
        if(pdata==null)
            pbuf = dbuf;
        else
            pbuf = pdata;
        byte[] buf = new byte[4];
        System.arraycopy(pbuf,4,buf,0,4);
        int nlen = (int) Clib.btoi(buf);
        return nlen;
    }
    public static byte[] getData(byte[] pdata)
    {
        byte[] pbuf = null;
        if(pdata==null)
            pbuf = dbuf;
        else
            pbuf = pdata;
        int nlen = getNameLenth(pbuf);
        int dlen = getDataLenth(pbuf);
        byte[] buf = null;
        if((dlen!=0)&&(dlen<=(pbuf.length-nlen))) {
            buf = new byte[dlen];
            System.arraycopy(pbuf, nlen + 8, buf, 0, dlen);
        }
        return buf;
    }
    /**pdata structure as floww:
     char 	crc[2];
     byte	nlen[2];
     byte	dlen[4];
     byte	pdata[0];*/
    public static byte[] genPackage(String fname, byte[] pdata)
    {
        byte[] pname = fname.getBytes();
        int nlen = pname.length;
        int dlen = pdata.length;
        byte[] buf = new byte[nlen+dlen+8];
        System.arraycopy(pname,0,buf,8,nlen);
        System.arraycopy(pdata,0,buf,(8+nlen),dlen);
        int crc = Clib.CRC16((short) 0,buf,8,nlen+dlen);
        byte[] bcrc = Clib.u16tob(crc);
        System.arraycopy(bcrc,0,buf,0,2);
        byte[] bnlen = Clib.u16tob(nlen);
        System.arraycopy(bnlen,0,buf,2,2);
        byte[] bdlen = Clib.u32tob(dlen);
        System.arraycopy(bdlen,0,buf,4,4);
        return buf;
    }
    public static byte[] genPackage(String fname, String str)
    {
        byte[] pname = fname.getBytes();
        byte[] pdata = str.getBytes();
        int nlen = pname.length;
        int dlen = pdata.length;
        byte[] buf = new byte[nlen+dlen+8];
        System.arraycopy(pname,0,buf,8,nlen);
        System.arraycopy(pdata,0,buf,(8+nlen),dlen);
        int crc = Clib.CRC16((short) 0,buf,8,nlen+dlen);
        byte[] bcrc = Clib.u16tob(crc);
        System.arraycopy(bcrc,0,buf,0,2);
        byte[] bnlen = Clib.u16tob(nlen);
        System.arraycopy(bnlen,0,buf,2,2);
        byte[] bdlen = Clib.u32tob(dlen);
        System.arraycopy(bdlen,0,buf,4,4);
        return buf;
    }
    public static byte[] genPackage(String fname, byte state, String str)
    {
        byte[] pname = fname.getBytes();
        byte[] pdata = str.getBytes();
        int nlen = pname.length;
        int dlen = pdata.length;
        byte[] buf = new byte[nlen+dlen+8+1];
        buf[0] = state;
        System.arraycopy(pname,0,buf,9,nlen);
        System.arraycopy(pdata,0,buf,(9+nlen),dlen);
        int crc = Clib.CRC16((short) 0,buf,9,nlen+dlen);
        byte[] bcrc = Clib.u16tob(crc);
        System.arraycopy(bcrc,0,buf,1,2);
        byte[] bnlen = Clib.u16tob(nlen);
        System.arraycopy(bnlen,0,buf,3,2);
        byte[] bdlen = Clib.u32tob(dlen);
        System.arraycopy(bdlen,0,buf,5,4);
        return buf;
    }
    public static byte[] genPackage(String fname)
    {
        byte[] pname = fname.getBytes();
        int nlen = pname.length;
        byte[] buf = new byte[nlen+8];
        System.arraycopy(pname,0,buf,8,nlen);
        int crc = Clib.CRC16((short) 0,buf,8,nlen);
        byte[] bcrc = Clib.u16tob(crc);
        System.arraycopy(bcrc,0,buf,0,2);
        byte[] bnlen = Clib.u16tob(nlen);
        System.arraycopy(bnlen,0,buf,2,2);
        byte[] bdlen = Clib.u32tob(0);
        System.arraycopy(bdlen,0,buf,4,4);
        return buf;
    }
    public static byte[] genPackage(String fname, byte state)
    {
        byte[] pname = fname.getBytes();
        int nlen = pname.length;
        byte[] buf = new byte[nlen+8 + 1];
        buf[0] = state;
        System.arraycopy(pname,0,buf,9,nlen);
        int crc = Clib.CRC16((short) 0,buf,9,nlen+1);
        byte[] bcrc = Clib.u16tob(crc);
        System.arraycopy(bcrc,0,buf,1,2);
        byte[] bnlen = Clib.u16tob(nlen);
        System.arraycopy(bnlen,0,buf,3,2);
        byte[] bdlen = Clib.u32tob(0);
        System.arraycopy(bdlen,0,buf,5,4);
        return buf;
    }
}
