package com.tarena.fgr.biz;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.provider.ContactsContract.PhoneLookup;

import com.tarena.fgr.entity.Calllog;

/**
 * 通话记录业务处理类
 * 
 * @author 冯国芮
 * 
 */
public class CalllogManager {
	public static List<Calllog> getCalllogs(Context context) {
		List<Calllog> calllogs = new ArrayList<Calllog>();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = CallLog.Calls.CONTENT_URI;// 注意类名
		// 以Calls内容提供者中无法直接查询联系人的头像
		// 查询的字段直接查到的是5个列
		// id,姓名,电话,日期,类型
		String[] projection = new String[] { Calls._ID, Calls.CACHED_NAME,
				Calls.NUMBER, Calls.DATE, Calls.TYPE };
		// 按照时间降序排序,升序是asc
		// 最近的通话记录在最前面
		String sortOrder = Calls.DATE + " desc ";// 空格

		Cursor cursor = resolver.query(uri, projection, null, null, sortOrder);

		while (cursor.moveToNext()) {

			Calllog calllog = new Calllog();

			int _id = cursor.getInt(cursor.getColumnIndex(Calls._ID));// id
			String name = cursor.getString(cursor
					.getColumnIndex(Calls.CACHED_NAME));// 姓名
			String phone = cursor
					.getString(cursor.getColumnIndex(Calls.NUMBER));// 电话
			long date = cursor.getLong(cursor.getColumnIndex(Calls.DATE));// 时间
			int type = cursor.getInt(cursor.getColumnIndex(Calls.TYPE));// 类型

			calllog.setId(_id);
			calllog.setName(name);
			calllog.setPhone(phone);
			calllog.setType(type);
			calllog.setCallTime(date);
			calllog.setPhotoid(getPhotoidByNumber(context, phone));// 设置头像
			calllog.setFormatCallTimeString(convertTime(date));// 格式化时间
			// 把通话记录添加到集合中
			calllogs.add(calllog);
		}

		cursor.close();

		return calllogs;
	}

	// 通话事件的格式化的思路
	// 1.如果通话时间是当天的话,时间格式化为-->HH:mm
	// 2.如果是一天以前,时间显示为-->昨天
	// 3.如果是一周以内,时间显示为-->星期几
	// 4.如果是一周以前,时间显示为-->yyyy-MM-dd

	// 日期格式化
	@SuppressLint("SimpleDateFormat")
	public static String convertTime(long stamp) {
		String strFormat = "";
		int daydiff = dayDiff(stamp);

		if (daydiff == 0) {
			// 2016年10月9日 16:21:07
			// 冯国芮:可以再往下写,判断是否是刚刚.60分钟之内,几小时之内,上午下午等...
			// 通话时间是当天
			Date date = new Date();
			date.setTime(stamp);
			strFormat = new SimpleDateFormat("HH:mm").format(date);
		} else if (daydiff == 1) {
			// 通话时间是昨天
			strFormat = "昨天";
		} else if (daydiff <= 7) {
			// 通话时间是一周以内的
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(stamp);
			int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
			switch (dayOfWeek) {
			case Calendar.MONDAY:
				strFormat = "周一";
				break;
			case Calendar.TUESDAY:
				strFormat = "周二";
				break;
			case Calendar.WEDNESDAY:
				strFormat = "周三";
				break;
			case Calendar.THURSDAY:
				strFormat = "周四";
				break;
			case Calendar.FRIDAY:
				strFormat = "周五";
				break;
			case Calendar.SATURDAY:
				strFormat = "周六";
				break;
			case Calendar.SUNDAY:
				strFormat = "周日";
				break;
			default:
				break;
			}
		} else {
			// 一周以前的通话
			strFormat = new SimpleDateFormat("yyyy-MM-dd").format(new Date(
					stamp));
		}
		return strFormat;
	}

	/**
	 * 数据库时间和当前时间差几天
	 * 
	 * @param stamp
	 *            数据库的时间
	 * @return
	 */
	public static int dayDiff(long stamp) {
		// 获得当前系统时间的日历对象
		Calendar calendar1 = Calendar.getInstance();
		// 通话时间的日历对象
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(stamp);
		// 两个时间相差的天数
		int daydiff = calendar1.get(Calendar.DAY_OF_YEAR)// 一年中的第几天
				- calendar2.get(Calendar.DAY_OF_YEAR);
		return daydiff;
	}

	/**
	 * 通过电话号码查询头像id
	 * 
	 * @param context
	 * @param number
	 *            电话号码
	 * @return 头像id
	 */
	public static int getPhotoidByNumber(Context context, String number) {
		int photoid = 0;
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);
		String[] projection = new String[] { PhoneLookup.PHOTO_ID };
		Cursor cursor = resolver.query(uri, projection, null, null, null);
		if (cursor.moveToNext()) {
			photoid = cursor.getInt(0);
		}
		cursor.close();
		return photoid;
	}

	/**
	 * 通过电话号码查询姓名
	 * 
	 * @param context
	 * @param number
	 *            电话号码
	 * @return 头像id
	 */
	public static String getNameByNumber(Context context, String number) {
		String name = "";
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, number);
		String[] projection = new String[] { PhoneLookup.DISPLAY_NAME };
		Cursor cursor = resolver.query(uri, projection, null, null, null);
		if (cursor.moveToNext()) {
			name = cursor.getString(0);
		}
		cursor.close();
		return name;
	}

	/**
	 * 把通话记录从数据库里删除
	 * 
	 * @param context
	 * @param calllog
	 *            联系人
	 */
	public static void deleteCalllog(Context context, Calllog calllog) {
		ContentResolver resolver = context.getContentResolver();
		// Uri uri = Uri.parse("content://call_log/calls");
		Uri uri = CallLog.Calls.CONTENT_URI;
		String where = Calls.NUMBER + "=?";
		String[] args = new String[] { calllog.getPhone() };
		resolver.delete(uri, where, args);
	}

	public static void deleteCalllog(Context context, String phone) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = CallLog.Calls.CONTENT_URI;
		String where = Calls.NUMBER + "=?";
		String[] args = new String[] { phone };
		resolver.delete(uri, where, args);
	}
}
