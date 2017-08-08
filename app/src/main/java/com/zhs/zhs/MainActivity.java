package com.zhs.zhs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.entity.VersionCheck;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.fragment.DeviceFragment;
import com.zhs.zhs.fragment.FamilyFragment;
import com.zhs.zhs.fragment.MallFragment;
import com.zhs.zhs.fragment.MineFragment;
import com.zhs.zhs.fragment.NewsFragment;
import com.zhs.zhs.handler.VersionHandler;
import com.zhs.zhs.mpush.Notifications;
import com.zhs.zhs.utils.GlobalSetting;
import com.zhs.zhs.utils.UpdateCheckUtils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

/**
 * 主页 首页页面切换
 */
public class MainActivity extends Activity implements View.OnClickListener {
    /**
     * 家
     */
    private FamilyFragment mFamilyFragment;
    /**
     * 信息
     */
    private NewsFragment mNewsFragment;


    /**
     * 数据
     */
    private DeviceFragment mDeviceFragment;

    /**
     * 商城
     */
    private MallFragment mMallFragment;
    /**
     * 我的
     */
    private MineFragment mMineFragment;

    private CheckBox mFamily;
    private CheckBox mNews;
    private CheckBox mDatas;
    private CheckBox mMall;
    private CheckBox mMine;

    /**
     * 当前选中的index
     */
    private int mPage = 0;
    /**
     * 点击退出时记录的时间
     */
    private long exitTime = 0;

    Context context;
    GlobalSetting pres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZhsApplication.addActivity(this);
        pres = GlobalSetting.getInstance(this);
        context = this;
        Notifications.I.init(this.getApplicationContext());
        Notifications.I.setSmallIcon(R.mipmap.ic_logo);
        Notifications.I.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_logo));
        ZhsApplication.startPush(pres.getMpushKey() + "-all", pres.getMpushTag());
        Log.d(TAG, "onCreate: pres.getMpushKey()" + pres.getMpushKey() + " pres.getMpushTag()" + pres.getMpushTag());

        //为空时初始化
        if (savedInstanceState == null) {
            initView();
        } else {
            mFamilyFragment = (FamilyFragment) getFragmentManager().findFragmentByTag("mFamilym");
            mNewsFragment = (NewsFragment) getFragmentManager().findFragmentByTag("mNews");
            mDeviceFragment = (DeviceFragment) getFragmentManager().findFragmentByTag("mDatas");
            mMineFragment = (MineFragment) getFragmentManager().findFragmentByTag("mMine ");
            mMallFragment = (MallFragment) getFragmentManager().findFragmentByTag("mMall");
            initView();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        versionCheck();
    }


    @Override
    protected void onResume() {
        super.onResume();

    }


    /**
     * 返回键监听事件
     */
    @Override
    public void onBackPressed() {
        exit();
    }

    /**
     * 初始化控件
     */
    protected void initView() {
        /**
         * 家
         */
        mFamily = (CheckBox) findViewById(R.id.main_bottom_rb_recomend);
        mFamily.setChecked(true);
        mFamily.setOnClickListener(this);

        /**
         * 信息
         */
        mNews = (CheckBox) findViewById(R.id.main_bottom_rb_game);
        mNews.setChecked(false);
        mNews.setOnClickListener(this);

        /**
         * 数据
         */
        mDatas = (CheckBox) findViewById(R.id.main_bottom_rb_bbs);
        mDatas.setChecked(false);
        mDatas.setOnClickListener(this);

        /**
         * 商城
         */
        mMall = (CheckBox) findViewById(R.id.main_bottom_rb_mall);
        mMall.setChecked(false);
        mMall.setOnClickListener(this);

        /**
         * 我的
         */
        mMine = (CheckBox) findViewById(R.id.main_bottom_rb_circle);
        mMine.setChecked(false);
        mMine.setOnClickListener(this);
        //为了应对修改密码等需求调到登录页面 下面的代码暂时注释
//        //控制第一次进入的页面
//        if (EtcommApplication.isFirstSetDefault) {
//            EtcommApplication.isFirstSetDefault = false;
//            //此代码在运行期间只执行一次
        selectFragment(0);//选择默认值
//        }
    }

    /**
     * Fragment切换控制
     *
     * @param i 切换到第几个Fragment
     */
    public void selectFragment(int i) {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragment(transaction);
        switch (i) {
            case 0:
                if (mFamilyFragment == null) {
                    mFamilyFragment = new FamilyFragment();
                    transaction.add(R.id.mian_fragment, mFamilyFragment, "mFamilym");
                } else {
                    /**
                     * 生命周期控制不合理
                     *
                     * add + show 与创建的先后顺序相关
                     */
                    transaction.show(mFamilyFragment);
                }
                mPage = 0;
                mFamily.setChecked(true);
                break;
            case 1:
                if (mNewsFragment == null) {
                    mNewsFragment = new NewsFragment();
                    transaction.add(R.id.mian_fragment, mNewsFragment, "mNews");
                } else {
                    transaction.show(mNewsFragment);
                }
                mPage = 1;
                mNews.setChecked(true);
                break;
            case 2:
                if (mDeviceFragment == null) {
                    mDeviceFragment = new DeviceFragment();
                    transaction.add(R.id.mian_fragment, mDeviceFragment, "mDatas");
                } else {
                    transaction.show(mDeviceFragment);
                }
                mPage = 2;
                mDatas.setChecked(true);
                break;


            case 3:
                if (mMallFragment == null) {
                    mMallFragment = new MallFragment();
                    transaction.add(R.id.mian_fragment, mMallFragment, "mMall");
                } else {
                    transaction.show(mMallFragment);
                }
                mPage = 3;
                mMall.setChecked(true);
                break;
            case 4:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    transaction.add(R.id.mian_fragment, mMineFragment, "mMine ");
                } else {
                    transaction.show(mMineFragment);
                }
                mPage = 4;
                mMine.setChecked(true);
                break;
        }
        transaction.commit();
    }


    /**
     * 隐藏Fragment
     *
     * @param transaction Fragment管理器
     */
    private void hideFragment(FragmentTransaction transaction) {
        mFamily.setChecked(false);
        mNews.setChecked(false);
        mDatas.setChecked(false);
        mMine.setChecked(false);
        mMall.setChecked(false);

        if (mFamilyFragment != null) {
            transaction.hide(mFamilyFragment);
        }
        if (mNewsFragment != null) {
            transaction.hide(mNewsFragment);
        }
        if (mDeviceFragment != null) {
            transaction.hide(mDeviceFragment);
        }
        if (mMineFragment != null) {
            transaction.hide(mMineFragment);
        }
        if (mMallFragment != null) {
            transaction.hide(mMallFragment);
        }
    }


    /**
     * 返回键退出控制
     */
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            ZhsApplication.finishActivity();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_bottom_rb_recomend://家
                selectFragment(0);
                break;
            case R.id.main_bottom_rb_game://信息
                selectFragment(1);
                break;
            case R.id.main_bottom_rb_bbs://数据
                selectFragment(2);
                break;
            case R.id.main_bottom_rb_mall://数据
                selectFragment(3);
                break;
            case R.id.main_bottom_rb_circle://我的
                selectFragment(4);
                break;
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /**
     * 版本更新检测
     */
    public void versionCheck() {
        JSONObject params = new JSONObject();
        params.put("versionType", "0");
        params.put("version", getVersion());
        try {
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            ApiClient.getInstance().getUpdateVersion(MainActivity.this, entity, new VersionHandler() {

                @Override
                public void onCancel() {
                    super.onCancel();
                }

                @Override
                public void onSuccess(VersionCheck check) {
                    super.onSuccess(check);
                    if (check.data != null) {
                        if (check.data.updateState) {
                            UpdateCheckUtils.getInstanse().lookVersion(MainActivity.this, true, check);
                        }
                    }

                }

                @Override
                public void onFailure(BaseException exception) {
                    super.onFailure(exception);
                }
            });
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
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
}
