package com.jifan.actv;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jifan.model.dev_hardware;

import pluto.Aps;
import pluto.AttributeID;
import pluto.Clib;
import pluto.Common;
import pluto.Scene;

public class SensorActivity extends BaseActivity implements View.OnClickListener{
    Button btn_clear;
    Handler myHandler;

    TextView tv_sensor_show = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sersor);
        btn_clear = (Button)findViewById(R.id.btn_sensor_clear_screen);
        tv_sensor_show = (TextView) findViewById(R.id.sensor_show_value);
        btn_clear.setOnClickListener(this);
        Aps.setSectionListener(AttributeID.Gen_Key.Value, AttributeID.Gen_Frequecy.Value,myListener);
        //ed_addr.setText(Clib.bytes2Hex(test_addr,'\0'));
        //Scene.setSectionListener(myListener);
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        tv_sensor_show.setText(tv_sensor_show.getText()+(String)msg.obj);
                        break;
                    case 2://clear text view
                        tv_sensor_show.setText("");
                        break;
                }
            }
        };
        Object o = getIntent().getSerializableExtra("mode");
        if (o != null) {
            dev_hardware   old = (dev_hardware) o;
            tv_sensor_show.setText(old.getMac()+"\r\n");
        } else {
            ShowTost("请先添加设备");
            this.finish();
            return;
        }

    }
    Aps.onSectionListener myListener = new Aps.onSectionListener() {
        @Override
        public void RecieveCB(int keyID, byte[] src, int seq, int port, int aID, int cmd, int option, byte[] pdata, int len) {
            switch(aID)
            {
                case AttributeID.Gen_WindSpeed.Value://wind speed
                    showMessage("wind speed:"+Clib.btostr(pdata,0,len)+"\r\n");
                    break;
                case AttributeID.Gen_Temperature.Value://temperature
                    showMessage("temperature:"+Clib.btostr(pdata,0,len)+"\r\n");
                    break;
                case AttributeID.Gen_Humidity.Value://humidity
                    showMessage("humidity:"+Clib.btostr(pdata,0,len)+"\r\n");
                    break;
            }
        }

        @Override
        public void SendStatus(byte[] src, int seq, int port, int aID, int cmd, int option, int state) {

        }
    };
    @Override
    public void onClick(View v) {
        Message msg;
        switch (v.getId()) {
            case R.id.btn_sensor_clear_screen:
                clearTextView();
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
}
