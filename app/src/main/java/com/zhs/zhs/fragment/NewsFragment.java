package com.zhs.zhs.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.WarnNewsAdapter;
import com.zhs.zhs.entity.News;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.MsgListHandler;
import com.zhs.zhs.utils.ClassUtils;
import com.zhs.zhs.views.DownPullRefreshListView;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhs.zhs.ZhsApplication.getActivityData;

/**
 * Created by tianyue on 2017/3/29.
 */
public class NewsFragment extends BaseFragment {
    /**
     * onCreateView
     * @param view
     * @param savedInstanceState
     */


    Context mContext;
    @Bind(R.id.more_news)
    ImageView moreNews;
    @Bind(R.id.listview)
    DownPullRefreshListView listview;
    private ArrayList<News.NewsData> adaptList = new ArrayList<>();
    protected ArrayList<News.NewsData> list = new ArrayList<>();
    WarnNewsAdapter adapter;

    PopupMenu popupMenu;
    Menu menu;

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_news;
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
        popupMenu = new PopupMenu(mContext, rootView.findViewById(R.id.more_news));
        menu = popupMenu.getMenu();
        // 通过代码添加菜单项  
        menu.add(Menu.NONE, Menu.FIRST + 0, 0, "报警信息");
        menu.add(Menu.NONE, Menu.FIRST + 1, 1, "订阅信息");
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        showToast("报警信息");
                        break;
                    case 2:
                        showToast("订阅信息");
                        break;
                }
                return false;
            }
        });
        adapter = new WarnNewsAdapter(mContext, adaptList);
        listview.setAdapter(adapter);
        initData();
        return rootView;
    }

    @OnClick({R.id.more_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more_news:
                popupMenu.show();
                break;

        }
    }


    public void getList() {
        cancelmDialog();
        showProgress(0, true);
        client.getMsgList(mContext, page_number++ + "", new MsgListHandler() {
            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
                listview.onRefreshComplete();
                loadStatus = false;
                loadingProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(News news) {
                super.onSuccess(news);
                cancelmDialog();
                list = news.data;
                if (list.size() > 0) {
                    if (listview.getFooterViewsCount() == 0) {
                        listview.addFooterView(footer);
                        listview.setAdapter(adapter);
                    }
                    for (Iterator<News.NewsData> iterator = list.iterator(); iterator.hasNext(); ) {
                        News.NewsData disscussCommentItems = iterator.next();
                        adaptList.add(disscussCommentItems);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("已无更多内容");
                    if (listview.getFooterViewsCount() > 0) {
                        listview.removeFooterView(footer);
                    }

                }
                loadStatus = false;
                listview.onRefreshComplete();
                loadingProgressBar.setVisibility(View.GONE);
                loadingText.setText(getResources().getString(R.string.loadmore));
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }
        });
    }

    public void initData() {
        getList();
        adapter = new WarnNewsAdapter(mContext, adaptList);
        listview.setAdapter(adapter);
        //点击角布局加载更多
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loadStatus && list.size() != 0) {
                    getList();
                }
            }
        });

        //上拉listview加载更多监听
        loadMoreListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    if (loadMore && !loadStatus) {
                        getList();
                    }
                    if (list.size() == 0) {
                        listview.removeFooterView(footer);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                listview.setFirstItemIndex(firstVisibleItem);
                if (firstVisibleItem != 1 && list.size() != 0) {
                    loadMore = firstVisibleItem + visibleItemCount == totalItemCount;
                } else {
                    loadMore = false;
                }
            }
        };
        listview.setOnScrollListener(loadMoreListener);
        listview.setOnRefreshListener(new DownPullRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (listview.getFooterViewsCount() > 0) {
                    listview.removeFooterView(footer);
                }
                page_number = 0;
                adaptList.clear();
                getList();
            }
        });
        //条目点击进入webview详情
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News.NewsData recommendItems = adapter.getItem(position - 1);
                Intent intent = new Intent();
                intent.putExtra("type", "3");
                intent.putExtra("ClientId", recommendItems.clientId);
                if (getActivityData().containsKey(adapter.getItem(position - 1).clientType)) {
                    intent.setClass(mContext, ClassUtils.getClass(getActivityData().get(adapter.getItem(position - 1).clientType)));
                    startActivity(intent);
                } else {
                    showToast("敬请期待！");
                }

            }
        });

    }
}
