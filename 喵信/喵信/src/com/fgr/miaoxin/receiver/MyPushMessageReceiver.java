package com.fgr.miaoxin.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fgr.miaoxin.app.MyApp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobInstallation;

public class MyPushMessageReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {

			String message = intent
					.getStringExtra(PushConstants.EXTRA_PUSH_MESSAGE_STRING);
			Log.d("TAG", BmobInstallation.getInstallationId(context) + "收到的内容："
					+ message);
			try {
				JSONObject jsonObject = new JSONObject(message);
				if (jsonObject.has("tag")) {
					String tag = jsonObject.getString("tag");
					// 如果tag的值为offline
					// 当前设备的登录用户的帐号,已经在另外一台设备上登录
					// 让当前设备的用户下线
					if ("offline".equals(tag)) {
						// 订阅者
						if (list.size() > 0) {
							// MyPushMessageReceiver通过调用订阅这的offline方法告知订阅者
							// 再有订阅这具体处理,让当前登录用户下线
							for (EventListener eventListener : list) {
								eventListener.onOffline();
							}
						} else {
							// 自行处理让用户下线
							MyApp.logout();
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private static List<EventListener> list = new ArrayList<EventListener>();

	public void regist(EventListener listener) {
		list.add(listener);
	}

	public void unRegist(EventListener listener) {
		list.remove(listener);
	}
}
