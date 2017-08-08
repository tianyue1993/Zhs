package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.WarnNewsAdapter;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.News;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.handler.MsgListHandler;
import com.zhs.zhs.utils.ClassUtils;
import com.zhs.zhs.views.DownPullRefreshListView;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.zhs.zhs.ZhsApplication.getActivityData;

public class WarnNewsListActivity extends Baseactivity {

    @Bind(R.id.warn_news)
    DownPullRefreshListView listview;
    private ArrayList<News.NewsData> adaptList = new ArrayList<>();
    protected ArrayList<News.NewsData> list = new ArrayList<>();
    WarnNewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_news);
        ButterKnife.bind(this);
        setTitleTextView("家庭报警信息", null);
        setRightText("标为已读", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress(0, true);
                client.getAllRead(mContext, new CommentHandler() {
                    @Override
                    public void onSuccess(Commen commen) {
                        super.onSuccess(commen);
                        cancelmDialog();
                    }

                    @Override
                    public void onFailure(BaseException exception) {
                        super.onFailure(exception);
                        cancelmDialog();
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                        cancelmDialog();
                    }
                });
            }
        });
        initData();
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

}
