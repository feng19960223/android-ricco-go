package com.fgr.miaoxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.fgr.miaoxin.R;

/**
 * 主Activity底部控件,可以渐变颜色,仿微信
 * 
 * @author 冯国芮
 *
 */
public class MyTabIcon extends View {

	Drawable drawable;// 自定义View要显示的图片

	Bitmap bitmap;// 将drawable要转成bitmap，才能绘制在屏幕上

	String text;// 自定义View要显示的文字

	int color;// 自定义View所使用的颜色

	int textsize;// 自定义View显示文字时使用的字体大小

	Paint textPaint;// 画文字的画笔
	Paint drawPaint;// 画图片的画笔

	// 0~255 0全透明 255完全不透明
	int alpha;// 设置颜色的透明度（通过透明度的改变来改变颜色的深浅）

	public MyTabIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
		initPaint();
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setTextSize(textsize);
		drawPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	}

	/**
	 * 读取布局文件中,使用该View是用户提供的自定义属性的属性值
	 * 
	 * @param context
	 *            上下文
	 * @param attrs
	 */
	private void init(Context context, AttributeSet attrs) {
		// 从布局文件中读取自定义属性的值
		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.MyTabIcon);

		// 读取图片
		drawable = t.getDrawable(R.styleable.MyTabIcon_tabicon_drawable);

		bitmap = ((BitmapDrawable) drawable).getBitmap();

		// 放大bitmap
		bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 2,
				bitmap.getHeight() * 2, true);
		// 读取文字
		text = t.getString(R.styleable.MyTabIcon_tabicon_text);

		// 读取颜色(如果未指定,默认使用绿色)
		color = t.getColor(R.styleable.MyTabIcon_tabicon_color, Color.GREEN);

		// 字体大小(如果未指定,默认使用14sp)
		textsize = t.getDimensionPixelSize(
				R.styleable.MyTabIcon_tabicon_textsize, (int) TypedValue
						.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14,
								getResources().getDisplayMetrics()));
		t.recycle();// 一定要回收
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 处理WRAP_CONTENT
		// 如果控件的宽度和高度都指定为wrap_content时
		// 通过计算指定控件的尺寸
		// 注意：请不要删除super.onMeasure(widthMeasureSpec, heightMeasureSpec)
		// 同时指定尺寸的setMeasuredDimension(widthSize, heightSize)
		// 一定要写在super.onMeasure(widthMeasureSpec, heightMeasureSpec)的下方
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST
				&& MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			// 计算宽度
			// 左内边距+Max(图片宽度，文字宽度)+右内边距
			int leftPadding = getPaddingLeft();
			int rightPadding = getPaddingRight();
			Rect bounds = new Rect();
			textPaint.getTextBounds(text, 0, text.length(), bounds);
			int myWidth = Math.max(bounds.width(), bitmap.getWidth());
			int widthSize = leftPadding + myWidth + rightPadding;
			// 计算高度
			// 上内边距+图片高度+文字高度+下内边距
			int topPadding = getPaddingTop();
			int bottomPadding = getPaddingBottom();
			int myHeight = bitmap.getHeight() + bounds.height();
			int heightSize = topPadding + myHeight + bottomPadding;
			// 为MyTabIcon指定我计算的尺寸
			setMeasuredDimension(widthSize, heightSize);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 绘制图片
		float left = getWidth() / 2 - bitmap.getWidth() / 2;
		float top = getHeight() / 2 - bitmap.getHeight() / 2 - 16;
		canvas.drawBitmap(bitmap, left, top, null);

		// 绘制文字
		Rect bounds = new Rect();
		textPaint.getTextBounds(text, 0, text.length(), bounds);

		float x = getWidth() / 2 - bounds.width() / 2;
		float y = getHeight() / 2 + bitmap.getHeight() / 2 + bounds.height()
				- 16;
		textPaint.setColor(Color.DKGRAY);
		canvas.drawText(text, x, y, textPaint);

		drawColorText(canvas, x, y);

		drawColorBitmap(canvas, left, top);
	}

	private void drawColorBitmap(Canvas canvas, float left, float top) {
		// 1)画一副彩色的图片
		Bitmap colorBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		// 通过myCanvas来绘制空白的colorBitmap
		Canvas myCanvas = new Canvas(colorBitmap);
		// 先把灰色的图片画到colorBitmap上
		myCanvas.drawBitmap(bitmap, 0, 0, null);
		// 画一个有颜色的方块到colorBitmap上
		Rect r = new Rect(0, 0, colorBitmap.getWidth(), colorBitmap.getHeight());
		drawPaint.setColor(color);
		drawPaint.setAlpha(alpha);
		// 利用drawPaint画色块时，除了指定颜色和透明度之外
		// 利用混合模式，让色块与colorBitmap已有的内容产生叠加效果
		drawPaint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		myCanvas.drawRect(r, drawPaint);

		// 2)把彩色图片画到屏幕上(left,top)，盖在原先灰色图片上
		canvas.drawBitmap(colorBitmap, left, top, null);
	}

	/**
	 * 画彩色的文字
	 * 
	 * @param canvas
	 * @param x
	 * @param y
	 */
	private void drawColorText(Canvas canvas, float x, float y) {
		textPaint.setColor(color);
		textPaint.setAlpha(alpha);
		canvas.drawText(text, x, y, textPaint);
	}

	public void setPaintAlpha(int alpha) {
		this.alpha = alpha;
		// 通过调用invali可以重新调用onDraw方法
		invalidate();
	}
}
