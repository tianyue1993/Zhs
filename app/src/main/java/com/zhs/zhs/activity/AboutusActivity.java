package com.zhs.zhs.activity;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.VersionCheck;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.VersionHandler;
import com.zhs.zhs.utils.UpdateCheckUtils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 关于我们
 */

public class AboutusActivity extends Baseactivity {

    @Bind(R.id.logo)
    ImageView logo;
    @Bind(R.id.detail)
    TextView detail;
    @Bind(R.id.link)
    TextView link;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.news)
    RelativeLayout news;
    @Bind(R.id.check)
    Button check;
    @Bind(R.id.version_name)
    TextView versionName;


    private BroadcastReceiver receiver;
    private Dialog noticeDialog;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.about_us), null);
        versionName.setText(getApplicationName() + " V" + getVersion());
    }


    @OnClick(R.id.check)
    public void onViewClicked() {
        versionCheck();
    }

    public void versionCheck() {
        cancelmDialog();
        showProgress(0, true);
        JSONObject params = new JSONObject();
        params.put("versionType", "0");
        params.put("version", getVersion());
        try {
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            showProgress(0, true);
            client.getUpdateVersion(this, entity, new VersionHandler() {

                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }

                @Override
                public void onSuccess(VersionCheck check) {
                    super.onSuccess(check);
                    cancelmDialog();
                    if (check.data.updateState) {
                        UpdateCheckUtils.getInstanse().lookVersion(mContext, true, check);
                    } else {
                        showToast(R.string.alreadyLastest);
                    }
                }

                @Override
                public void onFailure(BaseException exception) {
                    super.onFailure(exception);
                    cancelmDialog();
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }


    public class DownLoadBroadcastReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            SharedPreferences sPreferences = context.getSharedPreferences("downloadapp", 0);
            long refernece = sPreferences.getLong("plato", 0);
            if (refernece == myDwonloadID) {
                String serviceString = Context.DOWNLOAD_SERVICE;
                DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
                Intent install = new Intent(Intent.ACTION_VIEW);
                Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }

    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = getApplicationContext().getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }
        String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

}
