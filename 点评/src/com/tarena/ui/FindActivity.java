package com.tarena.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BaiduMap.SnapshotReadyCallback;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tarena.app.MyApp;
import com.tarena.entity.Business.Businesses;
import com.tarena.groupon.R;

//假如用到位置提醒功能，需要import该类

public class FindActivity extends Activity {
	private Businesses businesses;
	private String from;// detail,main//从detail跳转还是main跳转的
	private MapView mMapView = null;
	private BaiduMap baiduMap = null;
	public LocationClient mLocationClient = null;
	public BDLocationListener myListener = new MyLocationListener();
	private Button button1 = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		button1 = (Button) findViewById(R.id.button1);
		businesses = (Businesses) getIntent().getSerializableExtra("business");
		from = getIntent().getStringExtra("from");
		// 获取地图控件引用
		mMapView = (MapView) findViewById(R.id.bmapView);
		initBaiduMap();
		if ("detail".equals(from)) {
			// 从商铺详情页面跳转过来的
			// 将商品地址显示在地图上
			showAddress();
			button1.setVisibility(View.INVISIBLE);
		} else {// main
			// 定位
			showLocation();
			button1.setVisibility(View.VISIBLE);
		}
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker arg0) {
				Bundle bundle = arg0.getExtraInfo();
				if (bundle != null) {
					String name = bundle.getString("name");
					String address = bundle.getString("address");
					double d = DistanceUtil.getDistance(MyApp.lastpoint,
							arg0.getPosition());
					d = Math.round(d * 10000) / 10000;
					// 在地图上添加信息窗
					TextView view = new TextView(FindActivity.this);
					view.setText(name + "\n" + address + "\n" + d + "米");
					view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
					view.setTextColor(Color.WHITE);
					int padding = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
									.getDisplayMetrics());
					view.setPadding(padding, padding, padding, padding);
					view.setBackgroundColor(Color.BLACK);
					InfoWindow infowindow = new InfoWindow(view, arg0
							.getPosition(), -80);
					baiduMap.showInfoWindow(infowindow);
				}
				return false;
			}
		});
	}

	public void snap(View view) {// 截屏
		baiduMap.snapshot(new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap arg0) {
				// 保存的位置由stream决定
				try {
					OutputStream stream = new FileOutputStream(new File(
							Environment.DIRECTORY_PICTURES, System
									.currentTimeMillis() + ".png"));
					arg0.compress(CompressFormat.PNG, 100, stream);// 压缩并保存
					Toast.makeText(FindActivity.this, "截图完毕",
							Toast.LENGTH_SHORT).show();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}

	// 查找
	public void shou(View view) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("请选择");
		builder.setIcon(android.R.drawable.ic_menu_info_details);
		final String[] items = new String[] { "美食", "银行", "电影院", "宾馆", "酒吧" };
		builder.setItems(items, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String keyword = items[which];
				showAround(keyword);
			}
		});
		builder.create().show();
	}

	private void showAround(final String keyword) {
		// point of interest//兴趣点poi
		PoiSearch poiSearch = PoiSearch.newInstance();
		poiSearch
				.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {

					@Override
					public void onGetPoiResult(PoiResult arg0) {
						if (arg0 == null
								|| arg0.error != SearchResult.ERRORNO.NO_ERROR) {
							Toast.makeText(FindActivity.this, "附近没有" + keyword,
									Toast.LENGTH_SHORT).show();
							return;
						}
						List<PoiInfo> pois = arg0.getAllPoi();
						baiduMap.clear();
						for (PoiInfo poiInfo : pois) {
							// 将每一个poi的位置都作为一个覆盖物
							MarkerOptions option = new MarkerOptions();
							option.position(poiInfo.location);
							option.icon(BitmapDescriptorFactory
									.fromResource(R.drawable.home_scen_icon_locate));
							Marker marker = (Marker) baiduMap
									.addOverlay(option);
							Bundle bundle = new Bundle();
							bundle.putString("name", poiInfo.name);
							bundle.putString("address", poiInfo.address);
							marker.setExtraInfo(bundle);
						}
						// 添加自己的位置,clear会清除自己的位置
						MarkerOptions option = new MarkerOptions();
						option.position(MyApp.lastpoint);
						option.icon(BitmapDescriptorFactory
								.fromResource(R.drawable.shop_footerbar_locate_checked));
						baiduMap.addOverlay(option);

					}

					@Override
					public void onGetPoiDetailResult(PoiDetailResult arg0) {

					}
				});
		PoiNearbySearchOption option = new PoiNearbySearchOption();
		// 1.搜索的中心点
		option.location(MyApp.lastpoint);
		// 2.搜索的半径(单位:米)
		option.radius(1000);
		// 3.搜索的内容
		option.keyword(keyword);
		poiSearch.searchNearby(option);
	}

	private void showLocation() {
		mLocationClient = new LocationClient(getApplicationContext()); // 声明LocationClient类
		mLocationClient.registerLocationListener(myListener); // 注册监听函数
		initLocation();
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);// 可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setCoorType("bd09ll");// 可选，默认gcj02，设置返回的定位结果坐标系
		int span = 1000 * 60 * 10;// 如果导航,或运动应用,定位时,不能有怎么长的时间
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
	}

	private void initBaiduMap() {
		baiduMap = mMapView.getMap();
		// 调整比例尺
		baiduMap.setMaxAndMinZoomLevel(20, 16);// 设置缩放级别
	}

	private void showAddress() {
		// 根据地址得到经纬度(地理位置编码)
		GeoCoder geoCoder = GeoCoder.newInstance();
		geoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
			@Override
			public void onGetReverseGeoCodeResult(ReverseGeoCodeResult arg0) {
				// 根据经纬度查询地址(反向地理位置编码查询)
			}

			@Override
			public void onGetGeoCodeResult(GeoCodeResult arg0) {
				// 根据地址查询经纬度
				if (arg0 == null || arg0.error != SearchResult.ERRORNO.NO_ERROR) {
					Toast.makeText(FindActivity.this, "服务器繁忙,请稍后重试",// 其实是没地址,善意一下,别说你没有
							Toast.LENGTH_SHORT).show();
					return;
				}
				LatLng latLng = arg0.getLocation();// 经纬度,方便用户体验,应该把这个位置设置为中心点

				// 根据经纬度将该位置呈现在百度地图上
				// OverlayOptions option;
				MarkerOptions option = new MarkerOptions();// 3.0版本
				// 覆盖物添加的位置
				option.position(latLng);
				// 覆盖物使用的图标
				option.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.home_scen_icon_locate));
				baiduMap.addOverlay(option);// 添加覆盖物

				// 将屏幕中心点从北京天安门,移动到商户的位置
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
						.newLatLng(latLng);
				baiduMap.animateMapStatus(mapStatusUpdate);

				// 在地图上添加信息窗
				TextView view = new TextView(FindActivity.this);
				view.setText(businesses.getAddress());
				view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				view.setTextColor(Color.WHITE);
				int padding = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
								.getDisplayMetrics());
				view.setPadding(padding, padding, padding, padding);
				view.setBackgroundColor(Color.BLACK);
				InfoWindow infowindow = new InfoWindow(view, latLng, -80);
				baiduMap.showInfoWindow(infowindow);
			}
		});
		GeoCodeOption option = new GeoCodeOption();
		option.address(businesses.getAddress());
		option.city(businesses.getCity());// 防止重名,限制城市
		geoCoder.geocode(option);// 发起地理编码查询
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

	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			int code = location.getLocType();
			double lat = -1;
			double lng = -1;
			String address = "";
			if (code == 61 || code == 66 || code == 161) {
				// 61 ： GPS定位结果，GPS定位成功。
				// 66 ： 离线定位结果。通过requestOfflineLocaiton调用时对应的返回结果。
				// 161： 网络定位结果，网络定位成功。
				// 定位成功
				lat = location.getLatitude();
				lng = location.getLongitude();
				address = location.getAddrStr();
			} else {
				// 定位不成功
				// 手动指定经纬度,北京天安门国旗
				lat = 40;
				lng = 116;
				address = "北京天安门";
			}
			LatLng latLng = new LatLng(lat, lng);

			MyApp.lastpoint = new LatLng(lat, lng);

			MarkerOptions option = new MarkerOptions();
			option.position(latLng);
			option.icon(BitmapDescriptorFactory
					.fromResource(R.drawable.shop_footerbar_locate_checked));
			baiduMap.addOverlay(option);
			MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory
					.newLatLng(latLng);
			baiduMap.animateMapStatus(mapStatusUpdate);
			TextView view = new TextView(FindActivity.this);

			view.setText(address);
			view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			view.setTextColor(Color.WHITE);
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 8, getResources()
							.getDisplayMetrics());
			view.setPadding(padding, padding, padding, padding);
			view.setBackgroundColor(Color.BLACK);
			InfoWindow infowindow = new InfoWindow(view, latLng, -80);
			baiduMap.showInfoWindow(infowindow);

			// 停止继续发起定位请求
			if (mLocationClient.isStarted()) {
				mLocationClient.unRegisterLocationListener(myListener);// 解绑
				mLocationClient.stop();// 停止
			}
		}
	}
}
