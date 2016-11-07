package com.tarena.fgr.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.ImageView;

import com.tarena.fgr.utils.StreamUtils;

/**
 * 自定义的网络图片加载器
 * 
 * @author 冯国芮
 * 
 */
public class ImageLoader {
	/*
	 * 当我们使用一幅图片的时候 1.先从内存缓存中查找，内存缓存中如果有使用的图片 则直接使用 2.内存缓存中如果没有，再从文件缓存中查找 ,如果有
	 * 直接使用 3.如果文件缓存中也没有，再从网络上加载图片 加载完毕将图片分别缓存在内存和文件中。
	 */
	// 用来缓存图片的内存
	static LruCache<String, Bitmap> lruCache = null;
	static {
		int maxsize = 1024 * 1024 * 4;// 4mb设置内存的最大的缓存空间
		lruCache = new LruCache<String, Bitmap>(maxsize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * 为每一个音乐对象设置专辑图片
	 */
	public static void setBitmapFromCache(Context context, ImageView imageview,
			String url) {
		if (TextUtils.isEmpty(url)) {
			return;
		}
		Bitmap bitmap = null;
		// 先从内存缓存中查找图片
		bitmap = getBitmapFromMemoryCache(url);
		if (bitmap != null) {
			imageview.setImageBitmap(bitmap);
			return;
		}
		// 再从文件缓存中查找图片
		bitmap = getBitmapFromFileCache(context, url);
		if (bitmap != null) {
			imageview.setImageBitmap(bitmap);
			return;
		}
		// 再从网络上加载
		loadBitmapAsync(context, imageview, url);
	}

	/**
	 * 
	 * @param context
	 *            上下文对象
	 * @param imageview
	 *            使用图片的控件
	 * @param url
	 *            图片的路径
	 */
	private static void loadBitmapAsync(Context context, ImageView imageview,
			String url) {
		// 使用异步的方式从网上加载
		MyAsyncTask task = new MyAsyncTask(context, imageview);
		task.execute(url);
	}

	public static class MyAsyncTask extends AsyncTask<String, Void, Bitmap> {
		Context context = null;
		ImageView imageview = null;

		public MyAsyncTask(Context context, ImageView imageview) {
			this.context = context;
			this.imageview = imageview;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			String path = params[0];
			Bitmap bitmap = null;
			try {
				URL url = new URL(path);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setRequestMethod("GET");
				connection.setConnectTimeout(5000);
				connection.setDoInput(true);

				int statuscode = connection.getResponseCode();
				if (statuscode == 200) {
					InputStream is = connection.getInputStream();
					// bitmap = BitmapFactory.decodeStream(is);
					// 压缩图片
					bitmap = compressBitmap(is);
					if (bitmap != null) {
						// 把图片同时缓存内存缓存中
						lruCache.put(path, bitmap);
						// 把图片缓存到文件中
						saveBitmapCacheFile(context, bitmap, path);
						return bitmap;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			// 将图片设置到imagevie控件上
			imageview.setImageBitmap(result);
		}
	}

	private static Bitmap compressBitmap(InputStream is) {
		Bitmap bitmap = null;
		// 将自字节输入流转换成一个字节类型的数组
		byte[] data = StreamUtils.streamToByteArray(is);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;// 仅对图片作边界的解码
		// 这句话一定要有
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		int outHeight = opts.outHeight;// 获得边界的高
		int outWidth = opts.outWidth;// 获得边界的宽
		// 计算出缩放的比例
		// 目标高度
		int targetHeight = 64;
		// 目标宽度
		int targetWidth = 64;
		// 高度方向上的压缩比例
		int blh = outHeight / targetHeight;
		// 宽度方向上的压缩比例
		int blw = outWidth / targetWidth;
		int inSampleSize = blh > blw ? blh : blw;
		if (inSampleSize <= 0) {
			inSampleSize = 1;
		}
		opts.inSampleSize = inSampleSize;
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		return bitmap;
	}

	private static Bitmap getBitmapFromFileCache(Context context, String url) {
		// http://172.60.50.208:8080/MusicServer/image/byebyedisc.jpg
		Bitmap bitmap = null;
		String fileName = url.substring(url.lastIndexOf("/") + 1);
		File cachedir = context.getCacheDir();
		if (cachedir != null) {
			// 获得该缓存目录下的所有的文件构成的数组
			File[] files = cachedir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().equals(fileName)) {
					// 把该缓存文件转换成一个bitmap对象
					bitmap = BitmapFactory.decodeFile(files[i]
							.getAbsolutePath());
					return bitmap;
				}
			}
		}
		return null;
	}

	/**
	 * 将网络加载成功后的图片缓存到文件中
	 * 
	 * @param context
	 * @param bitmap
	 * @param path
	 */
	private static void saveBitmapCacheFile(Context context, Bitmap bitmap,
			String path) {
		try {
			// 获得缓存目录
			File cacheFile = context.getCacheDir();
			if (!cacheFile.exists()) {
				cacheFile.mkdirs();
			}
			// 对被缓存的文件的文件名进行处理
			String fileName = path.substring(path.lastIndexOf("/") + 1);
			// 构建被缓存的文件对象
			File file = new File(cacheFile, fileName);
			// 图片压缩,优化
			// 创建一个文件输出流
			OutputStream os = new FileOutputStream(file);
			// 将图片文件存到缓存目录
			bitmap.compress(CompressFormat.JPEG, 100, os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 从内存缓存中查找图片
	private static Bitmap getBitmapFromMemoryCache(String url) {
		Bitmap bitmap = null;
		// 从内存中获得图片
		bitmap = lruCache.get(url);
		if (bitmap != null) {
			return bitmap;
		}
		return null;
	}
}
