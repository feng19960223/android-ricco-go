package com.fgr.miaoxin.util;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.j256.ormlite.dao.Dao;
import com.fgr.miaoxin.bean.BlogImage;
import com.fgr.miaoxin.listener.OnDatasLoadFinishListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

public class DBUtil {

	private DBHelper helper;

	private Dao<BlogImage,String> dao;

	public DBUtil(Context context) {
		try {
			helper = DBHelper.getInstance(context);
			dao = helper.getDao(BlogImage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 以异步的形式保存一幅图片到blog_image.db的BlogImage数据表中
	 * 
	 * @param url 要保存图片的网址
	 * @param bitmap 要保存的图片
	 * 
	 */
	public void save(final String url,final Bitmap bitmap){

		new Thread(){
			public void run() {
				try {
					BlogImage bi = new BlogImage();
					bi.setImgUrl(url);
					//bitmap--->byte[]
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 100, stream);
					byte[] bytes = stream.toByteArray();
					//byte[]--->String(Base64编码)
					String base64 = Base64.encodeToString(bytes, Base64.DEFAULT);
					bi.setBitmap(base64);
					//将bi对象作为一条数据记录保存到数据表中
					dao.createIfNotExists(bi);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			};
		}.start();


	}
	
	/**
	 * 以同步的方式从数据表中获取一幅图片
	 * 
	 * @param url 图片所对应的网络地址
	 * 
	 * @return bitmap 数据表中有相应的图片
	 *         null   数据表中没有对应的图片
	 */
	public Bitmap get(String url){
		try {
			//以url作为查询条件，到数据表中进行查询
			List<BlogImage> list = dao.queryForEq("imgUrl",url);
			if(list!=null&&list.size()>0){
				BlogImage bi = list.get(0);
				String base64 = bi.getBitmap();
				//string--Base64解码-->byte[]--BitmapFactory-->bitmap
				byte[] bytes = Base64.decode(base64, Base64.DEFAULT);
				Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
				return bitmap;
			}else{
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("数据库查询时出现错误");
		}
	}
	
	/**
	 * 
	 * 以异步的方式从数据表中获得图片
	 * 
	 * @param url 图片所对应的网址
	 * 
	 * @param listener 监听器，当从数据表中获取数据完毕后
	 *                 调用监听器的onLoadFinish回调方法，并将结果作为参数传入
	 */
	public void get(final String url,final OnDatasLoadFinishListener<Bitmap> listener){

		new Thread(){
			public void run() {
				try {
					List<BlogImage> list = dao.queryForEq("imgUrl", url);
					if(list!=null&&list.size()>0){

						BlogImage bi = list.get(0);

						String base64 = bi.getBitmap();

						byte[] bytes = Base64.decode(base64, Base64.DEFAULT);

						Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

						final List<Bitmap> bms = new ArrayList<Bitmap>();

						bms.add(bitmap);

						new Handler(Looper.getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								listener.onLoadFinish(bms);
							}
						});

					}else{

						new Handler(Looper.getMainLooper()).post(new Runnable() {

							@Override
							public void run() {
								listener.onLoadFinish(null);
							}
						});

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			};
		}.start();

	}
	
	/**
	 * 
	 * 判断数据表中是否有url所对应的图片
	 * 
	 * @param url 图片所对应的网址
	 * @return true 有对应的图片
	 *         false 没有对应的图片
	 */
	public boolean isExist(String url){
		try {
			List<BlogImage> list = dao.queryForEq("imgUrl", url);
			if(list!=null&&list.size()>0){
				return true;
			}else{
				return false;
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("数据库查询错误");
		}
	}

}
