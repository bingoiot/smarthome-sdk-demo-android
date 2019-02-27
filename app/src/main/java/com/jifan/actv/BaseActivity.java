package com.jifan.actv;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.jifan.Config;
import com.jifan.ContextHelper;
import com.jifan.MyApp;
import com.jifan.utils.StringHelper;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import pluto.Clib;
import pluto.Pluto;

/**
 * Created by ludy on 2016/12/28 0028.
 */

public class BaseActivity extends AppCompatActivity {
    static {Pluto.Init();}
    protected MyApp app;
    Handler myHandler;
    protected  Context currentcontext;
    private Toolbar mToolbar;
    private TextView mToolbarTitle;
    private TextView mToolbarSubTitle;
    protected ContextHelper contextHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (MyApp) getApplicationContext();
        currentcontext = this;
        contextHelper = new ContextHelper(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//状态栏效果，不透明(4.4以下)，透明(4.4以上)，半透明(5.0以上)):
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        */
           /*
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setSubtitle("Sub Title");
        */

        if (mToolbar != null) {
            //将Toolbar显示到界面
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);//隐藏原来的
        }

    }




    @Override
    protected void onStart() {
        super.onStart();
        /**
         * 判断是否有Toolbar,并默认显示返回按钮
         */
        if (null != getToolbar() && isShowBacking()) {
            showBack();
        }
    }

    @Override
    protected void onResume() {

        if(  Config.flag==0)
        {
            protectApp();
            return;
        }

        if (!Config.isActive) {
            //app 从后台唤醒，进入前台
            Config.isActive = true;
            Log.w("ACTIVITY", "程序从后台唤醒");
            Date notime = new Date();
            long interval = (notime.getTime() - Config.ActiveEndTime.getTime()) / 1000;
            Log.w("ACTIVITY", "两个时间相差" + interval + "秒");//会打印出相差3秒

            if (!Config.JifanIsLogin&&app.getLoginname()!=null&&app.getLoginname()!="") {//需要重新登录
                byte[] localmac = Clib.hexToBytes("81" + "86" + "0" + app.getLoginname());//手机
                byte[] localSN =StringHelper.strToEncryptKey(app.getPassword(), 16);//密码
                Pluto.reqLogin(localmac, localSN);//硬件socket登录
            }
        }

        super.onResume();
    }

    @Override
    protected void onStop() {
        if (!isAppOnForeground()) {
            //app 进入后台
            Config.isActive = false;//记录当前已经进入后台


            Config.ActiveEndTime = new Date();//当前时间
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd    hh:mm:ss");
            String date = sDateFormat.format(Config.ActiveEndTime);
            Log.i("ACTIVITY", "程序进入后台 睡眠时间 ：" + date);
            Config.JifanIsLogin=false;
        }
        super.onStop();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    protected void protectApp() {

        Intent intent=new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//清空栈里MainActivity之上的所有activty
        startActivity(intent);
        this.finish();
    }

    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }



    /**
     * this Activity of tool bar.
     * 获取头部.
     *
     * @return support.v7.widget.Toolbar.
     */
    public Toolbar getToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    /**
     * 版本号小于21的后退按钮图片
     */
    private void showBack() {
        //setNavigationIcon必须在setSupportActionBar(toolbar);方法后面加入
       // getToolbar().setNavigationIcon(R.drawable.arrow_left);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * 是否显示后退按钮,默认显示,可在子类重写该方法.
     *
     * @return
     */
    protected boolean isShowBacking() {
        return true;
    }




    protected void ShowTost(String msg) {
        Toast toast = Toast.makeText(currentcontext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


    /**
     * Activity跳转
     *
     * @param nextAct
     */
    public void toActivity(Class<? extends Activity> nextAct) {
        Intent intent = new Intent(currentcontext, nextAct);
        startActivity(intent);
    }

    public void toActivity(Class<? extends Activity> nextAct, Bundle bundle) {
        Intent intent = new Intent(currentcontext, nextAct);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

}
