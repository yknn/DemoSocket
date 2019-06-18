package com.xzkydz.socket;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;

public interface SocketListener {

    void onSocketConnectionFailed(ConnectionInfo info, Exception e);

    void onSocketDisconnection(ConnectionInfo info,  Exception e);

    void onSocketConnectionSuccess(ConnectionInfo info, String action);

    void onSocketWriteResponse(ConnectionInfo info, ISendable data);

    void onSocketReadResponse(ConnectionInfo info, OriginalData data);
}
