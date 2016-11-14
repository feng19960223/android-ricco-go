package com.fgr.bmobdemo.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.push.PushConstants;

/**
 * 观测者模式
 *
 */
public class MyPushMessageReceiver extends BroadcastReceiver {
	private static List<EventListener> list = new ArrayList<EventListener>();

	public static void regist(EventListener listener) {
		list.add(listener);
	}

	public static void unregist(EventListener listener) {
		list.remove(listener);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
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
						// 方法一
						// Intent intent2 = new Intent(
						// "com.fgr.bmobdemo.ACTION_NEW_POST");
						// context.sendBroadcast(intent2);
						// 方法二
						if (list.size() > 0) {// 调用集合中每一个接口
							// 通知每一个观察者
							// 被观察的事情发生了
							for (EventListener eventListener : list) {
								// 通过调用观察者的方法来告诉观察者
								eventListener.onNewPost();
							}
						}
					}
				}
			} catch (Exception e) {
			}
		}
	}

	// 每一个接口的实现这就是一个观察者
	public interface EventListener {
		void onNewPost();
	}
}