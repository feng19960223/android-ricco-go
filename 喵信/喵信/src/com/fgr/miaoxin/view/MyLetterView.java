package com.fgr.miaoxin.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.fgr.miaoxin.R;

/**
 * ListView引导 #a-z
 * 
 * @author 冯国芮
 */

public class MyLetterView extends View {

	Paint paint;// 画笔

	OnTouchLetterListener listener;

	int fontColor;

	// TextView tvLetter;

	// public void setTvLetter(TextView tvLetter) {
	// this.tvLetter = tvLetter;
	// }

	private String[] letters = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
			"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
			"W", "X", "Y", "Z", "#" };

	public MyLetterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.MyLetterView);
		fontColor = t
				.getColor(R.styleable.MyLetterView_font_color, Color.BLACK);
		t.recycle();

		initPaint();

	}

	/**
	 * 给listener属性赋值
	 * 
	 * @param listener
	 */
	public void setOnTouchLetterListener(OnTouchLetterListener listener) {
		this.listener = listener;
	}

	/**
	 * 初始化画笔
	 */
	private void initPaint() {
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				12, getResources().getDisplayMetrics()));
		paint.setColor(fontColor);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 如果你的测量原则与安卓默认的测量原则不同的时候
		// 才需要重写
		// 默认原则： 1)如果指定了明确的尺寸（比如60dp）
		// 那么就将你声明的尺寸作为View的尺寸
		// 2)如果指定了match_parent或wrap_content
		// 一律都按照match_parent进行处理
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 默认原则与你的原则有冲突
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			// 如果宽度使用的是wrap_content
			// 手动计算并设定view的宽度值
			// 1)手动计算
			// 宽度=左内边距+最宽的文字宽度+右内边距
			int leftPadding = getPaddingLeft();
			int rightPadding = getPaddingRight();
			int textwidth = 0;
			for (int i = 0; i < letters.length; i++) {
				Rect bounds = new Rect();
				paint.getTextBounds(letters[i], 0, letters[i].length(), bounds);
				if (bounds.width() > textwidth) {
					textwidth = bounds.width();
				}
			}

			int size = leftPadding + textwidth + rightPadding;
			// 2)手动设定
			setMeasuredDimension(size, MeasureSpec.getSize(heightMeasureSpec));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// 自定义View的宽带（黑方框的宽带）
		int width = getWidth();
		// 自定义View的高度除以要显示内容的数量（黑方框的高度）
		int height = getHeight() / letters.length;

		for (int i = 0; i < letters.length; i++) {
			Rect bounds = new Rect();
			paint.getTextBounds(letters[i], 0, letters[i].length(), bounds);
			int w = bounds.width();// "红方框"的宽度
			int h = bounds.height();// "红方框"的高度

			int x = width / 2 - w / 2;
			int y = height / 2 + h / 2 + i * height;

			canvas.drawText(letters[i], x, y, paint);

		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// action主要是三个动作，
		// 按下（DOWN），移动（MOVE），抬起（UP）
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			// 1)背景色变成灰色
			setBackgroundColor(Color.GRAY);
			// 2)ListView滚动到相应的分组

			// 当前手指的高度
			float y = event.getY();
			// 根据当前手指位置，换算出一个近似的下标值
			int idx = (int) ((y * letters.length) / getHeight());

			if (idx >= 0 && idx < letters.length) {
				String letter = letters[idx];
				if (listener != null) {
					listener.onTouchLetter(letter);
					// if(tvLetter!=null){
					// tvLetter.setVisibility(View.VISIBLE);
					// tvLetter.setText(letter);
					// }

				}
			}

			break;

		default:
			// 背景色变回透明色
			setBackgroundColor(Color.TRANSPARENT);
			// if(tvLetter!=null){
			// tvLetter.setVisibility(View.INVISIBLE);
			// //tvLetter.setText("");
			// }
			if (listener != null) {
				listener.onReleaseLetter();
			}

			break;
		}

		return true;
	}

	public interface OnTouchLetterListener {
		void onTouchLetter(String letter);

		void onReleaseLetter();
	}

}
