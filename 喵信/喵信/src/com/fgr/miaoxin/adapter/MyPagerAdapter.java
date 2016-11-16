package com.fgr.miaoxin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.fgr.miaoxin.fragment.FindFragment;
import com.fgr.miaoxin.fragment.FriendFragment;
import com.fgr.miaoxin.fragment.MessageFragment;
import com.fgr.miaoxin.fragment.SettingFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
	List<Fragment> fragments;

	public MyPagerAdapter(FragmentManager fm) {
		super(fm);
		fragments = new ArrayList<Fragment>();
		fragments.add(new MessageFragment());
		fragments.add(new FriendFragment());
		fragments.add(new FindFragment());
		fragments.add(new SettingFragment());
	}

	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

}
