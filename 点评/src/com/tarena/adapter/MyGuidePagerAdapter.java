package com.tarena.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.tarena.fragment.FragmentA;
import com.tarena.fragment.FragmentB;
import com.tarena.fragment.FragmentC;
import com.tarena.fragment.FragmentD;

public class MyGuidePagerAdapter extends FragmentPagerAdapter {

	List<Fragment> list;

	public MyGuidePagerAdapter(FragmentManager fm) {
		super(fm);
		list = new ArrayList<Fragment>();
		list.add(new FragmentA());
		list.add(new FragmentB());
		list.add(new FragmentC());
		list.add(new FragmentD());
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
