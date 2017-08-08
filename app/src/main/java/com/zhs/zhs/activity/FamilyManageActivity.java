package com.zhs.zhs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.FamilyManageAdapter;
import com.zhs.zhs.entity.CustomData;
import com.zhs.zhs.entity.UserFamily;
import com.zhs.zhs.handler.CustomListHandler;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FamilyManageActivity extends Baseactivity {

    @Bind(R.id.listview)
    ListView listview;
    FamilyManageAdapter adapter;
    ArrayList<UserFamily> adaptList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_manage);
        ButterKnife.bind(this);
        setTitleTextView("家庭管理", null);
        setRightImage(R.mipmap.ic_add_scene, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddUserFamify.class);
                intent.putExtra("rivise", "2");
                mContext.startActivity(intent);
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
        client.getCustomerList(mContext, new CustomListHandler() {
            @Override
            public void onSuccess(CustomData userFamily) {
                super.onSuccess(userFamily);
                cancelmDialog();
                adaptList = userFamily.data;
                adapter = new FamilyManageAdapter(mContext, adaptList);
                listview.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                super.onFailure();
                cancelmDialog();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }
        });

    }
}
