package com.zhs.zhs.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.zhs.zhs.R;
import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.views.DialogFactory;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonnalSettingActivity extends Baseactivity {

    @Bind(R.id.reset_pswd)
    RelativeLayout resetPswd;
    @Bind(R.id.news_warning)
    RelativeLayout newsWarning;
    @Bind(R.id.personnal_setting)
    RelativeLayout personnalSetting;
    @Bind(R.id.exist)
    RelativeLayout exist;
    private Dialog delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnal_setting);
        ButterKnife.bind(this);
        setTitleTextView("基本设置", null);
    }

    @OnClick({R.id.reset_pswd, R.id.news_warning, R.id.personnal_setting, R.id.exist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reset_pswd:
                startActivity(new Intent(mContext, ResetPswdActivity.class));
                break;
            case R.id.news_warning:
                startActivity(new Intent(mContext, SelectRemindTypeActivity.class));
                break;
            case R.id.personnal_setting:
                startActivity(new Intent(mContext, PersonNewsSetActivity.class));
                break;
            case R.id.exist:

                delete = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要退出登录？", "取消", "确定", new View.OnClickListener() {
                    @SuppressWarnings("unused")
                    @Override
                    public void onClick(View v) {
                        delete.dismiss();
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (delete != null && delete.isShowing()) {
                            delete.dismiss();
                            exist();
                        }
                    }
                });
                break;
        }
    }

    private void exist() {
        cancelmDialog();
        showProgress(0, true);
        client.invokeExit(mContext, new CommentHandler() {
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
                if (commen.code == 1) {
                    ZhsApplication.finishActivity();
                    prefs.clear();
                    ZhsApplication.unbindUser();
                    startActivity(new Intent(mContext, LoginActivity.class));
                }

            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }
}
