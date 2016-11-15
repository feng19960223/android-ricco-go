package com.fgr.miaoxin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;

import com.fgr.miaoxin.R;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_login);

	}

	@OnClick(R.id.tv_login_regist)
	public void regist(View v) {
		Intent intent = new Intent(this, RegistActivity.class);
		startActivity(intent);
		finish();
	}

}
