package com.zhs.zhs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;

import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.MenuItem;
import com.yydcdut.sdlv.SlideAndDragListView;
import com.zhs.zhs.R;
import com.zhs.zhs.adapter.MoreSceneAdapter;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.MoreScene;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.handler.SceneHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoreSceneActivity extends Baseactivity {

    @Bind(R.id.listview)
    SlideAndDragListView listView;
    @Bind(R.id.id_swipe_ly)
    SwipeRefreshLayout idSwipeLy;
    private MoreSceneAdapter adapter;
    private ArrayList<MoreScene.Scene> adaptList = new ArrayList<>();
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("updatestate")) {
                adaptList.clear();
                getList();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_scene);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.more_scene), null);
        IntentFilter filter = new IntentFilter();
        filter.addAction("updatestate");
        registerReceiver(receiver, filter);
        Menu menu = new Menu(new ColorDrawable(Color.WHITE), true, 0);// the
        menu.addItem(new MenuItem.Builder().setWidth(200)// set Width
                .setBackground(new ColorDrawable(Color.RED))// set background
                .setText("删除")// set text string
                .setDirection(MenuItem.DIRECTION_RIGHT).setTextColor(Color.WHITE)// set
                .setTextSize(20)// set text size
                .build());
        listView.setMenu(menu);
        listView.setDividerHeight(10);

        listView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
            @Override
            public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
                Log.i(tag, "onMenuItemClick itemPosition:" + itemPosition + " buttonPosition: " + buttonPosition + " direction: " + direction);
                switch (direction) {
                    case MenuItem.DIRECTION_LEFT:
                        switch (buttonPosition) {
                            case 0:// One
                                return Menu.ITEM_SCROLL_BACK;
                        }
                        break;
                    case MenuItem.DIRECTION_RIGHT:
                        switch (buttonPosition) {
                            case 0:// icon
                                delet(adapter.getItem(itemPosition));
                                return Menu.ITEM_NOTHING;
                        }
                        break;
                    default:
                        return Menu.ITEM_NOTHING;
                }
                return Menu.ITEM_NOTHING;
            }
        });


        //设置卷内的颜色
        idSwipeLy.setColorSchemeResources(R.color.base_color);
        idSwipeLy.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adaptList.clear();
                getList();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        getList();
    }

    public void getList() {
        showProgress(0, true);
        client.getSceneList(mContext, new SceneHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(MoreScene scene) {
                super.onSuccess(scene);
                cancelmDialog();
                if (scene.data.size() > 0) {
                    adaptList = scene.data;
                    adapter = new MoreSceneAdapter(mContext, adaptList);
                    listView.setAdapter(adapter);
                } else {
                    showToast("您暂时没有创建场景");
                }

            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
        idSwipeLy.setRefreshing(false);
    }

    public void delet(final MoreScene.Scene scene) {
        cancelmDialog();
        showProgress(0, true);
        client.getSceneDelet(mContext, scene.sceneId, new CommentHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(Commen commen) {
                super.onSuccess(commen);
                cancelmDialog();
                showToast(commen.msg);
                adaptList.remove(scene);
                adapter.notifyDataSetChanged();
                sendBroadcast(new Intent("homerefresh"));
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }
}
