package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.bean.BmobRecent;
import cn.bmob.im.db.BmobDB;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.BadgeView;
import com.fgr.miaoxin.view.XCRoundImageView;

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
		setAvatar(recent.getAvatar(), vh.ivAvatar);
		vh.tvUsername.setText(recent.getUserName());
		// 时间
		// recent.getTime的返回值类型是long，但是时间的单位是秒,防止Bmob聊天轰炸
		vh.tvTime.setText(TimeUtil.getTime(recent.getTime() * 1000));

		// TODO(个人干净可扩展型不是很好,例如加入红包,通话,变化太多,这样写不好)
		// 正文 根据组后一条聊天消息的类型，来决定vh.tvContent中呈现什么
		// type为1，文本类型的聊天消息 vh.tvContent就显示getMessage()返回的内容
		// type为2，图片类型的聊天消息 vh.tvContent就显示[图片]
		// type为3，位置类型的聊天消息 vh.tvContent就显示[位置]
		// type为4，语音类型的聊天消息 vh.tvContent就显示[语音]
		int type = recent.getType();
		switch (type) {
		case 1:
			// vh.tvContent.setText(recent.getMessage());
			vh.tvContent
					.setText(EmoUtil.getSpannableString(recent.getMessage()));
			break;
		case 2:
			vh.tvContent.setText("[图片]");
			break;
		case 3:
			vh.tvContent.setText("[位置]");
			break;
		case 4:
			vh.tvContent.setText("[语音]");
			break;
		default:
			vh.tvContent.setText("[不确定的消息格式]");
			break;
		}

		// 未读消息数量
		int count = BmobDB.create(context).getUnreadCount(recent.getTargetid());
		vh.bvCount.setBadgeCount(count);// 不为0显示,如果是0还是不显示,人性化不错!

		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_recent_avatar)
		XCRoundImageView ivAvatar;
		@Bind(R.id.tv_item_recent_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_recent_time)
		TextView tvTime;// 会话中最后一条消息发生的时间
		@Bind(R.id.tv_item_recent_content)
		TextView tvContent;// 会话中最后一条消息的正文
		@Bind(R.id.bv_item_recent_count)
		BadgeView bvCount;// 会话中所有未读消息的数量

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}
	}
}
