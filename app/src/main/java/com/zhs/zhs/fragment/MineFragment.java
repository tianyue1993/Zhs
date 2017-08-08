package com.zhs.zhs.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.AboutusActivity;
import com.zhs.zhs.activity.FamilyManageActivity;
import com.zhs.zhs.activity.FeedbackActivity;
import com.zhs.zhs.activity.MyFamilyActivity;
import com.zhs.zhs.activity.PayActivity;
import com.zhs.zhs.activity.PersonnalSettingActivity;
import com.zhs.zhs.activity.QuestionActivity;
import com.zhs.zhs.utils.StringUtils;
import com.zhs.zhs.views.CircleImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by tianyue on 2017/3/23.
 */
public class MineFragment extends BaseFragment {
    @Bind(R.id.imageView)
    ImageView imageView;
    @Bind(R.id.base_title)
    TextView baseTitle;
    @Bind(R.id.title)
    RelativeLayout title;
    @Bind(R.id.mine_adress)
    RelativeLayout mineAdress;
    @Bind(R.id.mine_member)
    RelativeLayout mineMember;
    @Bind(R.id.mine_mall)
    RelativeLayout mineMall;
    @Bind(R.id.mine_aboutus)
    RelativeLayout mineAboutus;
    @Bind(R.id.mine_feedback)
    RelativeLayout mineFeedback;
    @Bind(R.id.mine_question)
    RelativeLayout mineQuestion;
    @Bind(R.id.mine_pay)
    RelativeLayout minePay;
    @Bind(R.id.avator)
    CircleImageView avator;
    @Bind(R.id.name)
    TextView name;
    @Bind(R.id.phone)
    TextView phone;
    @Bind(R.id.email)
    TextView email;
    @Bind(R.id.mine_news)
    RelativeLayout mineNews;
    @Bind(R.id.personnal_setting)
    RelativeLayout personnal_setting;

    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("rename")) {
                name.setText(prefs.getCustomName());
            }
        }
    };


    Context mContext;


    @Override
    protected void initView(View view, Bundle savedInstanceState) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        IntentFilter filter = new IntentFilter();
        filter.addAction("rename");
        mContext.registerReceiver(receiver, filter);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        if (StringUtils.isNotEmptyOrNull(prefs.getCustomName())) {
            name.setText(prefs.getCustomName());
        } else {
            name.setText("请设置昵称");
        }

        if (StringUtils.isNotEmptyOrNull(prefs.getUserPhone())) {
            phone.setText(prefs.getUserPhone());
        } else {
            phone.setText("请设置手机号");
        }

        if (StringUtils.isNotEmptyOrNull(prefs.getUserEmail())) {
            email.setText(prefs.getUserEmail());
        } else {
            email.setText("请设置邮箱");
        }

        return rootView;
    }


    @OnClick({R.id.mine_member, R.id.mine_aboutus, R.id.mine_feedback, R.id.mine_question, R.id.mine_pay,
            R.id.personnal_setting, R.id.email, R.id.mine_mall, R.id.mine_adress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mine_member:
                startActivity(new Intent(mContext, MyFamilyActivity.class));
                break;
            case R.id.mine_aboutus:
                startActivity(new Intent(mContext, AboutusActivity.class));
                break;
            case R.id.mine_feedback:
                startActivity(new Intent(mContext, FeedbackActivity.class));
                break;
            case R.id.mine_question:
                startActivity(new Intent(mContext, QuestionActivity.class));
                break;
            case R.id.mine_pay:
                startActivity(new Intent(mContext, PayActivity.class));
                break;
            case R.id.mine_mall:
                break;
            case R.id.mine_adress:
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //开启权限访问对话框
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 10003);

                } else {
                    startActivity(new Intent(mContext, FamilyManageActivity.class));
                }
                break;
            case R.id.personnal_setting:
                startActivity(new Intent(mContext, PersonnalSettingActivity.class));
                break;
        }
    }


}
