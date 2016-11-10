package com.tarena.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.tarena.adapter.DealsAdapter;
import com.tarena.entity.TuanGou;
import com.tarena.entity.TuanGou.Deals;
import com.tarena.groupon.R;
import com.tarena.utils.HttpUtil;

public class MainActivity extends FragmentActivity {
	private TextView textView_place = null;// 城市
	private ImageView imageView_menu = null;// 显示菜单
	private LinearLayout linearLayout_menu = null;// 菜单显示于隐藏
	private PullToRefreshListView pullToRefreshListView = null;// 主要下拉刷新
	private ListView listview = null;// 下拉刷新添加头信息
	private DealsAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTitle();
		initMain();
		initBottom();
		refresh();

	}

	@SuppressLint("SimpleDateFormat")
	private void refresh() {
		HttpUtil.getTuanInfo(textView_place.getText().toString(),
				new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// 告诉pullToRefreshListview刷新结束,隐藏头
						pullToRefreshListView.onRefreshComplete();
						TuanGou tuanGou = new Gson().fromJson(arg0,
								TuanGou.class);
						List<Deals> deals = tuanGou.getDeals();
						listview.removeHeaderView(view7);
						adapter.addAll(deals, true);
					}
				});
	}

	private void initBottom() {
		RadioButton radio_search = (RadioButton) findViewById(R.id.radio_search);
		radio_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						FindActivity.class);
				intent.putExtra("from", "main");
				startActivity(intent);
			}
		});
	}

	View view7;

	private void initMain() {
		pullToRefreshListView = (PullToRefreshListView) findViewById(R.id.pullToRefreshListView_main);
		listview = pullToRefreshListView.getRefreshableView();
		LayoutInflater inflater = getLayoutInflater();
		View list1 = inflater.inflate(R.layout.include_main_pull1, listview,
				false);
		initList1(list1);

		View list2 = inflater.inflate(R.layout.include_main_pull2, listview,
				false);
		View list3 = inflater.inflate(R.layout.include_main_pull3, listview,
				false);
		View list4 = inflater.inflate(R.layout.include_main_pull4, listview,
				false);
		View list5 = inflater.inflate(R.layout.include_main_pull5, listview,
				false);
		View list6 = inflater.inflate(R.layout.include_main_pull6, listview,
				false);
		view7 = inflater.inflate(R.layout.include_loading_layout, listview,
				false);// 加载中动画
		ImageView imageView_loading = (ImageView) view7
				.findViewById(R.id.imageView_loading);
		AnimationDrawable animation = (AnimationDrawable) imageView_loading
				.getDrawable();
		animation.start();
		listview.addHeaderView(list1);
		listview.addHeaderView(list2);
		listview.addHeaderView(list3);
		listview.addHeaderView(list4);
		listview.addHeaderView(list5);
		listview.addHeaderView(list6);
		listview.addHeaderView(view7);
		listview.setOnScrollListener(new OnScrollListener() { // 标题动画
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if (firstVisibleItem == 0) {
					textView_place.setVisibility(View.VISIBLE);// 可见
					imageView_menu.setVisibility(View.VISIBLE);
				} else {
					textView_place.setVisibility(View.GONE);// 不可见
					imageView_menu.setVisibility(View.GONE);
				}
			}
		});
		adapter = new DealsAdapter(this, new ArrayList<TuanGou.Deals>());
		pullToRefreshListView.setAdapter(adapter);
		pullToRefreshListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onRefresh(
							PullToRefreshBase<ListView> refreshView) {
						refresh();
					}
				});
	}

	private void initList1(View view) {// 滑动
		final ViewPager viewPager = (ViewPager) view
				.findViewById(R.id.viewPager_main);
		final ImageView imageView_banner_1 = (ImageView) view
				.findViewById(R.id.imageView_banner_1);
		final ImageView imageView_banner_2 = (ImageView) view
				.findViewById(R.id.imageView_banner_2);
		final ImageView imageView_banner_3 = (ImageView) view
				.findViewById(R.id.imageView_banner_3);

		PagerAdapter adapter = new PagerAdapter() {
			private int resId[] = new int[] { R.layout.include_main_pull1_1,
					R.layout.include_main_pull1_2,
					R.layout.include_main_pull1_3 };

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return 3000;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View view = getLayoutInflater().inflate(resId[position % 3],
						viewPager, false);

				// 点击“美食“按钮，跳转到商户页面BusinessActivity
				if (position % 3 == 0) {
					ImageView food = (ImageView) view
							.findViewById(R.id.imageView_home_food);
					food.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MainActivity.this,
									BusinessActivity.class);
							intent.putExtra("city", textView_place.getText()
									.toString());
							startActivity(intent);
						}
					});
				}

				container.addView(view);
				return view;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);
			}
		};
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(1500);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {// 不让下拉listview影响viewpager滑动事件
					@Override
					public void onPageSelected(int arg0) {
						// ViewPager一页滑动结束时,该方法会被调用
						listview.requestDisallowInterceptTouchEvent(false);
						imageView_banner_1
								.setImageResource(R.drawable.banner_dot);
						imageView_banner_2
								.setImageResource(R.drawable.banner_dot);
						imageView_banner_3
								.setImageResource(R.drawable.banner_dot);
						switch (arg0 % 3) {
						case 0:
							imageView_banner_1
									.setImageResource(R.drawable.banner_dot_pressed);
							break;
						case 1:
							imageView_banner_2
									.setImageResource(R.drawable.banner_dot_pressed);
							break;
						case 2:
							imageView_banner_3
									.setImageResource(R.drawable.banner_dot_pressed);
							break;
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// ViewPager滑动开始以及获得过程中,该方法会被调用
						// 请求不要拦截触摸事件
						listview.requestDisallowInterceptTouchEvent(true);
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
					}
				});

	}

	private void initTitle() {
		textView_place = (TextView) findViewById(R.id.textView_place);
		imageView_menu = (ImageView) findViewById(R.id.imageView_menu);
		linearLayout_menu = (LinearLayout) findViewById(R.id.include_main_menu);
		imageView_menu.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (linearLayout_menu.getVisibility() == View.VISIBLE) {
					linearLayout_menu.setVisibility(View.INVISIBLE);
				} else {
					linearLayout_menu.setVisibility(View.VISIBLE);
				}
			}
		});
		textView_place.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						CityActivity.class);
				startActivityForResult(intent, 101);
			}
		});
	}

	@Override
	// 接收startActivityForResult(intent, 101);的返回值
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if (resultCode == RESULT_OK && requestCode == 101) {
			// 从data中获取CityActivity转会的用户选择的城市
			String cityName = intent.getStringExtra("CityNameKey");
			textView_place.setText(cityName);
			refresh();
		}
	}

}
