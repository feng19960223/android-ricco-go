package com.fgr.miaoxin.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Bind;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.FriendAdapter;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.AddFriendActivity;
import com.fgr.miaoxin.ui.RobotActivity;
import com.fgr.miaoxin.util.PinYinUtil;
import com.fgr.miaoxin.view.MyLetterView;
import com.fgr.miaoxin.view.MyLetterView.OnTouchLetterListener;

public class FriendFragment extends BaseFragment {
	@Bind(R.id.lv_friend_listview)
	ListView listView;
	List<MyUser> users;
	FriendAdapter adapter;

	@Bind(R.id.tv_friend_letter)
	TextView tvLetter;

	@Bind(R.id.mlv_friend_letters)
	MyLetterView mlvLetters;

	View header;// listview头部

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_friend, container, false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
		initView();
	}

	private void initHeaderView() {
		setHeaderTitle("喵友", Position.CENTER);
		setHeaderImage(Position.END, R.drawable.ic_add_newfriend, true,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						jumpTo(AddFriendActivity.class, false);
					}
				});
	}

	private void initListView() {
		users = new ArrayList<MyUser>();
		adapter = new FriendAdapter(getActivity(), users);

		if (header != null) {
			listView.removeHeaderView(header);
		}

		header = baseActivity.getLayoutInflater().inflate(
				R.layout.header_listview_friend, listView, false);
		listView.addHeaderView(header);
		// 为header中的内容添加监听，跳转界面
		TextView tvNew = (TextView) header
				.findViewById(R.id.tv_header_newfriend);
		tvNew.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpTo(AddFriendActivity.class, false);
			}
		});

		TextView tvNear = (TextView) header
				.findViewById(R.id.tv_header_nearfriend);
		tvNear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO
				toast("--2-->");
			}
		});
		TextView tvRobot = (TextView) header.findViewById(R.id.tv_header_robot);
		tvRobot.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				jumpTo(RobotActivity.class, false);
			}
		});
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO
				toast("--短-->" + adapter.getItem(position - 1).getUsername());
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO
				toast("<--长--" + adapter.getItem(position - 1).getUsername());
				return true;
			}
		});
	}

	private void initView() {
		tvLetter.setVisibility(View.INVISIBLE);
		// mlvLetters.setTvLetter(tvLetter);
		mlvLetters.setOnTouchLetterListener(new OnTouchLetterListener() {

			@Override
			public void onTouchLetter(String letter) {
				listView.setSelection(adapter.getPositionForSection(letter
						.charAt(0)) + 1);
				tvLetter.setVisibility(View.VISIBLE);
				tvLetter.setText(letter);
			}

			@Override
			public void onReleaseLetter() {
				tvLetter.setVisibility(View.INVISIBLE);
			}
		});

	}

	@Override
	public void onResume() {
		super.onResume();

		refresh();
	}

	public void refresh() {
		// 创建假的好友数据
		// TODO
		List<MyUser> list = createFakeFriends();

		Collections.sort(list, new Comparator<MyUser>() {
			@Override
			public int compare(MyUser lhs, MyUser rhs) {
				// return lhs.getPyName().compareTo(rhs.getPyName());//(#a-z)
				// 把'#'移动到'z'下面(a-z#),'#'中也应该按照字典顺序排序
				char mychar = '#' + 128;
				String l = lhs.getPyName();
				if (l.charAt(0) == '#') {
					l = l.replace('#', mychar);
				}

				String r = rhs.getPyName();
				if (r.charAt(0) == '#') {
					r = r.replace('#', mychar);
				}

				if (l.charAt(0) == mychar && r.charAt(0) == mychar) {
					// #分组内名字的排序
					String l2 = l.replace(mychar, lhs.getUsername().charAt(0));
					String r2 = r.replace(mychar, rhs.getUsername().charAt(0));

					return l2.compareTo(r2);

				} else {
					// #分组与其它分组名字的排序
					return l.compareTo(r);
				}
			}
		});
		adapter.addItems(list, true);
	}

	private List<MyUser> createFakeFriends() {
		List<MyUser> list = new ArrayList<MyUser>();

		MyUser mu = null;
		for (int i = 0; i < 10; i++) {
			mu = new MyUser();
			mu.setUsername("" + (char) (new Random().nextInt(10) + 48) + "测试数据");
			mu.setPyName(PinYinUtil.getPinYin(mu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(mu.getUsername()));
			list.add(mu);
		}
		for (int i = 0; i < 100; i++) {
			mu = new MyUser();
			mu.setUsername("" + (char) (new Random().nextInt(26) + 65) + "测试数据");
			mu.setPyName(PinYinUtil.getPinYin(mu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(mu.getUsername()));
			list.add(mu);
		}

		for (int i = 0; i < 100; i++) {
			mu = new MyUser();
			mu.setUsername("" + (char) (new Random().nextInt(26) + 97) + "测试数据");
			mu.setPyName(PinYinUtil.getPinYin(mu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(mu.getUsername()));
			list.add(mu);
		}

		return list;
	}
}
