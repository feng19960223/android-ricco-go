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
import butterknife.Bind;
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

	String from;// "mylocation"代表需要定位 "showaddress"代表需要显示一个指定地址
	@Bind(R.id.bmapView)
	MapView mapView;
	BaiduMap baiduMap;

	LocationClient client;
	BDLocationListener listener;

	ProgressDialog pd;

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
		baiduMap = mapView.getMap();
		initBaiduMap();
		if ("mylocation".equals(from)) {
			// 定位
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
										File file = new File(
												Environment
														.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
												System.currentTimeMillis()
														+ ".jpg");
										OutputStream stream = new FileOutputStream(
												file);
										bitmap.compress(CompressFormat.JPEG,
												30, stream);
										final String localfilePath = file
												.getAbsolutePath();
										// 将地图截图上传到服务器
										final BmobFile bf = new BmobFile(file);
										bf.uploadblock(LocationActivity.this,
												new UploadFileListener() {

													@Override
													public void onSuccess() {
														final String url = bf
																.getFileUrl(LocationActivity.this);
														// 根据定位得到的经纬度，进行街道名称的查询
														GeoCoder geoCoder = GeoCoder
																.newInstance();
														geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

															@Override
															public void onGetReverseGeoCodeResult(
																	ReverseGeoCodeResult arg0) {
																pd.dismiss();
																// 根据给定的经纬度找到了对应的街道名称
																String address;
																if (arg0 == null
																		|| arg0.error != SearchResult.ERRORNO.NO_ERROR) {

																	address = "位置道路";

																} else {
																	address = arg0
																			.getAddress();

																}

																Intent data = new Intent();
																data.putExtra(
																		"address",
																		address);
																data.putExtra(
																		"localFilePath",
																		localfilePath);
																data.putExtra(
																		"url",
																		url);
																setResult(
																		RESULT_OK,
																		data);
																finish();

															}

															@Override
															public void onGetGeoCodeResult(
																	GeoCodeResult arg0) {
																// TODO
																// Auto-generated
																// method stub

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
														pd.dismiss();
														toastAndLog(
																"截图失败，稍后重试",
																arg0, arg1);

													}
												});

									} catch (Exception e) {
										if (pd != null)
											pd.dismiss();
										e.printStackTrace();
									}
								}
							});
						}
					});

			getMyLocation();

		} else {
			// 显示一个位置
			String address = getIntent().getStringExtra("address");
			setHeaderTitle(address);
			setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							finish();

						}
					});

			showAddress();
		}
	}

	private void showAddress() {
		double lat = getIntent().getDoubleExtra("lat", 0.0);
		double lng = getIntent().getDoubleExtra("lng", 0.0);

		LatLng location = new LatLng(lat, lng);

		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(location);

		baiduMap.animateMapStatus(msu);

		MarkerOptions option = new MarkerOptions();
		option.position(location);
		option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker));
		baiduMap.addOverlay(option);

	}

	private void getMyLocation() {
		client = new LocationClient(this);
		listener = new MyLocationListener();
		client.registerLocationListener(listener);

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
		client.start();

	}

	private void initBaiduMap() {
		baiduMap.setMaxAndMinZoomLevel(20, 15);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mapView.onDestroy();
		if (client != null) {
			client.stop();
			client = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mapView.onResume();

	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mapView.onPause();
	}

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();

			double lat = -1;
			double lng = -1;

			if (code == 61 || code == 66 || code == 161) {

				lat = location.getLatitude();
				lng = location.getLongitude();

			} else {

				lat = MyApp.lastPoint.getLatitude();
				lng = MyApp.lastPoint.getLongitude();
			}

			LatLng mylocation = new LatLng(lat, lng);

			// 移动屏幕中心点
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(mylocation);
			baiduMap.animateMapStatus(msu);
			// 放一个标志
			MarkerOptions option = new MarkerOptions();
			option.position(mylocation);
			option.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.ic_marker));
			baiduMap.addOverlay(option);

			// 放一个信息窗

			TextView textview = new TextView(LocationActivity.this);
			textview.setText("我在这");
			textview.setBackgroundColor(Color.RED);
			textview.setTextColor(Color.WHITE);
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 3, getResources()
							.getDisplayMetrics());
			textview.setPadding(padding, padding, padding, padding);
			InfoWindow infowindow = new InfoWindow(textview, mylocation, -50);
			baiduMap.showInfoWindow(infowindow);

			if (client.isStarted()) {
				client.stop();
				client.registerLocationListener(listener);
			}

			if (mylocation.latitude != MyApp.lastPoint.getLatitude()
					|| mylocation.longitude != MyApp.lastPoint.getLongitude()) {
				MyApp.lastPoint = new BmobGeoPoint(lng, lat);
				updateUserLocation(null);
			}

		}

	}

}
