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
import cn.bmob.im.bean.BmobChatUser;

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
				// TODO
				LogUtil.d("ÃÌº”¡À" + getItem(position).getUsername());
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
