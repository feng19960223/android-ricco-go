package com.tarena.fgr.youlu;

import java.util.ArrayList;
import java.util.List;

import com.tarena.fgr.adapter.MyGuidePagerAdapter;
import com.tarena.fgr.view.CircleIndicator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 新手引导页面或者新功能演示界面
 * 
 * @author 冯国芮
 */
public class GuideActivity extends Activity implements OnPageChangeListener,
		OnClickListener {
	private ViewPager viewPager = null;
	private MyGuidePagerAdapter viewPagerAdapter = null;
	private List<View> views = null;
	private CircleIndicator circleIndicator = null;
	private Button goButton = null;// 开始体验

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		initViews();// 初始化视图
		viewPager.setOnPageChangeListener(this);
		goButton.setOnClickListener(this);
	}

	private void initViews() {
		// 初始化自定义的view
		circleIndicator = (CircleIndicator) findViewById(R.id.circleIndicator1);

		// 加载我们需要的视图
		LayoutInflater layoutInflater = LayoutInflater.from(this);
		views = new ArrayList<View>();
		views.add(layoutInflater.inflate(R.layout.guide_one, null));
		views.add(layoutInflater.inflate(R.layout.guide_two, null));
		views.add(layoutInflater.inflate(R.layout.guide_three, null));
		viewPagerAdapter = new MyGuidePagerAdapter(views);
		viewPager = (ViewPager) findViewById(R.id.viewpager_guide);
		viewPager.setAdapter(viewPagerAdapter);

		goButton = (Button) views.get(views.size() - 1)// 得到最后一个页面的按钮
				.findViewById(R.id.button_go);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int position, float offset, int arg2) {
		circleIndicator.updateDraw(position, offset);
	}

	@Override
	public void onPageSelected(int arg0) {
	}

	@Override
	public void onClick(View v) {// 按钮事件
		Intent intent = new Intent(GuideActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

}
