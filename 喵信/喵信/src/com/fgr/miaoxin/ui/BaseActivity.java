package com.fgr.miaoxin.ui;

import java.util.Locale;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.util.WindowUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;

public abstract class BaseActivity extends Activity {

	// 公共属性
	// BmobIMSDK提供的一个工具类
	// 用来“管理”用户（用户的登录，登出，添加好友，删除好友）
	BmobUserManager userManager;
	// BmobIMSDK提供的一个工具类
	// 用来“管理”聊天内容（聊天内容的创建、发送、删除、存储）
	BmobChatManager chatManager;
	// BmobIMSDK提供的一个工具类
	// 用来“管理”本地数据库
	BmobDB bmobDB;

	Toast toast;

	View headerView;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowUtil.translucent(getWindow());// 透明状态栏????继承他的Activity会透明吗???
		userManager = BmobUserManager.getInstance(MyApp.context);
		chatManager = BmobChatManager.getInstance(MyApp.context);
		bmobDB = BmobDB.create(MyApp.context);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		initLayout();
		init();
	}

	/**
	 * 由子类选择性重写
	 */
	public void init() {
		// NO_OP “钩子方法”
	}

	private void initLayout() {
		// 尝试调用setContentView(layoutId)方法
		// 尝试根据类名(例如：MainActivity)--->资源文件的名字(activity_main)
		String clazzName = this.getClass().getSimpleName();// MainAcitivity
		if (clazzName.contains("Activity")) {
			String activityName = clazzName.substring(0,
					clazzName.indexOf("Activity")).toLowerCase(Locale.US);// main
			String resName = "activity_" + activityName;// activity_main
			// 根据resName找到其对应的resId(根据activity_main--->R.layout.activity_main)
			int resId = getResources().getIdentifier(resName, "layout",
					getPackageName());
			if (resId != 0) {
				// 确实找到了资源ID(R.layout.activity_main)
				setContentView(resId);
			} else {
				setMyContentView();
			}

		} else {
			setMyContentView();
		}

		ButterKnife.bind(this);

		headerView = findViewById(R.id.headerview);

	}

	/**
	 * 必须由子类重写 提供子类所使用的布局文件的名称
	 */
	public abstract void setMyContentView();

}
