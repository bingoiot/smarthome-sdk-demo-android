package com.jifan.actv;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.jifan.Config;
import com.jifan.model.aid_value;
import com.jifan.model.dev_hardware;
import com.jifan.model.dev_port;
import com.jifan.utils.Adapter_dev_GridView;
import com.jifan.utils.ControlHelper;
import com.jifan.utils.DevManage;
import com.jifan.utils.MyGridView;
import com.jifan.utils.UIHelper;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pluto.Aps;
import pluto.AttributeID;
import pluto.Clib;
import pluto.Common;
import pluto.Pluto;

public class DevListActivity extends BaseActivity {

    @ViewInject(R.id.gv_dev)
     MyGridView gv_dev;

    private Adapter_dev_GridView adapterDevGridView;
    List<dev_port> deviceportlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_list);
        init();
    }

void  init()
{
    x.view().inject(this);
    List<dev_hardware> all = app.getDevList();
    if(!(all!=null&&all.size()>0))
    {
        ShowTost("请先添加设备");
        this.finish();
        return;
    }
    deviceportlist=new ArrayList<>();
    for (dev_hardware dev :all)
    {
        deviceportlist.addAll(dev.getDev_portList());
    }
    deviceportlist = UIHelper.InitDevportIcon( deviceportlist, app.getSeedList());//端口初始化 图标等

    adapterDevGridView=new Adapter_dev_GridView(currentcontext,deviceportlist);
    gv_dev.setAdapter(adapterDevGridView);


    gv_dev.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            dev_port item = deviceportlist.get(position);
            ControlHelper.clickDevPort(currentcontext, item, deviceportlist);//点击 发送数据

        }
    });
    gv_dev.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {//长按进入编辑页
        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
            dev_port item = deviceportlist.get(position);
            ControlHelper.longclickDevPort(currentcontext, item);
            return true;
        }//设置事件监听(长按)


    });

    myHandler = new Handler(currentcontext.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            switch (msg.what) {
                case 1:// 更新数据
                    adapterDevGridView.notifyDataSetChanged();
                    break;
            }
        }
    };

    Setlisen();
    SendReadDevStat();
}

    /**
     * 设备状态监听
     */
    void Setlisen() {
        Aps.setSectionListener(AttributeID.Gen_Key.Value,AttributeID.Unkown , new Aps.onSectionListener() {
            @Override
            public void RecieveCB(int keyID, byte[] src, int seq, int port, int aID, int cmd, int option, byte[] pdata, int len) {//收到设备状态
                int aid_dev_type = 0;
                for (dev_port item : deviceportlist) {
                    if (Arrays.equals(item.getDevAddr(), src) && port == item.getPort()) {
                        List<aid_value> list = item.getAid_valueList();
                        for (aid_value av : list) {
                            if (av.getAid() == aID) {
                                av.setValues(Clib.bytes2Hex(pdata, '\0'));
                                break;
                            }
                        }
                        item.setAid_valueList(list);
                        break;
                    }
                }
                myHandler.sendEmptyMessage(1);//刷新listview
            }

            @Override
            public void SendStatus(byte[] src,int seq,int port, int aID, int cmd,int  option, int state) {

            }
        });
        Pluto.setOnLoginStateListener(new Pluto.onLoginStateListener() {//登录状态 监听
            @Override
            public void recieve(int state) {
                /* dev_stopLogin,//停止=0
                    dev_startLogin,//开始登录 =1
                    dev_online,//登录成功，在线=2
                    dev_offline,//离线=3
                    dev_loginFailed,//登录失败=4
                    dev_logoutFailed,//退出登录失败=5
                */
                if (state == 2) {
                    if (!Config.JifanIsLogin) {
                        Config.JifanIsLogin = true;
                        SendReadDevStat();//登录后，读取设备状态
                    }

                }
            }
        });
    }

    //发送读取设备状态
    void SendReadDevStat() {
        if (deviceportlist!= null)
            for (dev_port devPort : deviceportlist) {
                ControlHelper.SendDataRead(devPort);//发送读的状态
            }
    }


}
