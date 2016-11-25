package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import cn.bmob.im.BmobUserManager;
import cn.bmob.im.bean.BmobMsg;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.LocationActivity;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.TimeUtil;
import com.fgr.miaoxin.view.XCRoundImageView;

public class ChatAdapter extends MyBaseAdapter<BmobMsg> {

	private static final int RECEIVE_TEXT_MSG = 0;
	private static final int SEND_TEXT_MSG = 1;
	private static final int RECEIVE_IMAGE_MSG = 2;
	private static final int SEND_IMAGE_MSG = 3;
	private static final int RECEIVE_LOC_MSG = 4;
	private static final int SEND_LOC_MSG = 5;
	private static final int RECEIVE_VOICE_MSG = 6;
	private static final int SEND_VOICE_MSG = 7;

	public ChatAdapter(Context context, List<BmobMsg> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder vh;

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

			case 3:

				if (itemType == RECEIVE_LOC_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_location_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_location_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.ivContent = (ImageView) convertView
						.findViewById(R.id.iv_item_chat_content);
				vh.tvContent = (TextView) convertView
						.findViewById(R.id.tv_item_chat_content);
				convertView.setTag(vh);

				break;

			case 4:

				if (itemType == RECEIVE_VOICE_MSG) {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_voice_left, parent, false);
				} else {
					convertView = layoutInflater.inflate(
							R.layout.item_chat_voice_right, parent, false);
				}

				vh = new ViewHolder();

				vh.tvTime = (TextView) convertView
						.findViewById(R.id.tv_item_chat_time);
				vh.ivAvatar = (XCRoundImageView) convertView
						.findViewById(R.id.iv_item_chat_avatar);
				vh.ivContent = (ImageView) convertView
						.findViewById(R.id.iv_item_chat_content);
				vh.tvContent = (TextView) convertView
						.findViewById(R.id.tv_item_chat_content);
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

		case 3:
			// 地址&图片本地地址&图片网络地址&纬度&经度
			String info = msg.getContent();
			final String[] infos = info.split("&");
			vh.tvContent.setText(infos[0]);
			if (getItemViewType(position) == SEND_LOC_MSG) {
				// 发送出去的位置类型聊天消息
				// 显示地图截图时使用本地地址
				Bitmap bm = BitmapFactory.decodeFile(infos[1]);
				vh.ivContent.setImageBitmap(bm);
			} else {
				// 接收到的位置类型聊天消息
				// 显示地图截图时使用网络地址
				setAvatar(infos[2], vh.ivContent);
			}

			vh.ivContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 跳转到LocationActivity，在百度地图上显示条目中的地址
					Intent intent = new Intent(context, LocationActivity.class);
					intent.putExtra("from", "showaddress");
					intent.putExtra("address", infos[0]);
					intent.putExtra("lat", Double.parseDouble(infos[3]));
					intent.putExtra("lng", Double.parseDouble(infos[4]));
					context.startActivity(intent);
				}
			});

			break;

		case 4:

			// 如果是接收到的语音消息:语音文件服务器地址&语音文件的长度
			// 如果是发送的语音消息：
			// 第一次刷新：语音文件本地地址&语音文件的长度
			// 第二次刷新：语音文件本地地址&语音文件服务器地址&语音文件的长度

			final String voiceInfo = msg.getContent();

			if (getItemViewType(position) == RECEIVE_VOICE_MSG) {
				vh.tvContent.setText(voiceInfo.split("&")[1] + "'");
			} else {
				String[] voiceInfos = voiceInfo.split("&");
				if (voiceInfos.length == 2) {
					vh.tvContent.setText(voiceInfos[1] + "'");
				} else {
					vh.tvContent.setText(voiceInfos[2] + "'");
				}
			}

			if (vh.pbSending != null) {
				if (msg.getStatus() == 1) {
					// 语音文件已经发送成功了
					vh.pbSending.setVisibility(View.INVISIBLE);
					vh.tvContent.setVisibility(View.VISIBLE);
				} else {
					// 语音文件尚未开始发送
					vh.pbSending.setVisibility(View.VISIBLE);
					vh.tvContent.setVisibility(View.INVISIBLE);
				}
			}

			vh.ivContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 点击开始播放语音文件
					String voiceUrl;
					Position pos;
					if (getItemViewType(position) == RECEIVE_VOICE_MSG) {
						voiceUrl = voiceInfo.split("&")[0];
						pos = Position.START;
					} else {
						voiceUrl = voiceInfo.split("&")[0];
						pos = Position.END;
					}

					playVoice(voiceUrl, pos, vh.ivContent);
				}
			});

			break;

		default:

			throw new RuntimeException("不正确的消息类型");

		}

		return convertView;
	}

	protected void playVoice(String voiceUrl, final Position pos,
			final ImageView iv) {

		try {
			MediaPlayer mp = new MediaPlayer();
			mp.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					mp.start();
					if (pos == Position.START) {
						iv.setImageResource(R.drawable.play_voice_left_anim);
					} else {
						iv.setImageResource(R.drawable.play_voice_right_anim);
					}
					((AnimationDrawable) iv.getDrawable()).start();

				}
			});

			mp.setOnCompletionListener(new OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					mp.stop();
					if (pos == Position.START) {
						iv.setImageResource(R.drawable.voice_right3);
					} else {
						iv.setImageResource(R.drawable.voice_left3);
					}
					mp.release();
				}
			});
			mp.setDataSource(voiceUrl);
			mp.prepareAsync();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public int getViewTypeCount() {

		return 8;
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

		case 3:

			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_LOC_MSG;
			} else {
				return RECEIVE_LOC_MSG;
			}

		case 4:
			if (belongId.equals(BmobUserManager.getInstance(context)
					.getCurrentUserObjectId())) {
				return SEND_VOICE_MSG;
			} else {
				return RECEIVE_VOICE_MSG;
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
