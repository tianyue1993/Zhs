package com.zhs.zhs.mpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.mpush.api.Constants;
import com.zhs.zhs.ZhsApplication;
import com.zhs.zhs.activity.OfflineActivity;
import com.zhs.zhs.utils.ClassUtils;
import com.zhs.zhs.utils.GlobalPrefrence;
import com.zhs.zhs.utils.GlobalSetting;

import org.json.JSONObject;

import static com.zhs.zhs.ZhsApplication.getActivityData;

public class MyReceiver extends BroadcastReceiver {

    private String msg;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (MPushService.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            byte[] bytes = intent.getByteArrayExtra(MPushService.EXTRA_PUSH_MESSAGE);
            int messageId = intent.getIntExtra(MPushService.EXTRA_PUSH_MESSAGE_ID, 0);
            String message = new String(bytes, Constants.UTF_8);
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(message);

            Log.d("pushData", "pushData: " + message);

            if (messageId > 0) MPush.I.ack(messageId);
            if (TextUtils.isEmpty(message)) return;

            if (message.contains("content")) {
                msg = object.getString("content");
            }
            if (object.getString("pushType").equals("2")) {
                if (object.getString("dataType").equals("1")) {
                    ZhsApplication.pausePush();
                    Intent intent1 = new Intent();
                    intent1.putExtra("msg", msg);
                    intent1.setClass(ZhsApplication.getContext(), OfflineActivity.class);
                    ZhsApplication.getContext().startActivity(intent1);
                }
            } else if (object.getString("pushType").equals("1")) {
                Intent deviceIntent = new Intent(GlobalPrefrence.DEVICEDATA);
                deviceIntent.putExtra("message", msg);
                ZhsApplication.getContext().sendBroadcast(deviceIntent);

            } else if (object.getString("pushType").equals("3")) {
//                Toast.makeText(ZhsApplication.getContext(), message, Toast.LENGTH_SHORT).show();
                NotificationDO ndo = fromJson(message);
                if (ndo != null) {
                    Intent it = new Intent(context, MyReceiver.class);
                    it.setAction(MPushService.ACTION_NOTIFICATION_OPENED);
                    if (ndo.getExtras() != null) {
                        it.putExtra("my_extra", ndo.getExtras().toString());
                    }
                    it.putExtra("ClientType", object.getString("dataType"));
                    if (TextUtils.isEmpty(ndo.getTitle())) ndo.setTitle("MPush");
                    if (TextUtils.isEmpty(ndo.getTicker())) ndo.setTicker(ndo.getTitle());
                    if (TextUtils.isEmpty(ndo.getContent())) ndo.setContent(ndo.getTitle());
                    Notifications.I.notify(ndo, it);
                }

                GlobalSetting prefs = GlobalSetting.getInstance(ZhsApplication.getContext());
                prefs.saveWarnCount(prefs.getWarnCount() + 1);
                Intent deviceIntent = new Intent(GlobalPrefrence.CHANGE_ENVIR_DATA);
                ZhsApplication.getContext().sendBroadcast(deviceIntent);

            }

        } else if (MPushService.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Notifications.I.clean(intent);
            String extras = intent.getStringExtra("my_extra");
            String ClientType = intent.getStringExtra("ClientType");
            Toast.makeText(context, "通知被点击了， extras=" + extras, Toast.LENGTH_SHORT).show();
            com.alibaba.fastjson.JSONObject object = JSON.parseObject(extras);
            intent.putExtra("ClientId", object.getString("ClientId"));
            if (getActivityData().containsKey(ClientType)) {
                intent.setClass(ZhsApplication.getContext(), ClassUtils.getClass(getActivityData().get(ClientType)));
                ZhsApplication.getContext().startActivity(intent);
            } else {
                Toast.makeText(ZhsApplication.getContext(), "敬请期待！", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private NotificationDO fromJson(String message) {
        try {
            JSONObject messageDO = new JSONObject(message);
            if (messageDO != null) {
                JSONObject jo = new JSONObject(messageDO.optString("content"));
                NotificationDO ndo = new NotificationDO();
                ndo.setContent(jo.optString("content"));
                ndo.setTitle(jo.optString("title"));
                ndo.setTicker(jo.optString("ticker"));
                ndo.setNid(jo.optInt("nid", 1));
                ndo.setExtras(jo.optJSONObject("extras"));
                return ndo;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
