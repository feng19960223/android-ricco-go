package com.fgr.miaoxin.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import butterknife.Bind;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.v3.listener.FindListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.RecentAdapter;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.ChatActivity;
import com.fgr.miaoxin.ui.MainActivity;
import com.fgr.miaoxin.util.DialogUtil;
import com.fgr.miaoxin.util.LogUtil;

public class MessageFragment extends BaseFragment {
	@Bind(R.id.lv_message_listview)
	ListView listView;
	// BmobRecent是BmobIMSDK中提供的实体类
	// 用来封装从本地数据库recent表中查询出来的数据记录
	List<BmobRecent> recents;
	RecentAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_message, container,
				false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {
		setHeaderTitle("喵信", Position.CENTER);
	}

	private void initListView() {
		recents = new ArrayList<BmobRecent>();
		adapter = new RecentAdapter(getActivity(), recents);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 点击跳转到聊天界面
				// 改变当前会话中未读消息的状态均变为已读
				String toId = adapter.getItem(position).getTargetid();
				bmobDB.resetUnread(toId);
				// 调用MainActiviyt中的setBadgeCount方法
				// 更新总的未读消息数量
				FragmentActivity act = getActivity();
				((MainActivity) act).setBadgeCount();
				// 界面的跳转，跳转到ChatActivity
				// 要把会话(recent)中对方用户传递到ChatActivity
				String username = adapter.getItem(position).getUserName();

				bmobUserManager.queryUserByName(username,
						new FindListener<BmobChatUser>() {

							@Override
							public void onSuccess(List<BmobChatUser> arg0) {
								BmobChatUser user = arg0.get(0);
								LogUtil.i("user" + user);
								Intent intent = new Intent(getActivity(),
										ChatActivity.class);
								intent.putExtra("user", user);
								jumpTo(intent, false);
							}

							@Override
							public void onError(int arg0, String arg1) {
								toastAndLog("查询用户失败", arg0, arg1);

							}
						});

				// jumpTo(ChatActivity.class, false);

			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				final BmobRecent recent = adapter.getItem(position);
				DialogUtil.showDialog(getActivity(), "删除会话",
						"确实要删除与" + recent.getUserName() + "之间的全部聊天内容吗？", true,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 1)服务器上的聊天内容
								// 说明：喵信中呈现双方全部聊天内容只从chat表中查询
								// 所以服务器上的聊天内容删除或不删除对呈现聊天信息没有影响
								// 因此喵信不删除服务器上的聊天内容
								// 如果你认为需要删除，请自行添加相应的逻辑
								// 2)本地数据库chat表中的聊天内容
								bmobDB.deleteMessages(recent.getTargetid());
								// 3)本地数据库recent表中的会话
								bmobDB.deleteRecent(recent.getTargetid());
								// 4)删除ListView数据源中的内容
								adapter.removeData(recent);
								// 5)更新MainActivity中所有未读消息的数量
								((MainActivity) getActivity()).setBadgeCount();

							}
						});

				return true;
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	public void refresh() {
		// 获取会话信息
		// 会话信息仅存在于本地数据库的recent表中
		List<BmobRecent> list = bmobDB.queryRecents();
		adapter.addItems(list, true);
	}

}
