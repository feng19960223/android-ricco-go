package com.tarena.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 自定义的控件指示引导界面,小球的运动轨迹不再生硬
 * 
 * @author 冯国芮
 * 
 */
public class CircleIndicator extends View {

	public CircleIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CircleIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleIndicator(Context context) {
		super(context);
	}

	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int radius = 10;
	private float width;
	private float height;
	private float varWidth;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w / 2 - 3 * radius;
		varWidth = w / 2 - 3 * radius;
		height = h / 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(Color.BLACK);
		for (int i = 0; i < 3; i++) {
			canvas.drawCircle(width + 3 * i * radius, height, radius, paint);
		}
		paint.setStyle(Style.FILL);
		paint.setColor(Color.RED);
		canvas.drawCircle(varWidth, height, radius - 3, paint);
	}

	public void updateDraw(int position, float offset) {
		varWidth = width + (position + offset) * 3 * radius;
		invalidate();
	}
}
