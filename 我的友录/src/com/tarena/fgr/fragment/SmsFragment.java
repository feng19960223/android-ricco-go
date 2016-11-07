package com.tarena.fgr.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tarena.fgr.adapter.ConversationBaseAdapter;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.youlu.ChatActivity;
import com.tarena.fgr.youlu.R;

/**
 * 短信息
 * 
 * @author 冯国芮
 * 
 */
public class SmsFragment extends BaseFragment implements OnItemClickListener,
		OnItemLongClickListener {
	private ListView listView = null;
	private ConversationBaseAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置为false,container并不是跟节点,而是依赖的参数
		contentView = inflater.inflate(R.layout.fragment_sms, container, false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// 初始化所以的UI控件
		registReceiver();
		return contentView;
	}

	private IntentFilter intentFilter;

	private void registReceiver() {
		conversationReceiver = new ConversationReceiver();
		intentFilter = new IntentFilter();
		intentFilter.addAction(ChatActivity.RECEIVE_SMS);
		intentFilter.setPriority(1000);
		getActivity().registerReceiver(conversationReceiver, intentFilter);
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		listView = (ListView) contentView.findViewById(R.id.listView_sms);
		// listView.setScrollBarXXX();//设置滚动条的各种样式
		// 改变滚动条的位置
		// listView.scrollTo(listView.getWidth() + 24, 0);
		// 2016年10月12日 09:04:19 运行结果是一个巨大的黑边...好失败

		initialActionbar(R.drawable.add_sms, "短信息", -1);
		ImageView imageView_send_sms = (ImageView) contentView
				.findViewById(R.id.imageView_actionbar_left);
		imageView_send_sms.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_SENDTO, Uri
						.parse("smsto:")));
			}
		});
		adapter = new ConversationBaseAdapter(getActivity());
		listView.setAdapter(adapter);
		refreshSms();
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
	}

	private void refreshSms() {
		// 查询所以会话
		List<Conversation> list = SMSManager.getConversations(getActivity());
		// 更新UI
		adapter.addItems(list, true);
	}

	@Override
	// 页面重新显示的时候执行
	public void onResume() {
		super.onResume();
		refreshSms();
		getActivity().registerReceiver(conversationReceiver, intentFilter);
	}

	public static final String ACTION_THREAD_ID = "thread_id";
	public static final String ACTION_PHONE = "phone";
	public static final String ACTION_NAME = "name";

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 获得当前选中的会话对象
		Conversation conversation = adapter.getItem(position);

		String phone = conversation.getPhone();
		int thread_id = conversation.getThread_id();
		String name = conversation.getName();

		if (conversation.getRead() == 0) {
			// 修改会话状态,更新已读
			SMSManager.updateConverationType(getActivity(), thread_id);
		}

		Intent intent = new Intent(getActivity(), ChatActivity.class);
		intent.putExtra(ACTION_THREAD_ID, thread_id);
		intent.putExtra(ACTION_PHONE, phone);
		intent.putExtra(ACTION_NAME, name);
		getActivity().unregisterReceiver(conversationReceiver);
		startActivity(intent);
	}

	@Override
	// 长按删除短信
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		DialogManager.showDeleteConversationDialog(getActivity(),
				adapter.getItem(position), adapter);
		return false;
	}

	private ConversationReceiver conversationReceiver = null;

	// 收到短信,刷新页面,显示小绿点
	public class ConversationReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 收到短信
			if (action.equals(ChatActivity.RECEIVE_SMS)) {
				Bundle bundle = intent.getExtras();
				Sms sms = SMSManager.getSmsByReceiver(bundle);
				SMSManager.save(context, sms, 0);// 第三个参数我真的不知道写什么...但给个0没有出现错误...
				// 刷新界面
				refreshSms();
				abortBroadcast();// 屏蔽系统的通知
			}
		}
	}

}
