package com.xzkydz.socket.demosocket;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuhao.didi.core.iocore.interfaces.ISendable;
import com.xuhao.didi.core.pojo.OriginalData;
import com.xuhao.didi.socket.client.sdk.client.ConnectionInfo;
import com.xzkydz.socket.KySocket;
import com.xzkydz.socket.SocketListener;

import java.nio.charset.Charset;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_show)
    EditText etShow;
    @BindView(R.id.et_send)
    EditText etSend;
    @BindView(R.id.et_ip)
    EditText etIp;
    @BindView(R.id.et_port)
    EditText etPort;
    @BindView(R.id.btn_open)
    Button btnOpen;
    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.btn_clear)
    Button btnClear;
    private String TAG = "123456";
    private KySocket kySocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        //第一步：实例状态监听器。（如果不需要监听连接状态可以省略这一步）
        SocketListener socketListener = new SocketListener() {

            @Override
            public void onSocketConnectionFailed(ConnectionInfo info, Exception e) {
                showInf("--> 连接失败" + e.getMessage());
                btnOpen.setEnabled(true);
                btnOpen.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
            }

            @Override
            public void onSocketDisconnection(ConnectionInfo info, Exception e) {

                if (e == null) {
                    showInf("--> 正常断开连接");
                } else {
                    showInf("--> 异常断开连接");
                }
                btnOpen.setEnabled(true);
                btnOpen.setVisibility(View.VISIBLE);
                btnSend.setVisibility(View.GONE);
                btnClose.setVisibility(View.GONE);
            }

            @Override
            public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
                showInf("--> 建立连接");
                kySocket.send(new SendData("你好，我是客户端..."));
                btnOpen.setEnabled(true);
                btnOpen.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
                btnClose.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSocketWriteResponse(ConnectionInfo info, ISendable data) {
                showInf("--> 向服务端发送数据");
                String str = new String(data.parse(), Charset.forName("utf-8"));
                showInf(str);
            }

            @Override
            public void onSocketReadResponse(ConnectionInfo info, OriginalData data) {
                showInf("--> 读取服务端发送的数据");
                String str = new String(data.getBodyBytes(), Charset.forName("utf-8"));
                showInf(str);
            }
        };

        //第二步：实例化KySocket。（如果不需要设置监听器，可以通过无参构造器实例化）
        kySocket = new KySocket(socketListener);
    }

    @OnClick({R.id.btn_open, R.id.btn_close, R.id.btn_send, R.id.btn_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_open:
                btnOpen.setEnabled(false);
                //建立连接
                kySocket.connect(etIp.getText().toString(), Integer.parseInt(etPort.getText().toString()));
                break;
            case R.id.btn_close:
                //断开连接
                kySocket.disconnect();
                break;
            case R.id.btn_send:
                //发送数据
                if (!TextUtils.isEmpty(etSend.getText())) {
                    kySocket.send(new SendData(etSend.getText().toString().trim()));
                }
                break;
            case R.id.btn_clear:
                etShow.setText("");
                break;
        }
    }

    /*打印*/
    private void showInf(String inf) {
        StringBuilder sbr = new StringBuilder();
        sbr.append(inf + "\n");
        etShow.append(sbr);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //第三步：销毁
        kySocket.destroy();
    }
}
