package com.fgr.miaoxin.ui;

import android.content.DialogInterface;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.MyPagerAdapter;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.receiver.MyPushMessageReceiver;
import com.fgr.miaoxin.util.DialogUtil;
import com.fgr.miaoxin.util.SPUtil;
import com.fgr.miaoxin.view.MyTabIcon;

public class MainActivity extends BaseActivity implements EventListener {

	@Bind(R.id.vp_main_viewpager)
	ViewPager viewPager;
	MyPagerAdapter adapter;

	@Bind(R.id.mti_main_message)
	MyTabIcon mtiMessage;

	@Bind(R.id.mti_main_friend)
	MyTabIcon mtiFriend;
	@Bind(R.id.iv_main_newinvitation)
	ImageView ivNewInvitation;

	@Bind(R.id.mti_main_find)
	MyTabIcon mtiFind;
	@Bind(R.id.mti_main_setting)
	MyTabIcon mtiSetting;

	MyTabIcon[] tabIcons;
	SPUtil sputil;

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_main);
	}

	@Override
	public void init() {
		super.init();
		sputil = new SPUtil(this, bmobUserManager.getCurrentUserObjectId());
		initViewPager();
		initView();
	}

	private void initView() {
		tabIcons = new MyTabIcon[4];
		tabIcons[0] = mtiMessage;
		tabIcons[1] = mtiFriend;
		tabIcons[2] = mtiFind;
		tabIcons[3] = mtiSetting;
		for (MyTabIcon mti : tabIcons) {
			mti.setPaintAlpha(0);
		}
		tabIcons[0].setPaintAlpha(255);
	}

	private void initViewPager() {
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				for (MyTabIcon mti : tabIcons) {
					mti.setPaintAlpha(0);
				}
				tabIcons[arg0].setPaintAlpha(255);
			}

			/**
			 * arg0 页码 arg1 滑动百分比 arg2 滑动的像素数
			 * 
			 */
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				if (arg0 < 3) {
					// 颜色由彩色----->灰色变化
					// alpha是255----->0
					tabIcons[arg0].setPaintAlpha((int) (255 * (1 - arg1)));
					// 颜色由灰色----->彩色变化
					// alpha是0----->255
					tabIcons[arg0 + 1].setPaintAlpha((int) (255 * arg1));
				}
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		// MainActivity成为MyReceiver订阅者队列中的一员
		MyPushMessageReceiver.regist(this);

		// 更加当前登录用户所对应数据库中是否有未处理的
		// "添加好友申请"来决定ivNewInvitation是否可见
		if (bmobDB.hasNewInvite()) {
			ivNewInvitation.setVisibility(View.VISIBLE);
		} else {
			ivNewInvitation.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyPushMessageReceiver.unRegist(this);
	}

	@OnClick({ R.id.mti_main_message, R.id.mti_main_friend, R.id.mti_main_find,
			R.id.mti_main_setting })
	public void setCurrentFragment(View v) {
		switch (v.getId()) {
		case R.id.mti_main_message:
			viewPager.setCurrentItem(0, false);
			break;
		case R.id.mti_main_friend:
			viewPager.setCurrentItem(1, false);
			break;
		case R.id.mti_main_find:
			viewPager.setCurrentItem(2, false);
			break;
		default:
			viewPager.setCurrentItem(3, false);
			break;
		}
	}

	@Override
	public void onMessage(BmobMsg message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onReaded(String conversionId, String msgTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNetChange(boolean isNetConnected) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAddUser(BmobInvitation message) {
		// 当收到了“添加好友申请”时，该方法会被MyReceiver调用
		ivNewInvitation.setVisibility(View.VISIBLE);
		if (sputil.isAllowSound())
			MyApp.mediaPlayer.start();
	}

	@Override
	public void onOffline() {
		// 当收到下线通知时，该方法会被MyReceiver调用
		DialogUtil.showDialog(this, "下线通知", "检测到您的账号在另一台设备登录，请您重新登录！", false,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						MyApp.logout();
					}
				});
	}

}
