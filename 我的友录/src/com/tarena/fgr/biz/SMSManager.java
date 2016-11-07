package com.tarena.fgr.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tarena.fgr.entity.Conversation;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.utils.LogUtilS;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.widget.TextView;

/**
 * 短信业务处理类
 * 
 * @author 冯国芮 2016年10月10日 15:48:26
 * 
 */
public class SMSManager {
	// 短信会话Uri
	public static final Uri CONVERSATION_URI = Uri
			.parse("content://mms-sms/conversations");
	// 短信Uri 对应的ContentProvider会协调处理短信的收件箱和发件箱
	public static final Uri SMS_URI = Uri.parse("content://sms");
	// 短信发件箱:
	// 注意sent 和 send
	public static final Uri SMS_SEND_URI = Uri.parse("content://sms/sent");
	// 短信收件箱:
	public static final Uri SMS_INBOX_URI = Uri.parse("content://sms/inbox");

	public static List<Conversation> getConversations(Context context) {
		List<Conversation> conversations = new ArrayList<Conversation>();
		ContentResolver resolver = context.getContentResolver();
		String[] projection = new String[] { "body", "date", "read",
				"thread_id", "address" };
		String sortOrder = "date" + " desc";// 空格
		Cursor cursor = resolver.query(CONVERSATION_URI, projection, null,
				null, sortOrder);
		while (cursor.moveToNext()) {
			Conversation conversation = new Conversation();
			String body = cursor.getString(cursor.getColumnIndex("body"));
			long date = cursor.getLong(cursor.getColumnIndex("date"));
			int read = cursor.getInt(cursor.getColumnIndex("read"));
			int thread_id = cursor.getInt(cursor.getColumnIndex("thread_id"));
			String phone = cursor.getString(cursor.getColumnIndex("address"));
			conversation.setBody(body);
			conversation.setDate(date);
			conversation.setRead(read);
			conversation.setThread_id(thread_id);
			conversation.setPhone(phone);

			int photoId = CalllogManager.getPhotoidByNumber(context, phone);
			conversation.setPhotoid(photoId);
			String formatDate = CalllogManager.convertTime(date);
			conversation.setFormatdate(formatDate);
			String name = CalllogManager.getNameByNumber(context, phone);
			if (TextUtils.isEmpty(name)) {// 如果是陌生人显示电话号
				conversation.setName(phone);
			} else {// 熟悉的人显示联系人姓名
				conversation.setName(name);
			}
			conversations.add(conversation);
		}
		cursor.close();
		return conversations;
	}

	// 封装短信查询的业务处理的方法
	public static List<Sms> getSMSes(Context context, int thread_id) {
		List<Sms> smses = new ArrayList<Sms>();
		ContentResolver resolver = context.getContentResolver();
		String[] projection = new String[] { "_id", "address", "date", "type",
				"body" };
		// 升序排序
		String sortOrder = "date" + " asc";// 空格
		String selection = "thread_id=?";
		Cursor cursor = resolver.query(SMS_URI, projection, selection,
				new String[] { String.valueOf(thread_id) }, sortOrder);
		while (cursor.moveToNext()) {

			Sms sms = new Sms();

			int _id = cursor.getInt(cursor.getColumnIndex("_id"));
			String address = cursor.getString(cursor.getColumnIndex("address"));
			long date = cursor.getLong(cursor.getColumnIndex("date"));
			int type = cursor.getInt(cursor.getColumnIndex("type"));
			String body = cursor.getString(cursor.getColumnIndex("body"));

			sms.set_id(_id);
			sms.setAddress(address);
			sms.setDate(date);
			sms.setType(type);
			sms.setBody(body);
			// 格式化时间
			// String formateDate = CalllogManager.convertTime(date);
			// sms.setFormateDate(formateDate);
			// 格式化日期和通话记录的日期有一些不一样,稍后进行修改
			String formateDate = chatTime(date);
			sms.setFormateDate(formateDate);

			smses.add(sms);
		}
		cursor.close();
		return smses;
	}

	/**
	 * 聊天时间的格式化
	 * 
	 * @param stamp
	 *            时间毫秒值
	 * @return 格式化好的时间
	 */
	@SuppressLint("SimpleDateFormat")
	public static String chatTime(long stamp) {
		// 时间是当天格式化为 HH:mm
		// 昨天格式化为 昨天 HH:mm
		// 其他时间格式化为 yyyy-
		int daydiff = CalllogManager.dayDiff(stamp);
		String strdate = "";
		// 聊天时间是当天
		if (daydiff == 0) {
			strdate = new SimpleDateFormat("HH:mm").format(new Date(stamp));
		} else if (daydiff == 1) {
			strdate = "昨天 "
					+ new SimpleDateFormat("HH:mm").format(new Date(stamp));
		} else {
			strdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(
					stamp));
		}
		return strdate;
	}

	// 测试方法
	// 看看有那些列名,列名和数据库,有可能不对应
	public static void getConversationColumn(Context context) {
		// 短信会话表列名
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver
				.query(CONVERSATION_URI, null, null, null, null);
		if (cursor.moveToNext()) {
			int count = cursor.getColumnCount();
			for (int i = 0; i < count; i++) {
				LogUtilS.i("Tag:",
						cursor.getColumnName(i) + "-->" + cursor.getString(i));
			}
		}
		cursor.close();
	}

	// 测试方法
	// 看看有那些列名,列名和数据库,有可能不对应
	public static void getSMSColumn(Context context) {
		// 短信列名
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(SMS_URI, null, null, null, null);
		if (cursor.moveToNext()) {
			int count = cursor.getColumnCount();
			for (int i = 0; i < count; i++) {
				LogUtilS.i("Tag:",
						cursor.getColumnName(i) + "-->" + cursor.getString(i));
			}
		}
		cursor.close();
	}

	// 删除会话短信
	public static void deleteConveration(Context context, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		String where = "thread_id=?";
		resolver.delete(CONVERSATION_URI, where,
				new String[] { String.valueOf(thread_id) });
	}

	// 删除聊天短信[chatActivity]
	public static void deleteSms(Context context, int _id) {
		ContentResolver resolver = context.getContentResolver();
		String where = "_id = ?";
		resolver.delete(SMS_URI, where, new String[] { String.valueOf(_id) });
	}

	// 修改短信聊天的以读和未读状态
	public static void updateConverationType(Context context, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		// 更新短信收件箱数据库
		ContentValues values = new ContentValues();
		values.put("read", 1);// 由未读该为以读,要该的数据
		String where = "thread_id = ?";// 修改的条件
		String[] selectionArgs = new String[] { String.valueOf(thread_id) };// 条件的值
		resolver.update(SMS_INBOX_URI, values, where, selectionArgs);
	}

	/**
	 * 自己给别人发, 将短信插入到数据库中,
	 * 
	 * @param context
	 * @param phone
	 *            电话号码
	 * @param body
	 *            内容
	 */
	public static void insertSms(Context context, String phone, String body) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put("date", new Date().getTime());// 发送时间
		values.put("read", 1);// 阅读状态
		values.put("type", 2);// 1收2发
		values.put("address", phone);// 发送的电话号码
		values.put("body", body);// 发送的内容
		resolver.insert(SMS_SEND_URI, values);
		LogUtilS.i(">>>>>>>>>>>>>>>>", "insertSms");
	}

	// 在广播中得到短信
	public static Sms getSmsByReceiver(Bundle bundle) {
		Object[] pdus = (Object[]) bundle.get("pdus");
		// Android设备接收到的SMS是以pdu形式的(protocol description unit)。
		// 所以从intent提取数据时就会遇到pdus。
		SmsMessage[] messages = new SmsMessage[pdus.length];// 注意包
		for (int i = 0; i < pdus.length; i++) {
			messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
		}
		StringBuilder builder = new StringBuilder();// 高效,不安全
		long date = 0;
		String address = "";
		for (int i = 0; i < messages.length; i++) {
			if (i == 0) {// 电话号码和时间是一样的,所以取一次就可以了
				// 获得短信的电话号码
				// address = messages[i].getOriginatingAddress();
				address = messages[i].getDisplayOriginatingAddress();
				// 获得短信的发送时间
				date = messages[i].getTimestampMillis();
			}
			builder.append(messages[i].getDisplayMessageBody());
		}
		// LogUtilS.i("Tag:address", address);
		// LogUtilS.i("Tag:date", "" + date);
		// LogUtilS.i("Tag:body", builder.toString());
		Sms sms = new Sms();
		sms.setAddress(address);
		sms.setDate(date);
		sms.setBody(builder.toString());
		sms.setType(1);// 收到的短信
		return sms;
	}

	// 将别人发来的短信保存在收件箱中,chatActivity
	public static void saveReciveMessage(Context context, Sms sms, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();

		values.put("thread_id", thread_id);
		values.put("body", sms.getBody());
		values.put("address", sms.getAddress());
		values.put("date", sms.getDate());
		values.put("read", 1);// 不显示小绿点
		values.put("type", sms.getType());

		resolver.insert(SMS_INBOX_URI, values);
	}

	// 将别人发来的短信保存在收件箱中,会话用
	public static void save(Context context, Sms sms, int thread_id) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues values = new ContentValues();

		// 2016年10月12日 09:29:16
		// 冯国芮:这里的thread_id 我全部指定了0,也没有任何作用,也没报任何错误
		values.put("thread_id", thread_id);
		values.put("body", sms.getBody());
		values.put("address", sms.getAddress());
		values.put("date", sms.getDate());
		values.put("read", 0);// 显示小绿点
		values.put("type", sms.getType());

		resolver.insert(SMS_INBOX_URI, values);
	}

	public static final String SEND_SMS = "com.tarena.fgr.youlu.SEND_SMS";

	/**
	 * 实现发送短信功能 </br>这样的发送,不会向数据库写如短信记录
	 * 
	 * @param context
	 * @param body
	 *            短信内容
	 * @param phone
	 *            短信电话号
	 */
	public static void sendSms(Context context, String body, String phone) {
		// 直接使用发送短信的api，无需启动系统的短信应用
		SmsManager smsManager = SmsManager.getDefault();
		// 把长短信截成若干条短短信
		ArrayList<String> sms = smsManager.divideMessage(body);
		for (String string : sms) {
			Intent intent = new Intent(SEND_SMS);
			intent.putExtra("body", body);
			intent.putExtra("phone", phone);
			// 我们是通过广播发送的短信
			// 所以回执使用getBroadcast,他还有getService,getActivity
			PendingIntent sentIntent = PendingIntent.getBroadcast(context, 100,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			// arg0:接收方的手机,目标号码
			// arg1:短信中心号码，null表示使用默认,(发送方的手机号码???)
			// arg2:短信正文
			// arg3:自己发送是否成功的回执
			// arg4:对方接收是否成功的回执
			smsManager.sendTextMessage(phone, null, string, sentIntent, null);
		}
	}

	public static Typeface Fonts;

	// 自定义的字体
	public static void stetFontType(Context context, TextView textView) {
		if (Fonts == null) {
			AssetManager manager = context.getAssets();
			String path = "fonts/customfont.ttf";// 自定义的字体
			Fonts = Typeface.createFromAsset(manager, path);
		}
		// 将字体应用到控件上
		textView.setTypeface(Fonts);
	}

}
