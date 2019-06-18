package com.xzkydz.socket.demosocket;

public class FuncUtils {
    // 4字节byte转无符号int
    public static int fourByte2int(byte[] b) {
        return (b[3] & 0xFF) | (b[2] & 0xFF) << 8 | (b[  1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
    }

    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
            strBuilder.append("");
        }
        return strBuilder.toString();
    }

    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }


}
