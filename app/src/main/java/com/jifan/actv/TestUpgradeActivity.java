package com.jifan.actv;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jifan.model.dev_hardware;

import org.json.JSONObject;

import java.util.List;

import pluto.Clib;
import pluto.Upgrade;

public class TestUpgradeActivity extends BaseActivity implements View.OnClickListener {

    Button btn_start_upgrade;
    Button btn_upgrade_read_info;

    //byte[] test_addr={0x01,0x00,0x01,0x01,0x00,0x00,0x00,0x0C};
    ProgressBar bar ;
    EditText ed_ip;
    EditText ed_port;
    EditText ed_url;
    TextView tv_show;
    EditText ed_addr;
    Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_upgrade);

        btn_start_upgrade = (Button)findViewById(R.id.btn_start_upgrade);
        btn_upgrade_read_info = (Button)findViewById(R.id.btn_upgrade_read_info);

        btn_start_upgrade.setOnClickListener(this);
        btn_upgrade_read_info.setOnClickListener(this);
        bar = (ProgressBar) findViewById(R.id.pbar_upgrade);
        ed_ip = (EditText) findViewById(R.id.edit_upgrade_server_ip);
        ed_port = (EditText) findViewById(R.id.edit_upgrade_server_port);
        ed_url = (EditText) findViewById(R.id.edit_upgrade_server_url);
        ed_addr = (EditText) findViewById(R.id.edit_test_upgrade_addr);
        tv_show = (TextView)findViewById(R.id.tv_upgrade);
        ed_ip.setText("192.168.0.91");
        ed_port.setText("80");
        ed_url.setText("/Pluto_lib/user2.4096.new.4.bin");
       // ed_addr.setText(Clib.bytes2Hex(test_addr,'\0'));
        Upgrade.setSectionListener(myListener);
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        tv_show.setText(tv_show.getText()+(String)msg.obj);
                        break;
                    case 2://clear text view
                        tv_show.setText("");
                        break;
                }
            }
        };
        List<dev_hardware> all = app.getDevList();
        if(!(all!=null&&all.size()>0))
        {
            ShowTost("请先添加设备");
            this.finish();
            return;
        }else {
            ed_addr.setText(all.get(0).getMac());
        }
    }
    @Override
    public void onClick(View v) {
        byte[]  addr = Clib.hexToBytes(ed_addr.getText().toString());
        clearTextView();
        switch (v.getId()) {
            case R.id.btn_start_upgrade:
                Upgrade.reqUpdate((byte)0x02,addr,ed_ip.getText().toString(), Integer.parseInt(ed_port.getText().toString()),ed_url.getText().toString(),3);
                break;
            case R.id.btn_upgrade_read_info:
                Upgrade.reqReadInfo((byte)0x02,addr);
                break;
        }
    }
    private void clearTextView()
    {
        Message msg = Message.obtain();
        msg.obj = null;
        msg.what=2;   //标志消息的标志
        myHandler.sendMessage(msg);
    }
    private void showMessage(String str)
    {
        Message msg = Message.obtain();
        msg.obj = str;
        msg.what=1;   //标志消息的标志
        myHandler.sendMessage(msg);
    }
    Upgrade.onSectionListener myListener = new Upgrade.onSectionListener() {
        @Override
        public void ReadInfoCB(int keyID, byte[] addr, int state, JSONObject info) {
            showMessage(info.toString());
        }
        @Override
        public void UpdateStateCB(int keyID, byte[] addr, int state, int percent) {
            String str = "updata state:"+ Integer.toString(state)+"percent:"+ Integer.toString(percent);
            showMessage(str);
        }
    };


}
