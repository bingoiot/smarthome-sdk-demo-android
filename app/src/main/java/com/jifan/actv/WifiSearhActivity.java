package com.jifan.actv;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jifan.model.dev_hardware;
import com.jifan.model.dev_port;
import com.jifan.utils.DevManage;
import com.jifan.utils.StringHelper;
import com.jifan.utils.WifiUtils;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pluto.Aps;
import pluto.Clib;
import pluto.Common;
import pluto.DeviceHelper;

public class WifiSearhActivity extends BaseActivity {
    @ViewInject(R.id.ivcan)
    ImageView ivcan;
    @ViewInject(R.id.tv_scan)
    TextView tv_scan;


    ListView listView_dev;
    List<String> macs;
    List<dev_hardware> list_dev;//设备列表

    List<dev_port> list_port;//设备端口

    ArrayAdapter adapter;

    String Ssid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_searh);
        x.view().inject(this);

        listView_dev = (ListView) findViewById(R.id.list_dev);

        myHandler = new Handler(currentcontext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                switch (msg.what) {
                    case 1:// 更新数据
                        adapter.notifyDataSetChanged();
                        break;
                }
            }
        };
        list_dev = new ArrayList<>();
        list_port = new ArrayList<>();
        macs = new ArrayList<>();
        adapter = new ArrayAdapter<String>(this, R.layout.list_item_1, macs);
        listView_dev.setAdapter(adapter);



/*  默认全部添加，生产项目，最好手动确认
        listView_dev.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,final int pos, long l) {
                new AlertDialog.Builder(currentcontext)
                        .setTitle("确定添加设备：" + macs.get(pos) + "?")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {

                                synchronized (macs) {
                                synchronized (list_dev) {
                                    dev_hardware item=list_dev.get(pos);
                                    addDev( item);
                                    macs.remove(pos);
                                    list_dev.remove(pos);
                                    adapter.notifyDataSetChanged();

                                }}
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        });
        */
        DeviceHelper.setSectionListener(myDeviceHelperListener);
        seach();
    }

    //保存设备
    void addDev(dev_hardware item) {
        List<dev_hardware> all = app.getDevList();
        boolean isHave = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getMac().equals(item.getMac())) {
                isHave = true;
                break;
            }
        }
        if (!isHave) {
            all.add(item);
            app.setDevList(all);//保存本地
        }
        String key = item.getkeys();
        if (key != null && key != "") {
            byte[] comonkey = Clib.hexToBytes(key);
            byte[] addr = Clib.hexToBytes(item.getMac());
            Aps.addDeviceKey(addr, (byte) item.getKeyid(), comonkey);//往通信层添加设备
        }
    }

    private void seach() {
        WifiUtils wi = new WifiUtils(currentcontext);
        Ssid = wi.getWifiConnectedSsid();
        if (Ssid != null) {
         /*   scanThread lt = new scanThread();
            lt.StartThread();*/
            byte[] addr = {(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF};
            DeviceHelper.reqSendBeacon((byte) 0, addr, Common.getSeq());//广播beaocn
            DeviceHelper.reqSendBeacon((byte) 0, addr, Common.getSeq());
            DeviceHelper.reqSendBeacon((byte) 0, addr, Common.getSeq());

            ShowTost("已经广播，请等待");

            Animation rotate = AnimationUtils.loadAnimation(currentcontext, R.anim.rotate_anim);
            ivcan.setVisibility(View.VISIBLE);
            ivcan.setAnimation(rotate);
            ivcan.startAnimation(rotate);
            tv_scan.setVisibility(View.VISIBLE);
        } else {
            ShowTost("请先把手机连接wifi");
        }
    }

    DeviceHelper.onSectionListener myDeviceHelperListener = new DeviceHelper.onSectionListener() {
        @Override
        public void DeviceJoinIndicates(byte[] addr) {
        }

        @Override
        public void NewPortIndicates(byte[] addr, JSONObject describe) {
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
        public void CompleteDevice(byte[] addr, JSONObject devInfo, byte[] ports, ArrayList<JSONObject> descList) {//获取到设备信息

            synchronized (list_dev) {

                boolean isAdd = true;//是否可添加
                for (dev_hardware item : list_dev) {
                    if (Arrays.equals(StringHelper.hexStringToBytes(item.getMac()), addr)) {
                        isAdd = false;//已经存在
                        break;
                    }
                }
                if (isAdd)//可添加
                {
                    dev_hardware devHardware = DevManage.getDevInfo(addr);
                    List<dev_port> listP = DevManage.getdevport(addr);//端口
                    listP = DevManage.InitDev_port(listP);//初始化
                    devHardware.setDev_portList(listP);

                    list_dev.add(devHardware);
                    addDev( devHardware);//默认添加 注释掉手动添加
                    macs.add(StringHelper.bytesToHexString(addr));
                    myHandler.sendEmptyMessage(1);
                }
            }
        }

        @Override
        public void ReceiveLqi(byte[] addr, byte port, int lqi, int state) {

        }

        @Override
        public void ReceiveBeacon(byte[] addr, int state) {//新设备通知 相同设备有可能收到多次
            synchronized (list_dev) {
                boolean isAdd = true;//是否可添加
                for (dev_hardware item : list_dev) {
                    if (Arrays.equals(StringHelper.hexStringToBytes(item.getMac()), addr)) {
                        isAdd = false;//已经存在
                        break;
                    }
                }
                if (isAdd)//可添加
                {
                    DeviceHelper.AttachDevice(addr, null, null, null);//获取设备信息
                }
            }
        }

        @Override
        public void SendStatusCB(byte[] src, int seq, int port, int aID, int cmd, int option, int state) {
        }
    };
}
