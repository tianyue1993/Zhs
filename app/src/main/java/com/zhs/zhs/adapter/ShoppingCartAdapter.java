package com.zhs.zhs.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.entity.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

import static com.zhs.zhs.R.id.spinner;
import static com.zhs.zhs.activity.ShoppingCartActivity.EDIT_FINISH;
import static com.zhs.zhs.activity.ShoppingCartActivity.EDIT_FLAG;
import static com.zhs.zhs.activity.ShoppingCartActivity.totalPrice;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class ShoppingCartAdapter extends ZhsBaseAdapter {



    private List<String> typeList  = new ArrayList<>();
    private ArrayAdapter<String> typeAdapter;

    public ShoppingCartAdapter(Context context, ArrayList<ShoppingCart> List) {
        super(context);
        this.mList = List;
        mContext = context;


        for(int i =1 ;i<4 ;i++){
            typeList.add("         型号      规格  "+i);
        }
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
      //  Log.d("ShoppingCartAdapter", "getView..................");
        final ViewHolder holder;

        /**不是shoppingCart，shoppingCart只能操作最后一组数据*/
        final ShoppingCart shoppingCart = (ShoppingCart) mList.get(position);


        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shopping_cart, null);

            //绑定点击控件
            holder.cbProductCheck = (CheckBox) convertView.findViewById(R.id.cb_product_checked);
            holder.ivProductImage = (ImageView) convertView.findViewById(R.id.iv_product_image);
            holder.tvDeleteShoppingCart = (TextView) convertView.findViewById(R.id.tv_delete_shopping_cart);
            holder.tvProductDel = (TextView) convertView.findViewById(R.id.tv_product_del);
            holder.tvProductAdd = (TextView) convertView.findViewById(R.id.tv_product_add);
            holder.tvProductNums = (TextView) convertView.findViewById(R.id.tv_product_nums);

            //购物车信息
            holder.tvProductName = (TextView)convertView.findViewById(R.id.tv_product_name);
            holder.tvProductType = (TextView)convertView.findViewById(R.id.tv_product_type);
            holder.tvProductPrice = (TextView)convertView.findViewById(R.id.tv_product_price);
            holder.tvProductNum = (TextView)convertView.findViewById(R.id.tv_product_num);

            holder.spinner = (Spinner)convertView.findViewById(spinner);

            holder.shoppingCartInfo1 = (RelativeLayout) convertView.findViewById(R.id.shopping_cart_info1);
            holder.shoppingCartInfo2 = (RelativeLayout) convertView.findViewById(R.id.shopping_cart_info2);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (shoppingCart != null) {
        }

        if (EDIT_FINISH == EDIT_FLAG) {
            holder.shoppingCartInfo1.setVisibility(View.VISIBLE);
            holder. shoppingCartInfo2.setVisibility(View.INVISIBLE);
        } else{
            holder.shoppingCartInfo1.setVisibility(View.INVISIBLE);
            holder.shoppingCartInfo2.setVisibility(View.VISIBLE);
        }

        holder.cbProductCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final boolean isChecked = shoppingCart.isChecked();
                //更新数据
                shoppingCart.setChecked(!isChecked);
                notifyDataSetChanged();
                if(shoppingCart.isChecked()){
                    totalPrice =totalPrice + shoppingCart.getProductPrice()*shoppingCart.getProductNum();
                }else {
                    totalPrice = totalPrice - shoppingCart.getProductPrice()*shoppingCart.getProductNum();
                }

                sendTotalPrice(mContext);

                Log.d("ShoppingCartAdapter","isChecked:"+shoppingCart.isChecked());
                Log.d("ShoppingCartAdapter","Num:"+shoppingCart.getProductNum());
                Log.d("ShoppingCartAdapter","Price:"+shoppingCart.getProductPrice());



            }
        });
        holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("ShoppingCartAdapter", "图片点击。。。。。");
                Log.d("ShoppingCartAdapter","isChecked:"+shoppingCart.isChecked());
                Log.d("ShoppingCartAdapter","Num:"+shoppingCart.getProductNum());
                Log.d("ShoppingCartAdapter","Price:"+shoppingCart.getProductPrice());

            }
        });
        holder.tvDeleteShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(shoppingCart.isChecked()){
                    totalPrice =totalPrice - shoppingCart.getProductPrice()*shoppingCart.getProductNum();
                    shoppingCart.setChecked(false);
                }

                mList.remove(shoppingCart);

                notifyDataSetChanged();
                sendTotalPrice(mContext);
            }
        });

        holder.tvProductDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if((shoppingCart.isChecked()&&(shoppingCart.getProductNum()>1))){

                    totalPrice =totalPrice - shoppingCart.getProductPrice();
                }
                reduceProduct(shoppingCart);
                notifyDataSetChanged();
                sendTotalPrice(mContext);
                Log.d("ShoppingCartAdapter","del position:"+position);

            }
        });

        holder.tvProductAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addProduct(shoppingCart);
                if(shoppingCart.isChecked()){
                    totalPrice =totalPrice + shoppingCart.getProductPrice();
                }
                notifyDataSetChanged();
                sendTotalPrice(mContext);
                Log.d("ShoppingCartAdapter","add position:"+position);
            }
        });


       // spinner
        typeAdapter = new ArrayAdapter<>(mContext,android.R.layout.simple_spinner_item,typeList);
        //设置样式
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        holder.spinner.setAdapter(typeAdapter);



        //购物车信息
        holder.tvProductName.setText(shoppingCart.getProductName());
        holder.ivProductImage.setImageResource(shoppingCart.getProductImage());
        holder.tvProductType.setText(shoppingCart.getProductType());
        holder.tvProductNum.setText(String.format("X%d", shoppingCart.getProductNum()));
        holder.tvProductNums.setText(String.format("%d", shoppingCart.getProductNum()));
        holder.tvProductPrice.setText(String.format("￥%s", shoppingCart.getProductPrice()));
        holder.cbProductCheck.setChecked(shoppingCart.isChecked());


        return convertView;
    }



   private static class ViewHolder {

        RelativeLayout shoppingCartInfo1;
        RelativeLayout shoppingCartInfo2 ;

        //注册点击控件
        CheckBox cbProductCheck;
        ImageView ivProductImage;
        TextView tvDeleteShoppingCart;

        //购物车信息
        TextView tvProductName;
        TextView tvProductType;
        TextView tvProductPrice;
        TextView  tvProductNum; //完成界面的商品数量
        TextView tvProductDel;
        TextView tvProductAdd;
        TextView tvProductNums; //编辑界面的商品数量
        Spinner spinner;      //商品种类下拉




    }


    private void  reduceProduct(ShoppingCart shoppingCart) {
        int count = shoppingCart.getProductNum();
        if (count == 1) {
            return;
        }
        count--;
        shoppingCart.setProductNum(count);

    }

    private void  addProduct(ShoppingCart shoppingCart) {
        int count = shoppingCart.getProductNum();
        count++;
        shoppingCart.setProductNum(count);
    }


    private   void sendTotalPrice(Context context){
        Intent intent = new Intent();
        intent.setAction("activity.ShoppingCartActivity.CHECK_BROADCAST");
        intent.putExtra("totalPrice",totalPrice);
        context.sendBroadcast(intent);


    }

}



