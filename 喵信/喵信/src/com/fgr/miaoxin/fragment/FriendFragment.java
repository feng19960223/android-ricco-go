package com.fgr.miaoxin.fragment;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant.Position;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendFragment extends BaseFragment {
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_friend, container, false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		setHeaderTitle("ß÷ÓÑ", Position.CENTER);
	}
}
