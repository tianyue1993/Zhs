package com.zhs.zhs.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.zhs.zhs.R;
import com.zhs.zhs.adapter.DeviceAdapter;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.MyDevice;
import com.zhs.zhs.entity.device.Device;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.handler.DeviceListHandler;
import com.zhs.zhs.utils.ClassUtils;
import com.zhs.zhs.views.DialogFactory;
import com.zhs.zhs.zxing.activity.CaptureActivity;

import java.util.ArrayList;

import butterknife.ButterKnife;

import static com.zhs.zhs.ZhsApplication.getActivityData;
import static com.zhs.zhs.utils.GlobalPrefrence.DEVICEREFRESH;

/**
 * Created by tianyue on 2017/3/30.
 */
public class DeviceFragment extends BaseFragment {
    /**
     * onCreateView
     *
     * @param view
     * @param savedInstanceState
     */


    Context mContext;
    GridView device_grid;
    ImageView add;
    DeviceAdapter adapter;
    RelativeLayout empty;
    SwipeRefreshLayout mSwipeLayout;
    private ArrayList<Device> adaptList = new ArrayList<>();
    private Dialog delete;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DEVICEREFRESH)) {
                getLists();
            }

        }
    };

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DEVICEREFRESH);
        mContext.registerReceiver(receiver, filter);
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            //开启权限访问对话框
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 10003);
        }

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (receiver != null) {
            mContext.unregisterReceiver(receiver);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        add = (ImageView) rootView.findViewById(R.id.add_device);
        device_grid = (GridView) rootView.findViewById(R.id.device_grid);
        empty = (RelativeLayout) rootView.findViewById(R.id.empty);
        mSwipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.id_swipe_ly);
        //设置卷内的颜色
        mSwipeLayout.setColorSchemeResources(R.color.base_color);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page_number = 1;
                adaptList.clear();
                getLists();
            }
        });

        if (prefs.getHouseHolder()) {
            add.setVisibility(View.VISIBLE);
        } else {
            add.setVisibility(View.GONE);
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, CaptureActivity.class));
            }
        });
        device_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("type", "2");
                Bundle bundle = new Bundle();
                bundle.putSerializable("device", adapter.getItem(position));
                intent.putExtras(bundle);
                if (getActivityData().containsKey(adapter.getItem(position).productType)) {
                    intent.setClass(mContext, ClassUtils.getClass(getActivityData().get(adapter.getItem(position).productType)));
                    startActivity(intent);
                } else {
                    showToast("敬请期待！");
                }

            }
        });

        /**
         * 长按删除设备
         */
        if (prefs.getHouseHolder()) {

            device_grid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    delete = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要删除此设备？", "取消", "确定", new View.OnClickListener() {
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
                                getDelete(adapter.getItem(position).ClientId);
                            }
                        }
                    });

                    return true;
                }
            });
        }

        getLists();
        return rootView;
    }


    public void getLists() {
        cancelmDialog();
        showProgress(0, true);
        client.getImpowerList(mContext, new DeviceListHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(MyDevice myDevice) {
                super.onSuccess(myDevice);
                adaptList = myDevice.data;
                adapter = new DeviceAdapter(mContext, adaptList);
                device_grid.setAdapter(adapter);
                if (adaptList.size() == 0) {
                    empty.setVisibility(View.VISIBLE);
                } else {
                    empty.setVisibility(View.GONE);
                }
                cancelmDialog();
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
                empty.setVisibility(View.VISIBLE);
            }
        });
        mSwipeLayout.setRefreshing(false);
    }


    public void getDelete(String id) {
        showProgress(0, true);
        client.getDeleteDevice(mContext, id, new CommentHandler() {
            @Override
            public void onSuccess(Commen commen) {
                super.onSuccess(commen);
                showToast("删除成功");
                getLists();
                cancelmDialog();
            }

            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();

            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }

}
