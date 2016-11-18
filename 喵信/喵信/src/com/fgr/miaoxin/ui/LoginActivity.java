package com.fgr.miaoxin.ui;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.dd.CircularProgressButton;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.NetUtil;

public class LoginActivity extends BaseActivity {
	@Bind(R.id.et_login_password)
	EditText etPassword;
	@Bind(R.id.et_login_username)
	EditText etUsername;
	@Bind(R.id.btn_login_login)
	CircularProgressButton btnLogin;

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
		setHeaderTitle("欢迎使用");
		setHeaderImage(Position.START, R.drawable.ic_launcher, false, null);
	}

	@OnClick(R.id.tv_login_regist)
	public void regist(View v) {// 注册
		// Intent intent = new Intent(this,RegistActivity.class);
		// startActivity(intent);
		// finish();
		jumpTo(RegistActivity.class, true);
	}

	@OnClick(R.id.btn_login_login)
	public void login(View v) {// 登录
		// 1)判空
		if (isEmpty(etUsername, etPassword)) {
			return;
		}
		// 2)判断是否有网络
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("您在没有网络的二次元 -_-!!");
			return;
		}
		// 3)登录操作
		// CircularProgressButton进入工作状态
		btnLogin.setIndeterminateProgressMode(true);
		btnLogin.setProgress(50);

		// BmobChatUser user = null;
		BmobChatUser user = new BmobChatUser();
		user.setUsername(etUsername.getText().toString());
		user.setPassword(etPassword.getText().toString());

		// 登录成功后，依次调用了如下两个方法：
		// checkAndBindInstallation
		// 当用户在当前设备登录后，会去_installation数据表中检查
		// 该用户是否在其它设备上依然保持着登录状态。如果是，则从当前设备
		// 向其余设备发送一个消息{"tag":"offline"}
		// 其余设备在收到该消息时，应该做让当前登录用户强行下线的处理
		// 当前设备在发送消息完毕后，更新当前设备在_installation数据表中对应数据记录的uid字段值
		// 更新为当前登录用户的用户名
		// updateInstallIdForUser
		// 更新当前登录用户在_user表中对应的数据记录的installId和deviceType两个字段的值
		// 更新为当前所使用设备的设备ID，deviceType改为“android”

		// userManager.login内部完成的内容类似:当前帐号已在其他设备登录,然后可以找回密码...,(不允许一个帐号同时登录两个设备)
		bmobUserManager.login(user, new SaveListener() {
			@Override
			public void onSuccess() {
				// 登录成功
				btnLogin.setProgress(100);
				// 更新登录用户的位置
				updateUserLocation(new UpdateListener() {
					@Override
					public void onSuccess() {
						jumpTo(MainActivity.class, true);
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						toastAndLog("无法获取您的位置", arg0, arg1);
					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				switch (arg0) {
				case 101:
					toast("用户名或密码错误");
					break;
				default:
					btnLogin.setProgress(-1);
					toastAndLog("登录失败,稍后重试", arg0, arg1);
					break;
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						btnLogin.setProgress(0);
					}
				}, 2000);
			}
		});
	}

	@OnTextChanged(R.id.et_login_password)
	public void recover1(Editable s) {
		btnLogin.setProgress(0);
	}

	@OnTextChanged(R.id.et_login_username)
	public void recover2(Editable s) {
		btnLogin.setProgress(0);
	}

}
