package com.zhs.zhs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.RemindTypeAdapter;
import com.zhs.zhs.entity.NewsSettingData;
import com.zhs.zhs.entity.SettingList;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.SettingListHandler;
import com.zhs.zhs.utils.GlobalPrefrence;
import com.zhs.zhs.views.CustomListView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SelectRemindTypeActivity extends Baseactivity {

    RemindTypeAdapter adapter;
    ArrayList<NewsSettingData> adaptList = new ArrayList<>();
    public ArrayList<SettingList.Template> templates;
    @Bind(R.id.listview)
    CustomListView listview;
    @Bind(R.id.empty)
    TextView empty;

    public int buttonPosition;


    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(GlobalPrefrence.LISTCHANGE)) {
                getList();
                ArrayList<Integer> afterCheckList = (ArrayList<Integer>) intent.getExtras().getSerializable(GlobalPrefrence.LISTCHANGE);
                adaptList.get(buttonPosition).remindType = afterCheckList;
                adapter.notifyDataSetChanged();
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_remind_type);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.news_warning), null);
        IntentFilter filter = new IntentFilter();
        filter.addAction(GlobalPrefrence.LISTCHANGE);
        registerReceiver(receiver, filter);
        getList();
        listview.setDividerHeight(4);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                buttonPosition = position;
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", adapter.getItem(position));
                bundle.putSerializable("templates", templates);
                intent.putExtras(bundle);
                intent.setClass(mContext, SecondRemindTypeActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getList() {
        showProgress(0, true);
        client.getSettingList(mContext, new SettingListHandler() {
            @Override
            public void onSuccess(SettingList list) {
                super.onSuccess(list);
                cancelmDialog();
                if (list.data != null && list.data.list.size() > 0) {
                    adaptList = list.data.list;
                    adapter = new RemindTypeAdapter(mContext, adaptList);
                    templates = list.data.template;
                    listview.setAdapter(adapter);
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }

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
