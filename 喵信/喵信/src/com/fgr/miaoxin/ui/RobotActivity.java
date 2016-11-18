package com.fgr.miaoxin.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.RobotAdapter;
import com.fgr.miaoxin.bean.MyRobot;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.LogUtil;

public class RobotActivity extends BaseActivity {
	@Bind(R.id.lv_robot_listview)
	ListView listview;
	RobotAdapter adapter;
	List<MyRobot> robots;

	@Bind(R.id.et_robot_content)
	EditText etContent;

	private String[] hello = { "再下已经恭候多时了!喵", "哆啦B梦永远要陪着你!", "哆啦B梦,知道这世界上的一切",
			"哆啦B梦感应到了,你需要我", "输入内容,我将给你答案,高数作业除外", "最强大脑已经启动,随时可以出发",
			"好久不见,甚是想念!喵", "有什么可以帮助你的吗?", "天气、星座、笑话...我全可以,要女朋友干什么?" };
	RequestQueue queque;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		queque = Volley.newRequestQueue(this);
	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_robot);
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {
		setHeaderTitle("机器人");
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initListView() {
		robots = new ArrayList<MyRobot>();
		robots.add(new MyRobot(hello[new Random().nextInt(hello.length)],
				MyRobot.RECEIVER, getTime()));// 问候语
		adapter = new RobotAdapter(this, robots);
		listview.setAdapter(adapter);
	}

	@OnClick(R.id.btn_robot_send)
	public void send(View v) {
		String content = etContent.getText().toString();
		adapter.addItem(new MyRobot(content, MyRobot.SEND, getTime()));
		if (robots.size() > 30) {// 保存30条记录,保存聊天记录,写TXT文件......
			for (int i = 0; i < robots.size(); i++) {
				robots.remove(i);
			}
		}
		loaddata(content);
	}

	private double currentTime = 0, oldTime = 0;

	@SuppressLint("SimpleDateFormat")
	private String getTime() {// 连续聊天,5秒显示一次时间
		currentTime = System.currentTimeMillis();
		SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date curDate = new Date();
		String str = format.format(curDate);
		if (currentTime - oldTime >= 1000 * 60) {
			oldTime = currentTime;
			return str;
		} else {
			return "";
		}
	}

	public void loaddata(String info) {
		String url = "http://www.tuling123.com/openapi/api?key=fdb4cb1a9303446dba1be5dfdf95e9f7&info="
				+ info;
		StringRequest request = new StringRequest(url, new Listener<String>() {
			@Override
			public void onResponse(String response) {
				LogUtil.i("TAG:response", response);
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(response);
					adapter.addItem(new MyRobot(jsonObject.getString("text"),
							1, getTime()));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				toastAndLog("哆啦B喵繁忙,请稍后重试", error.getMessage());
			}
		});
		queque.add(request);
	}

}
