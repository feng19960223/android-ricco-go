package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import butterknife.Bind;
import butterknife.OnClick;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.MyPagerAdapter;
import com.fgr.miaoxin.view.MyTabIcon;

public class MainActivity extends BaseActivity {

	@Bind(R.id.vp_main_viewpager)
	ViewPager viewPager;
	MyPagerAdapter adapter;

	@Bind(R.id.mti_main_message)
	MyTabIcon mtiMessage;
	@Bind(R.id.mti_main_friend)
	MyTabIcon mtiFriend;
	@Bind(R.id.mti_main_find)
	MyTabIcon mtiFind;
	@Bind(R.id.mti_main_setting)
	MyTabIcon mtiSetting;

	MyTabIcon[] tabIcons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void init() {
		super.init();
		initViewPager();
		initView();
	}

	private void initView() {
		tabIcons = new MyTabIcon[4];
		tabIcons[0] = mtiMessage;
		tabIcons[1] = mtiFriend;
		tabIcons[2] = mtiFind;
		tabIcons[3] = mtiSetting;
		for (MyTabIcon mti : tabIcons) {
			mti.setPaintAlpha(0);
		}
		tabIcons[0].setPaintAlpha(255);
	}

	private void initViewPager() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				for (MyTabIcon mti : tabIcons) {
					mti.setPaintAlpha(0);
				}
				tabIcons[arg0].setPaintAlpha(255);
			}

			/**
			 * arg0 页码 arg1 滑动百分比 arg2 滑动的像素数
			 * 
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg0 < 3) {
					// 颜色由彩色----->灰色变化
					// alpha是255----->0
					tabIcons[arg0].setPaintAlpha((int) (255 * (1 - arg1)));
					// 颜色由灰色----->彩色变化
					// alpha是0----->255
					tabIcons[arg0 + 1].setPaintAlpha((int) (255 * arg1));
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@OnClick({ R.id.mti_main_message, R.id.mti_main_friend, R.id.mti_main_find,
			R.id.mti_main_setting })
	public void setCurrentFragment(View v) {
		switch (v.getId()) {
		case R.id.mti_main_message:
			viewPager.setCurrentItem(0, false);
			break;
		case R.id.mti_main_friend:
			viewPager.setCurrentItem(1, false);
			break;
		case R.id.mti_main_find:
			viewPager.setCurrentItem(2, false);
			break;
		default:
			viewPager.setCurrentItem(3, false);
			break;
		}
	}

}
