package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.BadgeView;

public class RecentAdapter extends MyBaseAdapter<BmobRecent> {

	public RecentAdapter(Context context, List<BmobRecent> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_recent_layout,
					parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		BmobRecent recent = getItem(position);

		// 头像
		setAvatar(recent.getAvatar(), vh.ivAvatar);
		// 用户名
		vh.tvUsername.setText(recent.getUserName());
		// 时间
		vh.tvTime.setText(TimeUtil.getTime(recent.getTime() * 1000));
		// 正文
		// 根据不同的聊天类型，来做不同的显示

		int msgType = recent.getType();
		switch (msgType) {
		case 1:// 文本类型的聊天消息
			vh.tvContent
					.setText(EmoUtil.getSpannableString(recent.getMessage()));
			break;

		case 2:// 图形类型的聊天消息
			vh.tvContent.setText("[图片]");
			break;
		case 3:// 位置类型的聊天消息
			vh.tvContent.setText("[位置]");
			break;
		case 4:// 语音类型的聊天消息
			vh.tvContent.setText("[语音]");
			break;
		default:
			throw new RuntimeException("不正确的消息格式");
		}

		// 未读消息数量

		int count = BmobDB.create(context).getUnreadCount(recent.getTargetid());

		// 如果count不为0，则BadgeView以红底白字的形式来显示count
		vh.bvCount.setBadgeCount(count);

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_recent_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_recent_name)
		TextView tvUsername;
		@Bind(R.id.tv_item_recent_time)
		TextView tvTime;
		@Bind(R.id.tv_item_recent_content)
		TextView tvContent;
		@Bind(R.id.bv_item_recent_unread)
		BadgeView bvCount;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}

	}

}
