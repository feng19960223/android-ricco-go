package com.fgr.bmobdemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.adapter.PostAdapter;
import com.fgr.bmobdemo.app.MyApp;
import com.fgr.bmobdemo.bean.MyPost;
import com.fgr.bmobdemo.bean.MyUser;
import com.fgr.bmobdemo.receiver.MyPushMessageReceiver;
import com.fgr.bmobdemo.receiver.MyPushMessageReceiver.EventListener;

public class ShowActivity extends Activity implements EventListener {
	// 从MainActivity传递过来的，当前登录用户
	MyUser user;
	@Bind(R.id.iv_header_add)
	ImageView ivAdd;
	@Bind(R.id.iv_header_refresh)
	ImageView ivRefresh;
	@Bind(R.id.pb_header_refresh)
	ProgressBar pbLoading;
	@Bind(R.id.lv_show_listviews)
	ListView listView;
	@Bind(R.id.iv_header_newpost)
	ImageView ivNewPost;
	List<MyPost> posts;
	PostAdapter adapter;

	// 方法一 ,通过发广播,显示红点和音乐
	// NewPostReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show);
		ButterKnife.bind(this);
		user = (MyUser) getIntent().getSerializableExtra("user");
		initHeaderView();
		initListView();
	}

	private void initListView() {
		posts = new ArrayList<MyPost>();
		adapter = new PostAdapter(this, posts, user);
		listView.setAdapter(adapter);
	}

	private void initHeaderView() {
		ivAdd.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
		ivRefresh.setColorFilter(Color.WHITE, Mode.SRC_ATOP);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// 方法一
		// receiver = new NewPostReceiver();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("com.fgr.bmobdemo.ACTION_NEW_POST");
		// registerReceiver(receiver, filter);
		// 方法二
		MyPushMessageReceiver.regist(this);
		refresh();
	}

	@Override
	protected void onPause() {
		// unregisterReceiver(receiver);// 写在super之前
		MyPushMessageReceiver.unregist(this);
		super.onPause();
	}

	private void refresh() {
		ivRefresh.setVisibility(View.INVISIBLE);
		pbLoading.setVisibility(View.VISIBLE);
		ivNewPost.setVisibility(View.INVISIBLE);
		// 将所有MyPost数据表中的帖子放到ListView中呈现
		BmobQuery<MyPost> query = new BmobQuery<MyPost>();
		// 查询条件
		// 将帖子的作者信息从MyUser表中取出来
		query.include("user");
		// 利用数据表的机制来排序帖子
		query.order("-createdAt");
		// 发起查询
		query.findObjects(this, new FindListener<MyPost>() {
			@Override
			public void onSuccess(List<MyPost> arg0) {
				// TODO 思路：
				// 对arg0按照帖子的发帖时间进行排序
				// 排序的规则：将字符串形式的时间(yyyy-MM-dd HH:mm:ss)
				// 利用SimpleDateFormat转为long型时间戳
				// 比较时间戳的大小得到排序数据
				// 排序后再将数据放到ListView中呈现
				adapter.addAll(arg0, true);
				ivRefresh.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(ShowActivity.this,
						"帖子查询失败，错误代码：" + arg0 + "," + arg1, Toast.LENGTH_SHORT)
						.show();
				ivRefresh.setVisibility(View.VISIBLE);
				pbLoading.setVisibility(View.INVISIBLE);
			}
		});

	}

	@OnClick(R.id.iv_header_add)
	public void jumpTo(View v) {
		Intent intent = new Intent(this, PostActivity.class);
		intent.putExtra("from", "new");
		intent.putExtra("user", user);
		startActivity(intent);
	}

	@OnClick(R.id.iv_header_refresh)
	public void refreshPost(View v) {
		refresh();
	}

	// 方法二
	@Override
	public void onNewPost() {// 有新帖
		ivNewPost.setVisibility(View.VISIBLE);
		MyApp.palyer.start();
	}

	@Override
	public void onAtone() {// @
		ivNewPost.setVisibility(View.VISIBLE);
		MyApp.palyer.start();
		Toast.makeText(this, "刚刚有人发帖@了您", Toast.LENGTH_SHORT).show();
	}

	// 方法一
	// public class NewPostReceiver extends BroadcastReceiver {
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// String action = intent.getAction();
	// if ("com.fgr.bmobdemo.ACTION_NEW_POST".equals(action)) {
	// // 显示红点
	// ivNewPost.setVisibility(View.VISIBLE);
	// // 播放声音
	// MyApp.palyer.start();
	// }
	// }
	// }
}
