package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobChatManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.listener.PushListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.LogUtil;
import com.fgr.miaoxin.view.XCRoundImageView;

public class AddFriendAdapter extends MyBaseAdapter<BmobChatUser> {

	public AddFriendAdapter(Context context) {
		super(context);
	}

	public AddFriendAdapter(Context context, List<BmobChatUser> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder vh;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.item_addfriend_layout, parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final BmobChatUser user = getItem(position);

		String avatar = user.getAvatar();
		setAvatar(avatar, vh.ivAvatar);

		vh.tvUsername.setText(user.getUsername());

		vh.btnAdd.setVisibility(View.VISIBLE);
		vh.tvAdd.setVisibility(View.INVISIBLE);

		vh.btnAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 向第poison位置数据所代表的用户发送“添加好友申请”
				// step 1. 去_user表中确认一下是否有user这个用户
				// step 2. 创建了实体类BmobMsg对象，此时该对象两个重要属性的值分别是:tag属性，值为"add";
				// isReaded属性，值为0
				// step 3. 根据step2创建的BmobMsg对象，构建对应的JsonObject对象
				// step 4. 向user用户所使用的设备单独推送一条消息，消息的内容就是step3所创建的JsonObject
				// step 5. 推送成功后，将step2所创建的BmobMsg对象保存到了服务器的数据库BmobMsg数据表中
				// 保存的数据记录两个字段的值:tag为“add”,isReaded为0
				// step 6. 调用自己写的监听器中的相应方法
				BmobChatManager.getInstance(context).sendTagMessage("add",
						user.getObjectId(), new PushListener() {

							@Override
							public void onSuccess() {
								vh.btnAdd.setVisibility(View.INVISIBLE);
								vh.tvAdd.setVisibility(View.VISIBLE);
							}

							@Override
							public void onFailure(int arg0, String arg1) {
								LogUtil.d("TAG", "发送添加好友申请失败了," + arg0 + ","
										+ arg1);
							}
						});
			}
		});

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_addfriend_avatar)
		XCRoundImageView ivAvatar;
		@Bind(R.id.tv_item_addfriend_username)
		TextView tvUsername;
		@Bind(R.id.btn_item_addfriend_add)
		Button btnAdd;
		@Bind(R.id.tv_item_addfriend_add)
		TextView tvAdd;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}

	}
}
