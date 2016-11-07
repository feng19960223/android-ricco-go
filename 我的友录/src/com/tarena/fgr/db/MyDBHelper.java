package com.tarena.fgr.db;

import com.tarena.fgr.utils.LogUtilS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <h1>单例模式设计</h1></br> 数据库(phone.db)</br>--黑名单电话表(blacknumber) id:primary key
 * integer 自增列 number:text</br>--记录黑名单电话的数据表(phonelist) id:主键 自增列 number:text
 * type:integer date:integer</br>--记录黑名单电话的短信数据表(smslist) id:主键 自增列 number:text
 * body:text date:integer
 * 
 * @author 冯国芮 2016年10月13日 09:19:12
 * 
 */
public class MyDBHelper extends SQLiteOpenHelper {
	public static final String DB_NAME = "phone.db";// 数据库名
	public static final String TABLE_BLACKNUMBER = "blacknumber";// 黑名单电话表
	public static final String TABLE_PHONELIST = "phonelist";// 记录黑名单电话的数据表
	public static final String TABLE_SMSLIST = "smslist";// 记录黑名单电话的短信数据表
	public static final int VERSION = 1;

	// 持有一个私有静态本类类型的引用
	private static MyDBHelper myDBHelper = null;

	// 封装一个共有的静态的返回本类实例的方法
	public synchronized static MyDBHelper getInstance(Context context) {
		if (myDBHelper == null) {
			myDBHelper = new MyDBHelper(context);
		}
		return myDBHelper;
	}

	/**
	 * @param context
	 * @param name
	 *            创建的数据库文件的文件名
	 * @param factory
	 * @param version
	 *            数据库的版本号
	 */
	private MyDBHelper(Context context) {
		// db可写可不写
		super(context, DB_NAME, null, VERSION);
	}

	@Override
	// 当创建数据库完成后,执行此方法,在这里写创建表的语句
	public void onCreate(SQLiteDatabase db) {
		// 编写语句,创建三张表
		String blacknumberTable = "CREATE TABLE " + TABLE_BLACKNUMBER + "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "number TEXT NOT NULL)";
		String phonelistTable = "CREATE TABLE "
				+ TABLE_PHONELIST
				+ "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "number TEXT NOT NULL,type INTEGER NOT NULL,date INTEGER NOT NULL)";
		String smslistTable = "CREATE TABLE "
				+ TABLE_SMSLIST
				+ "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "number TEXT NOT NULL,body TEXT NOT NULL,date INTEGER NOT NULL)";

		db.execSQL(blacknumberTable);
		db.execSQL(phonelistTable);
		db.execSQL(smslistTable);

		LogUtilS.i("TAG", "tabel create ok!");
	}

	@Override
	// 版本编号发送变化时,数据库更新时候执行
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
