package com.zhs.zhs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.ShoppingCartAdapter;
import com.zhs.zhs.entity.ShoppingCart;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhs.zhs.R.id.tv_edit_and_finish;


public class ShoppingCartActivity extends Baseactivity {


    public ListView listView;
    public ShoppingCartAdapter shoppingCartAdapter;

    public  static ArrayList<ShoppingCart> shoppingCartList = new ArrayList<>();

    public  ShoppingCart shoppingCart;

    public static final int EDIT_FLAG=1;
    public static final int FINISH_FLAG=2;
    public static int EDIT_FINISH=1;
    public static double totalPrice=0;
    public static boolean isChoose = false;  //商品名称前的CheckBox状态
    public static boolean isChooseAll= false;  //全选CheckBox状态
    public static int CountNum = 0;         //结算商品数量

    private IntentFilter intentFilter;
    private  CheckBroadcast checkBroadcast;

    @Bind(R.id.cb_product_name)
    CheckBox cbProductName;
    @Bind(tv_edit_and_finish)
    TextView tvEditAndFinish;
    @Bind(R.id.cb_choose_all)
    CheckBox cbChooseAll;
    @Bind(R.id.tv_all_money)
    public TextView tvAllMoney;
    @Bind(R.id.tv_count)
    TextView tvCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        ButterKnife.bind(this);
        setTitleTextView("购物车", null);

        tvAllMoney.setText("￥:"+totalPrice);
        cbProductName.setChecked(isChoose);
        cbChooseAll.setChecked(isChooseAll);
        tvCount.setText("结算:"+"("+CountNum+")");

        //初始化购物车列表
        if(shoppingCartList.isEmpty()){
            InitShoppingCartList();
        }


        listView = (ListView) findViewById(R.id.lv_product_info);

        shoppingCartAdapter = new ShoppingCartAdapter(ShoppingCartActivity.this, shoppingCartList);
        listView.setAdapter(shoppingCartAdapter);

        //item点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // startActivity(new Intent(mContext, ProductDetailActivity.class));

            }
        });

        intentFilter = new IntentFilter();
        intentFilter.addAction("activity.ShoppingCartActivity.CHECK_BROADCAST");
        checkBroadcast = new CheckBroadcast();
        registerReceiver(checkBroadcast,intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // 测试。。。
       // totalPrice =0;
    }

    public void InitShoppingCartList() {

        for (int  i= 0; i < 4; i++) {
            shoppingCart = new ShoppingCart("i","商品名称"+i,R.mipmap.ic_logo,100.0,"型号 规格"+i,i+1,false,false);
            //shoppingCart = new ShoppingCart();
            shoppingCartList.add(shoppingCart);

        }

    }





    //ShoppingCartActivity中控件的点击事件
    @OnClick({tv_edit_and_finish, R.id.cb_choose_all, R.id.tv_count})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case tv_edit_and_finish:
                if(EDIT_FINISH==EDIT_FLAG){
                    tvEditAndFinish.setText("完成");
                    EDIT_FINISH=FINISH_FLAG;
                }
                else {
                    tvEditAndFinish.setText("编辑");
                    EDIT_FINISH=EDIT_FLAG;
                }
                shoppingCartAdapter.notifyDataSetChanged();
                Log.d("MainActivity","EDIT_FINISH="+EDIT_FINISH);
                break;
            case R.id.cb_choose_all:
                if(cbChooseAll.isChecked()){

                    for(int i = 0;i<shoppingCartList.size();i++){
                        if(!(shoppingCartList.get(i).isChecked())){
                            totalPrice = totalPrice + shoppingCartList.get(i).getProductNum()*shoppingCartList.get(i).getProductPrice();
                        }
                        shoppingCartList.get(i).setChecked(true);

                    }
                    tvAllMoney.setText("￥:"+totalPrice);
                    tvCount.setText("结算("+shoppingCartList.size()+")");
                    cbProductName.setChecked(true);
                }else {
                    for(int i = 0;i<shoppingCartList.size();i++){
                        shoppingCartList.get(i).setChecked(false);
                        totalPrice = totalPrice - shoppingCartList.get(i).getProductNum()*shoppingCartList.get(i).getProductPrice();
                    }
                    tvAllMoney.setText("￥:"+0.0);
                    tvCount.setText("结算("+0+")");
                    cbProductName.setChecked(false);
                }
                shoppingCartAdapter.notifyDataSetChanged();


                break;
            case R.id.tv_count:
               startActivity(new Intent(ShoppingCartActivity.this, PaymentActivity.class));

                break;
        }
    }

    public class CheckBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            tvAllMoney.setText(String.format("￥:%.1f", intent.getDoubleExtra("totalPrice", 0.0)));
            CountNum = 0;
            for (int i = 0; i < shoppingCartList.size(); i++) {
                if (shoppingCartList.get(i).isChecked()) {
                    CountNum++;
                }
            }

            if (CountNum == shoppingCartList.size()) {
                isChooseAll = true;
            } else {
                isChooseAll = false;
            }
            cbChooseAll.setChecked(isChooseAll);

            if (CountNum > 0) {
                isChoose = true;
            } else {
                isChoose = false;
            }
            cbProductName.setChecked(isChoose);

            tvCount.setText("结算(" + CountNum + ")");

        }
    }


}
