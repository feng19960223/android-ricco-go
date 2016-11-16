package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import butterknife.Bind;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;

/**
 * 喵信的欢迎页面
 * 
 * 1)定位 2)动画效果 3)界面跳转
 * 
 * @author pjy
 *
 */
public class SplashActivity extends BaseActivity {
	@Bind(R.id.tv_splash_miao)
	TextView tvMiao;// "喵"

	@Bind(R.id.tv_splash_xin)
	TextView tvXin;// "信"

	LocationClient client;// 百度地图定位客户端
	BDLocationListener listener;// 百度地图定位监听器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_splash);
		// ButterKnife.bind(this);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	public void init() {
		super.init();
		getLocation();
	}

	/**
	 * 发起定位
	 */
	private void getLocation() {
		client = new LocationClient(getApplicationContext()); // 声明LocationClient类
		listener = new MyLocationListener();
		client.registerLocationListener(listener); // 注册监听函数
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000 * 60 * 5;
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		client.setLocOption(option);
		// 发起定位请求
		client.start();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();
			double lat = -1;
			double lng = -1;
			if (code == 61 || code == 66 || code == 161) {
				// 定位成功
				lat = location.getLatitude();
				lng = location.getLongitude();
			} else {
				// 定位不成功
				// 手动指定一个经纬度北京潘家园建业苑写字楼
				lat = 39.876402;
				lng = 116.465049;
			}
			// 获得定位结果后，为MyApp.lastPoint属性赋值
			MyApp.lastPoint = new BmobGeoPoint(lng, lat);

			// 启动动画
			initAnim();

			// 停止继续发起定位请求
			if (client.isStarted()) {
				client.unRegisterLocationListener(listener);
				client.stop();
			}

		}

	}

	/**
	 * 播放动画效果
	 */
	public void initAnim() {
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
		tvMiao.startAnimation(anim);
		tvXin.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tvMiao.setVisibility(View.VISIBLE);
				tvXin.setVisibility(View.VISIBLE);
				// 动画结束后，跳转界面
				// 根据当前设备是否有处于登录状态的用户
				// BmobUserManager userManager =
				// BmobUserManager.getInstance(SplashActivity.this);
				BmobChatUser user = userManager.getCurrentUser();
				if (user != null) {
					// TODO 更新位置

					// 如果有，向MainActivity跳转
					// Intent intent = new
					// Intent(SplashActivity.this,MainActivity.class);
					// startActivity(intent);
					// finish();
					jumpTo(MainActivity.class, true);
				} else {
					// 如果没有，向LoginActivity跳转
					// Intent intent = new
					// Intent(SplashActivity.this,LoginActivity.class);
					// startActivity(intent);
					// finish();
					jumpTo(LoginActivity.class, true);
				}

			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (client != null) {
			client.stop();
			client = null;
		}

	}

}
