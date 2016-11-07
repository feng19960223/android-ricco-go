package com.tarena.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.tarena.app.MyApp;
import com.tarena.entity.CityNameBean;
import com.tarena.groupon.R;

public class SearchActivity extends Activity {
	private ImageView imageView_back = null;
	private EditText editText_search = null;
	private ListView listView_main = null;
	private List<String> cities;
	private ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		findViewById();
		setListenter();
	}

	private void setListenter() {
		editText_search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@SuppressLint("DefaultLocale")
			@Override
			public void afterTextChanged(Editable s) {// 每输入完毕一个后
				if (s.length() < 1) {// 输入一个,但删除了
					adapter.clear();
					return;
				} else {
					String cityName = s.toString().toUpperCase();
					getResult(cityName);
				}

			}
		});
		imageView_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listView_main.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 回传递CityActivity,再回传MainActivity
				String cityName = adapter.getItem(position);
				Intent intent = new Intent(SearchActivity.this,
						CityActivity.class);
				intent.putExtra("CityNameKey", cityName);// key要和MainActivity的一样
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	/**
	 * 根据用户输入的内容<br>
	 * 从MyApp.Cities中筛选出符合用户输入的内容,放到ListView中呈现
	 * 
	 * @param cityName
	 */
	protected void getResult(String cityName) {
		List<String> temp = new ArrayList<String>();
		if (cityName.matches("[\u4e00-\u9fff]{1,}")) {// 汉字1-多个
			for (CityNameBean cb : MyApp.cities) {
				if (cb.getCityName().contains(cityName)) {// 字符串包含contains
					temp.add(cb.getCityName());
				}
			}
		} else {// 拼音
			for (CityNameBean cb : MyApp.cities) {
				if (cb.getPyName().contains(cityName)) {// 字符串包含contains
					temp.add(cb.getCityName());
				}
			}
		}
		if (temp.size() > 0) {
			cities.clear();
			cities.addAll(temp);
			adapter.notifyDataSetChanged();
		}
	}

	private void findViewById() {
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		editText_search = (EditText) findViewById(R.id.editText1_search);
		listView_main = (ListView) findViewById(R.id.listView_search);
		cities = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, cities);
		listView_main.setAdapter(adapter);
	}
}
