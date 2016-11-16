package com.fgr.miaoxin.ui;

import java.util.List;

import android.os.Bundle;
import android.view.View;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatInstallation;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.util.LogUtil;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("定位信息:" + MyApp.lastPoint.getLatitude() + " / "
				+ MyApp.lastPoint.getLongitude());

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void init() {
		super.init();
	}

	@OnClick(R.id.btn_main_logout)
	public void logout(View v) {
		userManager.logout();// 登出,只解决了服务器Session中设备于服务器的解绑,但数据表没有修改
		// 解决_installation数据表中用户和设备的解绑
		BmobQuery<BmobChatInstallation> query = new BmobQuery<BmobChatInstallation>();
		query.addWhereEqualTo("installationId",
				BmobInstallation.getInstallationId(this));
		query.findObjects(this, new FindListener<BmobChatInstallation>() {

			@Override
			public void onSuccess(List<BmobChatInstallation> arg0) {
				BmobChatInstallation bci = arg0.get(0);
				// TODO 2016年11月16日 12:21:13
				bci.setUid("");
				bci.update(MainActivity.this, new UpdateListener() {

					@Override
					public void onSuccess() {
						jumpTo(LoginActivity.class, true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
					}
				});
			}

			@Override
			public void onError(int arg0, String arg1) {
			}
		});
		jumpTo(LoginActivity.class, true);
	}

}
