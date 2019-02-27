package com.jifan.actv;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.jifan.MyApp;
import com.jifan.model.dev_hardware;

import java.util.ArrayList;
import java.util.List;

import pluto.Clib;
import pluto.DeviceHelper;


public class dialog_dev_select extends android.app.Dialog {

    public interface finish_istener {

        void process(int position);
    }

    Context _context;
    protected MyApp app;
    View view, backView;//background
    ListView list_dev;
    private finish_istener listterner;
    int position;
    ArrayAdapter adapter;

    public dialog_dev_select(Context context, finish_istener _listterner) {
        super(context, android.R.style.Theme_Translucent);
        this._context = context;
        app = (MyApp)context.getApplicationContext();
        listterner = _listterner;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_dev_select);

        view = (RelativeLayout) findViewById(R.id.llcontent);
        backView = (RelativeLayout) findViewById(R.id.rl_root);
        backView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getX() < view.getLeft() || event.getX() > view.getRight()
                        || event.getY() > view.getBottom() || event.getY() < view.getTop()) {
                    dismiss();
                }
                return false;
            }
        });


        list_dev = (ListView) findViewById(R.id.list_dev);
        List<String> macs = new ArrayList<>();
        List<dev_hardware> all = app.getDevList();
        for (int i = 0; i < all.size(); i++) {
            macs.add(all.get(i).getMac());
        }
        adapter = new ArrayAdapter<String>(_context, R.layout.list_item_2, macs);
        list_dev.setAdapter(adapter);
        list_dev.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int pos, long l) {
                listterner.process(pos);
                dismiss();
            }
        });


        //长按删除
        list_dev.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){//长按修改
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View view,
                                           int position, long arg3) {
                final int pindex=position;
                new AlertDialog.Builder(_context)
                        .setTitle("删除设备")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                List<dev_hardware> list= app.getDevList();
                                DeviceHelper.RemoveDevice(Clib.hexToBytes(list.get(pindex).getMac()));//删除设备信息
                                list.remove(pindex);
                                app.setDevList(list);
                                dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {

                            }
                        }).show();

                return false;
            }
        });

    }


    @Override
    public void show() {
        super.show();
        view.startAnimation(AnimationUtils.loadAnimation(_context, R.anim.dialog_main_show_amination));
        backView.startAnimation(AnimationUtils.loadAnimation(_context, R.anim.dialog_root_show_amin));
    }

    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(_context, R.anim.dialog_main_hide_amination);

        anim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        dialog_dev_select.super.dismiss();
                    }
                });
            }
        });
        Animation backAnim = AnimationUtils.loadAnimation(_context, R.anim.dialog_root_hide_amin);
        view.startAnimation(anim);
        backView.startAnimation(backAnim);
    }
}
