package com.tarena.fgr.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.db.DBUtil;
import com.tarena.fgr.entity.BlackPhone;
import com.tarena.fgr.entity.BlackSMS;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.utils.LogUtilS;
import com.tarena.fgr.youlu.ChatActivity;

/**
 * 黑名单服务
 * 
 * @author 冯国芮
 * 
 */
public class BlackNumberService extends Service {
	private MyReceiver receiver = null;
	private DBUtil dbUtil = null;
	private TelephonyManager manager = null;// 电话管理的系统服务
	private PhoneStateListener phoneStateListener = null;// 电话状态监听器
	private ITelephony iTelephony = null;
	// 拨出电话的系统广播
	public static final String OUTGOING_CALL = "android.intent.action.NEW_OUTGOING_CALL";
	// 呼入电话的系统广播
	public static final String INCOMING_CALL = "android.intent.action.PHONE_STATE";

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtilS.i("黑名单电话管理的服务启动");

		receiver = new MyReceiver();
		registerReceiver();
		dbUtil = new DBUtil(this);
		// 获得电话服务
		manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		// 通过反射方法调用服务中的一个私有的方法获得ITelephony对象
		try {
			Method method = manager.getClass().getDeclaredMethod(
					"getITelephony", null);
			method.setAccessible(true);// 设置该方法可以被外接调用
			// 调用该方法获得ITelephony的实例
			iTelephony = (ITelephony) method.invoke(manager, null);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		// 封装一个挂电话的方法
		endBlockCall();
	}

	private void registerReceiver() {
		// 把广播接收器注册给该服务
		IntentFilter filter = new IntentFilter();
		// 订阅了系统发来的收到短信的广播
		// 该广播是一个有序广播
		filter.addAction(ChatActivity.RECEIVE_SMS);
		// 订阅拨出电话时系统发出的广播
		filter.addAction(OUTGOING_CALL);
		// 要将广播接收器的优先级设置的足够高
		// 使的该服务能够优先于系统的短信管理的应用接收
		filter.setPriority(1001);// 比会话的高一点,先于聊天收到
		registerReceiver(receiver, filter);
	}

	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 如果有短信发来,该方法会被调用
			String action = intent.getAction();
			if (action.equals(ChatActivity.RECEIVE_SMS)) {
				// 收到短信
				// 获得短信的电话号码
				Bundle bundle = intent.getExtras();
				Sms sms = SMSManager.getSmsByReceiver(bundle);
				String smsPhone = sms.getAddress();
				// 判断该电话是否是黑名单电话
				if (dbUtil.isBlackNumber(smsPhone)) {
					// 如果是黑名单电话则禁止广播向下一级接收器传递
					abortBroadcast();
					LogUtilS.i("该短信是黑名单电话发来的");
					// 并拆解短信的内容,将数据保存在当前应用的表中
					BlackSMS blackSMS = new BlackSMS();
					blackSMS.setNumber(smsPhone);
					blackSMS.setBody(sms.getBody());
					blackSMS.setDate(sms.getDate());
					dbUtil.insertSMS(blackSMS);
				}
			}
			// 自己要播出电话
			else if (action.equals(OUTGOING_CALL)) {
				LogUtilS.i("要播出电话");
				// 获得拨出的电话的电话号码
				String callPhone = getResultData();
				// 判断该电话是否是黑名单电话
				if (dbUtil.isBlackNumber(callPhone)) {
					LogUtilS.i("是黑名单电话");
					setResultData(null);
					BlackPhone blackPhone = new BlackPhone();
					blackPhone.setNumber(callPhone);
					blackPhone.setDate(new Date().getTime());
					blackPhone.setType(2);
					dbUtil.insertPhone(blackPhone);
				}
			}
			// 有电话打来,挂断电话比较私有
		}
	}

	private void endBlockCall() {
		phoneStateListener = new PhoneStateListener() {
			@Override
			public void onCallStateChanged(int state,
					final String incomingNumber) {
				super.onCallStateChanged(state, incomingNumber);
				switch (state) {
				case TelephonyManager.CALL_STATE_IDLE: // 空闲状态

					break;
				case TelephonyManager.CALL_STATE_OFFHOOK: //
					break;
				case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
					// 有电话打进来
					LogUtilS.i("有电话来电了");
					if (dbUtil.isBlackNumber(incomingNumber)) {
						// 是黑名单电话
						LogUtilS.i("一个黑名单电话来电了");
						try {
							// 挂断电话
							iTelephony.endCall();
							// 把打进来的电话的信息存的表中
							BlackPhone blackPhone = new BlackPhone();
							blackPhone.setNumber(incomingNumber);
							blackPhone.setDate(new Date().getTime());
							blackPhone.setType(2);
							dbUtil.insertPhone(blackPhone);
						} catch (RemoteException e) {
							e.printStackTrace();
						}

						// 当前的线程休眠一下,立即删除数据,可能没有写入数据
						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								// 删除通话记录表
								CalllogManager
										.deleteCalllog(BlackNumberService.this,
												incomingNumber);
							}
						}, 5000);
					}
					break;
				default:
					break;
				}

			}
		};
		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	@Override
	// 释放资源
	public void onDestroy() {
		LogUtilS.i("服务已停止");
		// 服务停止时注销广播接收器
		super.onDestroy();
		unregisterReceiver(receiver);
		manager.listen(null, PhoneStateListener.LISTEN_CALL_STATE);
		manager = null;
	}

}
