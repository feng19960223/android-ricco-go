package com.tarena.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tarena.fragment.FragmentPull1_1;
import com.tarena.fragment.FragmentPull1_2;
import com.tarena.fragment.FragmentPull1_3;

public class MyMainPagerAdapter extends FragmentPagerAdapter {

	List<Fragment> list;

	public MyMainPagerAdapter(FragmentManager fm) {
		super(fm);
		list = new ArrayList<Fragment>();
		list.add(new FragmentPull1_1());
		list.add(new FragmentPull1_2());
		list.add(new FragmentPull1_3());
	}

	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
