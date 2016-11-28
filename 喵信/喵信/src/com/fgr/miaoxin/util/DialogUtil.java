package com.fgr.miaoxin.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.widget.Button;

public class DialogUtil {
	public static void showDialog(Context context, String title,
			String message, boolean cancelButton, OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("确定", listener);
		if (cancelButton) {
			builder.setNegativeButton("取消", null);
		} else {
			builder.setCancelable(false);
		}

		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 改变按钮颜色
		// 一定要写在show方法后面
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);// 确定
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// 单纯设置一个btn有可能会有高度偏移,所以直接设置了2个
		// 不使用AlertDialog.THEME_DEVICE_DEFAULT_LIGHT这句话就会发生偏移
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);// 取消
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}
}
