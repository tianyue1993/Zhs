package com.zhs.zhs.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.AddUserFamify;
import com.zhs.zhs.entity.UserFamily;

import java.util.List;

/**
 * Created by admin on 2017/4/14.
 */

public class FamilyManageAdapter extends ZhsBaseAdapter<UserFamily> {

    public FamilyManageAdapter(Context context, List<UserFamily> List) {
        super(context);
        this.mList = List;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        final UserFamily mInfo = mList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_familymanage, null);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address = (TextView) convertView.findViewById(R.id.address);
            holder.write = (TextView) convertView.findViewById(R.id.write);
            holder.tvdefault = (TextView) convertView.findViewById(R.id.isDefault);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {
            holder.name.setText(mInfo.name);
            holder.address.setText(mInfo.address);
            if (mInfo.isDefault) {
                holder.tvdefault.setTextColor(mContext.getResources().getColor(R.color.orange));
            } else {
                holder.tvdefault.setTextColor(mContext.getResources().getColor(R.color.text_color));
            }

        }

        holder.write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddUserFamify.class);
                intent.putExtra("rivise", "1");
                Bundle bundle = new Bundle();
                bundle.putSerializable("userFamily", mInfo);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }


    private static class ViewHolder {
        TextView name, address, tvdefault, write, delete;
    }


}
