package com.tarena.fgr.fragment;

import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.tarena.fgr.adapter.CalllogBaseAdapter;
import com.tarena.fgr.biz.CalllogManager;
import com.tarena.fgr.biz.MediaManager;
import com.tarena.fgr.entity.Calllog;
import com.tarena.fgr.youlu.R;

/**
 * 拨号
 * 
 * @author 冯国芮
 * 
 */
public class DialpadFragment extends BaseFragment implements
		OnItemClickListener, OnScrollListener {
	private ListView listView = null;
	private RelativeLayout relativeLayout = null;
	private CalllogBaseAdapter adapter = null;
	private ImageView imageView_show = null;// 显示拨号键盘按钮
	private TextView textView_phone = null;// 电话号码,actionbar的拨打电话

	// 2016年10月10日 14:15:20
	// 把音效放到了MediaManager类中

	// private SoundPool mySoundPool = null;// 点击拨号时,音效播放
	// private int loadid;// 音效play需要的一个返回值

	// 一个失败的动画效果,二个动画没有同时执行
	// boolean isAnim = false;// 设置动画开始的标识

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// 设置为false,container并不是跟节点,而是依赖的参数
		contentView = inflater.inflate(R.layout.fragment_dialpad, container,
				false);
		// return super.onCreateView(inflater, container, savedInstanceState);
		initialUI();// 初始化所以的UI控件
		// initSP(); // 初始化音效
		return contentView;
	}

	private void initialUI() {
		actionbar = (LinearLayout) contentView
				.findViewById(R.id.include_actionbar);
		listView = (ListView) contentView.findViewById(R.id.listView_calllog);
		// 拨号键盘
		relativeLayout = (RelativeLayout) contentView
				.findViewById(R.id.relativeLayout_dailpad);
		// 显示拨号键盘的按钮
		imageView_show = (ImageView) contentView
				.findViewById(R.id.imageView_show);
		// 点击拨号的数字按钮,数字会显示到这里
		textView_phone = (TextView) contentView
				.findViewById(R.id.textView_actionbar_title);
		ImageView imageView_backspace = (ImageView) contentView
				.findViewById(R.id.imageView_actionbar_right);
		// 拨号按钮
		ImageView imageView_call = (ImageView) contentView
				.findViewById(R.id.imageView_call);

		// 删除小图片
		initialActionbar(-1, "拨号", R.drawable.ic_backspace);

		// 显示拨号键盘的监听事件,隐藏拨号键盘,显示按钮
		imageView_show.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				relativeLayout.setVisibility(View.VISIBLE);
				imageView_show.setVisibility(View.GONE);
			}
		});

		// actionbar的删除图标的单机事件,删除一个
		imageView_backspace.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = textView_phone.getText().toString();// 标题的文本
				int phoneLength = phone.length();// 标题文本的长度
				if (phone.equals("拨号")) {// 拨号无法删除
					// 注意:字符串用equlas比较
					return;
				} else if (phoneLength == 1) {
					textView_phone.setText("拨号");// 最后一个数组删除后,显示拨号
				} else if (phoneLength == 5 || phoneLength == 10) {
					String newphone = phone.substring(0, phoneLength - 2);// 删除数字的时候,同时删除"-"
					textView_phone.setText(newphone);
				} else {
					String newphone = phone.substring(0, phoneLength - 1);
					textView_phone.setText(newphone);
				}
			}
		});
		// actionbar的删除图标的长按事件,删除所以
		imageView_backspace.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				textView_phone.setText("拨号");
				return false;
			}
		});

		// 拨号按钮的静态事件
		imageView_call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = textView_phone.getText().toString();
				if (phone.equals("拨号")) {
					return;
				} else {
					// 播放一个音效
					// Load返回的resid,左声道音量,右声道音量,优先级,是否循环-1无限循环0不循环其他为循环次数,播放速率
					// play(soundID, leftVolume, rightVolume, priority, loop,
					// rate)
					// mySoundPool.play(loadid, 1.0f, 1.0f, 1, 0, 1.0f);
					MediaManager.playMusic(getActivity(), R.raw.a);
					// 拨号
					call(phone);
				}
			}
		});

		adapter = new CalllogBaseAdapter(getActivity());
		listView.setAdapter(adapter);
		// 2016年10月9日 18:43:49
		// 冯国芮:单纯的监听事件,不会实现我想要的效果,滑动是消失,滑动结束时显示
		// 而且用户体验也不会太好,我们滑动list可能是找最近的联系人,而如果我们在滑动
		// 结束的时候,弹出键盘,那么用户的滑动没有任何意义
		// 所以我添加了一个可以弹出按钮
		listView.setOnScrollListener(this);
		listView.setOnItemClickListener(this);
		refreshDialpad();// 通话记录的数据

		initialDailpad();// 拨号键盘

		textView_phone.addTextChangedListener(new MyTextWatcher());

	}

	// private void initSP() {// 加载音效

	// SoundPool用来播放一些小的声音文件
	// res/raw目录
	// 支持多少个声音,资源类型,资源的质量(预留)[0]
	// new SoundPool(maxStreams, streamType, srcQuality)

	// 上下文,资源id,priority 优先级(预留)[1]
	// load(context, resId, priority)

	// mySoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
	// loadid = mySoundPool.load(getActivity(), R.raw.b, 1);
	// }

	// 为标题加一个监听
	// 监听当标题的内容长度发生变化的时候
	// 判断其长度，如果大于13则删除13之后的内容
	public class MyTextWatcher implements TextWatcher {
		@Override
		// 文本变化之前
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		// 文本变化
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		// 文本变化之后
		public void afterTextChanged(Editable s) {
			// 当电话号码的长度超过13时不允许再增加内容
			if (s.length() > 13) {
				s.delete(13, s.length());
			}
		}

	}

	private void refreshDialpad() {
		// 通话记录的数据
		List<Calllog> list = CalllogManager.getCalllogs(getActivity());
		adapter.addItems(list, true);
	}

	private static String[] keys = { "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "*", "0", "#" };

	// 拨号键盘的初始化
	private void initialDailpad() {
		DisplayMetrics metrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(metrics);
		// 计算按键的宽度
		int width = metrics.widthPixels / 3;
		// 计算按键的高度
		int height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 59, getActivity().getResources()
						.getDisplayMetrics());
		// 高度59dp

		for (int i = 0; i < keys.length; i++) {
			TextView key = new TextView(getActivity());
			key.setId((i + 1));
			key.setText(keys[i]);
			key.setTextSize(40);
			key.setGravity(Gravity.CENTER);
			// 默认使用sp
			// key.setTextSize(TypedValue.COMPLEX_UNIT_SP,40);
			// 创建一个相对布局的布局参数
			LayoutParams params = new LayoutParams(width, height);
			if (i % 3 != 0) {// 设置右对齐
				params.addRule(RelativeLayout.RIGHT_OF, i);
			}
			if (i >= 3) {// 设置下对齐
				params.addRule(RelativeLayout.BELOW, i - 2);
			}
			relativeLayout.addView(key, params);
			// 单纯的点击事件,无法实现颜色的编号,所以使用触摸事件
			// key.setOnClickListener(this);

			// 为按键注册监听器
			// 使用触摸事件,非常简单的实现了颜色变化
			// 2016年10月10日 10:36:38
			// 颜色变化有一个小bug,当按住一直脱的时候,有可能一直变蓝色,不会发生变化,这有可能是模拟器的原因
			// 可以加一个手指滑动监听,当出去的时候,保证会变为黑色
			key.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						// 按钮音效
						MediaManager.playMusic(getActivity(), R.raw.b);

						// if (isAnim) {
						// return true;
						// }
						// 如果向下触摸
						// 获得标题的内容
						((TextView) v).setTextColor(Color.BLUE);
						String title = textView_phone.getText().toString();
						String k = ((TextView) v).getText().toString();
						if (title.equals("拨号")) {
							textView_phone.setText(k);
						} else if (title.length() == 3 || title.length() == 8) {
							textView_phone.append("-");
							textView_phone.append(k);
						} else {
							textView_phone.append(k);
						}
						break;
					case MotionEvent.ACTION_MOVE:
						((TextView) v).setTextColor(Color.BLUE);
						break;
					case MotionEvent.ACTION_UP:
						((TextView) v).setTextColor(Color.BLACK);
						break;
					default:
						break;
					}
					return true;
				}
			});
		}
	}

	/**
	 * 拨号
	 * 
	 * @param phone
	 *            电话号码
	 */
	private void call(String phone) {
		Intent intent = new Intent();
		// 直接拨打
		intent.setAction(Intent.ACTION_CALL);
		// 跳转到拨号界面
		// intent.setAction(Intent.ACTION_DIAL);
		intent.setData(Uri.parse("tel:" + phone));
		// 跳转到拨号界面
		// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		// 电话结束后会强制到系统的通话记录...
		// intent.putExtra("finishActivityOnSaveCompleted", true);
		// 当我们调用编辑完成后直接退系统的activity
		// 这段神奇的代码没有作用了
		startActivity(intent);
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshDialpad();// 界面重新被唤醒时,刷新数据
		// loadDailpadAnim();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// mySoundPool.release();// 销毁音效
		MediaManager.release(); // 释放音乐资源
	}

	// 2016年10月10日 12:32:18
	// 冯国芮:有了加载数字的动画,这个动画就不加了
	// public void loadDailpadAnim() {
	// Animation animation = AnimationUtils.loadAnimation(getActivity(),
	// R.anim.dailpad_out);
	// animation.setAnimationListener(new AnimationListener() {
	// @Override
	// public void onAnimationStart(Animation animation) {
	// isAnim = true;
	// }
	//
	// @Override
	// public void onAnimationRepeat(Animation animation) {
	//
	// }
	//
	// @Override
	// public void onAnimationEnd(Animation animation) {
	// isAnim = false;
	// }
	// });
	// relativeLayout.setAnimation(animation);
	// }

	@Override
	// 单机list隐藏拨号键盘,显示按钮,同时拨号
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// 点击的时候,按钮显示,拨号键盘隐藏
		relativeLayout.setVisibility(View.GONE);
		imageView_show.setVisibility(View.VISIBLE);

		String phone = adapter.getItem(position).getPhone();
		call(phone);
	}

	@Override
	// 滑动时执行
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// relativeLayout.setVisibility(View.VISIBLE);
		// 滑动的时候,按钮显示,拨号键盘隐藏
		relativeLayout.setVisibility(View.GONE);
		imageView_show.setVisibility(View.VISIBLE);
	}

	@Override
	// 滑动结束时执行
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	// 1.当标题文本的内容为"拨号",直接获得按键文本并设置给标题
	// 2.当标题文本的长度为3或8的时候,在标题中先追加分割线在追加按键的文本
	// 长度等于13,无法点击无效
	// 3.否则的话按一次按键,获得按键文本,追加到文本的后面

	// 2016年10月10日 10:28:52
	// 冯国芮:单纯的点击事件,无法实现颜色的编号,所以使用触摸事件
	// @Override
	// public void onClick(View v) {
	// int phoneLength = textView_phone.getText().toString().length();// 标题文本的长度
	// String str = ((TextView) v).getText().toString();// 拨号按钮的文本
	// if (textView_phone.getText().toString().equals("拨号")) {
	// textView_phone.setText(str);
	// } else if (phoneLength == 3 || phoneLength == 8) {
	// textView_phone.append("-");
	// textView_phone.append(str);
	// } else if (phoneLength >= 13) {
	// return;
	// } else {
	// textView_phone.append(str);
	// }
	// }
}
