package com.xzkydz.socket;

import android.os.Handler;
import android.util.Log;

import com.xuhao.didi.core.iocore.interfaces.IPulseSendable;
import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.core.protocol.IReaderProtocol;
import com.xuhao.didi.socket.client.impl.client.action.ActionDispatcher;
import com.xuhao.didi.socket.client.sdk.OkSocket;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xuhao.didi.socket.client.sdk.client.OkSocketOptions;
import com.xuhao.didi.socket.client.sdk.client.action.SocketActionAdapter;
import com.xuhao.didi.socket.client.sdk.client.connection.IConnectionManager;
import com.xuhao.didi.socket.client.sdk.client.connection.NoneReconnect;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class KySocket {

    public IConnectionManager mManager;
    public ConnectionInfo mConnectionInfo;
    private OkSocketOptions mOkOptions;
    public SocketActionAdapter adapter;
    private Settings settings = new Settings();

    public KySocket(final SocketListener socketListener) {

        adapter = new SocketActionAdapter() {

            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                super.onSocketConnectionSuccess(info, action);
                if (socketListener != null) {
                    socketListener.onSocketConnectionSuccess(info, action);
                }
            }

            @Override
            public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
                super.onSocketDisconnection(info, action, e);

                if (socketListener != null) {
                    socketListener.onSocketDisconnection(info, e);
                }
            }

            @Override
            public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
                super.onSocketConnectionFailed(info, action, e);
                if (socketListener != null) {
                    socketListener.onSocketConnectionFailed(info, e);
                }
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
                super.onSocketReadResponse(info, action, data);
                if (socketListener != null) {
                    socketListener.onSocketReadResponse(info, data);
                }
            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
                super.onSocketWriteResponse(info, action, data);
                if (socketListener != null) {
                    socketListener.onSocketWriteResponse(info, data);
                }
            }

            @Override
            public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
                super.onPulseSend(info, data);
            }
        };
    }

    public void initManager(String ip, int port) {
        final Handler handler = new Handler();
        //连接参数设置(IP,端口号),这也是一个连接的唯一标识,不同连接,该参数中的两个值至少有其一不一样
        mConnectionInfo = new ConnectionInfo(ip, port);
        mOkOptions = new OkSocketOptions.Builder()
                .setReconnectionManager(new NoneReconnect())
                .setConnectTimeoutSecond(settings.getmConnectTimeoutSecond())
                .setWritePackageBytes(settings.getmWritePackageBytes())
                .setReadPackageBytes(settings.getmReadPackageBytes())
                .setMaxReadDataMB(settings.getmMaxReadDataMB())
                .setReaderProtocol(new MyReaderProtocol())
                .setCallbackThreadModeToken(new OkSocketOptions.ThreadModeToken() {
                    @Override
                    public void handleCallbackEvent(ActionDispatcher.ActionRunnable runnable) {
                        handler.post(runnable);
                    }
                }).build();
        //调用OkSocket,开启这次连接的通道,拿到通道Manager
        mManager = OkSocket.open(mConnectionInfo).option(mOkOptions);
        //注册Socket行为监听器,SocketActionAdapter是回调的Simple类,其他回调方法请参阅类文档
        mManager.registerReceiver(adapter);
    }

    //设置参数
    public void setSettings(Settings settings){
        this.settings = settings;
    }

    public void connect(String ip, int port) {
        initManager(ip, port);
        if (!mManager.isConnect()) {
            mManager.connect();
        }
    }

    public void disconnect() {
        if (mManager != null && mManager.isConnect()) {
            mManager.disconnect();
        }
    }

    public void send(ISendable sendable) {
        if (mManager.isConnect()){
            mManager.send(sendable);
        }
    }

    public void destroy() {
        if (mManager != null) {
            if (mManager.isConnect()) {
                mManager.disconnect();
            }
            mManager.unRegisterReceiver(adapter);
        }
    }

    public class MyReaderProtocol implements IReaderProtocol {
        @Override
        public int getHeaderLength() {
            return 4;
        }

        @Override
        public int getBodyLength(byte[] header, ByteOrder byteOrder) {
            if (header == null || header.length < getHeaderLength()) {
                return 0;
            }
            ByteBuffer bb = ByteBuffer.wrap(header);
            bb.order(ByteOrder.BIG_ENDIAN);
            int len = bb.getInt();
            Log.d("123456", "getBodyLength: " + len);
            return len;
        }
    }
}
