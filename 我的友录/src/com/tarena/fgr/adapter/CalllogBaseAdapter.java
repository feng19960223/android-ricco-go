package com.tarena.fgr.adapter;

import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.ImageManager;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.youlu.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.CallLog.Calls;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class CalllogBaseAdapter extends MyBaseAdapter<Calllog> {

	public CalllogBaseAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_calllog, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView_photo = (ImageView) convertView
					.findViewById(R.id.imageView_photo);
			viewHolder.imageView_warning = (ImageView) convertView
					.findViewById(R.id.imageView_warning);
			viewHolder.imageView_call = (ImageView) convertView
					.findViewById(R.id.imageView_call);
			viewHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			viewHolder.textView_phone = (TextView) convertView
					.findViewById(R.id.textView_phone);
			viewHolder.textVIew_date = (TextView) convertView
					.findViewById(R.id.textVIew_date);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 获得要适配的数据对象
		Calllog calllog = getItem(position);
		// 如果是陌生人来的
		// 2016年10月9日 15:18:04
		// 冯国芮:接下来的联系人名字,感觉有一点重复,但不知道怎么回事,如果不这样重复写,就会出现bug...
		if (TextUtils.isEmpty(calllog.getName())) {// 没有名字
			viewHolder.textView_name.setTextColor(Color.RED);
			viewHolder.textView_name.setText("未知联系人");
			// 显示红叹号
			viewHolder.imageView_warning.setVisibility(View.VISIBLE);
		} else {// 如果是通讯录来电
			viewHolder.textView_name.setTextColor(Color.BLACK);
			viewHolder.imageView_warning.setVisibility(View.INVISIBLE);
			viewHolder.textView_name.setText(calllog.getName());
		}

		viewHolder.textView_phone.setText(calllog.getPhone());
		viewHolder.textVIew_date.setText(calllog.getFormatCallTimeString());
		if (calllog.getType() == Calls.OUTGOING_TYPE) {// 播出去的电话
			// Calls.INCOMING_TYPE;打进来的电话
			// 如果是打出去的电话,显示小图片
			viewHolder.imageView_call.setVisibility(View.VISIBLE);
		} else {
			viewHolder.imageView_call.setVisibility(View.INVISIBLE);
		}

		Bitmap bitmap = ContactManager.getPhotoByPhotoId(context,
				calllog.getPhotoid());// 得到头像
		if (bitmap != null) {
			// 得到圆形头像
			Bitmap circleBitmap = ImageManager.circleBitmap(context, bitmap);
			viewHolder.imageView_photo.setImageBitmap(circleBitmap);
		}
		return convertView;
	}

	public class ViewHolder {
		ImageView imageView_photo = null;// 头像
		ImageView imageView_warning = null;// 未知联系人
		ImageView imageView_call = null;// 通话类型
		TextView textView_name = null;// 联系人姓名
		TextView textView_phone = null;// 电话
		TextView textVIew_date = null;// 时间
	}

}
