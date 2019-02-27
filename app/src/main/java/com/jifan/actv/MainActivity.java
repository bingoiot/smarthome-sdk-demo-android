package com.jifan.actv;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jifan.Config;
import com.jifan.model.dev_hardware;
import com.jifan.utils.DevManage;
import com.jifan.utils.MyListView;

import java.util.List;

import pluto.Clib;
import pluto.DeviceHelper;
import pluto.Pluto;


public class MainActivity extends BaseActivity {
    String[] funtionArry = {"设备入网配置", "扫描发现/添加设备", "获取、修改设备信息", "控制/监听设备", "黑白名单", "设备升级", "工厂配置", "情景编程", "zigbee子设备加入"};
    MyListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        listView = (MyListView) findViewById(R.id.listv);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "退出登录", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //app.setLoginname("");
                Config.isAppLogin = false;
                toActivity(LoginActivity.class);
            }
        });
        setManu();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //ShowTost(funtionArry[i]);
                switch (i) {
                    case 0://设备入网配置
                        toActivity(WifiSendActivity.class);
                        break;
                    case 1://扫描发现设备
                        toActivity(WifiSearhActivity.class);
                        break;
                    case 2://获取设备信息
                        new dialog_dev_select(currentcontext, new dialog_dev_select.finish_istener() {
                            @Override
                            public void process(int position) {
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("mode", app.getDevList().get(position));
                                toActivity(DevInfoActivity.class, mBundle);
                            }
                        }).show();
                        break;
                    case 3:// 控制监听设备
                        toActivity(DevListActivity.class);
                        break;
                    case 4:// 黑白名单
                        new dialog_dev_select(currentcontext, new dialog_dev_select.finish_istener() {
                            @Override
                            public void process(int position) {
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("mode", app.getDevList().get(position));
                                toActivity(TestUserListActivity.class, mBundle);
                            }
                        }).show();
                        break;
                    case 5:// 设备升级
                        new dialog_dev_select(currentcontext, new dialog_dev_select.finish_istener() {
                            @Override
                            public void process(int position) {
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("mode", app.getDevList().get(position));
                                toActivity(TestUpgradeActivity.class, mBundle);
                            }
                        }).show();
                        break;
                    case 6:// 工厂配置
                        new dialog_dev_select(currentcontext, new dialog_dev_select.finish_istener() {
                            @Override
                            public void process(int position) {
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("mode", app.getDevList().get(position));
                                toActivity(TestFactoryActivity.class, mBundle);
                            }
                        }).show();
                        break;
                    case 7://情景编程
                        new dialog_dev_select(currentcontext, new dialog_dev_select.finish_istener() {
                            @Override
                            public void process(int position) {
                                Bundle mBundle = new Bundle();
                                mBundle.putSerializable("mode", app.getDevList().get(position));
                                toActivity(TestSceneActivity.class, mBundle);
                            }
                        }).show();
                        break;
                    case 8://zigbee 子设备加入
                        new dialog_dev_select(currentcontext, new dialog_dev_select.finish_istener() {
                            @Override
                            public void process(int position) {
                                dev_hardware item = app.getDevList().get(position);
                                if (DevManage.IsEnableJoin(item.getAids_gd())) {
                                    if(item.getKeyid()==2) {
                                        Bundle mBundle = new Bundle();
                                        mBundle.putSerializable("mode", item);
                                        toActivity(PortJoinActivity.class, mBundle);
                                    }else {
                                        ShowTost("非管理员，请重置设备，重新入网再测试！");
                                    }
                                } else {
                                    ShowTost("非网关设备！");
                                }
                            }
                        }).show();
                        break;
                }
            }
        });

        intit();
    }

    void setManu() {
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, funtionArry));
    }

    /**
     * 初始化数据
     */
    void intit() {
        List<dev_hardware> all = app.getDevList();
        if (all != null && all.size() > 0) {
            for (dev_hardware item : all) {
                DeviceHelper.AttachDevice(Clib.hexToBytes(item.getMac()), Clib.hexToBytes(item.getAdkey()), Clib.hexToBytes(item.getComkey()), Clib.hexToBytes(item.getGuestey()));//添加设备到通信层
            }

        }
    }
}
