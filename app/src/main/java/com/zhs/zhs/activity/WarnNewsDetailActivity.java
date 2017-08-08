package com.zhs.zhs.activity;

import android.os.Bundle;

import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.News;
import com.zhs.zhs.handler.CommentHandler;

public class WarnNewsDetailActivity extends Baseactivity {


    News.NewsData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_news2);
        setTitleTextView("家庭报警信息", null);
        Bundle bundle = getIntent().getExtras();
        data = (News.NewsData) bundle.getSerializable("info");
        if (!data.checked) {
            isRead(data._id);
        }

    }

    public void isRead(String id) {
        client.getIsRead(mContext, id, new CommentHandler() {
            @Override
            public void onSuccess(Commen commen) {
                super.onSuccess(commen);
            }
        });
    }
}
