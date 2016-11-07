package com.tarena.app;

import java.util.List;

import com.tarena.entity.CityNameBean;
import com.tarena.utils.SPUtil;

import android.app.Application;

public class MyApp extends Application {
	public static MyApp context;
	// 用来缓存CityActivity中的城市名称的内存缓存
	public static List<CityNameBean> cities;// 应该保证缓存数据的独立性

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		new SPUtil(context).setCloseBanner(false);// 每次启动有广告,用户退出时,本次不显示
	}

}
