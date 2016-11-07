package com.tarena.fgr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.ImageManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.youlu.R;

public class SmsBaseAdapter extends MyBaseAdapter<Sms> {

	public SmsBaseAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		// Sms sms = getItem(position);
		int type = getItemViewType(position);
		if (convertView == null) {
			// 获得当前要适配的数据采用的布局的类型
			// 2016年10月11日 14:23:01
			// 冯国芮:这里不先得到sms对象,而是采用getItemViewType来得到
			// 这样没有就将Adapter写死
			// int type = sms.getType();
			// if (type == 1) {// 接收到的短信
			if (type == 0) {// 接收到的短信
				convertView = layoutInflater.inflate(R.layout.item_left, null);
			}
			// if (type == 2) {// 发送的短信
			if (type == 1) {// 发送的短信
				convertView = layoutInflater.inflate(R.layout.item_right, null);
			}
			// 左右两个布局,元素的id一样
			viewHolder = new ViewHolder();

			viewHolder.ImageView_photo = (ImageView) convertView
					.findViewById(R.id.imageView_photo);
			viewHolder.textView_body = (TextView) convertView
					.findViewById(R.id.textView_body);
			viewHolder.textView_time = (TextView) convertView
					.findViewById(R.id.textView_time);

			// 设置聊天字体
			SMSManager.stetFontType(context, viewHolder.textView_body);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Sms sms = getItem(position);

		viewHolder.textView_body.setText(sms.getBody());
		// 2016年10月11日 11:52:01
		// 在后面应该判断时间,如果一分钟之内,把时间隐藏掉

		if (position == 0) {
			viewHolder.textView_time.setText(sms.getFormateDate());
		} else {// 1分钟之内不显示时间
			Sms smsUp = getItem(position - 1);
			long time = sms.getDate() - smsUp.getDate();
			// 显示空,有空隙
			// viewHolder.textView_time
			// .setText(getTime(sms.getFormateDate(), time));
			// 设置为空,不合适,应该隐藏
			if (time <= 1000 * 60) {
				viewHolder.textView_time.setVisibility(View.GONE);
			} else {
				viewHolder.textView_time.setVisibility(View.VISIBLE);
				viewHolder.textView_time.setText(sms.getFormateDate());
			}
		}

		// 头像
		Bitmap bitmap = null;
		if (sms.getType() == 1) {// 接收到的短信
			// 通过号码得头像id
			int photoId = CalllogManager.getPhotoidByNumber(context,
					sms.getAddress());
			// 通过头像id得头像图片
			bitmap = ContactManager.getPhotoByPhotoId(context, photoId);
		}
		if (sms.getType() == 2) {// 发送的短信
			// 自己的头像
			// 2016年10月11日 13:44:16
			// 得到用户直接的头像,如果用户设置了头像,用用户的,否则用一个默认的
			// 目前用res目录下的照片,作为自己的头像
			bitmap = BitmapFactory.decodeResource(convertView.getResources(),
					R.drawable.ic_contact);
		}

		if (bitmap != null) {
			// 得到圆形头像
			Bitmap circleBitmap = ImageManager.circleBitmap(context, bitmap);
			viewHolder.ImageView_photo.setImageBitmap(circleBitmap);
		}

		return convertView;
	}

	// private String getTime(String str, long time) {
	// if (time >= 1000 * 60) {
	// return str;
	// } else {
	// return "";
	// }
	// }

	@Override
	// 改变数据适配时采用的布局的类型
	public int getItemViewType(int position) {
		// 获得当前适配的数据对象
		Sms sms = getItem(position);
		int type = sms.getType();
		// return super.getItemViewType(position);
		return type - 1;// 0和1
	}

	@Override
	// 改变数据适配时采用的布局的个数
	public int getViewTypeCount() {
		// return super.getViewTypeCount();
		return 2;
	}

	public class ViewHolder {
		ImageView ImageView_photo;// 头像
		TextView textView_time;// 时间
		TextView textView_body;// 内容
	}

}
