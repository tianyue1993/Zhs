package com.zhs.zhs.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.Family;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.views.DialogFactory;

import java.util.List;

import static com.zhs.zhs.R.id.family_name;

/**
 * Created by admin on 2017/4/14.
 */

public class MyFamilyListAdapter extends ZhsBaseAdapter<Family.MyFamilys> {
    private Dialog use;

    public MyFamilyListAdapter(Context context, List<Family.MyFamilys> List) {
        super(context);
        this.mList = List;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Family.MyFamilys mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_family_list, null);
            holder.change = (ImageView) convertView.findViewById(R.id.change);
            holder.family_name = (TextView) convertView.findViewById(family_name);
            holder.root = (RelativeLayout) convertView.findViewById(R.id.root);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {
            holder.family_name.setText(mInfo.name);
            if (mInfo.checked) {
                holder.change.setVisibility(View.VISIBLE);
            } else {
                holder.change.setVisibility(View.INVISIBLE);
                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要切换到当前家？", "取消", "确定", new View.OnClickListener() {
                            @SuppressWarnings("unused")
                            @Override
                            public void onClick(View v) {
                                use.dismiss();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (use != null && use.isShowing()) {
                                    use.dismiss();
                                    familyChange(mInfo.familyId);
                                }
                            }
                        });

                    }
                });
            }


        }

        return convertView;
    }


    private static class ViewHolder {
        TextView family_name;
        ImageView change;
        RelativeLayout root;
    }

    public void familyChange(final String id) {
        showProgress(0, true);
        client.getFamilyChange(mContext, id, new CommentHandler() {
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
                pres.saveFamilyId(id);
                mContext.sendBroadcast(new Intent("homerefresh"));
            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });
    }
}
