package com.jifan.actv;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jifan.model.dev_hardware;

import java.util.List;

import pluto.Clib;
import pluto.Common;
import pluto.UserTable;

public class TestUserListActivity extends BaseActivity implements View.OnClickListener {

    Button btn_default_user_list;
    Button btn_delete_user_list;
    Button btn_read_user_list;
    Button btn_write_user_list;

    Handler myHandler;
    EditText ed_addr;
    TextView tv_user_list = null;
    //byte[] test_addr={0x01,0x00,0x01,0x01,0x00,0x00,0x00,0x11};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_user_list);

        btn_default_user_list = (Button)findViewById(R.id.btn_default_user_list);
        btn_delete_user_list = (Button)findViewById(R.id.btn_delete_user_list);
        btn_read_user_list = (Button)findViewById(R.id.btn_read_user_list);
        btn_write_user_list = (Button)findViewById(R.id.btn_write_user_list);

        btn_default_user_list.setOnClickListener(this);
        btn_delete_user_list.setOnClickListener(this);
        btn_read_user_list.setOnClickListener(this);
        btn_write_user_list.setOnClickListener(this);
        tv_user_list = (TextView) findViewById(R.id.tv_user_list);
        ed_addr = (EditText) findViewById(R.id.edit_test_user_list_addr);
        //ed_addr.setText(Clib.bytes2Hex(test_addr,'\0'));
        UserTable.setSectionListener(myListener);
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        tv_user_list.setText(tv_user_list.getText()+(String)msg.obj);
                        break;
                    case 2://clear text view
                        tv_user_list.setText("");
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
        byte[] addr = Clib.hexToBytes(ed_addr.getText().toString());
        clearTextView();
        switch (view.getId()) {
            case R.id.btn_default_user_list:
                Message msg = Message.obtain();
                msg.obj = load_default_user_list();
                msg.what=1;   //标志消息的标志
                myHandler.sendMessage(msg);
                break;
            case R.id.btn_delete_user_list:
                UserTable.reqDelete(0x02,addr, Common.getSeq());
                break;
            case R.id.btn_read_user_list:
                UserTable.reqRead(0x02,addr, Common.getSeq());
                break;
            case R.id.btn_write_user_list:
                UserTable.reqSave(0x02,addr, Common.getSeq(),tv_user_list.getText().toString());
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
    private UserTable.onSectionListener myListener = new UserTable.onSectionListener() {
        @Override
        public void ReadCB(int keyID, byte[] addr, int state, boolean white_list, byte[] userAddr, byte[] portlist) {
            if(state==0)
            {
                String str;
                if(white_list)
                    str = "read white list:\r\n";
                else
                    str = "read black list:\r\n";
                str += "user:"+ Clib.bytes2Hex(userAddr,' ')+"\r\n";
                str += "port:"+ Clib.bytes2Hex(portlist,' ') + "\r\n";
                Message msg = Message.obtain();
                msg.obj = str;
                msg.what=1;   //标志消息的标志
                myHandler.sendMessage(msg);
            }
            else
            {
            }
        }

        @Override
        public void WriteCB(int keyID, byte[] addr, int state) {
            Message msg = Message.obtain();
            msg.obj = "write black-white list state:"+ Integer.toString(state)+"\r\n";
            msg.what=1;   //标志消息的标志
            myHandler.sendMessage(msg);
        }

        @Override
        public void DeleteCB(int keyID, byte[] addr, int state) {
            if(state==0)
            {
            }
            else
            {
            }
        }

        @Override
        public void SendStatusCB(byte[] addr, int state) {
            if(state==0)
            {
            }
            else
            {
            }
        }
    };
    private String load_default_user_list()
    {
        byte[] addr0 = Clib.hexToBytes(ed_addr.getText().toString());
        byte[] addr1 = {(byte)0x81,(byte)0x86,0x18,0x37,0x67,0x63,(byte)0x82,0x00};
        byte[] addr2 = {(byte)0x81,(byte)0x86,0x18,0x37,0x67,0x63,(byte)0x83,0x00};
        byte[] port_list0={1,3};
        byte[] port_list1={3,2,(byte)254};
        byte[] port_list2={1,(byte)255};
        UserTable.create(false);
        UserTable.put(addr0,port_list0);
        UserTable.put(addr1,port_list1);
        UserTable.put(addr2,port_list2);
        return UserTable.output();
    }

}
