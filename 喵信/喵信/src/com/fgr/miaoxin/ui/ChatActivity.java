package com.fgr.miaoxin.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.view.ViewPager;
import android.text.Editable;
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
import com.fgr.miaoxin.app.MyApp;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.receiver.MyPushMessageReceiver;
import com.fgr.miaoxin.util.DialogUtil;
import com.fgr.miaoxin.util.EmoUtil;
import com.fgr.miaoxin.util.NetUtil;
import com.viewpagerindicator.CirclePageIndicator;

public class ChatActivity extends BaseActivity implements EventListener {
	// 与当前登录用户进行聊天的用户
	// 从MessageFragment或从UserInfoActivity传递过来的
	BmobChatUser targetUser;
	String targetUsername;// targetUser的username
	String targetId;// targetUser的objectId
	String myId;// 当前登录用户的objectId

	// 与文本聊天相关的视图组件
	@Bind(R.id.lv_chat_listview)
	ListView listView;
	List<BmobMsg> messages;
	ChatAdapter adapter;

	@Bind(R.id.et_chat_content)
	EditText etContent;
	@Bind(R.id.btn_chat_add)
	Button btnAdd;
	@Bind(R.id.btn_chat_send)
	Button btnSend;

	// 与表情布局相关的属性
	@Bind(R.id.ll_chat_morelayoutcontainer)
	LinearLayout moreContainer;

	RelativeLayout emoLayout;

	ViewPager emoViewPager;
	CirclePageIndicator emoCpi;
	EmoPagerAdapter emoPagerAdapter;

	// 与发送图片以及位置聊天相关的属性
	LinearLayout addLayout;
	String cameraPath;

	// 与语音聊天消息相关的内容
	@Bind(R.id.ll_chat_textinputcontainer)
	LinearLayout textinputContainer;

	@Bind(R.id.ll_chat_voiceinputcontainer)
	LinearLayout voiceinputContainer;

	@Bind(R.id.ll_chat_voicecontainer)
	LinearLayout voiceContainer;

	@Bind(R.id.iv_chat_voicevolum)
	ImageView ivVoiceVolum;
	@Bind(R.id.tv_chat_voicetip)
	TextView tvVoiceTip;

	@Bind(R.id.btn_chat_speak)
	Button btnSpeak;

	int[] volumImages;// 录音时表示音量大小的图片

	// BmobIMSDK作者封装的录音工具类
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
		targetUsername = targetUser.getUsername();
		targetId = targetUser.getObjectId();
		myId = bmobUserManager.getCurrentUserObjectId();
		initHeaderView();
		initView();
		initListView();

	}

	private void initHeaderView() {
		setHeaderTitle(targetUsername, Position.CENTER);
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();
					}
				});
	}

	private void initView() {
		initContentInput();
		initEmoLayout();
		initAddLayout();
		initVoiceLayout();

	}

	private void initVoiceLayout() {
		volumImages = new int[] { R.drawable.chat_icon_voice1,
				R.drawable.chat_icon_voice2, R.drawable.chat_icon_voice3,
				R.drawable.chat_icon_voice4, R.drawable.chat_icon_voice5,
				R.drawable.chat_icon_voice6 };

		recordManager = BmobRecordManager.getInstance(this);

		recordManager.setOnRecordChangeListener(new OnRecordChangeListener() {

			@Override
			public void onVolumnChanged(int value) {
				// 监听到录音过程中音量发生变化
				// 根据传入的表示音量大小的value，来选择图片进行显示
				ivVoiceVolum.setImageResource(volumImages[value]);

			}

			@Override
			public void onTimeChanged(int value, String localPath) {
				// 监听到录音过程中时间发生变化（单位：秒）
				// value代表此时一共录制的时长
				// localPath此时录制的语音文件在SD上的存储路径
				if (value >= 60) {
					// 停止录音
					// 将已经录制好的60秒的语音发送出去
					btnSpeak.setEnabled(false);
					btnSpeak.setClickable(false);
					btnSpeak.setPressed(false);
					voiceContainer.setVisibility(View.INVISIBLE);
					recordManager.stopRecording();
					sendVoiceMessage(value, localPath);
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							btnSpeak.setEnabled(true);
							btnSpeak.setClickable(true);

						}
					}, 1000);

				}

			}
		});

	}

	/**
	 * 发送一条语音类型的聊天消息
	 * 
	 * @param value
	 *            语音消息的时长
	 * @param localPath
	 *            语音消息在SD卡上的存储位置
	 */
	protected void sendVoiceMessage(int value, String localPath) {
		if (!NetUtil.isNetworkAvailable(this)) {
			toast("当前网络不给力");
			return;
		}

		bmobChatManager.sendVoiceMessage(targetUser, localPath, value,
				new UploadListener() {
					// 语音文件已经发送完毕
					// 在调用onStart方法时创建的BmobMsg对象已经被保存到了本地数据库的chat表中
					// 通过调用refresh方法刷新ListView
					// 此时chat表中BmobMsg对象所对应的数据记录的部分字段值与BmobMsg对象创建之初的属性发生了变化
					// content 语音文件的路径&长度&语音文件在服务器上的地址
					// status 1
					// isreaded 1
					@Override
					public void onSuccess() {
						refresh();

					}

					// 在语音聊天消息发送之前
					// 会根据传入的语音文件的路径和长度
					// 构建一个BmobMsg对象
					// 此时该BmobMsg对象的属性为：
					// tag ""
					// content 语音文件的路径&长度
					// msgType 4
					// status 0
					// isreaded 0
					// 调用监听器的onStart方法，将该BmobMsg对象传入
					@Override
					public void onStart(BmobMsg msg) {
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);

					}

					@Override
					public void onFailure(int error, String arg1) {
						toastAndLog("语音类型聊天消息发送失败", error, arg1);

					}
				});
	}

	/**
	 * 初始化addLayout
	 * 
	 */
	private void initAddLayout() {
		addLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.add_layout, moreContainer, false);

		TextView tvPicture = (TextView) addLayout
				.findViewById(R.id.tv_addlayout_picture);
		tvPicture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK);
				intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI,
						"image/*");
				startActivityForResult(intent, 101);

			}
		});

		TextView tvCamera = (TextView) addLayout
				.findViewById(R.id.tv_addlayout_camera);
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
				.findViewById(R.id.tv_addlayout_location);
		tvLocation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到地图界面进行定位
				Intent intent = new Intent(ChatActivity.this,
						LocationActivity.class);
				intent.putExtra("from", "mylocation");
				startActivityForResult(intent, 103);

			}
		});
	}

	/**
	 * 初始化emoLayout （示意图中绿色部分的内容）
	 */
	private void initEmoLayout() {
		emoLayout = (RelativeLayout) getLayoutInflater().inflate(
				R.layout.emo_layout, moreContainer, false);
		emoViewPager = (ViewPager) emoLayout
				.findViewById(R.id.vp_emolayout_viewpager);
		emoCpi = (CirclePageIndicator) emoLayout
				.findViewById(R.id.cpi_emolayout_indicator);

		List<View> views = new ArrayList<View>();
		// 向views中增加显示表情的视图
		// views中包含几个view来显示表情，取决于表情的总数量
		// view个数：表情总数量%21==0?表情总数量/21:表情总数量/21+1
		int pageno = EmoUtil.emos.size() % 21 == 0 ? EmoUtil.emos.size() / 21
				: EmoUtil.emos.size() / 21 + 1;
		for (int i = 0; i < pageno; i++) {
			View view = getLayoutInflater().inflate(
					R.layout.emo_gridview_layout, emoViewPager, false);

			GridView gridView = (GridView) view
					.findViewById(R.id.gv_emo_gridview);
			// 数据源
			// 所有的表情是在EmoUtil.emos
			// 每一个gridView都是从EmoUtil.emos取若干个表情
			int startPos = 21 * i;
			int endPos = Math.min(21 * (i + 1), EmoUtil.emos.size());
			List<String> list = EmoUtil.emos.subList(startPos, endPos);
			// 适配器
			final EmoGridViewAdapter emoGridViewAdapter = new EmoGridViewAdapter(
					this, list);
			gridView.setAdapter(emoGridViewAdapter);

			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {

					String resName = emoGridViewAdapter.getItem(position);

					etContent.append(EmoUtil.getSpannableString(resName));
				}
			});

			views.add(view);
		}

		emoPagerAdapter = new EmoPagerAdapter(views);
		emoViewPager.setAdapter(emoPagerAdapter);
		emoCpi.setViewPager(emoViewPager);
		emoCpi.setFillColor(Color.DKGRAY);

	}

	private void initContentInput() {
		// etContent添加一个监听器
		// 监听etContent的内容变化
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
					// 一旦etContent中有内容，btnAdd不可见，btnSend可见
					btnAdd.setVisibility(View.INVISIBLE);
					btnSend.setVisibility(View.VISIBLE);
				} else {
					// 一旦etContent中有内容，btnAdd可见，btnSend不可见
					btnAdd.setVisibility(View.VISIBLE);
					btnSend.setVisibility(View.INVISIBLE);
				}

			}
		});

	}

	private void initListView() {
		messages = new ArrayList<BmobMsg>();
		adapter = new ChatAdapter(this, messages);
		listView.setAdapter(adapter);

	}

	@Override
	protected void onResume() {
		super.onResume();
		MyPushMessageReceiver.regist(this);
		refresh();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyPushMessageReceiver.unRegist(this);
	}

	private void refresh() {
		// 获取ListView真正的数据源
		// 当前登录用户所对应数据库的chat数据表
		List<BmobMsg> list = bmobDB.queryMessages(targetId, 0);
		adapter.addItems(list, true);
		// 让ListView滚到最后一条数据进行呈现
		listView.setSelection(adapter.getCount() - 1);
	}

	@OnClick(R.id.btn_chat_send)
	public void sendTextMessage(View view) {
		String content = etContent.getText().toString();

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("当前网络不给力");
			return;
		}
		// 文本类型的聊天消息msg
		// tag ""
		// content 传入的content内容
		// msgType 1
		// isreaded 0 未读
		// status 1
		final BmobMsg msg = BmobMsg.createTextSendMsg(this, targetId, content);
		// 1. 去服务器_user表中查找targetUser用户
		// 2. 如果有，就根据msg对象创建一个JsonObject对象，然后利用BmobPushManager进行推送
		// 推送时的设备ID是targetUser最后一次登录时所使用的设备ID
		// 3. 推送完毕后，将msg对象保存到服务器的BmobMsg数据表中，此时这条数据记录的isreaded值为0
		// 4. 将msg的isreaded属性值从0更新为1，开始保存到本地数据库的chat表和recent表中
		// 5. 保存完毕后，调用自己写的监听器的相应方法
		// 注意：第3步中，在推送完毕后，保存到服务器之前，先后两次设定msg对象的status为1。

		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);
				etContent.setText("");
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("发送失败，稍后重试", arg0, arg1);

			}
		});
	}

	@OnClick(R.id.btn_chat_emo)
	public void addEmoLayout(View view) {
		if (moreContainer.getChildCount() > 0) {
			// moreContainer有子视图
			if (moreContainer.getChildAt(0) == addLayout) {
				moreContainer.removeAllViews();
				moreContainer.addView(emoLayout);
			} else {
				moreContainer.removeAllViews();
			}
		} else {
			// moreContainer中没有子视图
			moreContainer.addView(emoLayout);
		}
	}

	@OnClick(R.id.btn_chat_add)
	public void addAddLayout(View view) {
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
				textinputContainer.setVisibility(View.VISIBLE);
				voiceinputContainer.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_OK) {
			switch (arg0) {
			case 101:
				// 图库选图
				Uri uri = arg2.getData();
				Cursor cursor = getContentResolver().query(uri,
						new String[] { Images.Media.DATA }, null, null, null);
				cursor.moveToNext();
				String filePath = cursor.getString(0);
				cursor.close();
				sendImageMessage(filePath);
				break;
			case 102:
				// 相机拍照
				sendImageMessage(cameraPath);
				break;
			case 103:
				// 定位返回结果
				String address = arg2.getStringExtra("address");
				String localFilePath = arg2.getStringExtra("localFilePath");
				String url = arg2.getStringExtra("url");

				// log("获得的地址是："+address+",截图的本地路径："+localFilePath+",截图的服务器地址："+url);

				sendLocationMessage(MyApp.lastPoint.getLatitude(),
						MyApp.lastPoint.getLongitude(), address, url,
						localFilePath);

				break;
			}
		}
	}

	/**
	 * 发送位置类型的聊天消息
	 * 
	 * @param lat
	 *            纬度
	 * @param lng
	 *            经度
	 * @param address
	 *            地址
	 * @param url
	 *            截图的网络地址
	 * @param localFilePath
	 *            截图的本地地址
	 */
	private void sendLocationMessage(double lat, double lng, String address,
			String url, String localFilePath) {

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("当前网络不给力");
			return;
		}
		// 创建“位置类型聊天消息”指定了msg对象msgType为3，
		// 然后将地址，纬度，经度拼接为地址&纬度&经度大字符串后
		// 剩下的步骤均与发送文本类型的聊天消息一致
		// 经过修改后得到的msg的content就是：地址&本地截图地址&截图网络地址&纬度&经度
		final BmobMsg msg = BmobMsg.createLocationSendMsg(this, targetId,
				address + "&" + localFilePath + "&" + url, lat, lng);
		bmobChatManager.sendTextMessage(targetUser, msg, new PushListener() {

			@Override
			public void onSuccess() {
				adapter.addItem(msg);
				listView.setSelection(adapter.getCount() - 1);

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("发送位置聊天消息失败，稍后重试", arg0, arg1);

			}
		});

	}

	/**
	 * 发送图像类型的聊天消息
	 * 
	 * @param filePath
	 *            图像在SD卡上的存储地址
	 */
	private void sendImageMessage(String filePath) {

		if (!NetUtil.isNetworkAvailable(this)) {
			toast("当前网络不给力");
			return;
		}

		// log("图片的路径是："+filePath);
		// sendImageMessage:
		// 1. 创建BmobMsg对象
		// tag ""
		// content file:///+filePath
		// status 0
		// msgType 2
		// isreaded 0
		// 2. 调用UploadListener onStart方法，将第1步创建出来的BmobMsg对象作为参数传入
		// 3. 上传本地图片(filePath所对应的图片)到服务器
		// 4. 将图片在服务器上的长地址转化为短地址(自BmobSDK3.4.6开始，作废，源代码逻辑已修改！)
		// 5. 把第一步创建的BmobMsg对象的content属性，从file:///+filePath更改为了第四步得到的网络地址
		// 6. 真正发送聊天消息（当接收方收到这条图像类型的聊天消息时，其content属性值就是图片在服务器上的一个地址）
		// 推送成功后，将第一步msg对象status属性值从0--->1
		// 将msg对象保存到服务器的BmobMsg数据表，isreaded 0
		// 将msg对象的isreaded字段值从0改为1之后，保存到本地数据库的chat表和recent表
		// 7.
		// 当聊天消息发送完毕后，又将第一步创建的BmobMsg对象的content属性从网络地址改为了file:///+filePath&网络地址
		// 同时修改了isreaded属性值从1改为了2，然后将该消息保存到本地数据库的chat表
		// 因为在第六步中，该BmobMsg对象已经被保存过了，因此第七步再次做保存时并不会覆盖原有的数据，仅仅会更新原有数据的
		// 3个字段值：content，status，belongavatar字段，并不会更改isreaded字段
		// 所以，经过第七步保存后的数据记录中content file:///+filePath&网络地址
		// 但是isreaded字段值依然维持1，并未被更改
		// 8. 调用UploadListener onSuccess方法
		bmobChatManager.sendImageMessage(targetUser, filePath,
				new UploadListener() {

					@Override
					public void onSuccess() {
						// 此时，onStart方法调用时所传入的BmobMsg对象已经被保存到了本地数据库的chat表
						// 但是部分属性值已经发生了改变
						// content file:///+filePath&图像在服务器的地址
						// status 1
						// isreaded 1
						// 让发送图像类型消息条目中ProgressBar隐藏
						refresh();

					}

					@Override
					public void onStart(BmobMsg msg) {
						// 此时，ListView中新增了内容，但是msg还未发送
						adapter.addItem(msg);
						listView.setSelection(adapter.getCount() - 1);

					}

					@Override
					public void onFailure(int error, String arg1) {
						toastAndLog("图像聊天消息发送失败，稍后重试", error, arg1);

					}
				});

	}

	@OnClick(R.id.btn_chat_voice)
	public void showVoiceInputContainer(View view) {
		textinputContainer.setVisibility(View.INVISIBLE);
		voiceinputContainer.setVisibility(View.VISIBLE);
		moreContainer.removeAllViews();
		btnAdd.setVisibility(View.VISIBLE);
		btnSend.setVisibility(View.INVISIBLE);
	}

	@OnClick(R.id.btn_chat_keyboard)
	public void showTextInputContainer(View view) {
		textinputContainer.setVisibility(View.VISIBLE);
		voiceinputContainer.setVisibility(View.INVISIBLE);
	}

	@OnTouch(R.id.btn_chat_speak)
	public boolean speak(View view, MotionEvent event) {

		int action = event.getAction();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 录音开始
			voiceContainer.setVisibility(View.VISIBLE);
			recordManager.startRecording(targetId);
			break;

		case MotionEvent.ACTION_MOVE:
			btnSpeak.setPressed(true);
			float y = event.getY();
			if (y < 0) {
				// 手指在按钮之外
				tvVoiceTip.setText("松开手指，取消发送");
			} else {
				// 手指在按钮之内
				tvVoiceTip.setText("手指上滑，取消发送");
			}

			break;

		default:
			// 录音结束了
			btnSpeak.setPressed(false);
			voiceContainer.setVisibility(View.INVISIBLE);
			if (event.getY() < 0) {
				// 在按钮之外抬起的手指
				// 应该取消录制的内容
				recordManager.cancelRecording();
			} else {
				// 将录制的内容作为语音类型的聊天消息发送出去
				int value = recordManager.stopRecording();
				String localPath = recordManager.getRecordFilePath(targetId);
				sendVoiceMessage(value, localPath);
			}

			break;
		}

		return true;

	}

	@Override
	public void onMessage(BmobMsg message) {
		// 作为订阅者，将MyReceiver收到并保存的聊天消息
		// 放到ListView中呈现
		if (message.getBelongId().equals(targetId)) {
			adapter.addItem(message);
			listView.setSelection(adapter.getCount() - 1);
			// 在ListView中呈现的消息都是“已读”
			// 所以，每呈现一条消息都要去修改chat表中
			// 该message对象isreaded字段值从2--更新为-->1
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
