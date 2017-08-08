package com.zhs.zhs.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.entity.News;

import java.util.List;

/**
 * Created by admin on 2017/4/14.
 */

public class WarnNewsAdapter extends ZhsBaseAdapter<News.NewsData> {
    private Dialog use;

    public WarnNewsAdapter(Context context, List<News.NewsData> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final News.NewsData mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_warn_news, null);
            holder.detail = (TextView) convertView.findViewById(R.id.detail);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.root = (RelativeLayout) convertView.findViewById(R.id.root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {

            holder.detail.setText(mInfo.title);
            holder.time.setText(mInfo.createdTime);

            if (mInfo.checked) {
                holder.root.setBackgroundColor(mContext.getResources().getColor(R.color.base_bg));
            } else {
                holder.root.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }

        }
        return convertView;
    }


    private static class ViewHolder {
        TextView detail, time;
        RelativeLayout root;
    }
}
