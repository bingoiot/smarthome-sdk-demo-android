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
import pluto.Scene;

public class TestSceneActivity extends BaseActivity implements View.OnClickListener{
    Button btn_default_scene;
    Button btn_delete_scene;
    Button btn_read_scene;
    Button btn_write_scene;
    Button btn_scroll_light;
    Button btn_loop_scene;
    Handler myHandler;

    TextView tv_scene = null;
    EditText ed_addr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scene);

        btn_default_scene = (Button)findViewById(R.id.btn_default_scene);
        btn_delete_scene = (Button)findViewById(R.id.btn_delete_scene);
        btn_read_scene = (Button)findViewById(R.id.btn_read_scene);
        btn_write_scene = (Button)findViewById(R.id.btn_write_scene);
        btn_scroll_light = (Button)findViewById(R.id.btn_scroll_light);
        btn_loop_scene = (Button)findViewById(R.id.btn_loop_scene);

        btn_default_scene.setOnClickListener(this);
        btn_delete_scene.setOnClickListener(this);
        btn_read_scene.setOnClickListener(this);
        btn_write_scene.setOnClickListener(this);
        btn_scroll_light.setOnClickListener(this);
        btn_loop_scene.setOnClickListener(this);
        tv_scene = (TextView) findViewById(R.id.tv_scene);
        ed_addr = (EditText)findViewById(R.id.edit_test_scene_addr);
        //ed_addr.setText(Clib.bytes2Hex(test_addr,'\0'));
        Scene.setSectionListener(myListener);
        myHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        tv_scene.setText(tv_scene.getText()+(String)msg.obj);
                        break;
                    case 2://clear text view
                        tv_scene.setText("");
                        break;
                }
            }
        };
        Object o = getIntent().getSerializableExtra("mode");
        if (o != null) {
            dev_hardware   old = (dev_hardware) o;
            ed_addr.setText(old.getMac());
        } else {
            ShowTost("请先添加设备");
            this.finish();
            return;
        }

    }

    @Override
    public void onClick(View v) {
        byte[] addr = Clib.hexToBytes(ed_addr.getText().toString());
        clearTextView();
        Message msg;
        switch (v.getId()) {
            case R.id.btn_default_scene:
                msg = Message.obtain();
                msg.obj = load_default_scene();
                msg.what=1;   //标志消息的标志
                myHandler.sendMessage(msg);
                break;
            case R.id.btn_delete_scene:
                Scene.reqDel(0x02,addr, Common.getSeq(),"test.vm");
                break;
            case R.id.btn_read_scene:
                Scene.reqRead(0x02,addr, Common.getSeq(),"test.vm");
                break;
            case R.id.btn_write_scene:
                String str = tv_scene.getText().toString();
                Scene.reqWrite(0x02,addr, Common.getSeq(),"test.vm",str.getBytes());
                break;
            case R.id.btn_scroll_light:
                msg = Message.obtain();
                msg.obj = load_scroll_light();
                msg.what=1;   //标志消息的标志
                myHandler.sendMessage(msg);
                break;
            case R.id.btn_loop_scene:
                msg = Message.obtain();
                msg.obj = load_loop_scene();
                msg.what=1;   //标志消息的标志
                myHandler.sendMessage(msg);
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
    Scene.onSectionListener myListener = new Scene.onSectionListener() {
        @Override
        public void ReadStateCB(int keyID, byte[] addr, String name, int state) {
            String str = "Read Scene:"+name+ " state:"+ Integer.toString(state)+"\r\n";
            showMessage(str);
        }

        @Override
        public void WriteStateCB(int keyID, byte[] addr, String name, int state) {
            String str = "write Scene:"+name+ " state:"+ Integer.toString(state)+"\r\n";
            showMessage(str);
        }

        @Override
        public void ReadNameCB(int keyID, byte[] addr, String name, int state) {
            String str = "read Scene:"+name+ " state:"+ Integer.toString(state)+"\r\n";
            showMessage(str);
        }

        @Override
        public void RenameCB(int keyID, byte[] addr, String fname, int state) {
            String str = "rename Scene:"+fname+ " state:"+ Integer.toString(state)+"\r\n";
            showMessage(str);
        }

        @Override
        public void ReadCB(int keyID, byte[] addr, String fname, String pdata) {
            String str = "read Scene:"+fname+ ":\r\n"+pdata+"\r\n";
            showMessage(str);
        }

        @Override
        public void WriteCB(int keyID, byte[] addr, String fname, int state) {
            String str = "write Scene:"+fname+ " state:"+ Integer.toString(state)+"\r\n";
            showMessage(str);
        }

        @Override
        public void DeleteCB(int keyID, byte[] addr, String fname, int state) {
            String str = "delete Scene:"+fname+ " state:"+ Integer.toString(state)+"\r\n";
            showMessage(str);
        }

        @Override
        public void Alarm(int keyID, byte[] addr, String fname, int state) {
            String str = "Scene alram :"+fname+ " state:"+ Integer.toString(state)+"\r\n";
            showMessage(str);
        }
    };
    private String load_default_scene()
    {
        String addr_str = ed_addr.getText().toString();
        int jbody = Scene.creatBody();
        //addAction(jbody,"state","auto");
        int jwhile = Scene.addWhileBlock(jbody, "1");
        int jif = Scene.addIfBlock(jwhile, "if", "wait(port=04,cmd=80,option=00,aID=009020)");
        Scene.addAction(jif, "do", "k=0");
        int jwhile1 = Scene.addWhileBlock(jif, "k < 5");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:01,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:02,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:03,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:03,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:02,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:01,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "do", "k++");
        String out = Scene.output(jbody);
        return out;
    }

    private String load_scroll_light()
    {
        String addr_str = ed_addr.getText().toString();
        int jbody = Scene.creatBody();
        //addAction(jbody,"state","auto");
        int jwhile = Scene.addWhileBlock(jbody, "1");
        Scene.addAction(jwhile, "send", "addr:"+addr_str+",keyID:00,port:01,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile, "delay", "1000");
        Scene.addAction(jwhile, "send", "addr:"+addr_str+",keyID:00,port:02,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile, "delay", "1000");
        Scene.addAction(jwhile, "send", "addr:"+addr_str+",keyID:00,port:03,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile, "delay", "1000");
        Scene.addAction(jwhile, "send", "addr:"+addr_str+",keyID:00,port:03,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile, "delay", "1000");
        Scene.addAction(jwhile, "send", "addr:"+addr_str+",keyID:00,port:02,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile, "delay", "1000");
        Scene.addAction(jwhile, "send", "addr:"+addr_str+",keyID:00,port:01,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile, "delay", "1000");
        String out = Scene.output(jbody);
        return out;
    }
    private String load_loop_scene()
    {
        String addr_str = ed_addr.getText().toString();
        int jbody = Scene.creatBody();
        //addAction(jbody,"state","auto");
        int jwhile = Scene.addWhileBlock(jbody, "1");
        int jif = Scene.addIfBlock(jwhile, "if", "time(z=8,time=0:0:1)==1&&time(z=8,time=18:0:0)==-1");
        Scene.addAction(jif, "do", "k=0");
        int jwhile1 = Scene.addWhileBlock(jif, "k < 5");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:01,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:02,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:03,cmd:02,option:00,aID:009020,dlen:0001,data:01");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:03,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:02,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "send", "addr:"+addr_str+",keyID:00,port:01,cmd:02,option:00,aID:009020,dlen:0001,data:00");
        Scene.addAction(jwhile1, "delay", "1000");
        Scene.addAction(jwhile1, "do", "k++");
        String out = Scene.output(jbody);
        return out;
    }
}
