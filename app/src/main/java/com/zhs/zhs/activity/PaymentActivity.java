package com.zhs.zhs.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.zhs.zhs.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentActivity extends Baseactivity {

    @Bind(R.id.tv_usr_name)
    TextView tvUsrName;
    @Bind(R.id.tv_usr_tel)
    TextView tvUsrTel;
    @Bind(R.id.cb_wechat)
    CheckBox cbWechat;
    @Bind(R.id.cb_alipay)
    CheckBox cbAlipay;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.btn_pay)
    Button btnPay;

    private int PAYMENT = 0;
    private static final int NOPAYMENT = 0;
    private static final int WECHART = 1;
    private static final int ALIPAY = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        setTitleTextView("确认信息", null);
    }

    @OnClick({R.id.cb_wechat, R.id.cb_alipay, R.id.btn_pay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cb_wechat:
                cbAlipay.setChecked(false);
                getPayment();
                break;
            case R.id.cb_alipay:
                cbWechat.setChecked(false);
                getPayment();
                break;
            case R.id.btn_pay:
                Log.d("PaymentActivity","支付方式:"+PAYMENT);
                switch (PAYMENT){
                    case WECHART:
                        Toast toast =Toast.makeText(this,"跳转到微信支付",Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        break;
                    case ALIPAY:
                        Toast toast1 =Toast.makeText(this,"跳转到支付宝支付",Toast.LENGTH_LONG);
                        toast1.setGravity(Gravity.CENTER, 0, 0);
                        toast1.show();
                        break;
                    case NOPAYMENT:
                        Toast toast3 =Toast.makeText(this,"请选择支付方式",Toast.LENGTH_LONG);
                        toast3.setGravity(Gravity.CENTER, 0, 0);
                        toast3.show();
                        break;
                }
                break;
        }
    }

    private void getPayment(){
        if(cbWechat.isChecked()){
            PAYMENT = WECHART;
        }else if(cbAlipay.isChecked()){
            PAYMENT = ALIPAY;
        }else {
            PAYMENT = NOPAYMENT;
        }
    }
}
