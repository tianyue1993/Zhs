package com.zhs.zhs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhs.zhs.R;

import java.util.ArrayList;


/**
 * Created by admin on 2017/4/14.
 */

public class GroupAdapter extends ZhsBaseAdapter<String> {

    public GroupAdapter(Context context, ArrayList<String> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final String mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_group, null);
            holder.family_name = (TextView) convertView.findViewById(R.id.family_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.family_name.setText(mInfo);
        return convertView;
    }


    private static class ViewHolder {
        TextView family_name;

    }
}
