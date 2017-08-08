package com.zhs.zhs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.entity.device.Device;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class DeviceAdapter extends ZhsBaseAdapter<Device> {

    public DeviceAdapter(Context context, ArrayList<Device> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Device mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mydevice, null);
            holder.avator = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.device_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (mInfo != null) {
            holder.name.setText(mInfo.name);
        }

        return convertView;
    }


    private static class ViewHolder {
        ImageView avator;
        TextView name;

    }
}
