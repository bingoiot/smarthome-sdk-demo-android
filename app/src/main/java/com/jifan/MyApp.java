package com.jifan;

import android.app.Application;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jifan.model.aid_seed;
import com.jifan.model.dev_hardware;
import com.jifan.utils.FileUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/23 0023.
 */

public class MyApp extends Application {




    public String getLoginname() {
        return FileUtils.shareToModel(this, String.class, "loginname");
    }

    public void setLoginname(String loginname) {
        FileUtils.modelToShare(this, loginname, "loginname");
    }

    public String getPassword() {
        return FileUtils.shareToModel(this, String.class, "password");
    }

    public void setPassword(String password) {
        FileUtils.modelToShare(this, password, "password");
    }

/*
    public void saveDev(dev_hardware item)
    {
        List<dev_hardware> list= getDevList();
       for(int i=0;i<list.size();i++)
       {
           if(list.get(i).getMac().equals(item.getMac()))
           {
               list.set(i,item);
               setDevList(list);
               break;
           }

       }
    }
*/
    //获取所有的设备
    public List<dev_hardware> getDevList() {
        String data = FileUtils.shareToModel(this, String.class,
                Config.KEY_SHARE_ALLDEV);
        if (data != null && data != "") {
            Gson gson2 = new GsonBuilder().create();
            Type objectType = new TypeToken<List<dev_hardware>>() {
            }.getType();
            List<dev_hardware> item = gson2.fromJson(data, objectType);
            return item;
        } else {
            return new ArrayList<>();
        }
    }

    //设置所有的设备
    public void setDevList(List<dev_hardware> data) {
        Gson gson2 = new GsonBuilder().create();
        String pStr = gson2.toJson(data);
        FileUtils.modelToShare(this, pStr, Config.KEY_SHARE_ALLDEV);
    }

    //设置单个的设备
    public void setDev(dev_hardware item) {
        List<dev_hardware> list=getDevList();
        for (int i=0;i<list.size();i++)
        {
            if(list.get(i).getMac().equals(item.getMac()))
            {
                list.set(i,item);
                break;
            }
        }
            setDevList(list);
    }
    public  List<aid_seed> getSeedList() {
         ContextHelper contextHelper=new ContextHelper(this.getApplicationContext());
        String data = contextHelper.getAssetjson("aid_dev_type_config");
        Gson gson = new Gson();
        Type clz = new TypeToken<List<aid_seed>>() {
        }.getType();
        List<aid_seed> seedList = gson.fromJson(data, clz);
        return  seedList;
       /* String data2 = contextHelper.getAssetjson("aid_gen_type_config");
        app.aid_genList = gson.fromJson(data2, clz);
        return seedList;
        */
    }


/*
    //添加监听
    public static void AddOnRecieveMessageListener(String Id, Aps.onSectionListener listener) {
        Aps.setSectionListener(Common.aID_Gen_Type_KeyValue,Common.aID_Gen_Type_Unkown,listener);
    }
//移除监听
    public static void DelOnRecieveMessageListener(String Id, Aps.onSectionListener listener) {
        Aps.removeSectionListener(Common.aID_Gen_Type_KeyValue,Common.aID_Gen_Type_Unkown,listener);
    }
    */
}