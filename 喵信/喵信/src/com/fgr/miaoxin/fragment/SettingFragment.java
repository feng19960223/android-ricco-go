package com.fgr.miaoxin.fragment;

import java.lang.reflect.Method;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.util.SPUtil;

public class SettingFragment extends BaseFragment {
	private static final String NOTIFICATION = "notification";
	private static final String SOUND = "sound";
	private static final String VIBRATE = "vibrate";

	private static final int SWITCH_ON = 0;
	private static final int SWITCH_OFF = 1;

	@Bind(R.id.tv_setting_username)
	TextView tvUsername;

	@Bind(R.id.tv_setting_notification)
	TextView tvNotification;
	@Bind(R.id.tv_setting_sound)
	TextView tvSound;
	@Bind(R.id.tv_setting_vibrate)
	TextView tvVibrate;

	@Bind(R.id.iv_setting_editornotification)
	ImageView ivNotification;
	@Bind(R.id.iv_setting_editorsound)
	ImageView ivSound;
	@Bind(R.id.iv_setting_editorvibrate)
	ImageView ivVibrate;

	SPUtil sputil;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_setting, container,
				false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		sputil = new SPUtil(getActivity(),
				bmobUserManager.getCurrentUserObjectId());
		initHeaderView();
		initView();
	}

	private void initHeaderView() {
		setHeaderTitle("设置", Position.CENTER);
	}

	private void initView() {
		// 当前登录用户的用户名
		tvUsername.setText(bmobUserManager.getCurrentUserName());
		// 根据当前登录用户的偏好设置文件设置TextView和ImageView的显示
		if (sputil.isAllowNotification()) {
			switcher(NOTIFICATION, SWITCH_ON);
			ivSound.setClickable(true);
			ivVibrate.setClickable(true);
		} else {
			switcher(NOTIFICATION, SWITCH_OFF);
			ivSound.setClickable(false);
			ivVibrate.setClickable(false);
		}

		if (sputil.isAllowSound()) {
			switcher(SOUND, SWITCH_ON);
		} else {
			switcher(SOUND, SWITCH_OFF);
		}
		if (sputil.isAllowVibrate()) {
			switcher(VIBRATE, SWITCH_ON);
		} else {
			switcher(VIBRATE, SWITCH_OFF);
		}

	}

	@OnClick(R.id.iv_setting_editornotification)
	public void setNotification(View view) {
		if (!sputil.isAllowNotification()) {
			switcher(NOTIFICATION, SWITCH_ON);
			ivSound.setClickable(true);
			ivVibrate.setClickable(true);
		} else {
			switcher(NOTIFICATION, SWITCH_OFF);
			ivSound.setClickable(false);
			ivVibrate.setClickable(false);

			switcher(SOUND, SWITCH_OFF);

			switcher(VIBRATE, SWITCH_OFF);

		}
	}

	@OnClick(R.id.iv_setting_editorsound)
	public void setSound(View view) {
		if (!sputil.isAllowSound()) {
			switcher(SOUND, SWITCH_ON);
		} else {
			switcher(SOUND, SWITCH_OFF);
		}
	}

	@OnClick(R.id.iv_setting_editorvibrate)
	public void setVibrate(View view) {
		if (!sputil.isAllowVibrate()) {
			switcher(VIBRATE, SWITCH_ON);
		} else {
			switcher(VIBRATE, SWITCH_OFF);
		}
	}

	@OnClick(R.id.btn_setting_logout)
	public void logout(View v) {
		MyApp.logout();
	}

	/**
	 * 反射</br> 设定ImageView中的图片 TextView中的文本 偏好设置
	 * 
	 * @param name
	 *            NOTIFICATION SOUND VIBRATE
	 * @param state
	 *            SWITCH_ON SWITCH_OFF
	 */
	private void switcher(String name, int state) {
		try {
			// "notification"
			// ImageView对应的id
			String ivResName = "iv_setting_editor" + name;
			int ivResId = getResources().getIdentifier(ivResName, "id",
					getActivity().getPackageName());

			// TextView对应的id
			String tvResName = "tv_setting_" + name;
			int tvResId = getResources().getIdentifier(tvResName, "id",
					getActivity().getPackageName());

			if (ivResId == 0 || tvResId == 0) {
				throw new RuntimeException("未能找到正确的视图");
			}

			ImageView iv = (ImageView) getView().findViewById(ivResId);

			if (state == SWITCH_ON) {
				iv.setImageResource(R.drawable.ic_switch_on);
			} else {
				iv.setImageResource(R.drawable.ic_switch_off);
			}

			TextView tv = (TextView) getView().findViewById(tvResId);

			tv.setText((state == SWITCH_ON ? "允许" : "禁止")
					+ (NOTIFICATION.equals(name) ? "通知"
							: (SOUND.equals(name) ? "声音" : "振动")));

			// 调用sputil中的方法来设定偏好设置文件
			// "notification"--->"setNotification"
			char[] chars = name.toCharArray();
			chars[0] -= 32;
			String methodName = "set" + new String(chars);

			Method method = sputil.getClass().getDeclaredMethod(methodName,
					boolean.class); // boolean.class或Boolean.TYPE把上面改为boolean,Boolean.class把上面的该为Boolean

			method.invoke(sputil, state == SWITCH_ON ? true : false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
