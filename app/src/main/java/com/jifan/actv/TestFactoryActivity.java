package com.jifan.actv;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jifan.model.dev_hardware;

import org.json.JSONObject;

import java.util.List;

import pluto.Clib;
import pluto.Common;
import pluto.Factory;

public class TestFactoryActivity extends BaseActivity implements View.OnClickListener {
    Button btn_default_factory;
    Button btn_read_factory;
    Button btn_write_factory;

    EditText tv_actory = null;
    EditText ed_password = null;
    EditText ed_addr = null;
   // byte[] test_addr={0x01,0x00,0x01,0x01,0x00,0x00,0x00,0x0C};

    Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_factory);

        btn_default_factory = (Button)findViewById(R.id.btn_default_factory);
        btn_read_factory = (Button)findViewById(R.id.btn_read_factory);
        btn_write_factory = (Button)findViewById(R.id.btn_write_factory);

        btn_default_factory.setOnClickListener(this);
        btn_read_factory.setOnClickListener(this);
        btn_write_factory.setOnClickListener(this);
        tv_actory = (EditText) findViewById(R.id.tv_factory);
        ed_password = (EditText)findViewById(R.id.edit_factory_password);
        ed_addr = (EditText)findViewById(R.id.edit_factory_addr);
        Factory.setSectionListener(myListener);
        //ed_addr.setText(Clib.bytes2Hex(test_addr,'\0'));
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        tv_actory.setText(tv_actory.getText()+(String)msg.obj);
                        break;
                    case 2://clear text view
                        tv_actory.setText("");
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
    public void onClick(View view) {
        clearTextView();
        byte[] addr = Clib.hexToBytes(ed_addr.getText().toString());
        switch (view.getId()) {
            case R.id.btn_default_factory:
                Message msg = Message.obtain();
                msg.obj = load_default_setting();
                msg.what=1;   //标志消息的标志
                myHandler.sendMessage(msg);
                break;
            case R.id.btn_read_factory:
                Factory.reqReadInfo(0x02,addr, Common.getSeq());
                break;
            case R.id.btn_write_factory:
                Factory.putExtraString("password",ed_password.getText().toString());
                String str = tv_actory.getText().toString();
                Message msg1 = Message.obtain();
                msg1.obj = str;
                msg1.what=1;   //标志消息的标志
                myHandler.sendMessage(msg1);
                Factory.reqSaveInfo(0x02,addr, Common.getSeq(),str);
                break;
        }
    }
    Factory.onSectionListener myListener = new Factory.onSectionListener() {
        @Override
        public void ReadCB(int keyID, byte[] addr, int state, JSONObject info) {
            if(state== Common.op_succeed)
            {
                Message msg = Message.obtain();
                msg.obj = info.toString();
                msg.what=1;   //标志消息的标志
                myHandler.sendMessage(msg);
            }
        }

        @Override
        public void WriteCB(int keyID, byte[] addr, int state) {
            Message msg = Message.obtain();
            msg.obj = "write factory info state:"+ Integer.toString(state)+"\r\n";
            msg.what=1;   //标志消息的标志
            myHandler.sendMessage(msg);
        }
    };
    private void clearTextView()
    {
        Message msg = Message.obtain();
        msg.obj = null;
        msg.what=2;   //标志消息的标志
        myHandler.sendMessage(msg);
    }
    private String load_default_setting()
    {/*
     "PID":	1,
	"VID":	1,
	"product_date":	"2018-07-06",
	"password":	"123456",
	"sn":	"7DBEA63ADB206D1C45F24450D1D5C489",
	"addr":	"010001010000000C",
	"server_url":	"www.glalaxy.com",
	"server_ipv4":	"119.23.8.181",
	"server_udp_port":	16729,
	"server_tcp_port":	16728,
	"local_udp_port":	17729
      */
        Factory.creat("www.glalaxy.com","119.28.8.181",16729,16728,17729);
        Factory.putExtraNum("PID",1);
        Factory.putExtraNum("VID",1);
        Factory.putExtraString("product_date","2018-07-01");
        Factory.putExtraString("addr","010001010000000C");
        Factory.putExtraString("sn","7DBEA63ADB206D1C45F24450D1D5C489");
        String str = Factory.output();
        return str;
    }
}
