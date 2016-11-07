package com.tarena.fgr.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MyGuidePagerAdapter extends PagerAdapter {
	// 用来承载所以的view
	private List<View> views = null;

	public MyGuidePagerAdapter(List<View> views) {
		this.views = views;
	}

	@Override
	// 当view不需要的时候,可以将其销毁
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView(views.get(position));
	}

	@Override
	// 加载view的方法,类似于listview中的getview方法
	public Object instantiateItem(View container, int position) {
		((ViewPager) container).addView(views.get(position));
		return views.get(position);
	}

	@Override
	// 返回当前view的数量
	public int getCount() {
		return views.size();
	}

	@Override
	// 判断当前的view是不是我们需要的对象
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

}
