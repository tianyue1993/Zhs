package com.zhs.zhs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhs.zhs.activity.ShoppingCartActivity.isChooseAll;
import static com.zhs.zhs.activity.ShoppingCartActivity.shoppingCartList;

public class ProductDetailActivity extends Baseactivity implements OnBannerListener{

    ShoppingCart shoppingCart;
    int i = 1;

    @Bind(R.id.my_mall)
    ImageView mymall;
    @Bind(R.id.buy_now)
    Button buyNow;
    @Bind(R.id.add_to_shopping_cart)
    Button addToShoppingCart;
    @Bind(R.id.banner_product)
    Banner banner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.produce_setting), null);

        List images= new ArrayList<>();
        images.add(R.mipmap.a);
        images.add(R.mipmap.b);
        images.add(R.mipmap.c);


        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置监听器
        banner.setOnBannerListener(this);

        //banner设置方法全部调用完毕时最后调用
        banner.start();


    }

    @OnClick({R.id.my_mall, R.id.buy_now, R.id.add_to_shopping_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.my_mall:
                startActivity(new Intent(this, ShoppingCartActivity.class));
                break;
            case R.id.buy_now:
                shoppingCart = new ShoppingCart("id" + i, "商品名称" + i, R.mipmap.ic_logo, 100.0, "型号 规格", i, false, false);
                i++;
                shoppingCartList.add(shoppingCart);
                startActivity(new Intent(this, ShoppingCartActivity.class));
                isChooseAll = false;
                break;
            case R.id.add_to_shopping_cart:
                shoppingCart = new ShoppingCart("id" + i, "商品名称" + i, R.mipmap.ic_logo, 100.0, "型号 规格", i, false, false);
                i++;
                shoppingCartList.add(shoppingCart);
                Toast.makeText(this, "已添加到购物车", Toast.LENGTH_LONG).show();
                isChooseAll = false;
                break;
        }
    }


    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(getApplicationContext(),"你点击了图片："+position,Toast.LENGTH_SHORT).show();
    }

    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片
            Glide.with(context).load(path).into(imageView);
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
}
