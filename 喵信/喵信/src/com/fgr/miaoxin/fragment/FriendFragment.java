package com.fgr.miaoxin.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import android.content.DialogInterface;
import android.content.Intent;
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
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.UpdateListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.FriendAdapter;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.AddFriendActivity;
import com.fgr.miaoxin.ui.MainActivity;
import com.fgr.miaoxin.ui.NearFriendActivity;
import com.fgr.miaoxin.ui.NewFriendActivity;
import com.fgr.miaoxin.ui.RobotActivity;
import com.fgr.miaoxin.ui.UserInfoActivity;
import com.fgr.miaoxin.util.DialogUtil;
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
		if (header != null) {// 4->2会有问题,所以使用这种
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
				// 点击跳转到NewFriendActivity显示收到的“添加好友申请”
				jumpTo(NewFriendActivity.class, false);
			}
		});

		TextView tvNear = (TextView) header
				.findViewById(R.id.tv_header_nearfriend);
		tvNear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				jumpTo(NearFriendActivity.class, false);
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

				Intent intent = new Intent(getActivity(),
						UserInfoActivity.class);
				intent.putExtra("from", "friend");
				// ListView有一个header
				String username = adapter.getItem(position - 1).getUsername();
				intent.putExtra("username", username);
				jumpTo(intent, false);

			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {

				final MyUser user = adapter.getItem(position - 1);

				DialogUtil.showDialog(getActivity(), "删除好友",
						"确认要删除" + user.getUsername() + "吗？", true,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 1)从服务器端（更新当前登录用户的contacts字段值,将user去除掉）
								// 和本地数据库的friend表中（将user这条数据记录删除掉）解除好友关系
								// 2)删除聊天记录
								// chat表中把两人之间所有的聊天记录全部删除
								// recent表中把两人之间的会话记录删除
								bmobUserManager.deleteContact(
										user.getObjectId(),
										new UpdateListener() {

											@Override
											public void onSuccess() {
												// 3)从FriendFragment显示好友的ListView数据源中删除
												adapter.removeData(user);
												// 4)调用MainActivity的refreshMessageFragment方法
												// 刷新MessageFragment的ListView以及MainActivity的角标
												((MainActivity) getActivity())
														.refreshMessageFragment();
											}

											@Override
											public void onFailure(int arg0,
													String arg1) {
												toastAndLog("删除好于失败", arg0,
														arg1);
											}
										});

							}

						});

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
		// List<MyUser> list = createFakeFriends();

		List<BmobChatUser> contacts = bmobDB.getAllContactList();

		// 根据List<BmobChatUser>获得一个List<MyUser>

		List<MyUser> list = getMyUserList(contacts);

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
		// 将排序好的数据放到ListView中呈现
		adapter.addItems(list, true);
	}

	/**
	 * 根据List<BmobChatUser>获得一个List<MyUser>
	 * 
	 * @param contacts
	 * @return
	 */

	private List<MyUser> getMyUserList(List<BmobChatUser> contacts) {
		List<MyUser> list = new ArrayList<MyUser>();

		for (BmobChatUser bcu : contacts) {
			MyUser mu = new MyUser();
			mu.setAvatar(bcu.getAvatar());
			mu.setUsername(bcu.getUsername());
			mu.setPyName(PinYinUtil.getPinYin(bcu.getUsername()));
			mu.setLetter(PinYinUtil.getFirstLetter(bcu.getUsername()));
			// 为了删除好友时，必须提供objectId
			mu.setObjectId(bcu.getObjectId());

			list.add(mu);
		}

		return list;
	}

	@SuppressWarnings("unused")
	private List<MyUser> createFakeFriends() {
		// 创建假的好友数据
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
