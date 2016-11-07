package com.tarena.view;

import com.tarena.groupon.R;

import android.annotation.SuppressLint;
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

public class MyLetterView extends View {
	private Paint paint = null;

	private String[] letters = { "热门", "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z" };// 这里有空格,可能无法滑动
	private onTouchLetterListener listener;
	private int letter_color;

	public void setOnTouchLetterListener(onTouchLetterListener listener) {
		this.listener = listener;
	}

	public MyLetterView(Context context) {// Java代码构建,调用
		super(context);
	}

	@SuppressLint("Recycle")
	public MyLetterView(Context context, AttributeSet attrs) {// 布局文件使用,调用
		super(context, attrs);
		// 1.读取该自定义view的自定义属性
		TypedArray t = context.obtainStyledAttributes(attrs,
				R.styleable.MyLetterView);// 取有关自己的属性
		t.recycle();// ***一定要回收到
		letter_color = t.getColor(R.styleable.MyLetterView_letter_color,
				Color.BLACK);// 没有取到使用黑色
		// 2.初始化重要的属性:画笔,画布
		initPaint();
	}

	public MyLetterView(Context context, AttributeSet attrs, int defStyle) {// 有style属性时,调用
		super(context, attrs, defStyle);
	}

	private void initPaint() {
		// 抗锯齿有颜色渐变效果,棱角不明显
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);// 抗锯齿效果,
		paint.setColor(letter_color);
		paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
				12, getResources().getDisplayMetrics()));// 直接写数字,是dp,借助工具类,成为sp
		// TODO 颜色
	}

	@SuppressLint("DrawAllocation")
	@Override
	// 测量自定义view多长,多宽,父类View已经实现,如果一致没必要实现
	// 当设置为wrap_content时怎么处理....
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {// 可以写wrap_content属性了
		// TODO 如果MyLetterView的尺寸测量规则与View的默认测量规则不一致时,才有必要重写该方法
		// View的默认测量规则:如果指定了具体的尺寸值,就按早具体值指定宽高
		// 如果使用wrap_content或match_parent,则都安卓match_parent指定
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 对宽进行修改
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		if (specMode == MeasureSpec.AT_MOST) {
			// 如果specMode的值为MeasureSpec.EXACTLY,意味宽度是具体的值,或match-parent
			// 如果specMode的值为MeasureSpec.AT_MOST,意味宽度wrap_content
			// 执行指定宽度
			// 最宽的文字宽度+左右边距
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
			int width = leftPadding + textwidth + rightPadding;
			setMeasuredDimension(width, MeasureSpec.getSize(heightMeasureSpec));
		}

	}

	@Override
	// 如果自定义view是一个容器,用来摆放子view的位置,写一个容器类的view,有必要重写
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	// 避免对象分配在画/布局操作(preallocate和重用)
	@SuppressLint("DrawAllocation")
	@Override
	// 自定义view如何呈现,让用户看的部分,通过画笔Paint画画布Canvas
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float width = getWidth();
		float height = getHeight() / letters.length;
		for (int i = 0; i < letters.length; i++) {
			// 文字坐标在左下,其他全在左上
			Rect bounds = new Rect();
			paint.getTextBounds(letters[i], 0, letters[i].length(), bounds);// 文字边界,返回值被存放到bounds中,每一个字的宽高
			float x = width / 2.0f - bounds.width() / 2.0f;
			float y = height / 2.0f + bounds.height() / 2.0f + i * (height);
			canvas.drawText(letters[i], x, y, paint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {// true需要手指按下事件
		int action = event.getAction();
		switch (action) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:
			setBackgroundColor(Color.GRAY);
			float y = event.getY();// 当前手指的位置,距离(0,0)点的距离
			int idx = (int) y * letters.length / getHeight();
			if (idx >= 0 && idx < letters.length) {
				if (listener != null) {
					listener.onTouchLetter(letters[idx]);
				}
			}
			break;
		default:
			setBackgroundColor(Color.TRANSPARENT);// 透明色
			break;
		}
		return true;
	}

	public interface onTouchLetterListener {
		public void onTouchLetter(String letter);
	}
}
