package com.tarena.fgr.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.fgr.biz.ContactManager;
import com.tarena.fgr.biz.ImageManager;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.youlu.R;

/**
 * 网格布局,联系人适配器,包含一个头像和一个联系人姓名
 * 
 * @author anzhuo 2016年9月30日 14:28:37
 * 
 */
public class ContactBaseAdapter extends MyBaseAdapter<Contact> {

	public ContactBaseAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		// 优化
		if (convertView == null) {
			convertView = layoutInflater
					.inflate(R.layout.item_contact, null);
			viewHolder = new ViewHolder();
			viewHolder.imageView_header = (ImageView) convertView
					.findViewById(R.id.imageView_contact_photo);
			viewHolder.textView_name = (TextView) convertView
					.findViewById(R.id.textView_contact_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		Contact contact = getItem(position);
		viewHolder.textView_name.setText(contact.getName());

		// 根据头像id查出头像的信息
		// 2016年9月30日 15:48:33
		// 冯国芮:这样一直查寻数据库也不好,应该优化一下,将查询处理的图片缓存
		Bitmap bitmap = ContactManager.getPhotoByPhotoId(context,// 在MyBaseAdapter封装了context
				contact.getPhotoid());
		if (bitmap != null) {
			// 添加第一个
			if (position == 0) {
				viewHolder.textView_name.setText(contact.getName());
				viewHolder.imageView_header
						.setImageResource(R.drawable.ic_add_contact);
			} else {
				// 添加后续的
				// 有头像返回头像,否则返回默认图片
				Bitmap circleBitmap = ImageManager
						.circleBitmap(context, bitmap);
				viewHolder.imageView_header.setImageBitmap(circleBitmap);
			}
		}
		// 2016年9月30日 15:56:28
		// 冯国芮:在inflate_contact.xml中,直接给imageView设置了图片,这样接不需要在判断他没有图片的时候了
		// else {
		// viewHolder.imageView_header.setImageResource(R.drawable.ic_contact);//
		// 默认返回一张图片
		// }
		return convertView;
	}

	public class ViewHolder {
		ImageView imageView_header = null;
		TextView textView_name = null;
	}

}
