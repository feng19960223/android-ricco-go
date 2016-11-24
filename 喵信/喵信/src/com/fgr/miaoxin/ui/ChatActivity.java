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
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.im.bean.BmobInvitation;
import cn.bmob.im.bean.BmobMsg;
import cn.bmob.im.inteface.EventListener;
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
				// TODO 跳转到地图界面进行定位

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
				// TODO 定位返回结果
				break;
			}
		}
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
	}

	@Override
	public void onNetChange(boolean isNetConnected) {
	}

	@Override
	public void onAddUser(BmobInvitation message) {
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
