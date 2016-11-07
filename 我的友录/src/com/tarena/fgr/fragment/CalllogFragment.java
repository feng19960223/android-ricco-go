package com.tarena.fgr.fragment;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.tarena.fgr.adapter.CalllogBaseAdapter;
import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.youlu.R;

/**
 * 通话记录
 * 
 * @author 冯国芮 2016年9月29日 16:36:42
 * 
 */
public class CalllogFragment extends BaseFragment implements
		OnItemLongClickListener, OnItemClickListener {
	private ListView listView = null;
	private CalllogBaseAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置为false,container并不是跟节点,而是依赖的参数
		contentView = inflater.inflate(R.layout.fragment_calllog, container,
				false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// 初始化所以的UI控件
		return contentView;
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		listView = (ListView) contentView.findViewById(R.id.listView_calllog);

		initialActionbar(-1, "通话记录", -1);
		adapter = new CalllogBaseAdapter(getActivity());
		listView.setAdapter(adapter);
		listView.setOnItemLongClickListener(this);
		listView.setOnItemClickListener(this);
		refreshCalllog();
	}

	private void refreshCalllog() {
		// 通话记录的数据
		List<Calllog> list = CalllogManager.getCalllogs(getActivity());
		adapter.addItems(list, true);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshCalllog();// 界面重新被唤醒时,刷新数据
	}

	@Override
	// 长按删除通话记录
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		Calllog calllog = adapter.getItem(position);
		DialogManager.showDeleteCalllogDialog(getActivity(), calllog, adapter);
		return true;
	}

	@Override
	// 单击列表拨号
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		String phone = adapter.getItem(position).getPhone();
		Intent intent = new Intent();
		// 直接拨打
		// intent.setAction(Intent.ACTION_CALL);
		// 跳转到拨号界面
		intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phone));
		// 跳转到拨号界面
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// 电话结束后会强制到系统的通话记录...
		// intent.putExtra("finishActivityOnSaveCompleted", true);
		// 当我们调用编辑完成后直接退系统的activity
		// 这段神奇的代码没有作用了
		startActivity(intent);
	}
}
