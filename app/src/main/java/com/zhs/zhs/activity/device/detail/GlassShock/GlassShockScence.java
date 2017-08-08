package com.zhs.zhs.activity.device.detail.GlassShock;

import android.content.Intent;
import android.view.View;

import com.zhs.zhs.activity.Baseactivity;

import static com.zhs.zhs.utils.GlobalPrefrence.DEVICEDETAIL;

/**
 * Created by admin on 2017/7/19.
 */

public class GlassShockScence extends GlassShockBase {
    public GlassShockScence(Baseactivity baseactivity) {
        super(baseactivity);
    }

    @Override
    public void getDetail(String clintId) {
        super.getDetail(clintId);
        //显示指令信息
    }

    /**
     * 重写父类，只存指令，不传服务
     */
//    @Override
//    public void switchEvent() {
//        super.switchEvent();
//        activity.openCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//                if (isChecked) {
//                    object.put("SwitchOutput", "1");
//                } else {
//                    object.put("SwitchOutput", "0");
//                }
//                command.clear();
//                command.add(object);
//                Log.d(TAG, "onCheckedChanged: command" + command.toString());
//                JSONObject parms = new JSONObject();
//                parms.put("command", JSON.toJSONString(command));
//            }
//        });
//    }

    @Override
    public void sysBack() {
        Intent intent = new Intent();
        intent.putExtra("object", object.toString());
        activity.setResult(DEVICEDETAIL, intent);
        activity.finish();
    }

    @Override
    public void backEvent() {
        activity.leftTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sysBack();
            }
        });
    }
}
