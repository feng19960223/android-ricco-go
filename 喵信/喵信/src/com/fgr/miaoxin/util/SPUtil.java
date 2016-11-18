package com.fgr.miaoxin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.fgr.miaoxin.constant.Constant;

/**
 * 操作偏好设置文件的工具类
 * 
 * @author pjy
 *
 */
public class SPUtil {
	SharedPreferences sp;
	private static Editor editor;

	public static final String NOTIFICATION = "notification";

	public SPUtil(Context context, String name) {
		sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public SPUtil(Context context) {
		sp = PreferenceManager.getDefaultSharedPreferences(context);
		editor = sp.edit();
	}

	public boolean isAllowNotification() {
		return sp.getBoolean(Constant.NOTIFICATION, true);
	}

	public boolean isAllowSound() {
		return sp.getBoolean(Constant.SOUND, true);
	}

	public boolean isAllowVibrate() {
		return sp.getBoolean(Constant.VIBRATE, true);
	}

	public void setNotification(boolean flag) {
		editor.putBoolean(Constant.NOTIFICATION, flag);
		editor.commit();
	}

	public void setSound(boolean flag) {
		editor.putBoolean(Constant.SOUND, flag);
		editor.commit();
	}

	public void setVibrate(boolean flag) {
		editor.putBoolean(Constant.VIBRATE, flag);
		editor.commit();
	}
}
