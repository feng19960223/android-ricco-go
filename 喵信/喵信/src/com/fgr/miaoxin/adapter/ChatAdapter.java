package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.XCRoundImageView;

public class ChatAdapter extends MyBaseAdapter<BmobMsg> {

	private static final int RECEIVE_TEXT_MSG = 0;
	private static final int SEND_TEXT_MSG = 1;

	public ChatAdapter(Context context, List<BmobMsg> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh;

		BmobMsg msg = getItem(position);

		int msgType = msg.getMsgType();

		int itemType = getItemViewType(position);

		if (convertView == null) {

			switch (msgType) {
			case 1:

				if (itemType == RECEIVE_TEXT_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_text_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_text_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.tvContent = (TextView) convertView
						.findViewById(R.id.tv_item_chat_content);

				convertView.setTag(vh);

				break;

			default:

				throw new RuntimeException("不正确的消息格式");
			}

		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		switch (msgType) {
		case 1:

			vh.tvTime
					.setText(TimeUtil.getTime(Long.parseLong(msg.getMsgTime()) * 1000));

			setAvatar(msg.getBelongAvatar(), vh.ivAvatar);

			vh.tvContent.setText(msg.getContent());

			break;

		default:

			throw new RuntimeException("不正确的消息类型");

		}

		return convertView;
	}

	@Override
	public int getViewTypeCount() {

		return 2;
	}

	@Override
	public int getItemViewType(int position) {

		BmobMsg msg = getItem(position);

		int msgType = msg.getMsgType();

		String belongId = msg.getBelongId();

		switch (msgType) {
		case 1:

			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_TEXT_MSG;
			} else {
				return RECEIVE_TEXT_MSG;
			}

		default:

			throw new RuntimeException("不正确的聊天消息格式");

		}

	}

	public class ViewHolder {

		TextView tvTime;

		XCRoundImageView ivAvatar;

		TextView tvContent;

		// ImageView ivContent;

	}

}
