package com.fgr.bmobdemo.app;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.constant.Constant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.bmob.push.BmobPush;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobInstallation;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;

public class MyApp extends Application {
	public static Context context;
	public static MediaPlayer palyer;

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		// 第一：默认初始化
		Bmob.initialize(this, Constant.BOMB_KEY);
		// 使用推送服务时的初始化操作
		BmobInstallation.getCurrentInstallation(this).save();
		// 启动推送服务
		BmobPush.startWork(this);
		// 做ImageLoader的全局初始化
		ImageLoader.getInstance().init(
				ImageLoaderConfiguration.createDefault(this));
		palyer = MediaPlayer.create(this, R.raw.newpost);// 如果音乐文件特别大,不可以使用
	}

}
