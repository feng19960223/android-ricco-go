package com.fgr.miaoxin.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.UploadFileListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant.Position;

public class LocationActivity extends BaseActivity {

	String from;// "mylocation"代表定位 showlocation代表显示位置
	MapView mMapView = null;

	BaiduMap baiduMap;// 地图对象

	ProgressDialog pd;// 截图操作中给用户提示

	// 定位客户端
	public LocationClient mLocationClient = null;
	// 定位监听器
	public BDLocationListener myListener = new MyLocationListener();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_location);

	}

	@Override
	public void init() {
		super.init();
		from = getIntent().getStringExtra("from");
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		// 获取地图对象
		baiduMap = mMapView.getMap();
		// 调整一下比例尺(16 比例尺200米 ~ 20 比例尺为10米)
		baiduMap.setMaxAndMinZoomLevel(20, 16);

		if ("mylocation".equals(from)) {
			// 显示当前用户的位置
			setHeaderTitle("我的位置");
			setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});

			setHeaderImage(Position.END, R.drawable.ic_map_snap, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							pd = ProgressDialog.show(LocationActivity.this, "",
									"截图中...");
							// 地图截图
							baiduMap.snapshot(new SnapshotReadyCallback() {

								@Override
								public void onSnapshotReady(Bitmap bitmap) {
									try {
										// 截图上传到Bmob文件服务器
										File file = new File(
												Environment
														.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
												System.currentTimeMillis()
														+ ".jpg");
										OutputStream stream = new FileOutputStream(
												file);
										bitmap.compress(CompressFormat.JPEG,
												30, stream);
										final String path = file
												.getAbsolutePath();
										final BmobFile bf = new BmobFile(file);
										bf.uploadblock(LocationActivity.this,
												new UploadFileListener() {

													@Override
													public void onSuccess() {
														final String url = bf
																.getFileUrl(LocationActivity.this);
														// 根据经纬度查找街道名称（反向地理位置查询）
														GeoCoder geoCoder = GeoCoder
																.newInstance();
														geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

															@Override
															public void onGetReverseGeoCodeResult(
																	ReverseGeoCodeResult arg0) {
																pd.dismiss();
																String address = "";
																if (arg0 == null
																		|| arg0.error != SearchResult.ERRORNO.NO_ERROR) {
																	address = "未知地址";
																} else {
																	address = arg0
																			.getAddress();
																}

																// 将所有结果回传到ChatActivity
																Intent data = new Intent();
																data.putExtra(
																		"lat",
																		MyApp.lastPoint
																				.getLatitude());
																data.putExtra(
																		"lng",
																		MyApp.lastPoint
																				.getLongitude());
																data.putExtra(
																		"address",
																		address);
																data.putExtra(
																		"url",
																		url);
																data.putExtra(
																		"path",
																		path);
																setResult(
																		RESULT_OK,
																		data);
																finish();

															}

															@Override
															public void onGetGeoCodeResult(
																	GeoCodeResult arg0) {
															}
														});

														ReverseGeoCodeOption option = new ReverseGeoCodeOption();
														option.location(new LatLng(
																MyApp.lastPoint
																		.getLatitude(),
																MyApp.lastPoint
																		.getLongitude()));
														geoCoder.reverseGeoCode(option);

													}

													@Override
													public void onFailure(
															int arg0,
															String arg1) {
														// TODO Auto-generated
														// method stub

													}
												});

									} catch (Exception e) {
										e.printStackTrace();
									}

								}
							});
						}
					});

			getMyLocation();
		} else {

			String address = getIntent().getStringExtra("address");
			setHeaderTitle(address);
			setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();
						}
					});

			showLocation();
		}
	}

	private void getMyLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数

		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000 * 60 * 5;// 根据业务需求（定位间隔设置为5分钟）
		option.setScanSpan(span);// 可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		option.setIsNeedAddress(true);// 可选，设置是否需要地址信息，默认不需要
		option.setOpenGps(true);// 可选，默认false,设置是否使用gps
		option.setLocationNotify(true);// 可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
		option.setIsNeedLocationDescribe(true);// 可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationPoiList(true);// 可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIgnoreKillProcess(false);// 可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
		option.SetIgnoreCacheException(false);// 可选，默认false，设置是否收集CRASH信息，默认收集
		option.setEnableSimulateGps(false);// 可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
		mLocationClient.setLocOption(option);
		mLocationClient.start();

	}

	private void showLocation() {
		// 点击了ChatActivity中位置聊天信息后
		// 在地图中显示相应的位置点
		double lat = getIntent().getDoubleExtra("lat", -1);
		double lng = getIntent().getDoubleExtra("lng", -1);

		MarkerOptions option = new MarkerOptions();
		option.position(new LatLng(lat, lng));
		option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
		baiduMap.addOverlay(option);

		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(lat,
				lng));
		baiduMap.animateMapStatus(msu);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
		if (mLocationClient != null) {
			mLocationClient.stop();
			mLocationClient = null;
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	private class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			int code = location.getLocType();
			double lat = -1;
			double lng = -1;

			if (code == 61 || code == 66 || code == 161) {
				// 定位成功了
				lat = location.getLatitude();
				lng = location.getLongitude();
			} else {
				// 定位失败了
				// 则手动指定一个值(我的策略)
				lat = MyApp.lastPoint.getLatitude();
				lng = MyApp.lastPoint.getLongitude();
			}
			LatLng mylocation = new LatLng(lat, lng);
			MarkerOptions option = new MarkerOptions();
			option.position(mylocation);
			option.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_marker));
			baiduMap.addOverlay(option);

			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mylocation);
			baiduMap.animateMapStatus(msu);

			TextView view = new TextView(LocationActivity.this);

			view.setText("我在这");
			view.setTextColor(Color.WHITE);
			view.setBackgroundColor(Color.RED);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
			view.setPadding(5, 5, 5, 5);
			InfoWindow infowindow = new InfoWindow(view, mylocation, -50);
			baiduMap.showInfoWindow(infowindow);

			// 是否还需要重复定位？
			// 我的逻辑，显示成功后，就停止定位
			if (mLocationClient.isStarted()) {
				mLocationClient.unRegisterLocationListener(myListener);
				mLocationClient.stop();
			}

			// 此时myLocation是最新一次的定位结果
			// 应该与MyApp.lastPoint进行比较，
			// 如果不一致，应该对MyApp.lastPoint进行更新
			if (mylocation.latitude != MyApp.lastPoint.getLatitude()
					|| mylocation.longitude != MyApp.lastPoint.getLongitude()) {
				// 更新MyApp.lastPoint
				MyApp.lastPoint = new BmobGeoPoint(mylocation.longitude,
						mylocation.latitude);
				// 更新当前登录用户在_user表中的位置
				updateUserLocation(null);
			}

		}

	}

}
