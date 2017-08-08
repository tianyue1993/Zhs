package com.zhs.zhs.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FeedbackActivity extends Baseactivity {

    @Bind(R.id.feedback_tv)
    EditText feedbackTv;
    @Bind(R.id.contact_tv)
    TextView contactTv;
    @Bind(R.id.feedback_contact_tv)
    EditText feedbackContactTv;
    @Bind(R.id.contact_info_tv)
    TextView contactInfoTv;
    @Bind(R.id.feedback_commit)
    Button feedbackCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        setTitleTextView(getString(R.string.feed_back), null);
    }

    @OnClick(R.id.feedback_commit)
    public void onViewClicked() {
        if (feedbackTv.getText().length() < 1) {
            showToast("请输入反馈内容");
        } else if (feedbackContactTv.getText().length() < 1) {
            showToast("请输入联系方式");
        } else {
            feedBack();
        }
    }

    public void feedBack() {

        JSONObject params = new JSONObject();
        params.put("title", "");
        params.put("content", feedbackTv.getText().toString());
        params.put("phone", feedbackContactTv.getText().toString());
        try {
            StringEntity entity = new StringEntity(params.toString(), "UTF-8");
            showProgress(0, true);
            client.getFeedBack(this, entity, new CommentHandler() {
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
                    finish();
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
}
