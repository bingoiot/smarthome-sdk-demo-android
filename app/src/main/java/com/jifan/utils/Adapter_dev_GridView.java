package com.jifan.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.jifan.actv.R;
import com.jifan.model.dev_port;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/29.
 */

public class Adapter_dev_GridView extends BaseAdapter {
    private Context context;
    private List<dev_port> dataList;
    private LayoutInflater inflater;

    public Adapter_dev_GridView(Context _context, List<dev_port> _List) {
        this.context = _context;
        this.dataList = _List;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int arg0) {
        return dataList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Holder holder;
        boolean isFist = false;
        dev_port devPort = dataList.get(position);
        if (view == null) {
            isFist = true;
            view = LayoutInflater.from(context).inflate(R.layout.item_dev1, null);
            holder = new Holder();
            holder.iv_port = (ImageView) view.findViewById(R.id.iv_port);
            holder.tv_dev_titil = (TextView) view.findViewById(R.id.tv_dev_titil);

            binddata(holder, devPort);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        int cout = parent.getChildCount();
        if (cout == position)//adapter的getview 执行次数 解决办法
        {
            binddata(holder, devPort);
        }

        return view;
    }

    void binddata(Holder holder, dev_port devPort) {
        UIHelper.SetPort_item_ImageView(holder.iv_port, devPort);
        //if (!TextUtils.isEmpty(devPort.getPortname())) {
        holder.tv_dev_titil.setText(devPort.getPortname());
        //}
    }

    class Holder {
        ImageView iv_port;
        TextView tv_dev_titil;
    }

    public List<dev_port> getDataList() {
        return dataList;
    }
}
