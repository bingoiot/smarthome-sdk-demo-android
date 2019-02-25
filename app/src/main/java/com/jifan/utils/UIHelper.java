package com.jifan.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


import com.jifan.actv.R;
import com.jifan.model.aid_seed;
import com.jifan.model.aid_value;
import com.jifan.model.dev_port;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import pluto.*;

/**
 * Created by ludy on 2016/12/30 0030.
 */

public class UIHelper {

    //信号值 0 到255 四格
    public static void SetSignal_ImageView(ImageView iv_port, int signal) {
        int c = 0;
        if (0 < signal && signal <= 10) {
            c = 1;
        } else if (10 < signal && signal <= 30) {
            c = 2;
        } else if (30 < signal && signal <= 60) {
            c = 3;
        } else if (60 < signal) {
            c = 4;
        }
        String icon = "ic_signal" + c;
        int id = getResId(icon, R.drawable.class);
        if (id == -1)//找不到资源
        {
            id = R.drawable.ic_signal0;
        }
        iv_port.setImageResource(id);
    }


    public static void SetPort_item_ImageView(ImageView iv_port, dev_port devPort) {
        String icon = devPort.getIcon();


        List<aid_value> avlist = devPort.getAid_valueList();//所有aid和值
        int switch_den_type =ControlHelper.get_Alarm_aid(devPort.getAid_dev_type(),avlist);//是否有 报警
      if (switch_den_type == 0)//无报警
        {
            switch_den_type =  ControlHelper.get_Switch_aid(avlist);//是否有开关
        }
        if (switch_den_type != 0) {//有开关 报警
            aid_value avItem = null;
            String value = null;
            if (avlist != null) {
                for (aid_value av : avlist) {//获取主aid的值
                    if (av.getAid() == switch_den_type) {
                        value = av.getValues();
                        break;
                    }
                }
            }
            if (value != null) {//根据类型获取值，给不同的图标
                icon += value;
            }
        }

        int id = getResId(icon, R.drawable.class);
        if (id == -1)//找不到资源
        {
            id = R.drawable.ic_info_black;
        }
        iv_port.setImageResource(id);
    }

    //初始化 根据类型，把图标赋值
    public static List<dev_port> InitDevportIcon(List<dev_port> devPortList, List<aid_seed> seedList) {
        for (dev_port item : devPortList) {

            if (item.getIcon() == null) {
                for (aid_seed aidSeed : seedList) {
                    int aid_type = Integer.valueOf(aidSeed.getValues(), 16);
                    if (item.getAid_dev_type() == aid_type) {
                        item.setIcon(aidSeed.getIcon());
                        break;
                    }
                }
            }
        }
        return devPortList;
    }

    //初始化 根据类型，把图标赋值
    public static dev_port InitDevportIcon(dev_port item, List<aid_seed> seedList) {

        if (item.getIcon() == null) {
            for (aid_seed aidSeed : seedList) {
                int aid_type = Integer.valueOf(aidSeed.getValues(), 16);
                if (item.getAid_dev_type() == aid_type) {
                    item.setIcon(aidSeed.getIcon());
                    break;
                }
            }
        }
        return item;
    }

    //解决 在ScrollView中嵌套ListView空间，无法正确的计算ListView的大小
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    //根据字符串获取资源id  int id = ResourceMan.getResId("icon", R.drawable.class);
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 根据值, 设置spinner默认选中:
     *
     * @param spinner
     * @param value
     */
    public static void setSpinnerItemSelectedByValue(Spinner spinner, String value) {
        SpinnerAdapter apsAdapter = spinner.getAdapter(); //得到SpinnerAdapter对象
        int k = apsAdapter.getCount();
        for (int i = 0; i < k; i++) {
            if (value.equals(apsAdapter.getItem(i).toString())) {
                spinner.setSelection(i, true);// 默认选中项
                break;
            }
        }
    }

}
