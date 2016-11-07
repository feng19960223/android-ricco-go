package com.tarena.utils;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.tarena.entity.CityNameBean;

public class DBUtil {
	DBHelper dbHelper;
	private Dao<CityNameBean, String> dao;// ?=主键id的类型

	public DBUtil(Context context) {
		DBHelper(context);
	}

	private void DBHelper(Context context) {
		dbHelper = DBHelper.getInstance(context);
		try {
			dao = dbHelper.getDao(CityNameBean.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void save(CityNameBean bean) {// 新增
		try {
			dao.createIfNotExists(bean);
			// dao.delete(bean);//删除
			// dao.update(bean);//更新
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 新增一堆,效率更高,大约10倍,3000条700毫秒
	public void addBatch(final List<CityNameBean> list) {
		try {
			dao.callBatchTasks(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					for (CityNameBean cityNameBean : list) {
						save(cityNameBean);
					}
					return null;
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void saveAll(List<CityNameBean> list) {// 新增一堆.3000条8秒
		for (CityNameBean cityNameBean : list) {
			save(cityNameBean);
		}
	}

	public List<CityNameBean> queryAll() {
		try {
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("sql查询全部异常" + e);
		}
	}
}
