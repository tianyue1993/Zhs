package com.zhs.zhs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.entity.NewsSettingData;

import java.util.ArrayList;


/**
 * Created by admin on 2017/6/2
 */

public class RemindTypeAdapter extends ZhsBaseAdapter<NewsSettingData> {
    public RemindTypeAdapter(Context context, ArrayList<NewsSettingData> List) {
        super(context);
        this.mList = List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final NewsSettingData mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_remind_type, null);
            holder.remind_name = (TextView) convertView.findViewById(R.id.remind_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (mInfo != null) {
            holder.remind_name.setText(mInfo.msgName);
            if (mInfo.remindType == null) {
                mInfo.remindType = new ArrayList<>();
            }
        }


//        holder.checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mInfo.remindType.add("1");
//                } else {
//                    mInfo.remindType.remove("1");
//                }
//                if (!holder.checkBox1.isChecked() && !holder.checkBox2.isChecked() && !holder.checkBox3.isChecked() && !holder.checkBox4.isChecked()) {
//                    holder.checkBox.setChecked(false);
//                }
//
//            }
//        });
//        holder.checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mInfo.remindType.add("2");
//                } else {
//                    mInfo.remindType.remove("2");
//                }
//                if (!holder.checkBox1.isChecked() && !holder.checkBox2.isChecked() && !holder.checkBox3.isChecked() && !holder.checkBox4.isChecked()) {
//                    holder.checkBox.setChecked(false);
//                }
//            }
//        });
//        holder.checkBox3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mInfo.remindType.add("3");
//                } else {
//                    mInfo.remindType.remove("3");
//                }
//                if (!holder.checkBox1.isChecked() && !holder.checkBox2.isChecked() && !holder.checkBox3.isChecked() && !holder.checkBox4.isChecked()) {
//                    holder.checkBox.setChecked(false);
//                }
//            }
//        });
//        holder.checkBox4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    mInfo.remindType.add("4");
//                } else {
//                    mInfo.remindType.remove("4");
//                }
//
//                if (!holder.checkBox1.isChecked() && !holder.checkBox2.isChecked() && !holder.checkBox3.isChecked() && !holder.checkBox4.isChecked()) {
//                    holder.checkBox.setChecked(false);
//                }
//            }
//        });

        return convertView;
    }


    private static class ViewHolder {
        TextView remind_name;
    }

}
