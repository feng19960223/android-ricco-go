package com.fgr.miaoxin.ui;

import java.util.Locale;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.WindowUtil;

public abstract class BaseActivity extends FragmentActivity {

	// 公共属性
	// BmobIMSDK提供的一个工具类
	// 用来“管理”用户（用户的登录，登出，添加好友，删除好友）
	BmobUserManager bmobUserManager;
	// BmobIMSDK提供的一个工具类
	// 用来“管理”聊天内容（聊天内容的创建、发送、删除、存储）
	BmobChatManager bmobChatManager;
	// BmobIMSDK提供的一个工具类
	// 用来“管理”本地数据库
	BmobDB bmobDB;

	Toast toast;

	View headerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowUtil.translucent(getWindow());
		bmobUserManager = BmobUserManager.getInstance(MyApp.context);
		bmobChatManager = BmobChatManager.getInstance(MyApp.context);
		bmobDB = BmobDB.create(MyApp.context);
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		MyApp.activities.add(this);
		initLayout();
		init();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApp.activities.remove(this);
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

	// 设置headerView
	// 设置headerView中的标题
	public void setHeaderTitle(String title) {
		// TextView tv = (TextView)
		// headerView.findViewById(R.id.tv_headerview_title);
		// if(title==null){
		// tv.setText("");
		// }else{
		// tv.setText(title);
		// }
		setHeaderTitle(title, Constant.CENTER);
	}

	public void setHeaderTitle(String title, int position) {
		TextView tv = (TextView) headerView
				.findViewById(R.id.tv_headerview_title);
		switch (position) {
		case Constant.START:
			tv.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			break;
		case Constant.CENTER:
			tv.setGravity(Gravity.CENTER);
			break;
		default:
			tv.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
			break;
		}
		if (title == null) {
			tv.setText("");
		} else {
			tv.setText(title);
		}

	}

	public void setHeaderTitle(String title, Position position) {
		switch (position) {
		case START:
			setHeaderTitle(title, Constant.START);
			break;
		case CENTER:
			setHeaderTitle(title, Constant.CENTER);
			break;
		case END:
			setHeaderTitle(title, Constant.END);
			break;
		}
	}

	/**
	 * 设定HeaderView的ImageView
	 * 
	 * @param pos
	 *            设定左侧/右侧的ImageView
	 * @param resId
	 *            ImageView中显示图片的资源ID
	 * @param colorFilter
	 *            是否需要为ImageView中显示的图像添加白色的前景 如果不需要就传入false
	 * @param listener
	 *            是否需要为ImageView添加单击事件监听器 如果不需要就传入null
	 */
	public void setHeaderImage(Position pos, int resId, boolean colorFilter,
			OnClickListener listener) {

		ImageView imageView;

		if (pos == Position.START) {
			imageView = (ImageView) headerView
					.findViewById(R.id.iv_headerview_left);
		} else {
			imageView = (ImageView) headerView
					.findViewById(R.id.iv_headerview_right);
		}

		imageView.setImageResource(resId);

		if (colorFilter) {
			imageView.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
		}

		if (listener != null) {
			imageView.setOnClickListener(listener);
		}
	}

	// 输出吐丝和打印log相关方法
	public void toast(String text) {
		if (!TextUtils.isEmpty(text)) {
			toast.setText(text);
			toast.show();
		}
	}

	public void log(String log) {
		if (Constant.DEBUG)
			Log.d(Constant.TAG, getClass().getSimpleName() + "输出的日志：" + log);
	}

	public void toastAndLog(String text, String log) {
		toast(text);
		log(log);
	}

	public void log(String log, int error, String msg) {
		log(log + ",错误代码：" + error + ": " + msg);
	}

	public void toastAndLog(String text, int error, String msg) {
		toast(text);
		log(text + ",出现错误，错误代码：" + error + ": " + msg);
	}

	// 界面跳转的相关方法
	public void jumpTo(Class<?> clazz, boolean isFinish) {
		Intent intent = new Intent(this, clazz);
		startActivity(intent);
		if (isFinish) {
			this.finish();
		}
	}

	public void jumpTo(Intent intent, boolean isFinish) {
		startActivity(intent);
		if (isFinish) {
			this.finish();
		}
	}

	/**
	 * 判断是否有未输入内容的EditText
	 * 
	 * @param ets
	 *            用来检测的多个EditText
	 * @return true 有未输入内容的EditText false 所有的EditText都输入了内容
	 */
	public boolean isEmpty(EditText... ets) {

		for (EditText et : ets) {
			String text = et.getText().toString();
			if (TextUtils.isEmpty(text)) {
				// TODO
				// 出现不了内容,背景和前景颜色一样
				et.setError("请输入完整！");
				return true;
			}
		}

		return false;
	}

	/**
	 * 更新当前设备上登录用户的位置
	 */
	public void updateUserLocation(UpdateListener listener) {
		MyUser user = bmobUserManager.getCurrentUser(MyUser.class);
		if (user != null) {
			// 更新当前设备上登录用户的位置
			user.setLocation(MyApp.lastPoint);
			if (listener != null) {
				user.update(this, listener);
			} else {
				user.update(this);
			}
		}
	}

	// 手动为headerView属性赋值的方法
	public void setHeaderView(View headerView) {
		this.headerView = headerView;
	}
}
