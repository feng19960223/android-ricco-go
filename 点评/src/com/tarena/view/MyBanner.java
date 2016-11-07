package com.tarena.view;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tarena.groupon.R;

public class MyBanner extends RelativeLayout {
	private ViewPager viewPager = null;
	private ImageView imageView = null;
	private LinearLayout linearLayout = null;
	private PagerAdapter adapter = null;
	private int[] resIds = null;

	private Handler handler = new Handler(Looper.getMainLooper());// 只往主线程提交的Handler
	private boolean flag;// 开始或停止轮播的标志
	private onCloseBannerListener listener;

	public MyBanner(Context context, int[] ids) {
		super(context);
		if (ids != null && ids.length > 0) {
			// 用户指定了轮播图片
			resIds = new int[ids.length + 2];
			resIds[0] = ids[ids.length - 1];
			resIds[resIds.length - 1] = ids[0];
			for (int i = 0; i < ids.length; i++) {
				resIds[i + 1] = ids[i];
			}
		} else {
			resIds = new int[] { R.drawable.home05, R.drawable.home01,
					R.drawable.home02, R.drawable.home03, R.drawable.home04,
					R.drawable.home05, R.drawable.home01 };
		}
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.ad_layout, this, false);
		addView(view);
		findViewById(view);
		init();
		setListener();
		start();
	}

	public void setOnCloseBannerListener(onCloseBannerListener listener) {
		this.listener = listener;
	}

	private void start() {// 实现自动滑动
		flag = true;// 给旗标置位
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (flag) {
					int idx = viewPager.getCurrentItem();
					viewPager.setCurrentItem(idx + 1);
					postDelayed(this, 5000);
				}
			}
		}, 5000);
	}

	private void stop() {// 停止轮播
		flag = false;
		handler.removeCallbacksAndMessages(null);
	}

	private void setListener() {
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				requestDisallowInterceptTouchEvent(false);
				if (arg0 == 0) {
					viewPager.setCurrentItem(resIds.length - 2, false);// false跳转没有动画
					setImageViewIndicator(linearLayout.getChildCount() - 1);
				} else if (arg0 == resIds.length - 1) {
					viewPager.setCurrentItem(1, false);
					setImageViewIndicator(0);
				} else {
					setImageViewIndicator(arg0 - 1);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// ViewPager滑动开始以及获得过程中,该方法会被调用
				// 请求不要拦截触摸事件
				requestDisallowInterceptTouchEvent(true);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		viewPager.setOnTouchListener(new OnTouchListener() {// 手指放上去时,动画停止
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						int action = event.getAction();
						if (action == MotionEvent.ACTION_DOWN) {
							stop();
							viewPager.clearAnimation();
						}
						if (action == MotionEvent.ACTION_UP) {
							start();
						}
						return false;// 如果返回true,则会没有触摸滑动事件
					}
				});
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null) {
					listener.onClose();
				}
			}
		});
	}

	private void findViewById(View view) {
		viewPager = (ViewPager) view.findViewById(R.id.viewPager_ad_main);
		linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout_ad);
		imageView = (ImageView) view.findViewById(R.id.imageView_ad_delete);
	}

	private void init() {
		adapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return resIds.length;
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				ImageView iv = new ImageView(getContext());
				iv.setImageResource(resIds[position]);
				iv.setScaleType(ScaleType.FIT_XY);
				container.addView(iv);
				return iv;
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView((View) object);
			}
		};
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(1);
		for (int i = 0; i < resIds.length - 2; i++) {// 指示器
			ImageView imageView = new ImageView(getContext());
			imageView.setImageResource(R.drawable.banner_dot);
			// imageView.setPadding(8, 0, 0, 0);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			int margin = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_SP, 8, getResources()
							.getDisplayMetrics());
			params.setMargins(margin, margin, margin, margin);
			imageView.setLayoutParams(params);
			linearLayout.addView(imageView);
		}
		setImageViewIndicator(0);
	}

	/**
	 * 设置指定位置的"指示器"中呈现橙色的图片
	 * 
	 * @param position
	 */
	private void setImageViewIndicator(int position) {
		for (int i = 0; i < linearLayout.getChildCount(); i++) {
			ImageView imageView = (ImageView) linearLayout.getChildAt(i);
			if (i == position) {
				imageView.setImageResource(R.drawable.banner_dot_pressed);
			} else {
				imageView.setImageResource(R.drawable.banner_dot);
			}
		}
	}

	public interface onCloseBannerListener {
		public void onClose();
	}
}
