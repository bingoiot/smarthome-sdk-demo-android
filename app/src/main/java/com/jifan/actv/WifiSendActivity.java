package com.jifan.actv;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.jifan.utils.FileUtils;
import com.jifan.utils.StringHelper;
import com.jifan.utils.WifiUtils;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pluto.DeviceHelper;

public class WifiSendActivity extends BaseActivity {


    TextView tv_ssid;
    EditText et_wifi_pwd;
    ButtonRectangle btn_config;


    @ViewInject(R.id.ivcan)
    ImageView ivcan;
    @ViewInject(R.id.tv_scan)
    TextView tv_scan;

    String Ssid;
    String wifimac;

    ListView list_dev;
    List<String> macs;
    static List<byte[]> devAddrList;//设备地址列表
    ArrayAdapter  adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_send);

        tv_ssid=(TextView)findViewById(R.id.tv_ssid);
        et_wifi_pwd=(EditText)findViewById(R.id.et_wifi_pwd);
        btn_config=(ButtonRectangle)findViewById(R.id.btn_scan);
        list_dev=(ListView)findViewById(R.id.list_dev);
        x.view().inject(this);
        currentcontext = this;

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

        WifiUtils wi = new WifiUtils(currentcontext);
        Ssid = wi.getWifiConnectedSsid();
        wifimac = wi.getWifiConnectedBssid();
        if (Ssid != null) {
            tv_ssid.setText(Ssid);
            String pwd = FileUtils.shareToModel(currentcontext, String.class, Ssid + "wifi");//获取密码
            et_wifi_pwd.setText(pwd);
        }

        btn_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Ssid != null) {
                    String pwd = et_wifi_pwd.getText().toString();

                    DeviceHelper.wifiSmartConfigStart(Ssid, pwd, false, 60000);//本地广播发送入网配置，超时60S后自动停止
                    FileUtils.modelToShare(currentcontext, pwd, Ssid + "wifi");//记住密码

                    ShowTost("已广播配置，请等待");
                    Animation rotate = AnimationUtils.loadAnimation(currentcontext, R.anim.rotate_anim);
                    ivcan.setVisibility(View.VISIBLE);
                    ivcan.setAnimation(rotate);
                    ivcan.startAnimation(rotate);
                    tv_scan.setVisibility(View.VISIBLE);

                } else {
                    ShowTost("请先把手机连接wifi,再操作");
                }
            }
        });
        devAddrList=new ArrayList<>();
        macs=new ArrayList<>();
        adapter=new ArrayAdapter<String>(this, R.layout.list_item_1, macs);
        list_dev.setAdapter(adapter);

        DeviceHelper.setSectionListener(myDeviceHelperListener);
    }

    void  injoin(byte[] src)//入网的设备
    {
        boolean isAdd = true;//是否可添加
        for (byte[] item : devAddrList) {
            if (Arrays.equals(item, src)) {
                isAdd = false;//已经存在
                break;
            }
        }
        if (isAdd)//可添加
        {
            devAddrList.add(src);
            macs.add(StringHelper.bytesToHexString(src));
            myHandler.sendEmptyMessage(1);
        }

    }

    DeviceHelper.onSectionListener myDeviceHelperListener = new DeviceHelper.onSectionListener() {//入网监听
        @Override
        public void DeviceJoinIndicates(byte[] addr)
        {
            injoin(addr);//有新设备入网
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
        public void CompleteDevice(byte[] addr, JSONObject devInfo, byte[] ports, ArrayList<JSONObject> descList) {
        }
        @Override
        public void ReceiveLqi(byte[] addr, byte port, int lqi, int state) {

        }
        @Override
        public void ReceiveBeacon(byte[] addr, int state) {
        }
        @Override
        public void SendStatusCB(byte[] src,int seq, int port, int aID, int cmd,int option, int state) {
        }
    };
}
