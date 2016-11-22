package com.fgr.miaoxin.ui;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import cn.bmob.v3.datatype.BmobGeoPoint;
import cn.bmob.v3.listener.FindListener;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NearFriendActivity extends BaseActivity {

	@Bind(R.id.mv_nearfriend_mapview)
	MapView mMapView;

	BaiduMap baiduMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_near_friend);
	}

	@Override
	public void init() {
		super.init();
		setHeaderTitle("附近的喵", Position.CENTER);
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});

		initBaiduMap();
	}

	private void initBaiduMap() {
		baiduMap = mMapView.getMap();

		baiduMap.setMaxAndMinZoomLevel(20, 15);
		// 添加Marker单击事件监听器
		baiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(Marker marker) {
				// 从点击的Marker中取出信息
				Bundle bundle = marker.getExtraInfo();

				String username = bundle.getString("username");
				String avatar = bundle.getString("avatar");
				String time = bundle.getString("time");
				double lat = bundle.getDouble("lat");
				double lng = bundle.getDouble("lng");

				// 获得信息窗的视图对象
				View infowindow = getLayoutInflater()
						.inflate(R.layout.infowindow_near_friend_layout,
								mMapView, false);

				TextView tvUsername = (TextView) infowindow
						.findViewById(R.id.tv_infowindow_nearfriend_username);
				TextView tvTime = (TextView) infowindow
						.findViewById(R.id.tv_infowindow_nearfriend_time);
				TextView tvDistance = (TextView) infowindow
						.findViewById(R.id.tv_infowindow_nearfriend_distance);
				ImageView ivAvatar = (ImageView) infowindow
						.findViewById(R.id.iv_infowindow_nearfriend_avatar);
				Button btnAdd = (Button) infowindow
						.findViewById(R.id.btn_infowindow_nearfriend_add);

				tvUsername.setText(username);

				tvTime.setText(time.split(" ")[0]);

				if (TextUtils.isEmpty(avatar)) {
					ivAvatar.setImageResource(R.drawable.ic_launcher);
				} else {
					ImageLoader.getInstance().displayImage(avatar, ivAvatar);
				}

				tvDistance.setText((int) (MyApp.lastPoint
						.distanceInKilometersTo(new BmobGeoPoint(lng, lat)) * 1000)
						+ "米");
				// 点击按钮应该向目标用户发送一个添加好友申请
				btnAdd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO

						// 通过用户名

						// BmobChatUser user = null;
						// BmobChatManager.getInstance(NearFriendActivity.this)
						// .sendTagMessage("add", user.getObjectId(),
						// new PushListener() {
						//
						// @Override
						// public void onSuccess() {
						// }
						//
						// @Override
						// public void onFailure(int arg0,
						// String arg1) {
						// LogUtil.d("TAG", "发送添加好友申请失败了,"
						// + arg0 + "," + arg1);
						// }
						// });

					}
				});
				// 创建信息窗
				InfoWindow window = new InfoWindow(infowindow, new LatLng(lat,
						lng), -30);
				// 在地图上显示信息窗
				baiduMap.showInfoWindow(window);

				return true;
			}
		});

		// 为百度地图添加一个单击事件监听器
		// 当点击地图时，隐藏信息窗
		baiduMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				baiduMap.hideInfoWindow();
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
		mMapView.onResume();
		refresh();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
		mMapView.onPause();
	}

	private void refresh() {
		// 搜索附近的人
		bmobUserManager.queryKiloMetersListByPage(false, 0, // 页码
				"location",// 表示位置的属性的名称
				MyApp.lastPoint.getLongitude(),// 搜索中心点的经度(当前登录用户位置的经度)
				MyApp.lastPoint.getLatitude(),// 搜索中心点的纬度(当前登录用户位置的纬度)
				false,// 是否搜索那些已经是当前登录用户好友的人
				5.0, // 搜索半径
				null, // 是否搜索时还需要额外的搜索条件
				null, // 如果指定了搜索条件，那么给定搜索条件的值
				new FindListener<MyUser>() {

					@Override
					public void onError(int arg0, String arg1) {
						toastAndLog("查询喵友时出现错误，请稍后重试", "查询好友时出现错误," + arg0
								+ ":" + arg1);

					}

					@Override
					public void onSuccess(List<MyUser> arg0) {
						if (arg0 != null && arg0.size() > 0) {

							showUserOnMap(arg0);

						} else {

							toast("附近一个喵友也没有/(ㄒoㄒ)/~~");
						}

					}
				});
	}

	/**
	 * 将附近的人以百度地图Marker的形式添加到地图上
	 * 
	 * @param users
	 *            搜索到的附近的人
	 */
	protected void showUserOnMap(List<MyUser> users) {
		// 移动地图中心点到当前登录用户位置(MyApp.lastPoint)
		MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(new LatLng(
				MyApp.lastPoint.getLatitude(), MyApp.lastPoint.getLongitude()));
		baiduMap.animateMapStatus(msu);
		// 遍历附近的人
		for (MyUser mu : users) {
			// 设置Marker的参数
			final MarkerOptions option = new MarkerOptions();
			option.position(new LatLng(mu.getLocation().getLatitude(), mu
					.getLocation().getLongitude()));

			// 根据用户性别设置Marker使用什么样的图片
			if (mu.getGender()) {
				option.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.boy));
			} else {
				option.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.girl));
			}
			// 将Marker添加到地图上
			Marker marker = (Marker) baiduMap.addOverlay(option);
			// 将用户的更多信息信息作为Marker的ExtraInfo添加到Marker中
			// 这些信息在Marker被点击的时候放到InfoWindow中呈现
			Bundle bundle = new Bundle();

			bundle.putString("username", mu.getUsername());
			bundle.putString("avatar", mu.getAvatar());
			bundle.putString("time", mu.getUpdatedAt());
			bundle.putDouble("lat", mu.getLocation().getLatitude());
			bundle.putDouble("lng", mu.getLocation().getLongitude());
			bundle.putString("objectId", mu.getObjectId());

			marker.setExtraInfo(bundle);

		}
	}
}
