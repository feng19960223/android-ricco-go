package com.tarena.fgr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.ImageManager;
import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.youlu.R;

public class ConversationBaseAdapter extends MyBaseAdapter<Conversation> {

	public ConversationBaseAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_conversation,
					null);
			viewHolder = new ViewHolder();
			viewHolder.imageView_photo = (ImageView) convertView
					.findViewById(R.id.imageView_photo);
			viewHolder.imageView_warning = (ImageView) convertView
					.findViewById(R.id.imageView_warning);
			viewHolder.imageView_read = (ImageView) convertView
					.findViewById(R.id.imageView_read);
			viewHolder.textView_body = (TextView) convertView
					.findViewById(R.id.textView_body);
			viewHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			viewHolder.textVIew_date = (TextView) convertView
					.findViewById(R.id.textVIew_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 获得要适配的数据对象
		Conversation conversation = getItem(position);
		// 如果名字和电话号相等,则显示红色的小警告图片
		if (conversation.getName().equals(conversation.getPhone())) {
			viewHolder.imageView_warning.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imageView_warning.setVisibility(View.INVISIBLE);
		}
		// 如果没有读取,显示小绿点图片
		if (conversation.getRead() == 0) {// 0未读
			viewHolder.imageView_read.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imageView_read.setVisibility(View.INVISIBLE);
		}
		viewHolder.textView_body.setText(conversation.getBody());
		viewHolder.textVIew_date.setText(conversation.getFormatdate());
		viewHolder.textView_name.setText(conversation.getName());
		// 通过photoid获得图片
		Bitmap bitmap = ContactManager.getPhotoByPhotoId(context,
				conversation.getPhotoid());
		if (bitmap != null) {
			// 获得圆形头像
			Bitmap circleBitmap = ImageManager.circleBitmap(context, bitmap);
			viewHolder.imageView_photo.setImageBitmap(circleBitmap);
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView imageView_photo = null;// 头像
		ImageView imageView_warning = null;// 未知联系人
		ImageView imageView_read = null;// 是否已经阅读
		TextView textView_name = null;// 联系人姓名
		TextView textView_body = null;// 内容
		TextView textVIew_date = null;// 时间
	}
}
