package com.fgr.miaoxin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

import com.fgr.miaoxin.app.MyApp;

public class SPUtil {
	public static Editor editor;
	public SharedPreferences sp;

	private static final String NOTIFICATION = "notification";
	private static final String SOUND = "sound";
	private static final String VIBRATE = "vibrate";

	public SPUtil(String filename) {
		sp = MyApp.context.getSharedPreferences(filename, Context.MODE_PRIVATE);
		editor = sp.edit();

	}

	public SPUtil() {
		sp = PreferenceManager.getDefaultSharedPreferences(MyApp.context);
		editor = sp.edit();
	}

	public boolean isAllowNotification() {
		return sp.getBoolean(NOTIFICATION, true);
	}

	public void setNotification(boolean flag) {
		editor.putBoolean(NOTIFICATION, flag);
		editor.commit();
	}

	public boolean isAllowSound() {
		return sp.getBoolean(SOUND, true);
	}

	public void setSound(boolean flag) {
		editor.putBoolean(SOUND, flag);
		editor.commit();
	}

	public boolean isAllowVibrate() {
		return sp.getBoolean(VIBRATE, true);
	}

	public void setVibrate(boolean flag) {
		editor.putBoolean(VIBRATE, flag);
		editor.commit();
	}

}
