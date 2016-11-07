package com.tarena.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.tarena.adapter.MyCityAdapter;
import com.tarena.app.MyApp;
import com.tarena.entity.City;
import com.tarena.entity.CityNameBean;
import com.tarena.groupon.R;
import com.tarena.utils.DBUtil;
import com.tarena.utils.HttpUtil;
import com.tarena.utils.PinYinUtil;
import com.tarena.view.MyLetterView;
import com.tarena.view.MyLetterView.onTouchLetterListener;

public class CityActivity extends Activity {
	private TextView textView_title1 = null;
	private TextView textView_title2 = null;
	private TextView textView_search = null;
	private ImageView imageView_back = null;
	private ListView listView_city = null;
	private ArrayList<String> dates = null;
	private MyCityAdapter adapter = null;
	private MyLetterView myLetterView_letters = null;

	DBUtil dbUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		dbUtil = new DBUtil(this);
		initTitle();
		initMain();
		refresh();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK && requestCode == 101) {// 必须有判断,否则报错:没有回传的数据
			setResult(RESULT_OK, data);
			finish();
		}
	}

	private void refresh() {
		// 查询内存缓存是否有数据,一级缓存
		if (MyApp.cities != null && MyApp.cities.size() > 0) {
			Log.d("TAG", "数据从内存加载");
			// adapter.addAll(MyApp.cities, true);
			// 数据分离,以后的增删改,不会影响缓存数据
			adapter.addAll(new ArrayList<CityNameBean>(MyApp.cities), true);
			return;
		}
		// 查询数据库,二级缓存,ormlite操作数据库框架
		List<CityNameBean> list = dbUtil.queryAll();
		if (list != null && list.size() > 1) {
			Log.d("TAG", "数据从数据库加载");
			adapter.addAll(new ArrayList<CityNameBean>(list), true);
			MyApp.cities = new ArrayList<CityNameBean>(list);
			return;
		}

		// 从网络加载
		HttpUtil.getCity(new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				City city = new Gson().fromJson(arg0, City.class);
				dates = city.getCities();
				final List<CityNameBean> cbs = new ArrayList<CityNameBean>();
				for (String string : dates) {
					CityNameBean cb = new CityNameBean();
					cb.setCityName(string);
					if ("全国".equals(string)) {
						cb.setPyName("AAAAA");
						cb.setLetter('*');
					} else {
						cb.setPyName(PinYinUtil.getPinYin(string));
						cb.setLetter(PinYinUtil.getFirstLetter(string));
					}
					cbs.add(cb);
				}
				// 排序
				Collections.sort(cbs, new Comparator<CityNameBean>() {
					@Override
					public int compare(CityNameBean lhs, CityNameBean rhs) {
						return lhs.getPyName().compareTo(rhs.getPyName());
					}
				});

				MyApp.cities = new ArrayList<CityNameBean>(cbs);// 把数据缓存到内存
				// 修改cbs,不会影响缓存数据
				new Thread() {
					public void run() {
						// dbUtil.saveAll(new ArrayList<CityNameBean>(cbs));//
						// 把数据保存到数据库,可能是一个耗时操作
						dbUtil.addBatch(new ArrayList<CityNameBean>(cbs));// 把数据保存到数据库,可能是一个耗时操作
					};
				}.start();
				adapter.addAll(cbs, true);
			}
		});
	}

	private void initMain() {
		listView_city = (ListView) findViewById(R.id.listView_city);
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.include_city_pull, listView_city,
				false);
		listView_city.addHeaderView(view);
		adapter = new MyCityAdapter(this, new ArrayList<CityNameBean>());
		listView_city.setAdapter(adapter);
		listView_city.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {// 第一个是视图,没有item点击事件
					return;
				}
				// 为什么-1,有一个头布局
				String cityName = adapter.getItem(position - 1).getCityName();
				Intent intent = new Intent(CityActivity.this,
						MainActivity.class);
				intent.putExtra("CityNameKey", cityName);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		myLetterView_letters = (MyLetterView) findViewById(R.id.myLetterView_letters);
		myLetterView_letters
				.setOnTouchLetterListener(new onTouchLetterListener() {
					@Override
					public void onTouchLetter(String letter) {
						// 移动ListView
						if ("热门".equals(letter)) {
							listView_city.setSelection(0);
						} else {
							listView_city.setSelection(adapter
									.getPositionForSection(letter.charAt(0)) + 1);// 为什么+1:listview_city上有一个头布局
						}
					}
				});
	}

	private void initTitle() {
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		textView_title1 = (TextView) findViewById(R.id.textView_title1);
		textView_title2 = (TextView) findViewById(R.id.textView_title2);
		textView_search = (TextView) findViewById(R.id.textView3);
		textView_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CityActivity.this,
						SearchActivity.class);
				startActivityForResult(intent, 101);
			}
		});
		imageView_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		textView_title1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textView_title1.setTextColor(Color.parseColor("#FFFFFF"));
				textView_title2.setTextColor(Color.parseColor("#FF6633"));
				textView_title1
						.setBackgroundResource(R.drawable.zeus_tab_left_press);
				textView_title2
						.setBackgroundResource(R.drawable.zeus_tab_right_normal);
			}
		});
		textView_title2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				textView_title1.setTextColor(Color.parseColor("#FF6633"));
				textView_title2.setTextColor(Color.parseColor("#FFFFFF"));
				textView_title1
						.setBackgroundResource(R.drawable.zeus_tab_left_normal);
				textView_title2
						.setBackgroundResource(R.drawable.zeus_tab_right_press);
			}
		});
	}
}
