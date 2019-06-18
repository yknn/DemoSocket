package com.xzkydz.socket.demosocket;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class GetTestData {

    private String str = "";

    public GetTestData(String str) {
        this.str = str;
    }

    public String getStr(){
        return  FuncUtils.ByteArrToHex(parse());
    }

    public byte[] parse() {
        byte[] body = str.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4+ body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }

}
