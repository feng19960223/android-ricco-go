package com.tarena.fgr.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.tarena.fgr.entity.BlackPhone;
import com.tarena.fgr.entity.BlackSMS;

/**
 * 对数据库表的数据的管理
 * 
 * @author 冯国芮
 * 
 */
public class DBUtil {// DAO
	private MyDBHelper helper = null;

	public DBUtil(Context context) {
		helper = MyDBHelper.getInstance(context);
	}

	// 这样的方式,在下面的方法,不能加static

	/**
	 * 添加黑名单电话
	 * 
	 * @param number
	 */
	public void insertBlackNumber(String number) {
		// 如果已经数据库已经有记录,不再执行数据的添加
		if (isBlackNumber(number)) {
			return;
		}
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", number);
		db.insert(MyDBHelper.TABLE_BLACKNUMBER, null, values);
		db.close();
	}

	/**
	 * 查询某一个电话号,是否是黑名单电话
	 * 
	 * @param number
	 * @return
	 */
	public boolean isBlackNumber(String number) {
		SQLiteDatabase db = helper.getReadableDatabase();
		// Cursor cursor = db.rawQuery(
		// "select _id from blacknumber where number =?",
		// new String[] { number });
		Cursor cursor = db.query(MyDBHelper.TABLE_BLACKNUMBER, null,
				"number = ?", new String[] { number }, null, null, null);
		if (cursor.moveToNext()) {
			db.close();
			return true;
		}
		db.close();
		return false;
	}

	/**
	 * 把拦截的短信存入记录黑名单电话的短信数据表
	 */
	public void insertSMS(BlackSMS sms) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", sms.getNumber());
		values.put("body", sms.getBody());
		values.put("date", sms.getDate());
		db.insert(MyDBHelper.TABLE_SMSLIST, null, values);
		db.close();
	}

	/**
	 * 把拦截的电话插入到记录黑名单电话的数据表
	 * 
	 * @param phone
	 */
	public void insertPhone(BlackPhone phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("number", phone.getNumber());
		values.put("type", phone.getType());
		values.put("date", phone.getDate());
		db.insert(MyDBHelper.TABLE_PHONELIST, null, values);
		db.close();
	}

}
