package com.fgr.bmobdemo.ui;

import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyUser;

/**
 * 使用注释的方法,方法名可以不一样,但是返回值和参数列表必须一样
 *
 */
public class MainActivity extends Activity {
	@Bind(R.id.et_main_username)
	EditText etUsername;
	@Bind(R.id.et_main_password)
	EditText etPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
	}

	public void regist(View view) {
		Intent intent = new Intent(this, RegistActivity.class);
		startActivity(intent);
	}

	@OnClick(R.id.btn_main_login)
	public void login(View v) {
		String username = etUsername.getText().toString();
		final String password = etPassword.getText().toString();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			return;
		}

		// 根据用户输入的username在服务器MyUser数据表中查找是否有这样的用户
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		// 增加一个查询条件
		query.addWhereEqualTo("username", username);
		// 发起查询
		query.findObjects(this, new FindListener<MyUser>() {
			@Override
			public void onSuccess(final List<MyUser> arg0) {
				// 服务器对查询的请求做出了响应
				// 将查询结果作为参数传入到onSuccess方法中
				if (arg0 != null && arg0.size() > 0) {
					// 意味着服务器的MyUser数据表中存在"username"
					// 为用户输入username这样的用户
					// 进行密码的比对
					// 用户登录时输入的密码
					String md5 = new String(Hex.encodeHex(DigestUtils
							.sha(password))).toUpperCase();
					// 用户注册时保留在服务器上的密码
					String pwd = arg0.get(0).getPassword();
					if (md5.equals(pwd)) {
						// 密码一致，登录成功

						// ------------------------
						// 发送一个消息给所有设备,告知大家,xxx登录了
						// BmobPushManager<BmobInstallation> manager = new
						// BmobPushManager<BmobInstallation>(
						// MainActivity.this);
						// manager.pushMessageAll(arg0.get(0).getUsername()
						// + "上线了");
						// 然后receiver中,判断,如果是自己好友,提醒,否则不提醒
						// ------------------------

						BmobPushManager<BmobInstallation> manager = new BmobPushManager<BmobInstallation>(
								MainActivity.this);
						JSONObject jsonObject = new JSONObject();
						try {
							jsonObject.put("tag", "online");
							jsonObject.put("user", arg0.get(0).getUsername()
									+ "上线了");
							jsonObject.put("tiem", System.currentTimeMillis());
						} catch (Exception e) {
						}
						manager.pushMessageAll(jsonObject);
						// 更新设备id
						MyUser user = arg0.get(0);
						user.setInstallationId(BmobInstallation
								.getInstallationId(MainActivity.this));
						user.update(MainActivity.this, new UpdateListener() {

							@Override
							public void onSuccess() {
								// 跳转界面
								Intent intent = new Intent(MainActivity.this,
										ShowActivity.class);
								intent.putExtra("user", arg0.get(0));
								startActivity(intent);
								finish();
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								Toast.makeText(MainActivity.this,
										"更新设备ID失败,请稍后重试", Toast.LENGTH_SHORT)
										.show();
							}
						});

					} else {
						Toast.makeText(MainActivity.this, "用户名或密码错误",
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(MainActivity.this, "用户名或密码错误",
							Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(MainActivity.this,
						"查询失败，请稍后重试。错误代码：" + arg0 + "," + arg1,
						Toast.LENGTH_SHORT).show();
			}
		});
	}
}
