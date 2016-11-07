package com.tarena.fgr.youlu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tarena.fgr.service.BlackNumberService;

/**
 * @author 冯国芮
 * 
 *         时间 2016年9月29日 11:52:31
 * 
 *         闪屏页面 主要用到的知识点有: 1.补间动画设置 2.页面结束时的进出动画
 * 
 *         动画共1.5秒,
 * 
 *         无 ---------有 [渐变alpha]
 * 
 *         50%-------->-----100% [放大scale]
 * 
 *         开始0------1>-----1.5-------->3结束
 * 
 */
// 2016年9月29日 18:20:48
// 广告页面
// 产品Logo展示界面
// 展现产品logo
// 初始化应用程序(初始化数据库,工作空间,开启service等加载数据)
// 检测应用程序的版本更新
// 校验应用线程的合法性(是否联网...)
public class SplashActivity extends Activity implements AnimationListener {
	private RelativeLayout relativeLayout = null;
	// private ImageView imageView_splash = null;
	// 获取版本号
	private PackageManager packageManager = null;
	private TextView textView_version = null;

	// 通过isFirstIn判断要发那种消息,此值要存储起来
	private boolean isFirstIn = false;
	// 延时的时间
	private static final int TIME = 3000;
	// 通过参数决定跳到那个界面
	// 引导界面和主页面
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	@SuppressLint("HandlerLeak")
	// 不应该在主线程沉睡,所以用handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		// 启动服务静态
		startService(new Intent(this, BlackNumberService.class));

		// 2016年9月29日 18:51:36
		// 冯国芮:给imageView加动画不如直接给布局加动画,所以我准备给一个Activity加动画
		// 实现方法:给SplashActivity的根布局加动画
		// 出现了一个大大大BUG,我给全部Activity设置了动画,结果发现我的APP有了一个很大的白色背景,
		// 并没有想我想象的那样,透明加载出一个Activity
		relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_splash_root);
		// imageView_splash = (ImageView) findViewById(R.id.imageView_bg);
		textView_version = (TextView) findViewById(R.id.textView_version);

		packageManager = getPackageManager();
		try {
			// AndroidManifest.xml配置文件中,manifest节点的package属性
			// package="com.tarena.fgr.youlu"
			// 冯国芮:我有种将versionName的值改为:炫酷叼炸天版本试一试的冲动...(这样的操作是被允许的)
			// 例如:内侧版,公开版...
			// android:versionName="1.0"
			// 一个应用的包信息
			PackageInfo packageInfo = packageManager.getPackageInfo(
					getPackageName(), 0);
			String versionName = packageInfo.versionName;
			textView_version.setText("版本号:" + versionName);
		} catch (NameNotFoundException e) {// 包名没有找到异常
			// 不会发生这个异常 can't reach
			e.printStackTrace();
		}

		// 2016年9月29日 18:17:20
		// 冯国芮:这是老师写的,而我准备利用java代码写一个动画
		// 利用xml
		Animation animationXML = AnimationUtils.loadAnimation(this,
				R.anim.anim_splash);

		// imageView_splash.setAnimation(animationXML);
		relativeLayout.setAnimation(animationXML);

		// 2016年9月29日 19:01:28,开始写利用Java完成的动画
		// // 缩放
		// ScaleAnimation animScale = new ScaleAnimation(0.5f, 1, 0.5f, 1, 0.5f,
		// 0.5f);
		// animScale.setDuration(1000);
		// animScale.setFillAfter(true);
		// // 渐变
		// AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
		// animAlpha.setDuration(1500);
		// animAlpha.setFillAfter(true);
		// // 动画集合
		// AnimationSet animSet = new AnimationSet(true);
		// 如果想让此页面多停留,可以给动画集合设置时间,毕竟有的广告有一定时间才给前...
		// animSet.setDuration(2000);
		// animSet.addAnimation(animScale);
		// animSet.addAnimation(animAlpha);
		// relativeLayout.setAnimation(animSet);

		// 当动画结束时跳转窗口
		// 2016年9月29日 20:35:17
		// 实现窗口的跳转
		// 交给Handler实现
		// 动画监听事件
		// animationXML.setAnimationListener(this);

		init();

	}

	private void init() {

		SharedPreferences sharedPreferences = getSharedPreferences("first",
				MODE_PRIVATE);
		isFirstIn = sharedPreferences.getBoolean("isFirstIn", true);
		if (!isFirstIn) {
			handler.sendEmptyMessageDelayed(GO_HOME, TIME);
		} else {
			handler.sendEmptyMessageDelayed(GO_GUIDE, TIME);
			// 进入过引导界面以后,就将此值存储起来
			Editor editor = sharedPreferences.edit();
			editor.putBoolean("isFirstIn", false);
			editor.commit();// 提交修改
		}
	}

	private void goHome() {
		// 进入主页面
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void goGuide() {
		// 进入新手引导页面
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	// 当动画开始的时候执行
	public void onAnimationStart(Animation animation) {
	}

	@Override
	// 当动画重复执行的时候,动画有Repeat属性
	public void onAnimationRepeat(Animation animation) {
	}

	@Override
	// 当动画结束的时候执行
	public void onAnimationEnd(Animation animation) {

		// Intent intent = new Intent(SplashActivity.this,
		// MainActivity.class);
		// startActivity(intent);

		// 入场和出场动画,切换动画
		// overridePendingTransition(enterAnim, exitAnim);
		// 2016年9月29日 13:16:39
		// 冯国芮:这个动画叼炸天,我接收不了,所以取消掉了
		// overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

		// finish();// 结束当前页面
	}
}
