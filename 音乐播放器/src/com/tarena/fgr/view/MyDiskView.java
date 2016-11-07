package com.tarena.fgr.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tarena.fgr.music.R;

public class MyDiskView extends RelativeLayout {
	private ImageView imageView_disc = null;// 底图
	private ImageView imageView_pin = null;// 唱片指针
	private XCRoundImageView xCRoundImageView1 = null;// 专辑图片
	private Context context;

	public MyDiskView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.diskview, this);
		imageView_disc = (ImageView) view.findViewById(R.id.imageView_disc);
		imageView_pin = (ImageView) view.findViewById(R.id.imageView_pin);
		xCRoundImageView1 = (XCRoundImageView) view
				.findViewById(R.id.xCRoundImageView1);
	}

	// 2016年10月21日 13:12:26
	// 冯国芮:为什么两个动画用的资源一样,却要定义两个?
	// 虽然用的资源是一个,但他俩的图片却不是一个,从而确定了宽高不一样,所以中心点的距离也就不一样.
	// 例如A图高100,宽100,他的旋转点就是50,50.而B图高10宽10
	// 当动画加载时(new) ,传入选择点坐标(50,50).A图正常旋转,而B图也会围绕(50,50)旋转,所以就会出现偏差

	private Animation animation1 = null;// 底片旋转动画
	private Animation animation2 = null;// 唱片片旋转动画

	// 设置唱片的开始旋转的动画
	public void startRotation() {
		animation1 = AnimationUtils.loadAnimation(context,
				R.anim.imageview_rotate);
		animation2 = AnimationUtils.loadAnimation(context,
				R.anim.imageview_rotate);
		imageView_disc.setAnimation(animation1);
		xCRoundImageView1.setAnimation(animation2);
		animation1.start();
		animation2.start();

		// Matrix matrix = new Matrix();
		// imageView_pin.setScaleType(ScaleType.MATRIX);
		// matrix.postRotate(15, 50, 50);
		// imageView_pin.setImageMatrix(matrix);

		RotateAnimation rotateAnimation = new RotateAnimation(0, 15,
				RotateAnimation.RELATIVE_TO_SELF, 0f,
				RotateAnimation.RELATIVE_TO_SELF, 0f);

		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		imageView_pin.setAnimation(rotateAnimation);

	}

	public void stopRotation() {
		animation1.cancel();
		animation2.cancel();
		// Matrix matrix = new Matrix();
		// imageView_pin.setScaleType(ScaleType.MATRIX);
		// matrix.postRotate(0);
		// imageView_pin.setImageMatrix(matrix);
		RotateAnimation rotateAnimation = new RotateAnimation(15, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0f,
				RotateAnimation.RELATIVE_TO_SELF, 0f);
		rotateAnimation.setDuration(1000);
		rotateAnimation.setFillAfter(true);
		imageView_pin.setAnimation(rotateAnimation);
	}

	public void setAlbumpic(int resId) {
		xCRoundImageView1.setImageResource(resId);
	}

	public void setAlbumpic(Bitmap bitmap) {
		xCRoundImageView1.setImageBitmap(bitmap);
	}

}
