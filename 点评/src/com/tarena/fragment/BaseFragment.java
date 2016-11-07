package com.tarena.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.View.OnClickListener;

import com.tarena.ui.MainActivity;

public class BaseFragment extends Fragment {

	public void skip(int resId, View view) {
		View v = (View) view.findViewById(resId);
		v.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), MainActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}
}
