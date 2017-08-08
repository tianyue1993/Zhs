package com.zhs.zhs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ListView;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.MyFamilyerAdapter;
import com.zhs.zhs.entity.Familyer;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.MemberListHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MyFamilyActivity extends Baseactivity {

    @Bind(R.id.listview)
    ListView listView;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout idSwipeLy;

    ArrayList<Familyer.MyFamilyer> adaptList = new ArrayList<>();
    MyFamilyerAdapter adapter;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("member_refresh")) {
                getFamily();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_family);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.my_family), null);
        IntentFilter filter = new IntentFilter();
        filter.addAction("member_refresh");
        registerReceiver(receiver, filter);
        if (prefs.getHouseHolder()) {
            setRightImage(R.mipmap.ic_add_scene, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("rivise", "2");
                    intent.setClass(mContext, AddFamify.class);
                    startActivity(intent);
                }
            });
        } else {
            setRightImage(0, null);
        }


        getFamily();
        //设置卷内的颜色
        idSwipeLy.setColorSchemeResources(R.color.base_color);
        idSwipeLy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adaptList.clear();
                getFamily();
            }
        });
    }

    /**
     * 请求服务器获取家人信息
     */
    public void getFamily() {
        showProgress(0, true);
        client.getMemberList(mContext, new MemberListHandler() {
            @Override
            public void onSuccess(Familyer familyer) {
                super.onSuccess(familyer);
                cancelmDialog();
                adaptList = familyer.data;
                adapter = new MyFamilyerAdapter(mContext, familyer.data);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
        idSwipeLy.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

//    public void deleteMember(final Familyer.MyFamilyer familyer) {
//        showProgress(0, true);
//        client.getMemberRemove(this, familyer.memberId + "", new CommentHandler() {
//            @Override
//            public void onCancel() {
//                super.onCancel();
//                cancelmDialog();
//            }
//
//            @Override
//            public void onSuccess(Commen commen) {
//                super.onSuccess(commen);
//                cancelmDialog();
//                showToast(commen.msg);
//                adaptList.remove(familyer);
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onFailure(BaseException exception) {
//                super.onFailure(exception);
//                cancelmDialog();
//            }
//        });
//
//    }
}
