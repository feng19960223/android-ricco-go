package com.tarena.fgr.biz;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.util.TypedValue;

/**
 * 实现圆形头像的处理
 * 
 * @author 冯国芮 2016年10月8日 09:21:53
 * 
 */
public class ImageManager {

	/**
	 * @param context
	 * @param bitmap
	 *            联系人头像
	 * @return
	 */
	public static Bitmap circleBitmap(Context context, Bitmap bitmap) {
		int width = bitmap.getWidth();// 头像的宽度
		int height = bitmap.getHeight();// 头像的高度

		// 画布的背景
		Bitmap backBitmap = Bitmap
				.createBitmap(width, height, Config.ARGB_8888);
		// 以此背景来创建画布
		Canvas canvas = new Canvas(backBitmap);

		// 在画布上画圆
		// 创建画笔
		Paint paint = new Paint();
		// 设置绘制的时候没有锯齿
		paint.setAntiAlias(true);
		// 设置画笔的颜色
		paint.setColor(Color.BLACK);

		// 圆的半径
		int radius = Math.min(width, height) / 2;

		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				radius, paint);

		// 画头像
		// 设置前景和背景相交时的处理模式
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, 0, 0, paint);
		// 画圆的白边
		// 重新设置一下白边
		paint.setColor(Color.WHITE);
		paint.setStyle(Paint.Style.STROKE);// 描边
		// 设置边线的宽度
		float strockWidth = TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources()
						.getDisplayMetrics());
		paint.setStrokeWidth(strockWidth);

		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				radius, paint);
		// canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
		// radius
		// - strockWidth, paint);

		return backBitmap;
	}
}
