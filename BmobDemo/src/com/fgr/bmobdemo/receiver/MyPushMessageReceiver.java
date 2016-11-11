package com.fgr.bmobdemo.receiver;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;

public class MyPushMessageReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
			// 获取服务器推送的内容
			String message = intent.getStringExtra("msg");
			Log.d("bmob", "客户端收到推送内容：" + intent.getStringExtra("msg"));// json
			try {

				JSONObject jsonObject = new JSONObject(message);
				if (jsonObject.has("tag")) {
					String tag = jsonObject.getString("tag");
					if ("new".equals(tag)) {
						// 有人发送了新帖子,要告知用户
						// 告知方式,让ShowActivity的右上角出现一个红点
						// 同时播放提示音
						Intent intent2 = new Intent(
								"com.fgr.bmobdemo.ACTION_NEW_POST");
						context.sendBroadcast(intent2);
					}
				}
			} catch (Exception e) {
			}
		}
	}
}