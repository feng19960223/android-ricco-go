package com.fgr.miaoxin.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTouch;
import cn.bmob.im.BmobRecordManager;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;
import cn.bmob.im.inteface.OnRecordChangeListener;
import cn.bmob.im.inteface.UploadListener;
import cn.bmob.v3.listener.PushListener;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.ChatAdapter;
import com.fgr.miaoxin.adapter.EmoGridViewAdapter;
import com.fgr.miaoxin.adapter.EmoPagerAdapter;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.receiver.MyPushMessageReceiver;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.NetUtil;
import com.viewpagerindicator.CirclePageIndicator;

public class ChatActivity extends BaseActivity implements EventListener {

	BmobChatUser targetUser;
	String targetId;// targetUser的objectId
	String targetName;// targetUser的username
	String myId;// 当前登录用户的objectId

	// 与文本类型聊天相关的属性

	@Bind(R.id.lv_chat_listview)
	ListView listView;
	List<BmobMsg> messages;
	ChatAdapter adapter;

	@Bind(R.id.et_chat_content)
	EditText etContent;// 输入文本内容
	@Bind(R.id.btn_chat_add)
	Button btnAdd;// 加号按钮
	@Bind(R.id.btn_chat_send)
	Button btnSend;// 发送按钮

	// 与表情相关的属性
	@Bind(R.id.ll_chat_morecontainer)
	LinearLayout moreContainer;

	RelativeLayout emoLayout;// 示意图中最外侧红色的框
	ViewPager emoViewPager;
	CirclePageIndicator emoCpi;
	// PagerAdapter与emoViewPager配合使用
	EmoPagerAdapter emoPagerAdapter;

	// 与发送图像类型聊天消息相关的属性
	LinearLayout addLayout;
	String cameraPath;

	// 与发送语音类型聊天消息相关的属性
	@Bind(R.id.ll_chat_textinputcontainer)
	LinearLayout textinputContainer;
	@Bind(R.id.ll_chat_voiceinputcontainer)
	LinearLayout voiceinputContainer;
	@Bind(R.id.ll_chat_voicecontainer)
	LinearLayout voiceContainer;
	@Bind(R.id.iv_chat_voiceVolume)
	ImageView ivVolume;
	@Bind(R.id.tv_chat_voicetip)
	TextView tvTip;

	@Bind(R.id.btn_chat_speak)
	Button btnSpeak;

	int[] volumeIamges;
	// BmobIMSDK作者提供的一个封装了
	// MediaRecorder进行录音操作的工具类
	BmobRecordManager recordManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_chat);
	}

	@Override
	public void init() {
		super.init();
		targetUser = (BmobChatUser) getIntent().getSerializableExtra("user");
		targetId = targetUser.getObjectId();
		targetName = targetUser.getUsername();
		myId = bmobUserManager.getCurrentUserObjectId();

		initHeaderView();

		// log(targetId+":"+targetName);
		initView();
		initListView();

	}

	private void initView() {
		// etContent添加TextWatcher监听器
		// 当etContent中有 / 无文字时，切换btnAdd和btnSend的可见性
		etContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.length() > 0) {
					btnAdd.setVisibility(View.INVISIBLE);
					btnSend.setVisibility(View.VISIBLE);
				} else {
					btnAdd.setVisibility(View.VISIBLE);
					btnSend.setVisibility(View.INVISIBLE);
				}

			}
		});

		initEmoLayout();
		initAddLayout();
		initVoiceLayout();
	}

	private void initVoiceLayout() {
		volumeIamges = new int[] { R.drawable.chat_icon_voice1,
				R.drawable.chat_icon_voice2, R.drawable.chat_icon_voice3,
				R.drawable.chat_icon_voice4, R.drawable.chat_icon_voice5,
				R.drawable.chat_icon_voice6 };

		recordManager = BmobRecordManager.getInstance(this);

		recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

			@Override
			public void onVolumnChanged(int value) {
				// 当录音过程中，声音大小发生变化时
				// 回调监听器该方法并将声音大小的值传入回调方法中
				ivVolume.setImageResource(volumeIamges[value]);

			}

			@Override
			public void onTimeChanged(int value, String localPath) {
				// 当录音过程中，随着录音时间的改变
				// 回调该方法(每一秒钟调用一次)
				// 如果语音录制已经到达60秒，则强行将语音文件发送出去
				if (value > 60) {
					btnSpeak.setPressed(false);
					btnSpeak.setEnabled(false);
					voiceContainer.setVisibility(View.INVISIBLE);
					sendVoiceMsg(value, localPath);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							btnSpeak.setEnabled(true);
						}
					}, 1000);
				}
			}
		});

	}

	/**
	 * 发送语音类型的聊天消息
	 * 
	 * @param value
	 * @param localPath
	 */
	protected void sendVoiceMsg(int value, String localPath) {
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("当前网络不给力");
			return;
		}
		bmobChatManager.sendVoiceMessage(targetUser, localPath, value,
				new UploadListener() {

					@Override
					public void onSuccess() {
						// 此时msg的content属性值是file:///语音文件本地地址&语音文件的网络地址&语音的文件的长度
						refresh();

					}

					@Override
					public void onStart(BmobMsg msg) {
						// 该msg的content属性是file:///语音文件本地地址&语音文件的长度
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);

					}

					@Override
					public void onFailure(int error, String arg1) {
						// TODO Auto-generated method stub

					}
				});
	}

	private void initAddLayout() {
		addLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.add_layout, moreContainer, false);

		TextView tvPicture = (TextView) addLayout
				.findViewById(R.id.tv_add_picture);
		tvPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(intent, 101);

			}
		});

		TextView tvCamera = (TextView) addLayout
				.findViewById(R.id.tv_add_photo);
		tvCamera.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				File file = new File(
						Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						System.currentTimeMillis() + ".jpg");
				cameraPath = file.getAbsolutePath();
				Uri imgUri = Uri.fromFile(file);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
				startActivityForResult(intent, 102);

			}
		});

		TextView tvLocation = (TextView) addLayout
				.findViewById(R.id.tv_add_location);
		tvLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到地图相关界面
				Intent intent = new Intent(ChatActivity.this,
						LocationActivity.class);
				intent.putExtra("from", "mylocation");
				startActivityForResult(intent, 103);

			}
		});

	}

	/**
	 * 初始化表情相关的布局
	 */
	private void initEmoLayout() {
		emoLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.emo_layout, moreContainer, false);
		emoViewPager = (ViewPager) emoLayout
				.findViewById(R.id.vp_emolayout_viewpager);
		emoCpi = (CirclePageIndicator) emoLayout
				.findViewById(R.id.cpi_emolayout_indicator);

		// emoViewPager配合使用的PagerAdapter
		List<View> views = new ArrayList<View>();
		// 把显示表情的一个个GridView放到views集合中on个

		int pageno = EmoUtil.emos.size() % 21 == 0 ? EmoUtil.emos.size() / 21
				: EmoUtil.emos.size() / 21 + 1;

		for (int i = 0; i < pageno; i++) {
			View view = getLayoutInflater().inflate(
					R.layout.emo_gridview_layout, emoLayout, false);
			GridView gridView = (GridView) view
					.findViewById(R.id.gv_emo_gridview);

			int end = Math.min((i + 1) * 21, EmoUtil.emos.size());
			List<String> emos = EmoUtil.emos.subList(i * 21, end);
			final EmoGridViewAdapter emoGridViewAdpater = new EmoGridViewAdapter(
					this, emos);
			gridView.setAdapter(emoGridViewAdpater);

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String emoname = emoGridViewAdpater.getItem(position);
					etContent.append(EmoUtil.getSpannableString(emoname));
				}
			});

			views.add(view);
		}

		emoPagerAdapter = new EmoPagerAdapter(views);
		emoViewPager.setAdapter(emoPagerAdapter);
		emoCpi.setViewPager(emoViewPager);
		// emoCpi圆点的大小和颜色是否需要改变
		emoCpi.setFillColor(Color.DKGRAY);

	}

	private void initListView() {
		messages = new ArrayList<BmobMsg>();
		adapter = new ChatAdapter(this, messages);
		listView.setAdapter(adapter);

	}

	private void initHeaderView() {
		setHeaderTitle(targetName, Position.CENTER);
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});

	}

	@Override
	protected void onResume() {
		super.onResume();
		MyPushMessageReceiver.regist(this);
		refresh();
	}

	@Override
	protected void onPause() {
		MyPushMessageReceiver.unRegist(this);
		super.onPause();
	}

	private void refresh() {
		// 聊天消息保存在两个地方
		// 1)Bmob服务器的BmobMsg数据表中
		// 2)保存在本地数据库的chat表中
		// ListView的数据源来自本地数据库的chat表
		// 呈现是，将当前登录用户与targetUser之间的所有聊天记录取出并呈现
		List<BmobMsg> list = bmobDB.queryMessages(targetId, 0);
		adapter.addItems(list, true);
		// 如果聊天消息数量超过了一屏可以显示的范围
		// 则优先显示最后的消息
		listView.setSelection(adapter.getCount() - 1);

	}

	@OnClick(R.id.btn_chat_send)
	public void sendTextMessage(View v) {
		String message = etContent.getText().toString();
		if (TextUtils.isEmpty(message)) {
			return;
		}

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("当前网络不给力");
			return;
		}

		// 发送文本类型的聊天消息
		// msg对象中5个重要属性的属性值什么：
		// tag:""
		// content:通过方法参数传入的etContent中的文本内容
		// msgType: 1 文本类型
		// isreaded: 0 未读
		// status: 1 Success
		final BmobMsg msg = BmobMsg.createTextSendMsg(this, targetId, message);

		// sendTextMessage方法做了什么

		// 1)在服务器_user表中查找targetUser所对应的数据记录中设备ID的值
		// 2)根据BmobMsg对象的内容创建JsonObject并利用BmobPushManager向第1步查询出来的设备ID进行推送
		// 3)当推送成功后，将BmobMsg对象的status属性值更新为1
		// 注意：该操作对发送文本类型聊天消息来说意义不大，因为该BmobMsg对象在创建时status的属性值就是1
		// 4)将BmobMsg对象保存到服务器BmobMsg数据表中，此时isReaded字段值为0，status字段值为1
		// 5)将BmobMsg对象的isReaded属性值设定为1(从0改为1)
		// 6)将BmobMsg对象保存或更新到本地数据库的chat数据表中
		// 7)根据BmobMsg对象提取部分属性构建一个BmobRecent对象，并将该BmobRecent对象保存到本地数据库的recent数据表中
		// 8)调用咱们自己写的监听器中的相应方法

		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				// 发送文本类型消息成功后
				etContent.setText("");
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("发送聊天消息失败，稍后重试", arg0, arg1);
			}
		});

	}

	@OnClick(R.id.btn_chat_voice)
	public void showVoiceInputContaienr(View v) {
		textinputContainer.setVisibility(View.INVISIBLE);
		voiceinputContainer.setVisibility(View.VISIBLE);

		moreContainer.removeAllViews();
	}

	@OnTouch(R.id.btn_chat_speak)
	public boolean speak(View v, MotionEvent event) {

		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			voiceContainer.setVisibility(View.VISIBLE);
			recordManager.startRecording(targetId);
			break;
		case MotionEvent.ACTION_MOVE:
			if (event.getY() < 0) {
				// 手指在按钮外侧
				tvTip.setText("手指松开，取消发送");
			} else {
				tvTip.setText("手指上滑，取消发送");
			}
			break;

		default:

			voiceContainer.setVisibility(View.INVISIBLE);

			if (event.getY() < 0) {
				// 取消发送
				recordManager.cancelRecording();
			} else {
				int value = recordManager.stopRecording();
				String localPath = recordManager.getRecordFilePath(targetId);
				sendVoiceMsg(value, localPath);
			}

			break;
		}

		return true;

	}

	@OnClick(R.id.btn_chat_keyboard)
	public void showTextInputContainer(View v) {
		textinputContainer.setVisibility(View.VISIBLE);
		voiceinputContainer.setVisibility(View.INVISIBLE);

	}

	@OnClick(R.id.btn_chat_emo)
	public void addEmoLayout(View v) {
		if (moreContainer.getChildCount() > 0) {
			if (moreContainer.getChildAt(0) == addLayout) {
				moreContainer.removeAllViews();
				moreContainer.addView(emoLayout);
			} else {
				moreContainer.removeAllViews();
			}
		} else {
			moreContainer.addView(emoLayout);
		}
	}

	@OnClick(R.id.btn_chat_add)
	public void addAddLayout(View v) {
		if (moreContainer.getChildCount() > 0) {
			if (moreContainer.getChildAt(0) == emoLayout) {
				moreContainer.removeAllViews();
				moreContainer.addView(addLayout);
			} else {
				moreContainer.removeAllViews();
			}
		} else {
			moreContainer.addView(addLayout);
			if (voiceinputContainer.getVisibility() == View.VISIBLE) {
				voiceinputContainer.setVisibility(View.INVISIBLE);
				textinputContainer.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case 101:
				Uri uri = arg2.getData();
				Cursor c = getContentResolver().query(uri,
						new String[] { Media.DATA }, null, null, null);
				c.moveToNext();
				String filePath = c.getString(0);
				c.close();
				// log("filePath:--->"+filePath);
				sendImageMessage(filePath);
				break;

			case 102:
				// log("filePath:--->"+cameraPath);
				sendImageMessage(cameraPath);
				break;
			case 103:
				// 从LocationActivity回传的相关内容

				double lat = arg2.getDoubleExtra("lat", -1);
				double lng = arg2.getDoubleExtra("lng", -1);
				String address = arg2.getStringExtra("address");
				String url = arg2.getStringExtra("url");
				String path = arg2.getStringExtra("path");

				// log("lat:"+lat+", lng:"+lng+",address:"+address+",path:"+path+",url:"+url);
				sendLocationMessage(lat, lng, address, url, path);
				break;
			}
		}
	}

	/**
	 * 发送地理位置类型的聊天消息
	 * 
	 * @param lat
	 * @param lng
	 * @param address
	 * @param url
	 * @param path
	 */
	private void sendLocationMessage(double lat, double lng, String address,
			String url, String path) {
		// 所谓“位置聊天”消息实际就是文本类型的聊天消息
		// msg的content属性值"街道&本地地址&网络地址&纬度&经度"
		final BmobMsg msg = BmobMsg.createLocationSendMsg(this, targetId,
				address + "&" + path + "&" + url, lat, lng);
		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				// TODO Auto-generated method stub

			}
		});
	}

	/**
	 * 发送图像类型的聊天消息
	 * 
	 * @param filePath
	 */
	private void sendImageMessage(String filePath) {
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("当前网络不给力");
			return;
		}

		bmobChatManager.sendImageMessage(targetUser, filePath,
				new UploadListener() {

					@Override
					public void onSuccess() {
						// 在调用onStart方法式传入的msg对象
						// 此时已经保存在了本地数据库中
						// 此时在chat表中保存的msg对象一些重要属性是
						// content file:///图像的本地地址&图像在服务器的地址
						// status 1
						// isreaed 1
						// msgType 2
						refresh();
					}

					@Override
					public void onStart(BmobMsg msg) {
						// msg对象的属性
						// tag ""
						// cotent file:///+图像的本地地址
						// msgType 2
						// status 0
						// isreaded 0
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);
					}

					@Override
					public void onFailure(int error, String arg1) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	public void onMessage(BmobMsg message) {
		// 确认message的发送人就是当前与我正在聊天的人
		if (message.getBelongUsername().equals(targetName)) {
			adapter.addItem(message);
			listView.setSelection(adapter.getCount() - 1);
			// 更新两人之间聊天记录均为“已读”
			bmobDB.resetUnread(targetId);
		}
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onOffline() {
		// TODO Auto-generated method stub

	}

}
