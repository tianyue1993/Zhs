package com.zhs.zhs.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.adapter.ZhsBaseAdapter;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.NewsSettingData;
import com.zhs.zhs.entity.SettingList;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.utils.GlobalPrefrence;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class SecondRemindTypeActivity extends Baseactivity {

    public SecondRemindAdapter adapter;
    public NewsSettingData settingData;
    public ArrayList<SettingList.Template> templates;

    @Bind(R.id.listview)
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_remind_type);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            settingData = (NewsSettingData) bundle.getSerializable("list");
            templates = (ArrayList<SettingList.Template>) bundle.getSerializable("templates");
            checkList = settingData.remindType;
            adapter = new SecondRemindAdapter(mContext, templates);
            listview.setAdapter(adapter);
        }
        if (settingData != null) {
            setTitleTextView(settingData.msgName, null);
        } else {
            setTitleTextView("消息设置", null);
        }

        setLeftTextView(R.mipmap.ic_back, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkList.size() == 0 && afterCheckList.size() > 0) {
                    add();
                } else if (checkList.size() > 0 && afterCheckList.size() == 0) {
                    remove();
                } else if (checkList.size() > 0 && afterCheckList.size() > 0) {
                    edit();
                }
                finish();
            }
        });

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (checkList.size() == 0 && afterCheckList.size() > 0) {
            add();
        } else if (checkList.size() > 0 && afterCheckList.size() == 0) {
            remove();
        } else if (checkList.size() > 0 && afterCheckList.size() > 0) {
            edit();
        }

        finish();

    }

    public ArrayList<Integer> checkList = new ArrayList<>();
    public ArrayList<Integer> afterCheckList = new ArrayList<>();

    public class SecondRemindAdapter extends ZhsBaseAdapter<SettingList.Template> {
        public NewsSettingData newsSettingData;

        public SecondRemindAdapter(Context context, ArrayList<SettingList.Template> List) {
            super(context);
            this.mList = List;
            for (int i = 0; i < checkList.size(); i++) {
                afterCheckList.add(checkList.get(i));
            }
            newsSettingData = settingData;

            Log.d("Reqeust:", "checkList" + checkList.toString());

        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            final SettingList.Template mInfo = mList.get(position);
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_remind_horizontal, null);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
                holder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (mInfo != null) {
                holder.name.setText(mInfo.name);
                for (int i = 0; i < checkList.size(); i++) {
                    if (checkList.get(i) == position) {
                        holder.checkBox.setChecked(true);
                    }
                }
            }

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        afterCheckList.add(position);
                    } else {
                        if (null != afterCheckList && afterCheckList.size() > 0) {
                            Iterator it = afterCheckList.iterator();
                            while (it.hasNext()) {
                                int stu = (int) it.next();
                                if (stu == position) {
                                    it.remove(); //移除该对象
                                }
                            }
                        }

                    }
                    Log.d("Reqeust:", "afterCheckList" + afterCheckList.toString());
                }
            });

            return convertView;
        }


        private class ViewHolder {
            CheckBox checkBox;
            TextView name;
        }

    }

    public void remove() {

        Log.d(TAG, "edit: ------remove" + settingData.msgSetId);
        client.getSettingRemove(mContext, settingData.msgSetId, new CommentHandler() {
            @Override
            public void onSuccess(Commen commen) {
                super.onSuccess(commen);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GlobalPrefrence.LISTCHANGE, afterCheckList);
                intent.putExtras(bundle);
                intent.setAction(GlobalPrefrence.LISTCHANGE);
                sendBroadcast(intent);
            }
        });
    }

    public void edit() {
        JSONObject object = new JSONObject();
        object.put("msgSetId", settingData.msgSetId);

        if (settingData.msgSetId.equals("0")) {
            Log.d(TAG, "edit: ------" + settingData.msgSetId);
        }

        StringBuilder remindType = new StringBuilder();
        for (int i = 0; i < afterCheckList.size(); i++) {
            remindType.append(afterCheckList.get(i)).append(",");
        }
        object.put("remindType", remindType.toString().substring(0, remindType.length() - 1));
        Log.d("Reqeust:", "remindType" + remindType);
        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            showProgress(0, true);
            client.getSettingEdit(this, entity, new CommentHandler() {
                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GlobalPrefrence.LISTCHANGE, afterCheckList);
                    intent.putExtras(bundle);
                    intent.setAction(GlobalPrefrence.LISTCHANGE);
                    sendBroadcast(intent);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    public void add() {
        Log.d(TAG, "edit: ------add" + settingData.msgId);
        JSONObject object = new JSONObject();
        object.put("msgId", settingData.msgId);
        StringBuilder remindType = new StringBuilder();
        for (int i = 0; i < afterCheckList.size(); i++) {
            remindType.append(afterCheckList.get(i)).append(",");
        }
        object.put("remindType", remindType.toString().substring(0, remindType.length() - 1));
        Log.d("Reqeust:", "remindType" + remindType);

        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            showProgress(0, true);
            client.getSettingAdd(this, entity, new CommentHandler() {
                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(GlobalPrefrence.LISTCHANGE, afterCheckList);
                    intent.putExtras(bundle);
                    intent.setAction(GlobalPrefrence.LISTCHANGE);
                    sendBroadcast(intent);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


}
