package com.fgr.miaoxin.util;

import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.fgr.miaoxin.bean.BlogImage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends OrmLiteSqliteOpenHelper{
	
	private static DBHelper instance;
	
	public static DBHelper getInstance(Context context){
		
		if(instance==null){
			synchronized (DBHelper.class) {
				if(instance==null){
					instance = new DBHelper(context);
				}
			}
		}
		
		return instance;
	}
	
	private DBHelper(Context context) {
		super(context, "blog_image.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		//格局BlogImage类创建数据表
		try {
			TableUtils.createTableIfNotExists(arg1, BlogImage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2, int arg3) {
		try {
			TableUtils.dropTable(arg1, BlogImage.class, true);
			onCreate(arg0, arg1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
