package com.fgr.miaoxin.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.media.MediaPlayer;
import cn.bmob.im.BmobChat;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobChatInstallation;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.baidu.mapapi.SDKInitializer;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant;
import com.fgr.miaoxin.ui.LoginActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class MyApp extends Application {
	public static MyApp context;// 上下文
	public static MediaPlayer mediaPlayer;// 音效
	public static BmobGeoPoint lastPoint;// 可以存储在Bmob服务器上的位置(定位),在SplashActivity初始化
	public static List<Activity> activities;

	@Override
	public void onCreate() {
		super.onCreate();

		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());

		BmobChat.getInstance(this).init(Constant.BMOB_KEY);// 一句等下面3句
		// // 初始化BmobSDK
		// Bmob.initialize(this, Constant.BMOB_KEY);
		// // 使用推送服务时的初始化操作
		// BmobInstallation.getCurrentInstallation(this).save();
		// // 启动推送服务
		// BmobPush.startWork(this);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		context = this;
		mediaPlayer = MediaPlayer.create(this, R.raw.notify);// 如果音乐文件特别大,不可以使用
		// 做ImageLoader的全局初始化
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));
		activities = new ArrayList<Activity>();
	}

	public static void logout() {
		BmobUserManager userManager = BmobUserManager.getInstance(context);
		userManager.logout();// 登出,只解决了服务器Session中设备于服务器的解绑,但数据表没有修改
		// 解决_installation数据表中用户和设备的解绑
		BmobQuery<BmobChatInstallation> query = new BmobQuery<BmobChatInstallation>();
		query.addWhereEqualTo("installationId",
				BmobInstallation.getInstallationId(context));
		query.findObjects(context, new FindListener<BmobChatInstallation>() {

			@Override
			public void onSuccess(List<BmobChatInstallation> arg0) {
				BmobChatInstallation bci = arg0.get(0);
				bci.setUid("");// ???
				bci.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// 杀死(退出)所以Activity
						for (Activity activity : activities) {
							activity.finish();
						}
						Intent intent = new Intent(context, LoginActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						// 不是在Activity中启动Activity,要加这句代码
						// 但开新栈,退出就有了问题,所以要有上面的杀死所以Acitivity
						context.startActivity(intent);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
					}
				});
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
	}

}
