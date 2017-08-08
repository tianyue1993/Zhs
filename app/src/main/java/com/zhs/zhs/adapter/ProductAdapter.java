package com.zhs.zhs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhs.zhs.R;
import com.zhs.zhs.entity.Product;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class ProductAdapter extends ZhsBaseAdapter {

    public ProductAdapter(Context context, List<Product> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Product mInfo = (Product) mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_product, null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {


        }
        return convertView;
    }


    private static class ViewHolder {

    }
}
