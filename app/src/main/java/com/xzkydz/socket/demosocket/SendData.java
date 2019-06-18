package com.xzkydz.socket.demosocket;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SendData implements ISendable {
    private String str = "";

    public SendData(String data) {

        long time=System.currentTimeMillis();
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1=new Date(time);
        String t1 = format.format(d1);

        //json字符串
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "client info");
            jsonObject.put("msg", data);
            jsonObject.put("date", t1);
            str = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public byte[] parse() {
        byte[] body = str.getBytes(Charset.defaultCharset());
        ByteBuffer bb = ByteBuffer.allocate(4+ body.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(body.length);
        bb.put(body);
        return bb.array();
    }
}
