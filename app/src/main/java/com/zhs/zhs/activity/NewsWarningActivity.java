package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.ApiClient;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.NewsSettingData;
import com.zhs.zhs.entity.SettingList;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.SettingListHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewsWarningActivity extends Baseactivity {

    @Bind(R.id.second_reminder)
    RelativeLayout secondReminder;
    @Bind(R.id.reminder_type)
    RelativeLayout reminderType;
    @Bind(R.id.start)
    TextView start;
    @Bind(R.id.end)
    TextView end;
    @Bind(R.id.mode_text)
    TextView modeText;
    ArrayList<NewsSettingData> adaptList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_warning);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.news_warning), null);
        getList();
    }

    @OnClick({R.id.second_reminder, R.id.reminder_type, R.id.commit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.second_reminder:
                break;
            case R.id.reminder_type:
                if (adaptList != null && adaptList.size() > 0) {
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("settinglist", adaptList);
                    intent.putExtras(bundle);
                    intent.setClass(mContext, SelectRemindTypeActivity.class);
                    startActivity(intent);
                } else {
                    showToast("当前用户暂无可设置类型");
                }

                break;
            case R.id.commit:
                if (adaptList != null) {
                    finish();
                }
                break;
        }
    }

    public void getList() {
        ApiClient.getInstance().getSettingList(mContext, new SettingListHandler() {
            @Override
            public void onSuccess(SettingList list) {
                super.onSuccess(list);
                adaptList = list.data.list;
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
            }
        });
    }
}
