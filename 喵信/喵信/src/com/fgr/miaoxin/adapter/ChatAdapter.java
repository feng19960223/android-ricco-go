package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.XCRoundImageView;

public class ChatAdapter extends MyBaseAdapter<BmobMsg> {

	private static final int RECEIVE_TEXT_MSG = 0;
	private static final int SEND_TEXT_MSG = 1;
	private static final int RECEIVE_IMAGE_MSG = 2;
	private static final int SEND_IMAGE_MSG = 3;

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

			case 2:

				if (itemType == RECEIVE_IMAGE_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_img_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_img_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.ivContent = (ImageView) convertView
						.findViewById(R.id.iv_item_chat_content);
				// 为vh.pbSending赋值时，如果convertView是从item_chat_image_left得到，则vh.pbSending为null
				vh.pbSending = (ProgressBar) convertView
						.findViewById(R.id.pb_item_chat_sending);
				convertView.setTag(vh);

				break;

			default:

				throw new RuntimeException("不正确的消息格式");
			}

		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tvTime
				.setText(TimeUtil.getTime(Long.parseLong(msg.getMsgTime()) * 1000));

		setAvatar(msg.getBelongAvatar(), vh.ivAvatar);

		switch (msgType) {
		case 1:

			vh.tvContent.setText(EmoUtil.getSpannableString(msg.getContent()));

			break;

		case 2:

			String imageUrl = msg.getContent();
			// 对于接收方来说：网址
			// 对于发送方来说：onStart时 file:///filePath
			// onSuccess时 file:///filePath&网址
			if (imageUrl.contains("&")) {
				// 从file:///filePath&网址取出filePath
				String address = imageUrl.split("&")[0];
				address = address.split("///")[1];
				Bitmap bm = BitmapFactory.decodeFile(address);
				vh.ivContent.setImageBitmap(bm);
			} else {
				if (getItemViewType(position) == RECEIVE_IMAGE_MSG) {
					setAvatar(imageUrl, vh.ivContent);
				} else {
					String address = imageUrl.split("///")[1];
					Bitmap bm = BitmapFactory.decodeFile(address);
					vh.ivContent.setImageBitmap(bm);
				}
			}

			if (vh.pbSending != null) {
				if (msg.getStatus() == 0) {
					vh.pbSending.setVisibility(View.VISIBLE);
				} else {
					vh.pbSending.setVisibility(View.INVISIBLE);
				}
			}

			break;

		default:

			throw new RuntimeException("不正确的消息类型");

		}

		return convertView;
	}

	@Override
	public int getViewTypeCount() {

		return 4;
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

		case 2:

			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_IMAGE_MSG;
			} else {
				return RECEIVE_IMAGE_MSG;
			}

		default:

			throw new RuntimeException("不正确的聊天消息格式");

		}

	}

	public class ViewHolder {

		TextView tvTime;

		XCRoundImageView ivAvatar;

		TextView tvContent;

		ImageView ivContent;

		ProgressBar pbSending;

	}

}
