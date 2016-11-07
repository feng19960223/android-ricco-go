package com.tarena.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.tarena.adapter.MyGuidePagerAdapter;
import com.tarena.groupon.R;
import com.viewpagerindicator.CirclePageIndicator;

public class GuideActivity extends FragmentActivity {
	private ViewPager viewPager = null;
	private MyGuidePagerAdapter adapter = null;
	private CirclePageIndicator circlePageIndicator = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViewPager();
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewPager_guide);
		circlePageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);

		adapter = new MyGuidePagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);

		circlePageIndicator.setViewPager(viewPager);

		// 通过方法设置
		// final float density = getResources().getDisplayMetrics().density;
		// circlePageIndicator.setBackgroundColor(0xFFCCCCCC);
		// circlePageIndicator.setRadius(10 * density);

		// 半径值10dp在当前设备上所对应的像素值
		// density 屏幕显示密度 linch内可以显示多少个像素点
		// dp是绝对长度，1dp=1/160inch 160dp=linch=25.4mm
		// 低密度 ldpi 120px/inch 1dp=0.75px
		// 中密度 mdpi 160px/inch 1dp=1px
		// 高密度 hdpi 240px/inch 1dp=1.5px
		// 很高密度 xhdpi 320px/inch
		// 很很高密度
		// circlePageIndicator.setPageColor(0x880000FF);
		// circlePageIndicator.setFillColor(0xFF888888);
		// circlePageIndicator.setStrokeColor(0xFF000000);
		// circlePageIndicator.setStrokeWidth(2 * density);

		// 如果为ViewPager指定了一个PagerIndicator的时候,
		// 再为ViewPager绑定滑动监听的时候,请绑定在PagerIndicator上
		circlePageIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// 第4个界面没有,指示器
				if (arg0 == 3) {
					circlePageIndicator.setVisibility(View.INVISIBLE);
				} else {
					circlePageIndicator.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
}
