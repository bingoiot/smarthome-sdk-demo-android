package com.jifan;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/5/25.
 */

public class ContextHelper {

    //获取token 失败次数

    private int failcout;
    Context currentcontext;


    public ContextHelper(Context _context) {
        failcout = 0;
        currentcontext = _context;
    }

    /* Activity跳转
    *
            * @param nextAct
    */
    public void toActivity(Class<? extends Activity> nextAct) {
        Intent intent = new Intent(currentcontext, nextAct);
        currentcontext.startActivity(intent);
    }

    public void toActivity(Class<? extends Activity> nextAct, Bundle bundle) {
        Intent intent = new Intent(currentcontext, nextAct);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        currentcontext.startActivity(intent);
    }




    public void ShowTost(String msg) {
        Toast toast = Toast.makeText(currentcontext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }


    public  String getAssetjson(String filename) {
        InputStream is = currentcontext.getClassLoader().getResourceAsStream("assets/" + filename);
        InputStreamReader streamReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(streamReader);
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            while ((line = reader.readLine()) != null) {
                // stringBuilder.append(line);
                stringBuilder.append(line);
            }
            reader.close();
            reader.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

}
