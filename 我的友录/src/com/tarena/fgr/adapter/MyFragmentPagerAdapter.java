package com.tarena.fgr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 适配器
 * 
 * @author 冯国芮
 * 
 *         2016年9月29日 16:00:20
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
	// 集合用于保存适配给viewpager的所以的fragment
	private List<Fragment> fragments = new ArrayList<Fragment>();

	// FragmentPagerAdapter没有无参构造方法
	public MyFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	// 通过此方法将fragment添加到集合中
	public void addFragment(Fragment fragment) {
		if (fragment != null) {
			this.fragments.add(fragment);
		}
	}
}
