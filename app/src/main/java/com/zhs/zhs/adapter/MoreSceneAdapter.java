package com.zhs.zhs.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhs.zhs.R;
import com.zhs.zhs.activity.AddSceneActivity;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.MoreScene;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.views.DialogFactory;

import java.util.List;

/**
 * Created by admin on 2017/4/14.
 */

public class MoreSceneAdapter extends ZhsBaseAdapter<MoreScene.Scene> {
    private Dialog use;
    ViewHolder holder;

    public MoreSceneAdapter(Context context, List<MoreScene.Scene> List) {
        super(context);
        this.mList = List;
        holder = new ViewHolder();

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final MoreScene.Scene mInfo = mList.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_morescene, null);
            holder.scene_name = (TextView) convertView.findViewById(R.id.scene_name);
            holder.use_scene = (Button) convertView.findViewById(R.id.use_scene);
            holder.root = (RelativeLayout) convertView.findViewById(R.id.root);
            holder.write = (ImageView) convertView.findViewById(R.id.write);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {
            holder.scene_name.setText(mInfo.name);
            if (mInfo.state != null) {
                if (mInfo.state.equals("0")) {
                    holder.use_scene.setBackgroundResource(R.drawable.bg_unchecked);
                    holder.use_scene.setText("应用场景");
                } else if (mInfo.state.equals("1")) {
                    holder.use_scene.setBackgroundResource(R.drawable.bg_checked);
                    holder.use_scene.setText("取消场景");
                }
            }
            holder.use_scene.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    use = DialogFactory.getDialogFactory().showCommonDialog(mContext,
                            getString(mInfo.state), "取消", "确定", new View.OnClickListener() {
                                @SuppressWarnings("unused")
                                @Override
                                public void onClick(View v) {
                                    use.dismiss();
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (use != null && use.isShowing()) {
                                        sceneApply(mInfo.sceneId, mInfo.state);
                                    }
                                }
                            });

                }
            });

        }


        holder.write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AddSceneActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("scene", mInfo);
                intent.putExtras(bundle);
                intent.putExtra("type", "2");
                mContext.startActivity(intent);
            }
        });


        return convertView;
    }


    public String getString(String state) {
        if (state != null) {
            if (state.equals("0")) {
                return "确定要切换到当前场景？";
            } else {
                return "确定要取消应用当前场景？";
            }
        } else {
            return "确定要切换到当前场景？";
        }
    }

    private static class ViewHolder {
        TextView scene_name, scene_detail;
        Button use_scene;
        RelativeLayout root;
        ImageView write;

    }

    public void sceneApply(String sceneId, String sceneState) {
        use.dismiss();
        showProgress(0, true);
        client.getSceneApply(mContext, sceneId, new CommentHandler() {
            @Override
            public void onCancel() {
                super.onCancel();
                cancelmDialog();
            }

            @Override
            public void onSuccess(Commen commen) {
                super.onSuccess(commen);
                showToast(commen.msg);
                mContext.sendBroadcast(new Intent("updatestate"));
                mContext.sendBroadcast(new Intent("homerefresh"));
                notifyDataSetChanged();
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
