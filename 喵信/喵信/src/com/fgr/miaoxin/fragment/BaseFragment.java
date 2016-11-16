package com.fgr.miaoxin.fragment;

import java.util.Locale;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.db.BmobDB;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.ui.BaseActivity;

public abstract class BaseFragment extends Fragment {

	BmobUserManager bmobUserManager;
	BmobChatManager bmobChatManager;
	BmobDB bmobDB;

	View headerView;

	BaseActivity baseActivity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bmobUserManager = BmobUserManager.getInstance(MyApp.context);
		bmobChatManager = BmobChatManager.getInstance(getActivity());
		bmobDB = BmobDB.create(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = null;
		// SettingFragment;
		String clazzName = getClass().getSimpleName();
		if (clazzName.contains("Fragment")) {
			// fragment_setting
			String layoutName = clazzName.substring(0,
					clazzName.indexOf("Fragment")).toLowerCase(Locale.US);
			layoutName = "fragment_" + layoutName;
			// R.layout.fragment_setting
			int resId = getResources().getIdentifier(layoutName, "layout",
					getActivity().getPackageName());

			if (resId != 0) {
				view = inflater.inflate(resId, container, false);
			} else {
				view = createMyView(inflater, container, savedInstanceState);
			}
		} else {
			view = createMyView(inflater, container, savedInstanceState);
		}

		ButterKnife.bind(this, view);
		headerView = view.findViewById(R.id.headerview);
		baseActivity = (BaseActivity) getActivity();

		init();

		return view;
	}

	public void init() {
		// No-OP 钩子方法
	}

	/**
	 * 由子类进行重写，提供视图
	 * 
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @return
	 */
	public abstract View createMyView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState);

}
