package com.tarena.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tarena.groupon.R;

public class FragmentB extends BaseFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_guide_2, container,
				false);
		skip(R.id.imageView_skip, view);
		return view;
	}
}
