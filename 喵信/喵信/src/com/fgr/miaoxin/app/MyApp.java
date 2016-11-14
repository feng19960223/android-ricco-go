package com.fgr.miaoxin.app;

import com.baidu.mapapi.SDKInitializer;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.im.BmobChat;
import cn.bmob.v3.datatype.BmobGeoPoint;
import android.app.Application;
import android.media.MediaPlayer;

public class MyApp extends Application {
	public static MyApp context;// 上下文
	public static MediaPlayer mediaPlayer;// 音效
	public static BmobGeoPoint lastPoint;// 可以存储在Bmob服务器上的位置(定位),在SplashActivity初始化

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
	}

}
