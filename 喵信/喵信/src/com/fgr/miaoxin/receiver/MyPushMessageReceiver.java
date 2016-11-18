package com.fgr.miaoxin.receiver;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobNotifyManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.util.BmobJsonUtil;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.BmobInstallation;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.ui.MainActivity;
import com.fgr.miaoxin.util.SPUtil;

public class MyPushMessageReceiver extends BroadcastReceiver {
	private SPUtil sputil = new SPUtil(MyApp.context, BmobUserManager
			.getInstance(MyApp.context).getCurrentUserObjectId());

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
					if ("offline".equals(tag)) {
						// 如果tag的值为offline
						// 当前设备的登录用户的帐号,已经在另外一台设备上登录
						// 让当前设备的用户下线
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
					if ("add".equals(tag)) {
						// 收到了一个添加好友的申请
						// 判断添加好友申请是否是发给当前登录用户的
						String tid = BmobJsonUtil.getString(jsonObject, "tId");
						if (tid != null) {
							handleAddFriendInvitation(context, message, tid);
						}
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// 添加好友申请
	private void handleAddFriendInvitation(Context context, String message,
			String tid) {
		// 将收到的Json字符串格式的添加好友申请保存本地数据库的数据表中
		// step1. 根据收到的Json字符串创建BmobInvitation实体类对象，该对象一个重要的属性值status为2。
		// status为2意味着收到了一条添加好友申请但尚未处理
		// 该对象最终也是saveReceiveInvite方法的返回值
		// step2. 将step1创建的实体类对象的相关内容写入到tid所对应的数据库的tab_new_contacts数据表中
		// 该数据记录的status字段的值也是为2
		// step3. 将收到的这条好友申请在服务器BmobMsg表中所对应数据记录的isReaded字段值从0更新为1
		// 意味着该添加好友申请已经收到了

		BmobInvitation bmobInvitation = BmobChatManager.getInstance(context)
				.saveReceiveInvite(message, tid);

		// 该好友申请是发送给tid，那tid是当前设备的登录用户时
		// 告知当前设备上的登录用户
		String uid = BmobUserManager.getInstance(context)
				.getCurrentUserObjectId();
		if (tid.equals(uid)) {

			if (list.size() > 0) {
				// 如果有订阅者
				// 将收到“添加好友申请”的事情告诉订阅者
				// 再由订阅者告诉当前设备登录用户
				for (EventListener listener : list) {
					listener.onAddUser(bmobInvitation);
				}
			} else {
				// 如果没有订阅者
				// MyReceiver通过发送通知的方式
				// 告诉当前设备登录用户
				if (sputil.isAllowNotification()) {
					BmobNotifyManager.getInstance(context).showNotify(
							sputil.isAllowSound(), sputil.isAllowVibrate(),
							R.drawable.ic_notification,
							bmobInvitation.getFromname() + "请求添加您为好友", "添加好友",
							bmobInvitation.getFromname() + "请求添加您为好友",
							MainActivity.class);
				}
			}
		}

	}

	private static List<EventListener> list = new ArrayList<EventListener>();

	public static void regist(EventListener listener) {
		list.add(listener);
	}

	public static void unRegist(EventListener listener) {
		list.remove(listener);
	}
}
