package com.jifan.actv;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jifan.Config;
import com.jifan.model.dev_hardware;
import com.jifan.model.dev_port;

import com.jifan.utils.DevManage;
import com.jifan.utils.MyListView;


import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import pluto.*;

public class PortJoinActivity extends BaseActivity

{
    dev_hardware item;

    @ViewInject(R.id.gv_dev)
    MyListView gv_dev;

    //@ViewInject(R.id.ivcan)
    //ImageView ivcan;

    int cout;

    List<dev_port> list_port;//端口列表
    List<Integer> list_p;//端口列表
    List<String> names;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_join);

        Object o = getIntent().getSerializableExtra("mode");
        if (o != null) {
            item = (dev_hardware) o;
        } else {
            item = null;
        }
        init();
    }

    DeviceHelper.onSectionListener myDeviceHelperListener = new DeviceHelper.onSectionListener() {
        @Override
        public void DeviceJoinIndicates(byte[] addr) {

        }

        @Override
        public void NewPortIndicates(byte[] addr, JSONObject describe) {//网关有新设备进入
            boolean ishave = false;
            int port = 0;
            try {
                port = (byte) describe.getInt("port");
                for (Integer p : list_p) {
                    if (p == port) {
                        ishave = true;
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (!ishave) {
                dev_port dp = DevManage.getdevportInit(addr, describe.toString());
                names.add(dp.getPortname());
                list_p.add((int) port);
                Message msg = new Message();
                msg.what = 1;
                myHandler.sendMessage(msg);
            }
        }

        @Override
        public void ReceiveDeviceInfo(byte[] addr, JSONObject devInfo, int state) {

        }

        @Override
        public void ReceivePortList(byte[] addr, byte[] ports, int state) {

        }

        @Override
        public void ReceivePortDescribe(byte[] addr, JSONObject describe, int state) {

        }

        @Override
        public void CompleteDevice(byte[] addr, JSONObject devInfo, byte[] ports, ArrayList<JSONObject> descList) {

        }

        @Override
        public void ReceiveLqi(byte[] addr, byte port, int lqi, int state) {

        }

        @Override
        public void ReceiveBeacon(byte[] addr, int state) {

        }

        @Override
        public void SendStatusCB(byte[] src, int seq, int port, int aID, int cmd, int option, int state) {

        }
    };

    private void init() {

        x.view().inject(this);
        currentcontext = this;


        myHandler = new Handler(currentcontext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                switch (msg.what) {

                    case 1://
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        cout = 0;
        list_port = new ArrayList<>();
        list_p = new ArrayList<>();
        names = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        gv_dev.setAdapter(adapter);
        Setlisen();
        SendJoin();

    }

    void SendJoin() {
        byte[] addr = Clib.hexToBytes(item.getMac());
        DeviceHelper.reqDevEnableJoin((byte) item.getKeyid(), addr, Common.getSeq(), (2 * 60 * 1000));//允许新设备加入
        contextHelper.ShowTost("发送成功，5分钟后自动禁止");

    }

    void Setlisen() {
        DeviceHelper.setSectionListener(myDeviceHelperListener);
    }

    @Override
    protected void onDestroy() {
        byte[] addr = Clib.hexToBytes(item.getMac());
        DeviceHelper.reqDevDisableJoin((byte) item.getKeyid(), addr, Common.getSeq());//禁止入网
        DeviceHelper.removeSectionListener(myDeviceHelperListener);
        super.onDestroy();
    }
}
