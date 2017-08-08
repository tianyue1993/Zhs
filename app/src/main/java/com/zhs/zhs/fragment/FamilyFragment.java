package com.zhs.zhs.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.zhs.zhs.R;
import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.activity.AddSceneActivity;
import com.zhs.zhs.activity.MoreSceneActivity;
import com.zhs.zhs.activity.MyadressActivity;
import com.zhs.zhs.activity.WarnNewsListActivity;
import com.zhs.zhs.adapter.MyFamilyListAdapter;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.DefaultFamily;
import com.zhs.zhs.entity.device.Environment;
import com.zhs.zhs.entity.Family;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.handler.DefaultFamilyHandler;
import com.zhs.zhs.utils.GlobalPrefrence;
import com.zhs.zhs.utils.StringUtils;
import com.zhs.zhs.utils.ZhsUtils;
import com.zhs.zhs.views.CircleBar;
import com.zhs.zhs.views.DialogFactory;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;


/**
 * Created by tianyue on 2017/3/31.
 */
public class FamilyFragment extends BaseFragment {


    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.adress)
    TextView adress;
    @Bind(R.id.add_scene)
    ImageView addScene;
    @Bind(R.id.family)
    RelativeLayout family;
    @Bind(R.id.scene1)
    Button scene1;
    @Bind(R.id.scene2)
    Button scene2;
    @Bind(R.id.scene3)
    Button scene3;
    @Bind(R.id.more_scene)
    Button moreScene;
    @Bind(R.id.scene)
    LinearLayout scene;
    @Bind(R.id.humidity)
    TextView humidity;
    @Bind(R.id.ll_temperature)
    LinearLayout llTemperature;
    @Bind(R.id.temperature)
    TextView temperature;
    @Bind(R.id.ll_humidity)
    LinearLayout llHumidity;
    @Bind(R.id.pm)
    TextView pm;
    @Bind(R.id.hcho)
    TextView hcho;
    @Bind(R.id.ll_hcho)
    LinearLayout llHcho;
    @Bind(R.id.open_check)
    CheckBox openCheck;
    @Bind(R.id.anfang)
    LinearLayout anfang;
    @Bind(R.id.count)
    TextView count;
    @Bind(R.id.warning_text)
    TextView warningText;
    @Bind(R.id.ll_warn)
    LinearLayout llWarn;
    @Bind(R.id.room_warning)
    RelativeLayout roomWarning;
    @Bind(R.id.round_bar)
    CircleBar roundBar;
    Context mContext;
    boolean protectState;
    private Dialog delete;
    private PopupWindow popupWindow;
    private ListView lv_group;
    private View view;
    public ArrayList<Family.MyFamilys> arrayList = new ArrayList<>();
    public MyFamilyListAdapter groupAdapter;


    /**
     * 刷新页面信息
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("homerefresh")) {
                getDefaultFamily();
            } else if (intent.getAction().equals(GlobalPrefrence.DEVICEDATA)) {
                String string = intent.getStringExtra("message");
                Environment environment = JSON.parseObject(string.toString(), Environment.class);
                hcho.setText(environment.GasStrength + "");
                temperature.setText(environment.Temperature + "");
                humidity.setText(environment.Humidity + "");
                pm.setText(environment.PM + "");
            } else if (intent.getAction().equals(GlobalPrefrence.CHANGE_ENVIR_DATA)) {
                count.setText(prefs.getWarnCount() + "");
            }

        }
    };


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_family;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        IntentFilter filter = new IntentFilter();
        filter.addAction("homerefresh");
        filter.addAction(GlobalPrefrence.DEVICEDATA);
        filter.addAction(GlobalPrefrence.CHANGE_ENVIR_DATA);
        mContext.registerReceiver(receiver, filter);
        getDefaultFamily();
        getMsgCount();
        if (popupWindow == null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.group_list, null);
            lv_group = (ListView) view.findViewById(R.id.lvGroup);
            popupWindow = new PopupWindow(view, dm.widthPixels / 3 * 2, dm.heightPixels / 3 * 2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        addScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddSceneActivity.class);
                intent.putExtra("type", "1");
                mContext.startActivity(intent);
            }
        });
        adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //开启权限访问对话框
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10003);

                } else {
                    Intent intent = new Intent(mContext, MyadressActivity.class);
                    intent.putExtra("adress", adress.getText().toString());
                    if (StringUtils.isEmpty(prefs.getLat() + "") && StringUtils.isEmpty(prefs.getLat() + "")) {
                        intent.putExtra("lat", prefs.getLat());
                        intent.putExtra("lon", prefs.getLat());
                        startActivity(intent);
                    }
                }

            }
        });
        moreScene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MoreSceneActivity.class));
            }
        });
        roomWarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, WarnNewsListActivity.class));
            }
        });
        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showWindow(image);
                return false;
            }
        });
        openCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                updateState();
            }
        });


        return rootView;
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


    public void getMsgCount() {
        client.getMsgCount(mContext, new CommentHandler() {
            @Override
            public void onSuccess(Commen commen) {
                super.onSuccess(commen);
                count.setText(commen.data);
            }
        });
    }

    /**
     * 场景应用
     */
    public void sceneApply(String id) {
        client.getSceneApply(mContext, id, new CommentHandler() {
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
                getDefaultFamily();
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }

    public String getString(boolean state) {
        if (!state) {
            return "确定要切换到当前场景？";
        } else {
            return "确定要取消应用当前场景？";
        }
    }

    private void showWindow(View parent) {
        // 使其聚集  
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失  
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);// 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(parent, 0, 0);
    }


    /**
     * 获取默认家庭信息
     */
    public void getDefaultFamily() {
        client.getDefaultFamily(mContext, new DefaultFamilyHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            private void setDefault() {
                roundBar.setText("0");
                roundBar.setSweepAngle(0);
                hcho.setText("0");
                temperature.setText("0");
                humidity.setText("0");
                pm.setText("0");
                roundBar.setDesText("无");
            }

            @Override
            public void onSuccess(final DefaultFamily defaultFamily) {
                super.onSuccess(defaultFamily);
                cancelmDialog();
                //显示位置信息
                Log.d(TAG, "getDefaultFamily: " + defaultFamily.toString());
                prefs.saveHouseHolder(defaultFamily.data.houseHolder);
                if (defaultFamily.data.address != null) {
                    prefs.saveAddress(defaultFamily.data.address.address);
                    prefs.saveLan(defaultFamily.data.address.lat);
                    prefs.saveLon(defaultFamily.data.address.lon);
                    adress.setText(defaultFamily.data.address.address);
                } else {
                    adress.setText(prefs.getCurrentAddress());
                }

                //我的家庭组
                arrayList = defaultFamily.data.familys;
                groupAdapter = new MyFamilyListAdapter(mContext, arrayList);
                lv_group.setAdapter(groupAdapter);


                //家庭安防设置
                protectState = defaultFamily.data.protectState;
                if (protectState) {
                    openCheck.setChecked(true);
                } else {
                    openCheck.setChecked(false);
                }

                //家庭室内环境
                if (defaultFamily.data.enviroment != null) {
                    Map<String, Object> data = defaultFamily.data.enviromentData;
                    if (data != null) {
                        roundBar.setText(data.get("PM") == null ? "0" : data.get("PM").toString());
                        roundBar.setSweepAngle(data.get("PM") == null ? 0 : ZhsUtils.pmToPercent(data.get("PM").toString()));
                        roundBar.setDesText(data.get("PM") == null ? "无" : ZhsUtils.level(data.get("PM").toString()));
                        hcho.setText(data.get("GasStrength") == null ? "0" : data.get("GasStrength").toString());
                        temperature.setText(data.get("Temperature") == null ? "0" : data.get("Temperature").toString());
                        humidity.setText(data.get("Humidity") == null ? "0" : data.get("Humidity").toString());
                        pm.setText(data.get("PM") == null ? "0" : data.get("PM").toString());
                        ZhsApplication.startDevicePush("user-123457171-00020000000001", "00020000000001");
                    } else {
                        setDefault();
                    }
                } else {
                    setDefault();
                }
//

                //默认场景
                if (defaultFamily.data.scenes.size() > 0) {
                    if (defaultFamily.data.scenes.size() == 1) {
                        scene1.setText(defaultFamily.data.scenes.get(0).name);
                        scene1.setVisibility(View.VISIBLE);
                        scene2.setVisibility(View.GONE);
                        scene3.setVisibility(View.GONE);
                        if (defaultFamily.data.scenes.get(0).use) {
                            scene1.setBackgroundResource(R.drawable.bg_checked);
                        } else {
                            scene1.setBackgroundResource(R.drawable.bg_unchecked);
                        }
                    } else if (defaultFamily.data.scenes.size() == 2) {
                        scene1.setText(defaultFamily.data.scenes.get(0).name);
                        scene2.setText(defaultFamily.data.scenes.get(1).name);
                        scene1.setVisibility(View.VISIBLE);
                        scene2.setVisibility(View.VISIBLE);
                        scene3.setVisibility(View.GONE);
                        if (defaultFamily.data.scenes.get(0).use) {
                            scene1.setBackgroundResource(R.drawable.bg_checked);
                        } else {
                            scene1.setBackgroundResource(R.drawable.bg_unchecked);
                        }
                        if (defaultFamily.data.scenes.get(1).use) {
                            scene2.setBackgroundResource(R.drawable.bg_checked);
                        } else {
                            scene2.setBackgroundResource(R.drawable.bg_unchecked);
                        }
                    } else if (defaultFamily.data.scenes.size() >= 3) {
                        scene1.setText(defaultFamily.data.scenes.get(0).name);
                        scene2.setText(defaultFamily.data.scenes.get(1).name);
                        scene3.setText(defaultFamily.data.scenes.get(2).name);
                        scene1.setVisibility(View.VISIBLE);
                        scene2.setVisibility(View.VISIBLE);
                        scene3.setVisibility(View.VISIBLE);
                        if (defaultFamily.data.scenes.get(0).use) {
                            scene1.setBackgroundResource(R.drawable.bg_checked);
                        } else {
                            scene1.setBackgroundResource(R.drawable.bg_unchecked);
                        }
                        if (defaultFamily.data.scenes.get(1).use) {
                            scene2.setBackgroundResource(R.drawable.bg_checked);
                        } else {
                            scene2.setBackgroundResource(R.drawable.bg_unchecked);
                        }
                        if (defaultFamily.data.scenes.get(2).use) {
                            scene3.setBackgroundResource(R.drawable.bg_checked);
                        } else {
                            scene3.setBackgroundResource(R.drawable.bg_unchecked);
                        }
                    }
                } else {
                    scene1.setVisibility(View.GONE);
                    scene2.setVisibility(View.GONE);
                    scene3.setVisibility(View.GONE);
                }
                scene1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete = DialogFactory.getDialogFactory().showCommonDialog(mContext, getString(defaultFamily.data.scenes.get(0).use), "取消", "确定", new View.OnClickListener() {
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
                                    sceneApply(defaultFamily.data.scenes.get(0).id);
                                }
                            }
                        });

                    }
                });
                scene2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete = DialogFactory.getDialogFactory().showCommonDialog(mContext, getString(defaultFamily.data.scenes.get(1).use), "取消", "确定", new View.OnClickListener() {
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
                                    sceneApply(defaultFamily.data.scenes.get(1).id);
                                }
                            }
                        });
                    }

                });
                scene3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        delete = DialogFactory.getDialogFactory().showCommonDialog(mContext, getString(defaultFamily.data.scenes.get(0).use), "取消", "确定", new View.OnClickListener() {
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
                                    sceneApply(defaultFamily.data.scenes.get(2).id);
                                }
                            }
                        });
                    }
                });


            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }


    /**
     * 修改家庭防护状态
     */
    public void updateState() {
        RequestParams params = new RequestParams();
        cancelmDialog();
        showProgress(0, true);
        client.getUptateState(mContext, params, new CommentHandler() {
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
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });

    }

}
