package com.tarena.fgr.biz;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.fgr.adapter.CalllogBaseAdapter;
import com.tarena.fgr.adapter.ContactBaseAdapter;
import com.tarena.fgr.adapter.ConversationBaseAdapter;
import com.tarena.fgr.adapter.SmsBaseAdapter;
import com.tarena.fgr.db.DBUtil;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.entity.Contact;
import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.youlu.R;

/**
 * contact的点击图片弹出的不同内容
 * 
 * @author 冯国芮2016年10月8日 11:15:22
 */
/**
 * @author anzhuo
 * 
 */
public class DialogManager {
	/**
	 * 添加新的联系人,弹出一个dialog
	 * 
	 * @param context
	 */
	public static void showAddContactDialog(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		// builder.setIcon(icon);
		// builder.setTitle(title);
		// builder.setMessage(message);
		final AlertDialog dialog = builder.create();
		dialog.show();// 显示
		View view = View.inflate(context, R.layout.daliog_add_contact, null);
		dialog.setContentView(view);
		// 初始化对话框上的控件;
		ImageButton imageButton_close = (ImageButton) view
				.findViewById(R.id.imageButton_close);
		ImageButton imageButton_selected = (ImageButton) view
				.findViewById(R.id.imageButton_selected);

		// 必须填写的姓名和电话
		final EditText editText_name = (EditText) view
				.findViewById(R.id.edit_dialogadd_name);
		final EditText editText_phone = (EditText) view
				.findViewById(R.id.edit_dialogadd_phone);
		// 其他信息
		final EditText editText_address = (EditText) view
				.findViewById(R.id.edit_dialogadd_address);
		final EditText editText_email = (EditText) view
				.findViewById(R.id.edit_dialogadd_email);

		imageButton_close.setOnClickListener(new OnClickListener() {
			// 取消按钮
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		imageButton_selected.setOnClickListener(new OnClickListener() {
			// 确定按钮
			@Override
			public void onClick(View v) {
				// 1得到数据,2保存数据3dismiss4.刷新gridview
				String name = editText_name.getText().toString();
				String phone = editText_phone.getText().toString();
				if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
					Toast.makeText(context, "请输入联系人的姓名和电话号码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				String address = editText_address.getText().toString();
				String email = editText_email.getText().toString();
				// 把联系人的信息添加到联系人的列表中
				// 调用系统的联系人添加功能
				Intent intent = new Intent(
						ContactsContract.Intents.SHOW_OR_CREATE_CONTACT); // 显示或创建联系人

				Uri dataUri = Uri.parse("tel:" + phone);
				intent.setData(dataUri);
				// 设置联系人的其他信息
				intent.putExtra(ContactsContract.Intents.Insert.NAME, name);
				intent.putExtra(ContactsContract.Intents.Insert.EMAIL, email);
				intent.putExtra(ContactsContract.Intents.Insert.POSTAL, address);

				context.startActivity(intent);
				dialog.dismiss();
				// 刷新数据谁调用谁刷新,而不是在这里显示刷新
			}
		});
	}

	/**
	 * 点击非第一个时,查看详细信息,修改相关信息
	 * 
	 * @param context
	 */
	public static void showDetailContactDialog(final Context context,
			final Contact contact) {
		// 创建显示联系人的信息的对话框
		// 初始化对话框上的控件
		// 构建position.从集合中获得联系人对象
		// 再将联系人的数据设置到对话框的相应控件上
		AlertDialog.Builder builder = new Builder(context);
		// builder.setIcon(icon);
		// builder.setTitle(title);
		// builder.setMessage(message);
		final AlertDialog dialog = builder.create();
		dialog.show();// 显示
		View view = View.inflate(context, R.layout.daliog_information_contact,
				null);
		dialog.setContentView(view);
		// 初始化对话框上的控件;
		ImageButton imageButton_close = (ImageButton) view
				.findViewById(R.id.imageButton_close);// 取消
		ImageButton imageButton_edit = (ImageButton) view
				.findViewById(R.id.imageButton_edit);// 修改
		ImageView imageView_phone = (ImageView) view
				.findViewById(R.id.imageView_contact_photo);// 头像
		TextView textView_name = (TextView) view
				.findViewById(R.id.contact_name);// 姓名
		TextView textView_phone = (TextView) view
				.findViewById(R.id.contact_phone);// 电话
		final TextView textView_email = (TextView) view
				.findViewById(R.id.contact_email);
		TextView textView_address = (TextView) view
				.findViewById(R.id.contact_address);
		ImageView add_blackphone = (ImageView) view
				.findViewById(R.id.add_blackphone);
		ImageView send_email = (ImageView) view.findViewById(R.id.send_email);// 发送邮件
		// ImageView send_or_call = (ImageView) view// 弹出菜单,打电话或者发短信,目前未实现
		// .findViewById(R.id.send_or_call);

		send_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
				emailIntent.setData(Uri.parse("mailto:"
						+ textView_email.getText().toString()));
				emailIntent.putExtra(Intent.EXTRA_SUBJECT, "标题");
				emailIntent.putExtra(Intent.EXTRA_TEXT, "内容");
				context.startActivity(emailIntent);
			}
		});
		add_blackphone.setOnClickListener(new OnClickListener() {
			// 添加黑名单电话
			@Override
			public void onClick(View v) {
				String phone = contact.getPhone();
				new DBUtil(context).insertBlackNumber(phone);
				Toast.makeText(context, "加入黑名单成功", Toast.LENGTH_SHORT).show();
				// 删除联系人的显示
				ContactManager.deleteContact(context, contact);// 删除联系人
				dialog.dismiss();
			}
		});

		// 头像
		Bitmap bitmap = ContactManager.getPhotoByPhotoId(context,// 在MyBaseAdapter封装了context
				contact.getPhotoid());
		if (bitmap != null) {
			Bitmap circleBitmap = ImageManager.circleBitmap(context, bitmap);
			imageView_phone.setImageBitmap(circleBitmap);
		}

		textView_name.setText(contact.getName());// 设置姓名
		textView_phone.setText(contact.getPhone());// 设置电话
		textView_email.setText(contact.getEmail());
		textView_address.setText(contact.getAddress());

		imageButton_close.setOnClickListener(new OnClickListener() {// 取消
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
		// 点击修改
		imageButton_edit.setOnClickListener(new OnClickListener() {// 修改联系人的信息
					@Override
					public void onClick(View v) {
						dialog.dismiss();
						// 要修改联系人的信息之前要先清空缓存中的数据,不然查出来的数据还是原来的数据
						ContactManager.clearCache(contact);
						// 创建一个激活系统编辑联系人的activity意图
						// 调用系统的联系人编辑的组件实现联系人的编辑处理
						Intent intent = new Intent(Intent.ACTION_EDIT);

						// 文档里找到的
						Uri dataUri = Uri.parse("content://contacts/people/"
								+ contact.getId());
						intent.setData(dataUri);
						// 冯国芮:迷之代码2016年10月8日 16:24:33
						// 当我们调用编辑完成后直接退系统的activity
						intent.putExtra("finishActivityOnSaveCompleted", true);
						context.startActivity(intent);
					}
				});

	}

	// 删除联系人
	public static void showDeleteContactDialog(final Context context,
			final Contact contact, final ContactBaseAdapter adapter) {
		// 弹出一个删除联系人的对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("系统提示");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("确定要删除该联系人吗?");// API要求14
		builder.setNegativeButton("再想想?", null);
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// 执行联系人删除操作时先删除raw_contact联系人账户
				// 再删除联系人在data表中的数据
				// contacts表中的数据不需要手工删除
				// 当联系人的账户信息和数据信息被删除后contentprovide会同步删除

				// 把联系人从数据库里删除
				ContactManager.deleteContact(context, contact);
				// 将数据从适配器中删除,更新适配器UI
				adapter.removeData(contact);
				dialog.dismiss();
			}
		});
		// 显示对话框
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 改变按钮颜色
		// 一定要写在show方法后面
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// 单纯设置一个btn有可能会有高度偏移,所以直接设置了2个
		// 不使用AlertDialog.THEME_DEVICE_DEFAULT_LIGHT这句话就会发生偏移
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	// 删除通话记录对话框
	public static void showDeleteCalllogDialog(final Context context,
			final Calllog calllog, final CalllogBaseAdapter adapter) {
		// 弹出一个删除联系人的对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("系统提示");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("确定要删除该通话记录吗?");// API要求14
		builder.setNegativeButton("再想想?", null);
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// 把联系人从数据库里删除
				CalllogManager.deleteCalllog(context, calllog);

				// context.getContentResolver().delete(
				// Uri.parse("content://call_log/calls"), "number = ?",
				// new String[] { calllog.getPhone() });

				// 将数据从适配器中删除,更新适配器UI
				adapter.removeData(calllog);
				dialog.dismiss();
			}
		});
		// 显示对话框
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 改变按钮颜色
		// 一定要写在show方法后面
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// 单纯设置一个btn有可能会有高度偏移,所以直接设置了2个
		// 不使用AlertDialog.THEME_DEVICE_DEFAULT_LIGHT这句话就会发生偏移
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	// 删除短信对话框
	public static void showDeleteConversationDialog(final Context context,
			final Conversation conversation,
			final ConversationBaseAdapter adapter) {
		// 弹出一个删除联系人的对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("系统提示");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("确定要删除该所以短信记录吗?");// API要求14
		builder.setNegativeButton("再想想?", null);
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				// 把短信从数据库里删除
				SMSManager.deleteConveration(context,
						conversation.getThread_id());
				// 将数据从适配器中删除,更新适配器UI
				adapter.removeData(conversation);
				dialog.dismiss();
			}
		});
		// 显示对话框
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 改变按钮颜色
		// 一定要写在show方法后面
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// 单纯设置一个btn有可能会有高度偏移,所以直接设置了2个
		// 不使用AlertDialog.THEME_DEVICE_DEFAULT_LIGHT这句话就会发生偏移
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	// 删除一条聊天短信对话框
	public static void showDeleteSmsDialog(final Context context,
			final Sms sms, final SmsBaseAdapter adapter) {
		// 弹出一个删除联系人的对话框
		AlertDialog.Builder builder = new AlertDialog.Builder(context,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

		builder.setTitle("系统提示");
		builder.setIcon(R.drawable.ic_warning);
		builder.setMessage("确定要删除该这条短信记录吗?");// API要求14
		builder.setNegativeButton("再想想?", null);
		builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// 把短信从数据库里删除
				SMSManager.deleteSms(context, sms.get_id());
				// 将数据从适配器中删除,更新适配器UI
				adapter.removeData(sms);
				dialog.dismiss();
			}
		});
		// 显示对话框
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 改变按钮颜色
		// 一定要写在show方法后面
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// 单纯设置一个btn有可能会有高度偏移,所以直接设置了2个
		// 不使用AlertDialog.THEME_DEVICE_DEFAULT_LIGHT这句话就会发生偏移
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}
}
