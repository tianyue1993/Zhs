package com.zhs.zhs.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhs.zhs.R;
import com.zhs.zhs.activity.ProductDetailActivity;
import com.zhs.zhs.activity.ShoppingCartActivity;
import com.zhs.zhs.adapter.ProductAdapter;
import com.zhs.zhs.entity.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by tianyue on 2017/4/8.
 */
public class MallFragment extends BaseFragment implements OnBannerListener{


    Context mContext;
    ListView listView;
    ProductAdapter productAdapter;

    @Bind(R.id.my_mall)
    ImageView myMall;
    @Bind(R.id.banner_ad)
    Banner banner;

    private ArrayList<Product> productAdapterList = new ArrayList<>();

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mall;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        getList();
        listView = (ListView) rootView.findViewById(R.id.listview);
        productAdapter = new ProductAdapter(mContext, productAdapterList);
        listView.setAdapter(productAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(mContext, ProductDetailActivity.class));
            }
        });

        List images = new ArrayList<>();
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
        return rootView;

    }

    public void getList() {
        for (int j = 0; j < 10; j++) {
            Product item2 = new Product();
            productAdapterList.add(item2);
        }
    }

    @OnClick(R.id.my_mall)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), ShoppingCartActivity.class));
    }


    @Override
    public void OnBannerClick(int position) {
        Toast.makeText(mContext,"你点击了图片："+position,Toast.LENGTH_SHORT).show();
    }

    //图片加载
    private class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            //Glide 加载图片
            Glide.with(context).load(path).into(imageView);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        //开始轮播
        banner.startAutoPlay();
    }
    @Override
    public void onStop() {
        super.onStop();
        //结束轮播
        banner.stopAutoPlay();
    }
}
