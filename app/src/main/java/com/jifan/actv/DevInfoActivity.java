package com.jifan.actv;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jifan.model.dev_hardware;
import com.jifan.utils.DevManage;
import com.jifan.utils.StringHelper;

import org.json.JSONObject;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pluto.Clib;
import pluto.Common;
import pluto.DeviceHelper;

public class DevInfoActivity extends BaseActivity {

    @ViewInject(R.id.tv_modenname)
    TextView tv_modenname;

    @ViewInject(R.id.tv_mac)
    TextView tv_mac;
    @ViewInject(R.id.tv_chipid)
    TextView tv_chipid;
    @ViewInject(R.id.tv_jdoVer)
    TextView tv_jdoVer;
    @ViewInject(R.id.tv_remoteLock)
    TextView tv_remoteLock;
    @ViewInject(R.id.tv_localLock)
    TextView tv_localLock;
    @ViewInject(R.id.tv_shareLock)
    TextView tv_shareLock;
    @ViewInject(R.id.tv_guestLock)
    TextView tv_guestLock;


    @ViewInject(R.id.rltwo)
    RelativeLayout rltwo;

    byte[] mac;//要修改的设备mac
    int cout;//端口个数
    boolean isGet;
dev_hardware old;//要修改的设备
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_info);


        cout = 0;
        List<dev_hardware> all = app.getDevList();
        if (all != null && all.size() > 0) {
            old= all.get(0);
            mac= Clib.hexToBytes(old.getMac());
            ShowTost("编辑第一个设备：" + old.getMac());
        } else {
            ShowTost("请先添加设备");
            this.finish();
            return;
        }

        init();
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

            System.out.printf("devvice info addr:" + Clib.bytes2Hex(addr, ':') + devInfo.toString() + "\r\n");//获取到设备信息

            if (Arrays.equals(mac, addr)) {

                myHandler.sendEmptyMessage(1);
            }

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

        //tv_chipid.setText(devHardware.getChipid());
        DeviceHelper.reqReadDeviceInfo((byte) old.getKeyid(), mac, Common.getSeq());//发送获取设备指令
        DeviceHelper.setSectionListener(myDeviceHelperListener);//接收信息监听
        myHandler = new Handler(currentcontext.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                Bundle data = msg.getData();
                switch (msg.what) {
                    case 1://

                        dev_hardware item = DevManage.getDevInfo(mac);
                        bindshowdata(item);
                        ShowTost("获取设备信息成功");
                        isGet = true;
                        break;
                }
            }
        };
        if (DevManage.IsEnableJoin(old.getAids_gd()) && old.getKeyid() == 2) {
            rltwo.setVisibility(View.VISIBLE);
        }

    }

    void bindshowdata(dev_hardware item) {
        if (item != null) {
            tv_jdoVer.setText(String.valueOf(item.getJdoVer()));
            tv_remoteLock.setText(item.isRemoteLock() ? "开" : "关");
            tv_localLock.setText(item.isLocalLock() ? "开" : "关");
            tv_shareLock.setText(item.getShareLock() ? "开" : "关");
            tv_guestLock.setText(item.isGuestLock() ? "开" : "关");
            tv_modenname.setText(item.getName());
            tv_mac.setText(item.getMac());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        currentcontext = this;

    }

    @Override
    protected void onDestroy() {
        DeviceHelper.removeSectionListener(myDeviceHelperListener);
        super.onDestroy();
    }

    /**
     * 单击事件
     * type默认View.OnClickListener.class，故此处可以简化不写，@Event(R.id.bt_main)
     */
    @Event(type = View.OnClickListener.class, value = R.id.btn_join)
    private void joninClick(View v) {
      /*  new dialog_port_join(currentcontext, devHardware, new dialog_port_join.finish_istener() {
            @Override
            public void process(int _cout) {


            }
        }).show();
        */


        Intent it = new Intent(currentcontext, PortJoinActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("mode", old);
        it.putExtras(mBundle);
        ((Activity) currentcontext).startActivity(it);

    }

    @Override
    protected void onStop() {

        super.onStop();

    }

    /**
     * 单击事件
     * type默认View.OnClickListener.class，故此处可以简化不写，@Event(R.id.bt_main)
     */
    @Event(type = View.OnClickListener.class, value = R.id.ib_edit)
    private void editClick(View v) {
        if (!isGet) {
            ShowTost("还未获取到设备信息，请稍后再试");

        } else if(old.getKeyid()!=2)
        {
            ShowTost("非管理员，不能修改");
        }
            else {
            showLogoutDialog(old.getName());

        }
    }


    public void showLogoutDialog(String title) {

        LayoutInflater layoutInflater = LayoutInflater.from(currentcontext);
        RelativeLayout layout = (RelativeLayout) layoutInflater.inflate(R.layout.dialog_editdev, null);
        final Dialog dialog = new AlertDialog.Builder((Activity) currentcontext).create();
        dialog.show();
        dialog.getWindow().setContentView(layout);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final EditText et_name = (EditText) layout.findViewById(R.id.et_name);

        final CheckBox cb_guest = (CheckBox) layout.findViewById(R.id.cb_guest);
        final CheckBox cb_share = (CheckBox) layout.findViewById(R.id.cb_share);
        final CheckBox cb_remote = (CheckBox) layout.findViewById(R.id.cb_remote);
        final CheckBox cb_local = (CheckBox) layout.findViewById(R.id.cb_local);
        et_name.setText(title);
        dev_hardware item = DevManage.getDevInfo(mac);
        cb_remote.setChecked(item.isRemoteLock());
        cb_guest.setChecked(item.isGuestLock());
        cb_local.setChecked(item.isLocalLock());
        cb_share.setChecked(item.getShareLock());


        layout.findViewById(R.id.btn_cacell).setOnClickListener(new View.OnClickListener() {//点击关闭
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        layout.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {//点击修改
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                String name = et_name.getText().toString();
                boolean isGeust = cb_guest.isChecked();
                boolean isshare = cb_share.isChecked();
                boolean isremote = cb_remote.isChecked();
                boolean islocal = cb_local.isChecked();

                DeviceHelper.setLocalLockFlag(mac, islocal);
                DeviceHelper.setRemoteLockFlag(mac, isremote);
                DeviceHelper.setGuestLockFlag(mac, isGeust);
                DeviceHelper.setShareLockFlag(mac, isshare);
                DeviceHelper.setDeviceName(mac, name);//修改名称
                DeviceHelper.updateDeviceInfo(mac, (byte) old.getKeyid());//更新设备
                dev_hardware item = DevManage.getDevInfo(old.getMac());
                //app.saveDev(devHardware);

                bindshowdata(item);
            }
        });

    }

}