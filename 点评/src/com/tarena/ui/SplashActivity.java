package com.tarena.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.tarena.groupon.R;
import com.tarena.utils.SPUtil;

public class SplashActivity extends Activity {
	SPUtil spUtil = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		spUtil = new SPUtil(this, "first");
		// 界面显示1.5秒后跳转
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// 判断用户是否是第一次运行
				Intent intent = null;
				if (spUtil.isFirst()) {
					spUtil.setFirst(false);
					// 第一次运行跳转到引导界面
					intent = new Intent(SplashActivity.this,
							GuideActivity.class);
				} else {
					// 不是第一次运行跳转到主页面
					intent = new Intent(SplashActivity.this, MainActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, 1500);
	}
}
