package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.MyUser;
import com.nostra13.universalimageloader.core.ImageLoader;

public class FriendAdapter extends MyBaseAdapter<MyUser> implements
		SectionIndexer {

	public FriendAdapter(Context context, List<MyUser> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_friend_layout,
					parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		MyUser user = getItem(position);

		String avatar = user.getAvatar();
		if (TextUtils.isEmpty(avatar)) {
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		} else {
			ImageLoader.getInstance().displayImage(avatar, vh.ivAvatar);
		}

		vh.tvLetter.setText(user.getLetter() + "");
		vh.tvUsername.setText(user.getUsername());

		if (getPositionForSection(getSectionForPosition(position)) != position) {
			vh.tvLetter.setVisibility(View.GONE);
		} else {
			vh.tvLetter.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_friend_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_friend_letter)
		TextView tvLetter;
		@Bind(R.id.tv_item_friend_username)
		TextView tvUsername;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}

	}

	@Override
	public Object[] getSections() {
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			MyUser mu = getItem(i);
			if (mu.getLetter() == section) {
				return i;
			}
		}
		// 意味着当前数据源中没有属于section的数据
		// 返回一个当前数据源中不存在的下标值(小于0即可)
		return -10;
	}

	@Override
	public int getSectionForPosition(int position) {
		MyUser mu = getItem(position);
		return mu.getLetter();
	}

}
