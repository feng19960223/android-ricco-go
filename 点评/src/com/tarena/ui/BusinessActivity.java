package com.tarena.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response.Listener;
import com.google.gson.Gson;
import com.tarena.adapter.BusinessAdapter;
import com.tarena.entity.Business;
import com.tarena.entity.Business.Businesses;
import com.tarena.entity.Region;
import com.tarena.entity.Region.Cities;
import com.tarena.entity.Region.Cities.Districts;
import com.tarena.groupon.R;
import com.tarena.utils.HttpUtil;
import com.tarena.utils.SPUtil;
import com.tarena.view.MyBanner;
import com.tarena.view.MyBanner.onCloseBannerListener;

public class BusinessActivity extends Activity {
	private ImageView imageView_back = null;
	private TextView tv_businss_tab_textview1 = null;
	private ListView listView_cate = null;
	private BusinessAdapter adapter = null;

	private String cityName;
	private SPUtil spUtil;

	private FrameLayout frameLayout = null;

	private ListView listView1 = null;
	private ListView listView2 = null;
	private ArrayAdapter<String> adapter1 = null;
	private ArrayAdapter<String> adapter2 = null;
	private List<Districts> districts = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_business);
		spUtil = new SPUtil(this);
		cityName = getIntent().getStringExtra("city");// 没有取到是null
		if (TextUtils.isEmpty(cityName) || "全国".equals(cityName)) {
			cityName = "北京";
		}
		findViewById();
		setListener();
		refresh(null);// 默认加载所以
	}

	private void refresh(String region) {
		HttpUtil.getBusines(cityName, region, "美食", new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				Business business = new Gson().fromJson(arg0, Business.class);
				List<Businesses> businesses = business.getBusinesses();
				if (business.getCount() < 1) {
					Toast.makeText(BusinessActivity.this, "没有数据",
							Toast.LENGTH_SHORT).show();
					return;
				}
				adapter.addAll(businesses, true);
			}
		});

	}

	@SuppressLint("ShowToast")
	private void setListener() {
		imageView_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_businss_tab_textview1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (frameLayout.getVisibility() == View.VISIBLE) {
					frameLayout.setVisibility(View.GONE);
				} else {
					frameLayout.setVisibility(View.VISIBLE);
					refreshRegion();
				}
			}
		});

		listView1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter2.clear();
				List<String> list2 = new ArrayList<String>();
				if (position == 0) {// 附近
					list2.add("附加(智能范围)");
					list2.add("500米");
					list2.add("1000米");
					list2.add("2000米");
					list2.add("5000米");
				} else if (position == 1) {// 热门商区
					list2.add("全部商区");
					list2.add("无法");
					list2.add("从点评获得");
					list2.add("热门的数据");
					list2.add("使用测试数据");
				} else {
					list2.add("全部" + adapter1.getItem(position));// 街
					list2.addAll(districts.get(position - 2).getNeighborhoods());
				}
				adapter2.addAll(list2);
				adapter2.notifyDataSetChanged();
			}
		});

		listView2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 显示数据
				if (position == 0) {
					refresh(adapter2.getItem(position).substring(2));// 第一条加载所以区
				} else {
					refresh(adapter2.getItem(position));// 加载有街道的
				}
				// 关闭frameLayou
				frameLayout.setVisibility(View.GONE);
			}
		});
		listView_cate.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Businesses business = null;
				if (spUtil.isCloseBanner()) {// 没有广告
					business = adapter.getItem(position);
				} else {// 有广告
					business = adapter.getItem(position - 1);
				}
				Intent intent = new Intent(BusinessActivity.this,
						DetailActivity.class);
				// Businesses类和其内部类要实现Serializable接口
				intent.putExtra("business", business);
				startActivity(intent);
			}
		});
	}

	private void refreshRegion() {
		HttpUtil.getRegions(cityName, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				Region region = new Gson().fromJson(arg0, Region.class);
				List<Cities> cities = region.getCities();
				districts = cities.get(0).getDistricts();
				List<String> list1 = new ArrayList<String>();// 区
				list1.add("附近");
				list1.add("热门商区");
				for (int i = 0; i < districts.size(); i++) {
					list1.add(districts.get(i).getDistrict_name());
				}
				adapter1.clear();
				adapter1.addAll(list1);
				adapter1.notifyDataSetChanged();
				List<String> r = new ArrayList<String>();
				r.add("附加(智能范围)");
				r.add("500米");
				r.add("1000米");
				r.add("2000米");
				r.add("5000米");
				adapter2.clear();
				adapter2.addAll(r);
				adapter2.notifyDataSetChanged();
			}
		});

	}

	private void findViewById() {
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		tv_businss_tab_textview1 = (TextView) findViewById(R.id.tv_businss_tab_textview1);
		listView_cate = (ListView) findViewById(R.id.listView_cate);
		frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
		listView1 = (ListView) findViewById(R.id.listView1);
		listView2 = (ListView) findViewById(R.id.listView2);
		adapter = new BusinessAdapter(this,
				new ArrayList<Business.Businesses>());
		adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		adapter2 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);

		if (!spUtil.isCloseBanner()) {
			final MyBanner myBanner = new MyBanner(this, null);
			myBanner.setOnCloseBannerListener(new onCloseBannerListener() {
				@Override
				public void onClose() {
					listView_cate.removeHeaderView(myBanner);
					spUtil.setCloseBanner(true);
				}
			});
			listView_cate.addHeaderView(myBanner);
		}

		listView_cate.setAdapter(adapter);
		listView1.setAdapter(adapter1);
		listView2.setAdapter(adapter2);
	}

}
