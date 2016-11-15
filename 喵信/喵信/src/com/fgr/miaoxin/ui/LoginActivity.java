package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant.Position;

public class LoginActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_login);

	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
	}

	private void initHeaderView() {
		setHeaderTitle("»¶Ó­Ê¹ÓÃ");
		setHeaderImage(Position.LEFT, R.drawable.ic_launcher, false, null);
	}

	@OnClick(R.id.tv_login_regist)
	public void regist(View v) {// ×¢²á
		// Intent intent = new Intent(this,RegistActivity.class);
		// startActivity(intent);
		// finish();
		jumpTo(RegistActivity.class, true);
	}

	@OnClick(R.id.btn_login_login)
	public void login(View v) {// µÇÂ¼

		// jumpTo(MainActivity.class, true);
	}

}
