package com.tarena.fgr.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {

	/**
	 * 从SD开上读取图片
	 * 
	 * @param bytes
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap loadBitmap(byte[] bytes, int width, int height) {
		Options options = new Options();
		// 解析时,仅仅加载边界属性
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
		// 加载完毕后,获取图片的宽与高
		int w = options.outWidth / width;
		int h = options.outHeight / height;
		// 设置加载时,所使用的伸缩比例
		int scale = w > h ? w : h;
		options.inSampleSize = scale;
		// 不加载边界属性
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
	}

	/**
	 * 保存图片到SD卡
	 * 
	 * @param file
	 * @param bitmap
	 * @throws IOException
	 */
	public static void saveBitmap(File file, Bitmap bitmap) throws IOException {
		// 父目录是否存在
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		// 文件是否存在
		if (!file.exists()) {
			file.createNewFile();
		}
		// 把bitmap压缩成JPEG格式输出
		bitmap.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
	}
}
