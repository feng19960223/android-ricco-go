package com.fgr.miaoxin.ui;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.AddFriendAdapter;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.listener.OnDatasLoadFinishListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

//模糊查询可能有很多数据,所以要使用分页技术
public class AddFriendActivity extends BaseActivity {
	@Bind(R.id.et_addfriend_username)
	EditText etUsername;

	@Bind(R.id.lv_addfriend_ptrlistview)
	PullToRefreshListView ptrListView;
	ListView listView;
	List<BmobChatUser> users;
	AddFriendAdapter adapter;

	List<BmobChatUser> friendList;// 当前登录用户的所有列表

	// 模糊查询时每页最多呈现的数据量//“一页”查询几个用户
	private static final int PAGE_LIMIT = 20;// 大一点比较好,最终返回的时候,会去掉已经是好友的
	int page;// 模糊查询时的页码

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_add_friend);
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initHeaderView() {
		setHeaderTitle("搜索喵友");
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initListView() {
		listView = ptrListView.getRefreshableView();
		users = new ArrayList<BmobChatUser>();
		adapter = new AddFriendAdapter(this);
		listView.setAdapter(adapter);

		ptrListView.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				page += 1;

				queryUserByPage(page, etUsername.getText().toString(),
						new OnDatasLoadFinishListener<BmobChatUser>() {

							@Override
							public void onLoadFinish(List<BmobChatUser> datas) {

								ptrListView.onRefreshComplete();

								if (datas == null) {
									toast("没有更多数据了");
									ptrListView.setMode(Mode.DISABLED);
								} else if (datas.size() == 0) {
									page += 1;
									queryUserByPage(page, etUsername.getText()
											.toString(), this);
								} else {
									adapter.addItems(datas, false);
								}
							}
						});

			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				toast("点击了" + adapter.getItem(position - 1).getUsername());
				// TODO 查看详细信息
			}
		});
	}

	@OnClick(R.id.btn_addfriend_search)
	public void serarch(View v) {// 准确搜索
		// 关闭PullToRefreshListView的"刷新功能"
		ptrListView.setMode(Mode.DISABLED);// 既不能下拉,也不能上推
		String username = etUsername.getText().toString();
		if (TextUtils.isEmpty(username)) {
			return;// 空
		}
		adapter.clear();// 清空以前
		if (username.equals(bmobUserManager.getCurrentUser().getUsername())) {
			return;// 查询自己
		}
		if (isFriend(username)) {
			toast(username + "已经是你的喵了");
			return;// 已经是好友
		}
		queryUserByName(username);

	}

	private void queryUserByName(String username) {
		BmobQuery<BmobChatUser> query = new BmobQuery<BmobChatUser>();
		query.addWhereEqualTo("username", username);
		query.findObjects(this, new FindListener<BmobChatUser>() {

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				if (arg0.size() > 0) {
					// 在_user表中找到了用户名为username的用户
					adapter.addItems(arg0, true);
				} else {
					toast("好尴尬,查无此喵 -_-!!");
				}
			}

			@Override
			public void onError(int arg0, String arg1) {
				toastAndLog("查询喵友失败,请稍后重试", arg0, arg1);
			}
		});

	}

	/**
	 * 判断username所对应的用户是否已经是当前登录用户的好友
	 * 
	 * @param username
	 *            用户名
	 * @return true 已经是好友
	 */
	private boolean isFriend(String username) {
		// 从本地数据库好友表中获得当前登录用户的所以的好友列表
		if (friendList == null) {// 没必要每次读取查数据库
			// 添加好友后,应该更新friendList
			friendList = bmobDB.getAllContactList();
		}
		for (BmobChatUser bmobChatUser : friendList) {
			if (bmobChatUser.getUsername().equals(username)) {
				return true;
			}
		}
		return false;
	}

	@OnClick(R.id.btn_addfriend_searchmore)
	public void searchMore(View v) {
		adapter.clear();
		// 让PullToRefreshListView可以上推
		ptrListView.setMode(Mode.PULL_FROM_END);
		final String username = etUsername.getText().toString();
		if (TextUtils.isEmpty(username)) {
			return;
		}
		// 如果username与当前登录用户的用户名一致，也应该
		// 允许用户继续发起模糊搜索
		// 例如：当前登录用户是abc，输入的username是abc
		// 那么可以搜索用户表中用户名包含“abc”的这些用户

		// 筛选当前登录用户的好友，也需要在获得查询结果后再做处理
		page = 0;
		queryUserByPage(page, username,
				new OnDatasLoadFinishListener<BmobChatUser>() {

					@Override
					public void onLoadFinish(List<BmobChatUser> datas) {
						if (datas == null) {
							toast("没有包含" + username + "的喵");
						} else if (datas.size() == 0) {
							// 发起下一次的查询
							page += 1;
							queryUserByPage(page, username, this);
						} else {
							adapter.addItems(datas, true);
						}
					}
				});
	}

	/**
	 * 分页查询 查询_user表中，用户名包含username的用户
	 * 
	 * @param page
	 *            页码
	 * @param username
	 *            要查询被包含的用户名
	 * @param listener
	 *            处理查询返回结果
	 */
	private void queryUserByPage(int page, final String username,
			final OnDatasLoadFinishListener<BmobChatUser> listener) {
		BmobQuery<BmobChatUser> query = new BmobQuery<BmobChatUser>();
		// 设定查询条件
		query.addWhereNotEqualTo("username",
				bmobUserManager.getCurrentUserName());
		// 关于分页的设定
		// 忽略查询结果的前PAGE_LIMIT*page个数据
		query.setSkip(PAGE_LIMIT * page);
		// 设定一次最多返回多少个数据
		query.setLimit(PAGE_LIMIT);
		query.findObjects(this, new FindListener<BmobChatUser>() {

			@Override
			public void onError(int arg0, String arg1) {
				toastAndLog("查询用户时错误，请稍后重试", arg0, arg1);

			}

			@Override
			public void onSuccess(List<BmobChatUser> arg0) {
				// 1)arg0.size()==0 _user数据表中确实没有包含username的用户
				// 2)arg0.size()>0
				// 2.1 有包含username的用户,这批用户全部都是当前登录用户的好友！？？
				// 2.2 通过删选后还剩下数据，可以放到ListView中呈现
				if (arg0.size() > 0) {
					// 将arg0不是当前登录用户好友且用户名中包含username的放入list
					List<BmobChatUser> list = new ArrayList<BmobChatUser>();
					// 数据删选
					for (BmobChatUser bcu : arg0) {
						if (!isFriend(bcu.getUsername())
								&& bcu.getUsername().contains(username)) {
							list.add(bcu);
						}
					}
					// list可能有两种情况
					// list.size()==0 从服务器返回的这批数据，没有一个通过删选
					// list.size()>0 从服务器返回的这批数据有数据通过删选
					listener.onLoadFinish(list);

				} else {
					listener.onLoadFinish(null);
				}

			}
		});
	}
}
