package com.tarena.app;

import java.util.List;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.tarena.entity.CityNameBean;
import com.tarena.utils.SPUtil;

import android.app.Application;

public class MyApp extends Application {
	public static MyApp context;
	// 用来缓存CityActivity中的城市名称的内存缓存
	public static List<CityNameBean> cities;// 应该保证缓存数据的独立性
	public static LatLng lastpoint;// 用来记录设备使用者的位置

	@Override
	public void onCreate() {
		super.onCreate();
		context = this;
		new SPUtil(context).setCloseBanner(false);// 每次启动有广告,用户退出时,本次不显示
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());

	}

}
