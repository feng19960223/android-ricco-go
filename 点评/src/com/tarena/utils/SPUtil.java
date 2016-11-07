package com.tarena.utils;

import com.tarena.constant.Constant;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * 操作偏好设置文件的工具类
 * 
 * @author 冯国芮
 * 
 */
public class SPUtil {
	SharedPreferences sharedPreferences = null;
	private static Editor editor = null;
	private static final String FIRST = "first";

	public SPUtil(Context context, String name) {
		sharedPreferences = context.getSharedPreferences(name,
				Context.MODE_PRIVATE);
		editor = sharedPreferences.edit();
	}

	public SPUtil(Context context) {
		sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		editor = sharedPreferences.edit();
	}

	public boolean isFirst() {
		return sharedPreferences.getBoolean(FIRST, true);
	}

	public void setFirst(boolean flag) {
		editor.putBoolean(FIRST, flag);
		editor.commit();
	}

	public boolean isCloseBanner() {
		return sharedPreferences.getBoolean(Constant.SP_BANNER, false);
	}

	public void setCloseBanner(boolean flag) {
		editor.putBoolean(Constant.SP_BANNER, flag);
		editor.commit();
	}

}
