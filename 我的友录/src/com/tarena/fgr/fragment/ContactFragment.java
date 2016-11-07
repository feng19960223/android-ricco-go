package com.tarena.fgr.fragment;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tarena.fgr.adapter.ContactBaseAdapter;
import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.youlu.R;

/**
 * 联系人
 * 
 * @author 冯国芮
 * 
 */
public class ContactFragment extends BaseFragment implements
		OnItemClickListener, OnItemLongClickListener {
	private GridView gridView = null;
	private ContactBaseAdapter adapter = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置为false,container并不是跟节点,而是依赖的参数
		contentView = inflater.inflate(R.layout.fragment_contact, container,
				false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// 初始化所以的UI控件
		return contentView;
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		gridView = (GridView) contentView.findViewById(R.id.gridView_contact);

		// 设置一个查询的小图片
		initialActionbar(R.drawable.ic_add_icon, "联系人", -1);
		ImageView imageView_add = (ImageView) contentView
				.findViewById(R.id.imageView_actionbar_left);
		imageView_add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogManager.showAddContactDialog(getActivity());
			}
		});

		// 2016年9月30日 14:36:54
		// GridView的属性必须设置为
		// android:layout_height="match_parent"
		// 否则优化没有作用,原因:每次都去重新计算布局

		adapter = new ContactBaseAdapter(getActivity());
		gridView.setAdapter(adapter);

		refreshContact();// 联系人数据
		// 监听事件
		gridView.setOnItemClickListener(this);
		gridView.setOnItemLongClickListener(this);// 长按删除
	}

	private void refreshContact() {
		// 将查到的数据,添加到适配器集合中
		// 2016年9月30日 14:59:52
		// 冯国芮:这种思路真的很棒,这样我就可以随意的写异步了
		// 可以放到异步里面进行加载,数据加载不需要通知更新,在MyBaseAdapter中,底层,添加完毕是就已经通知更新了
		List<Contact> list = ContactManager.getContacts(getActivity());
		// 第一种
		// 此数据是第一个添加联系人按钮
		// adapter.addItem(new Contact(-1, "添加联系人", "", "", "", 0));
		// 所以的联系人数据
		// adapter.addItems(list, false);// 添加的同时,不清空前面的添加联系人

		// 第二种
		// 2016年10月8日 14:11:05改进数据的添加
		list.add(0, new Contact(-1, "添加联系人", "", "", "", 0));
		adapter.addItems(list, true);// 添加的同时,不清空前面的添加联系人
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (position == 0) {// 添加联系人
			DialogManager.showAddContactDialog(getActivity());
			// refreshContact();//更新数据
			// 2016年10月8日 14:06:16
			// 用Activity的生命周期,写到onResume中
			// 刷新数据
			// adapter.notifyDataSetChanged();
			// 冯国芮2016年10月8日 12:37:32
			// 刷新数据没用

		} else {
			// 显示联系人的详细信息
			Contact contact = adapter.getItem(position);
			DialogManager.showDetailContactDialog(getActivity(), contact);
		}
	}

	@Override
	// 当点击了添加完成后,刷新数据可以写在这里
	// dialog不见的时候会执行
	public void onResume() {
		super.onResume();
		// adapter.addItems(null, true);//刷新的时候,需要删除前面的数据
		// 2016年10月8日 14:13:46
		// 冯国芮:使用第一种时,因为返回值是false没有清空原来的数据,所以要清空一次数据,再更新显示
		// 第二种,将第一个添加联系人的数据添加到list中,返回true,默认清空,只需要刷新就好了
		refreshContact();// 刷新联系人UI界面
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (position != 0) {// 不能删除第一个添加联系人
			Contact contact = adapter.getItem(position);
			DialogManager.showDeleteContactDialog(getActivity(), contact,
					adapter);
		}
		return true;// 不再执行单击事件,事件分发机制
	}
}
