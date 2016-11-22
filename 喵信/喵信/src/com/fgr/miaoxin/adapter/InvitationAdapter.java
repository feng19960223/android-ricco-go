package com.fgr.miaoxin.adapter;

import java.util.List;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.DialogUtil;
import com.fgr.miaoxin.util.LogUtil;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.db.BmobDB;
import cn.bmob.v3.listener.UpdateListener;

public class InvitationAdapter extends MyBaseAdapter<BmobInvitation> {

	public InvitationAdapter(Context context, List<BmobInvitation> datasource) {
		super(context, datasource);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final ViewHolder vh;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.item_newfriend_layout, parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final BmobInvitation invitation = getItem(position);
		// 设置头像
		setAvatar(invitation.getAvatar(), vh.ivAvatar);
		// 设置发送该添加好友申请的用户名
		vh.tvUsername.setText(invitation.getFromname());

		vh.ibAgree.setVisibility(View.VISIBLE);
		vh.ibReject.setVisibility(View.VISIBLE);
		vh.tvAdd.setVisibility(View.INVISIBLE);

		vh.ibAgree.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 同意添加好友
				// 1)去_user表中on个查找发送该“添加好友申请”的用户
				// 2)如果找到了该用户，就去更新当前登录用户在_user表中
				// 对应的数据记录的contacts字段的值，在服务器端将第1步
				// 查找到的用户添加为好友
				// 3)更新本地数据库中保存的“添加好友申请”数据记录status字段的值
				// 从2更新为1，意味着该添加好友申请已经为处理过了
				// 4)向第1步查找到的用户发送一个“回执”消息，这个消息的tag为agree
				// 5)将第1步查找到的用户添加到本地数据库的friends数据表
				// 6)回调咱们自己写的监听器中的方法
				BmobUserManager.getInstance(context).agreeAddContact(
						invitation, new UpdateListener() {

							@Override
							public void onSuccess() {
								vh.ibAgree.setVisibility(View.INVISIBLE);
								vh.ibReject.setVisibility(View.INVISIBLE);
								vh.tvAdd.setVisibility(View.VISIBLE);

							}

							@Override
							public void onFailure(int arg0, String arg1) {
								LogUtil.e("TAG", "添加好友失败," + arg0 + ":" + arg1);
								Toast.makeText(context, "添加好友失败",
										Toast.LENGTH_SHORT).show();
							}
						});
			}
		});

		vh.ibReject.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 拒绝添加好友
				// 将“添加好友申请”从tab_new_contacts数据表中删除掉即可
				DialogUtil.showDialog(context, "删除通知",
						"您确定要删除" + invitation.getFromname() + "的添加好友申请嘛？",
						true, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								List<BmobInvitation> list = BmobDB.create(
										context).queryBmobInviteList();

								for (BmobInvitation bi : list) {
									if (bi.getFromname().equals(
											invitation.getFromname())) {
										BmobDB.create(context).deleteInviteMsg(
												bi.getFromid(),
												String.valueOf(bi.getTime()));
									}
								}
								// 从数据源中删除对应的内容
								removeData(invitation);
							}
						});

			}
		});

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_newfriend_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_newfriend_name)
		TextView tvUsername;
		@Bind(R.id.ib_item_newfriend_agree)
		ImageButton ibAgree;
		@Bind(R.id.ib_item_newfriend_reject)
		ImageButton ibReject;
		@Bind(R.id.tv_item_newfriend_add)
		TextView tvAdd;

		public ViewHolder(View convertView) {
			super();
			ButterKnife.bind(this, convertView);
		}

	}

}
