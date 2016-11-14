package com.fgr.bmobdemo.ui;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import cn.bmob.v3.BmobInstallation;
import cn.bmob.v3.BmobPushManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyPost;
import com.fgr.bmobdemo.bean.MyUser;

public class PostActivity extends Activity {
	@Bind(R.id.iv_header_back)
	ImageView ivBack;
	@Bind(R.id.et_post_title)
	EditText etTitle;
	@Bind(R.id.et_post_content)
	EditText etContent;
	MyUser user;
	String from;// update new
	@Bind(R.id.btn_post_post)
	Button btnPost;
	@Bind(R.id.btn_post_update)
	Button btnUpate;
	MyPost updatePost;// 从PostAdapter准备更新的帖子

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		ButterKnife.bind(this);
		user = (MyUser) getIntent().getSerializableExtra("user");
		initHeaderView();
		from = getIntent().getStringExtra("from");// update,new
		if ("new".equals(from)) {
			// 从ShowActivity点击头部“加号”按钮跳转过来
			btnPost.setVisibility(View.VISIBLE);
			btnUpate.setVisibility(View.GONE);
		} else {
			// 从PostAdpater点击“更新”跳转过来
			btnPost.setVisibility(View.GONE);
			btnUpate.setVisibility(View.VISIBLE);
			updatePost = (MyPost) getIntent().getSerializableExtra("post");
			etTitle.setText(updatePost.getTitle());
			etContent.setText(updatePost.getContent());
		}
	}

	private void initHeaderView() {
		ivBack.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
	}

	@OnClick(R.id.iv_header_back)
	public void backTo(View v) {
		finish();
	}

	@OnClick(R.id.btn_post_post)
	public void post(View v) {
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			return;
		}
		final MyPost post = new MyPost();
		post.setTitle(title);
		post.setContent(content);
		post.setUser(user);
		post.save(this, new SaveListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(PostActivity.this, "发帖成功", Toast.LENGTH_SHORT)
						.show();
				etTitle.setText("");
				etContent.setText("");
				push(post);
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(PostActivity.this,
						"发帖失败，错误代码：" + arg0 + "," + arg1, Toast.LENGTH_SHORT)
						.show();
			}
		});

	}

	protected void push(final MyPost myPost) {
		// 推送消息
		BmobPushManager<BmobInstallation> manager = new BmobPushManager<BmobInstallation>(
				this);
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("tag", "new");
			jsonObject.put("user", myPost.getUser().getUsername());
			jsonObject.put("time", System.currentTimeMillis());
		} catch (Exception e) {
		}
		manager.pushMessageAll(jsonObject);
		// 利用Pattern,Matcher这两个类删除帖子的内容
		// 检测帖子中是否@了某个人
		// 如果有,则向被@的人进行单独的推送
		Pattern pattern = Pattern.compile("@[0-9a-zA-Z\\u4e00-\\u9fff]+");
		Matcher matcher = pattern.matcher(myPost.getContent());
		while (matcher.find()) {
			String name = matcher.group();// @abc
			name = name.substring(1);// abc
			Log.i("TAG", "被@的人有:" + name);
			// 向name单独推送,向name使用的设备推送消息
			BmobQuery<MyUser> query = new BmobQuery<MyUser>();
			query.addWhereEqualTo("username", name);
			query.findObjects(this, new FindListener<MyUser>() {

				@Override
				public void onSuccess(List<MyUser> arg0) {
					String installationId = arg0.get(0).getInstallationId();
					BmobPushManager<BmobInstallation> manager = new BmobPushManager<BmobInstallation>(
							PostActivity.this);
					// 向指定的installationId单独推送
					BmobQuery<BmobInstallation> query2 = new BmobQuery<BmobInstallation>();
					query2.addWhereEqualTo("installationId", installationId);// KEY是数据库的字段名
					manager.setQuery(query2);
					JSONObject jsonObject2 = new JSONObject();
					try {
						jsonObject2.put("tag", "one");
						jsonObject2.put("user", myPost.getUser().getUsername());
						jsonObject2.put("time", myPost.getCreatedAt());

					} catch (Exception e) {
					}
					manager.pushMessage(jsonObject2);
				}

				@Override
				public void onError(int arg0, String arg1) {

				}
			});
		}
	}

	/**
	 * @param v
	 */
	@OnClick(R.id.btn_post_update)
	public void updatePost(View v) {
		String title = etTitle.getText().toString();
		String content = etContent.getText().toString();
		if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
			return;
		}
		MyPost newPost = new MyPost();
		newPost.setObjectId(updatePost.getObjectId());
		newPost.setTitle(title);
		newPost.setContent(content);
		// updatePost.setTitle(title);
		// updatePost.setContent(content);
		newPost.update(this, new UpdateListener() {
			@Override
			public void onSuccess() {
				Toast.makeText(PostActivity.this, "帖子更新完毕", Toast.LENGTH_SHORT)
						.show();
				// 更新成功后，是否清空etTitle,etContent
				// 关闭界面回到ShowActivity，根据个人需要来
				finish();
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Toast.makeText(PostActivity.this,
						"帖子更新失败，请稍后重试。错误代码：" + arg0 + "," + arg1,
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 为etContent添加一个TextWatcher<br>
	 * 用来监听etContent中输入内容的变化<br>
	 * 一旦输入了'@',则跳转到UserActivity
	 */
	@OnTextChanged(R.id.et_post_content)
	public void showUsers(Editable s) {
		if (s.length() == 0) {// 输入了内容,又删干净了,从1->0
			return;
		}
		char c = s.charAt(s.length() - 1);
		if ('@' == c) {
			Intent intent = new Intent(this, UserActivity.class);
			startActivityForResult(intent, 101);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == 101) {
			// 从data中获得用户选择的user的名字
			String name = data.getStringExtra("name");
			etContent.append(name + " ");// 加空格,区分内容和名称
		}
	}

}
