package com.zhs.zhs.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.zhs.zhs.R;
import com.zhs.zhs.activity.AddFamify;
import com.zhs.zhs.entity.Commen;
import com.zhs.zhs.entity.Familyer;
import com.zhs.zhs.exception.BaseException;
import com.zhs.zhs.handler.CommentHandler;
import com.zhs.zhs.views.DialogFactory;

import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.zhs.zhs.R.id.write;

/**
 * Created by admin on 2017/4/18.
 */

public class MyFamilyerAdapter extends ZhsBaseAdapter<Familyer.MyFamilyer> {
    private Dialog use;

    public MyFamilyerAdapter(Context context, List<Familyer.MyFamilyer> List) {
        super(context);
        this.mList = List;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final Familyer.MyFamilyer mInfo = mList.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_familyer, null);
            holder.write = (TextView) convertView.findViewById(write);
            holder.accredit = (TextView) convertView.findViewById(R.id.accredit);
            holder.delete = (TextView) convertView.findViewById(R.id.delete);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.phone = (TextView) convertView.findViewById(R.id.phone);
            holder.edit_data = (RelativeLayout) convertView.findViewById(R.id.edit_data);
            holder.line = (ImageView) convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (mInfo != null) {
            holder.name.setText("姓名：" + mInfo.name);
            holder.phone.setText("手机号：" + mInfo.phone);
        }

        if (!pres.getHouseHolder()) {
            holder.edit_data.setVisibility(View.GONE);
            holder.line.setVisibility(View.GONE);
        } else {
            holder.edit_data.setVisibility(View.VISIBLE);
            holder.line.setVisibility(View.VISIBLE);
        }


        holder.write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("rivise", "1");
                Bundle bundle = new Bundle();
                bundle.putSerializable("member", mInfo);
                intent.putExtras(bundle);
                intent.setClass(mContext, AddFamify.class);
                mContext.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要删除该成员吗？", "取消", "确定", new View.OnClickListener() {
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
                            deleteMember(mInfo.memberId + "");
                        }
                    }
                });

            }
        });

        switch (mInfo.authType) {
            case 1:
                holder.accredit.setText("永久授权");
                holder.accredit.setTextColor(mContext.getResources().getColor(R.color.orange));
                holder.accredit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (pres.getHouseHolder()) {
                            use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要取消授权吗？", "取消", "确定", new View.OnClickListener() {
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
                                        accredit(mInfo.memberId + "", 0);
                                    }
                                }
                            });
                        }
                    }
                });

                break;
            case 2:
                if (mInfo.expire) {
                    holder.accredit.setText("未授权");
                    holder.accredit.setTextColor(mContext.getResources().getColor(R.color.text_grey));
                    holder.accredit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (pres.getHouseHolder()) {
                                use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要授权吗？", "临时授权", "永久授权", new View.OnClickListener() {
                                    @SuppressWarnings("unused")
                                    @Override
                                    public void onClick(View v) {
                                        use.dismiss();
                                        accredit(mInfo.memberId + "", 2);
                                    }
                                }, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        if (use != null && use.isShowing()) {
                                            use.dismiss();
                                            accredit(mInfo.memberId + "", 1);
                                        }
                                    }
                                });
                            }

                        }
                    });

                } else {
                    holder.accredit.setText("临时授权");
                    holder.accredit.setTextColor(mContext.getResources().getColor(R.color.orange));
                    holder.accredit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (pres.getHouseHolder()) {
                                use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要取消临时授权吗？", "取消", "确定", new View.OnClickListener() {
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
                                            accredit(mInfo.memberId + "", 0);
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
                break;
            case 0:
                holder.accredit.setText("未授权");
                holder.accredit.setTextColor(mContext.getResources().getColor(R.color.text_grey));
                holder.accredit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (pres.getHouseHolder()) {
                            use = DialogFactory.getDialogFactory().showCommonDialog(mContext, "确定要授权吗？", "临时授权", "永久授权", new View.OnClickListener() {
                                @SuppressWarnings("unused")
                                @Override
                                public void onClick(View v) {
                                    use.dismiss();
                                    accredit(mInfo.memberId + "", 2);
                                }
                            }, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    if (use != null && use.isShowing()) {
                                        use.dismiss();
                                        accredit(mInfo.memberId + "", 1);
                                    }
                                }
                            });
                        }

                    }
                });

                break;
        }


        return convertView;
    }


    private static class ViewHolder {
        TextView name, phone, accredit, write, delete;
        RelativeLayout edit_data;
        ImageView line;

    }


    public void accredit(String memberId, int authType) {
        JSONObject object = new JSONObject();
        object.put("memberId", memberId);
        object.put("authType", authType);
        try {
            StringEntity entity = new StringEntity(object.toString(), "UTF-8");
            showProgress(0, true);
            client.getImpowerApply(mContext, entity, new CommentHandler() {
                @Override
                public void onCancel() {
                    super.onCancel();
                    cancelmDialog();
                }

                @Override
                public void onSuccess(Commen commen) {
                    super.onSuccess(commen);
                    cancelmDialog();
                    mContext.sendBroadcast(new Intent("member_refresh"));
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

    public void deleteMember(String id) {
        showProgress(0, true);
        client.getMemberRemove(mContext, id, new CommentHandler() {
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
                mContext.sendBroadcast(new Intent("member_refresh"));

            }

            @Override
            public void onFailure(BaseException exception) {
                super.onFailure(exception);
                cancelmDialog();
            }
        });

    }

}
