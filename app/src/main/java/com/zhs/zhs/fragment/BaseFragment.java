package com.zhs.zhs.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.zhs.ApiClient;
import com.zhs.zhs.R;
import com.zhs.zhs.utils.GlobalSetting;
import com.zhs.zhs.views.ProgressDialog;


/**
 * ty
 * 主页面基类封装
 */
public abstract class BaseFragment extends Fragment {
    //推送
    public static boolean isActive = false;
    //SP 存储更新
    protected GlobalSetting prefs;
    //网络请求
    protected ApiClient client;
    //吐司
    protected Toast toast = null;

    //onCreate方法 里面执行各种初始化及调用
    protected abstract void initView(View view, Bundle savedInstanceState);

    //获取布局文件ID
    protected abstract int getLayoutId();

    //获取上下文对象
    protected Context getBaseActivity() {
        return getActivity();
    }


    //下来加载相关布局
    public int page_size = 10;//list请求每次条数
    public int page_number = 1;//分页加载，请求第几页
    public AbsListView.OnScrollListener loadMoreListener;//加载的回调监听
    public boolean loadMore;//list是否拉到最低端，需要重新加载下一页
    public boolean loadStatus = false;//加载状态，是否正在加载
    public View footer;//list正在加载的底部布局
    public ProgressBar loadingProgressBar;
    public TextView loadingText;
    public static final int MSG_CLOSE_PROGRESS = 1;
    public static final int MSG_SHOW_TOAST = 2;


    public Handler baseHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MSG_CLOSE_PROGRESS:
                    // cancelProgress(); //加载进度条
                    break;
                case MSG_SHOW_TOAST:
                    int resid = msg.arg1;
                    if (resid > 0) {
                        showToast(resid);
                    } else if (msg.obj != null) {
                        String mes = (String) msg.obj;
                        if (!TextUtils.isEmpty(mes)) {
                            showToast(mes);
                        }
                    }
                    break;
            }
        }

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        prefs = GlobalSetting.getInstance(getBaseActivity());
        client = ApiClient.getInstance();
        /**
         * 下拉列表相关布局变量
         */
        footer = View.inflate(getActivity(), R.layout.loadmore, null);
        loadingProgressBar = (ProgressBar) footer.findViewById(R.id.progressBar);
        loadingText = (TextView) footer.findViewById(R.id.title);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onStart() {
        isActive = true;
        super.onStart();
    }

    @Override
    public void onStop() {
        isActive = false;
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * 吐司提示，防止多个提示同时弹出时的崩溃
     *
     * @param message
     */
    public void showToast(String message) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(getActivity(), (!TextUtils.isEmpty(message)) ? message
                : this.getString(R.string.network_error), Toast.LENGTH_SHORT);
        toast.show();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 2000);
    }


    public void showToast(int resid) {
        if (toast != null) {
            return;
        }
        toast = Toast.makeText(getActivity(), resid, Toast.LENGTH_SHORT);
        toast.show();
        baseHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (toast != null) {
                    toast.cancel();
                    toast = null;
                }
            }
        }, 2000);
    }


    /**
     * 基类，网络请求进度弹框提示
     */
    public ProgressDialog mProgress;

    public void showProgress(int resId, boolean cancel) {
        mProgress = new ProgressDialog(getActivity());
        if (resId <= 0) {
            mProgress.setMessage(R.string.loading_data, cancel);
        } else {
            mProgress.setMessage(resId, cancel);
        }
        mProgress.show();
    }

    public void cancelmDialog() {
        if (mProgress != null && mProgress.isShowing()) {
            mProgress.dismiss();
        }
    }
}
