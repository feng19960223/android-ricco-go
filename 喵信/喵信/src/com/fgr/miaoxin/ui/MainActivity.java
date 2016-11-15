package com.fgr.miaoxin.ui;

import android.app.Activity;
import android.os.Bundle;
import butterknife.ButterKnife;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.util.LogUtil;
import com.fgr.miaoxin.util.WindowUtil;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		WindowUtil.translucent(getWindow());// 透明状态栏
		ButterKnife.bind(this);// @Bind()使用
		LogUtil.d("定位信息:" + MyApp.lastPoint.getLatitude() + " / "
				+ MyApp.lastPoint.getLongitude());

	}
}
