package com.zhs.zhs.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.UserFamily;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.utils.ChooseCityInterface;
import com.zhs.zhs.utils.ChooseCityUtil;
import com.zhs.zhs.utils.StringUtils;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUserFamify extends Baseactivity {

    public int type;//1为修改2是添加
    String areaCode = "";
    @Bind(R.id.edit_name)
    EditText editName;
    @Bind(R.id.textView)
    TextView textView;
    @Bind(R.id.rl_name)
    RelativeLayout rlName;
    @Bind(R.id.line1)
    ImageView line1;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.et_address)
    TextView etAddress;
    @Bind(R.id.linearLayout2)
    RelativeLayout linearLayout2;
    @Bind(R.id.line2)
    ImageView line2;
    @Bind(R.id.ed_area)
    EditText edArea;
    @Bind(R.id.linearLayout3)
    RelativeLayout linearLayout3;
    @Bind(R.id.line3)
    ImageView line3;
    @Bind(R.id.sure)
    Button sure;
    @Bind(R.id.activity_add_famify)
    RelativeLayout activityAddFamify;
    UserFamily userFamily;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_famify);
        ButterKnife.bind(this);
        editName.setSelection(editName.length());
        if (getIntent().getStringExtra("rivise").equals("1")) {
            type = 1;
            setTitleTextView("修改家庭信息", null);
            sure.setText(getString(R.string.revise_sure));
            Bundle bundle = getIntent().getExtras();
            userFamily = (UserFamily) bundle.getSerializable("userFamily");
            if (userFamily != null) {
                editName.setText(userFamily.name);
                areaCode = userFamily.areaCode;
            }

        } else {
            type = 2;
            setTitleTextView("添加家庭", null);
            sure.setText(getString(R.string.add_sure));
        }

    }

    @OnClick({R.id.sure, R.id.linearLayout2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sure:
                if (editName.getText().length() < 1) {
                    showToast("请输入家庭名称");
                } else if (etAddress.getText().length() < 1) {
                    showToast("请输入家庭地址");
                } else {
                    saveFamily();
                }
                break;
            case R.id.linearLayout2:
                chooseCityDialog(etAddress);
                break;
            default:
                break;
        }
    }


    //Choose Date 选择省市县
    public void chooseCityDialog(View view) {
        final ChooseCityUtil cityUtil = new ChooseCityUtil();
        String[] oldCityArray = etAddress.getText().toString().split("-");//将TextView上的文本分割成数组 当做默认值
        cityUtil.createDialog(this, oldCityArray, new ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                //oldCityArray为传入的默认值 newCityArray为返回的结果
                etAddress.setText(newCityArray[0] + "-" + newCityArray[1] + "-" + newCityArray[2]);
                areaCode = newCityArray[3];
            }
        });
    }

    public void saveFamily() {
        JSONObject object = new JSONObject();
        object.put("name", editName.getText().toString());
        object.put("address", etAddress.getText().toString());
        if (!StringUtils.isEmpty(areaCode)) {
            object.put("areaCode", areaCode);
        } else {
            showToast("请选择地址！");
            return;
        }
        object.put("lat", prefs.getCurrentLat());
        object.put("lon", prefs.getCurrentLon());
        if (userFamily != null) {
            object.put("id", userFamily.id);
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(object.toString(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        showProgress(0, true);
        if (type == 1) {
            client.getEditCustom(mContext, entity, new CommentHandler() {
                @Override
                public void onFailure(BaseException exception) {
                    super.onFailure(exception);
                    cancelmDialog();
                }

                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    showToast("操作成功");
                    finish();
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }
            });
        } else {
            client.getAddCustom(mContext, entity, new CommentHandler() {
                @Override
                public void onFailure(BaseException exception) {
                    super.onFailure(exception);
                    cancelmDialog();
                }

                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    showToast("操作成功");
                    finish();
                }

                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }
            });

        }
    }
}
