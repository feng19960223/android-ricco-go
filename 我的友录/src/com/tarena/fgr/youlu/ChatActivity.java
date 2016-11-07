package com.tarena.fgr.youlu;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tarena.fgr.adapter.SmsBaseAdapter;
import com.tarena.fgr.biz.DialogManager;
import com.tarena.fgr.biz.SMSManager;
import com.tarena.fgr.entity.Sms;
import com.tarena.fgr.fragment.SmsFragment;

/**
 * 短信聊天界面
 * 
 * @author 冯国芮
 * 
 */
public class ChatActivity extends Activity implements OnScrollListener,
		OnItemLongClickListener {
	private ImageView imageView_left = null;// 回退短信界面
	private ImageView imageView_right = null;// 隐藏
	private TextView textView_title = null;// 联系人姓名或电话
	private ListView listView_chat = null;// 聊天内容
	private ImageView imageView_plus = null;// 左下角的加号
	private EditText editText_sms = null;// 短信编辑区域
	private Button button_send = null;// 发送按钮
	private InputMethodManager IMM = null;// 软键盘
	private SmsBaseAdapter adapter = null;
	private List<Sms> list = null;
	private String phone = null;// 由上个页面传递过来的电话号码
	private SMSReceiver smsReceiver = null;
	// 收到短信的系统广播
	public final static String RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		initUI();// 初始化UI
		initIntent();// 上个页面传递过来的数据
		hideIMM();// 默认隐藏软键盘
		initListener();
		initData();
		// 广播接收器的注册方法
		// 1.动态注册(广播接收器是和订阅它的组件绑定在一起的)
		// 订阅广播接收器的组件被激活了,广播接收器才可以接收广播
		// 2.静态注册(广播接收器注册在清单文件中,只要有其订阅的广播发过来就能收到广播,
		// 但是广播接收器的生命周期大概只有10秒左右)
		registReceiver();
	}

	private IntentFilter intentFilter;

	private void registReceiver() {
		// 创建广播接收器对象
		smsReceiver = new SMSReceiver();
		// 订阅广播收到短信时系统发来的有序广播
		intentFilter = new IntentFilter();
		intentFilter.addAction(RECEIVE_SMS);

		// *********************************************
		// *********************************************
		// *********************************************
		intentFilter.addAction(SMSManager.SEND_SMS);
		// 这条太TM重要了,切记
		// *********************************************
		// *********************************************
		// *********************************************

		intentFilter.setPriority(1000);// 一般优先级的优先级为-1000到1000
		registerReceiver(smsReceiver, intentFilter);
	}

	private void initIntent() {
		Intent intent = getIntent();
		int intentThreadId = intent.getIntExtra(SmsFragment.ACTION_THREAD_ID,
				-1);
		phone = intent.getStringExtra(SmsFragment.ACTION_PHONE);
		String name = intent.getStringExtra(SmsFragment.ACTION_NAME);
		thread_id = intentThreadId;
		textView_title.setText(name);
	}

	private void initUI() {
		IMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imageView_left = (ImageView) findViewById(R.id.imageView_actionbar_left);
		imageView_right = (ImageView) findViewById(R.id.imageView_actionbar_right);
		textView_title = (TextView) findViewById(R.id.textView_actionbar_title);
		listView_chat = (ListView) findViewById(R.id.listView_chat);
		imageView_plus = (ImageView) findViewById(R.id.imageView_plus);
		editText_sms = (EditText) findViewById(R.id.editText_sms);
		button_send = (Button) findViewById(R.id.button_send);
		imageView_right.setVisibility(View.INVISIBLE);// 隐藏
		adapter = new SmsBaseAdapter(this);
	}

	private int thread_id;

	private void initData() {
		// 短信聊天的数据
		refreshChat();
		listView_chat.setAdapter(adapter);
		listView_chat.setSelection(list.size() - 1);// 默认显示,最进的一条短信
	}

	private void refreshChat() {
		list = SMSManager.getSMSes(this, thread_id);
		adapter.addItems(list, true);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshChat();
		registerReceiver(smsReceiver, intentFilter);
	}

	private void initListener() {

		// 如果edittext为空显示白色,有内容显示蓝色
		editText_sms.addTextChangedListener(new MyTextWatcher());
		// 点击加号显示或隐藏软键盘
		imageView_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				autoIMM();
			}
		});
		// 点击文本框弹出软键盘
		editText_sms.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showIMM();
			}
		});
		// 回退到短信界面
		imageView_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		button_send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 2016年10月12日 09:07:16
				// 冯国芮:这是为了实现功能写个一个有bug的方法
				// 巨大的bug,短信没有发送成功也会保存到数据库
				// 2016年10月12日 10:50:47
				// 冯国芮:通过广播发送,然后获得一个是否发送成功,问题解决
				// 去掉前后空格
				if (!TextUtils
						.isEmpty(editText_sms.getText().toString().trim())) {// 不为空的时候发送
					// 强行关闭软键盘
					closeIMM();
					// 发送短信
					// 这样的发送,不会向数据库写如短信记录
					String message = editText_sms.getText().toString();

					SMSManager.sendSms(ChatActivity.this, message, phone);

					// 2016年10月12日 10:54:22
					// 增加记录和刷新数据,到广播里短信发送成功

					// 数据库增加记录
					// 直接加,会话列表会增加吗???
					// SmsManager.insertSms(ChatActivity.this, phone, body);

					// item同时需要增加
					// 重新加载数据
					// refreshChat();
					// 默认显示,最近的一条短信
					// 当我打开的时候,显示最后一条,向上滑,然后直接发送短信,短信发送成功,应该显示发送出去的短信
					// listView_chat.setSelection(list.size() - 1);

					// 清空edit
					editText_sms.setText("");
				}
			}
		});

		// 查看聊天记录,滑动事件,重要强制关闭软键盘
		listView_chat.setOnScrollListener(this);
		listView_chat.setOnItemLongClickListener(this);
	}

	public class SMSReceiver extends BroadcastReceiver {
		@Override
		// 当它订阅的那些类型的广播发来的时候会回调方法
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (action.equals(RECEIVE_SMS)) {// 收到短信
				// 短信140个字节
				// LogUtil.i("TAG:", action);
				// 获得短信的内容,对短信内容进行拆解
				Bundle bundle = intent.getExtras();

				Sms sms = SMSManager.getSmsByReceiver(bundle);

				// LogUtil.i("TAG:sms", sms.toString());
				// 判断一下是不是发给当前会话的短信
				// 如果是的话将短信保存到收件箱并忽略收到短信的
				// 有序广播 ，不再向系统的短信收发应用传递
				if (phone.equals(sms.getAddress())) {
					// 将短信保存在收件箱中
					SMSManager.saveReciveMessage(context, sms, thread_id);
					// 忽略当前的有序广播
					// 阻止该短信的广播向下一级广播传递,使系统的短信应用不再接收该短信
					// 不会显示系统短信的Notification
					abortBroadcast();
					refreshChat();
				}
			} else if (action.equals(SMSManager.SEND_SMS)) {
				// 短信发送成功

				// 数据库增加记录
				String phone = intent.getStringExtra("phone");
				String body = intent.getStringExtra("body");

				SMSManager.insertSms(context, phone, body);

				Toast.makeText(ChatActivity.this, "发送成功", Toast.LENGTH_SHORT)
						.show();

				// item同时需要增加
				// 重新加载数据
				refreshChat();
				// 默认显示,最近的一条短信
				// 当我打开的时候,显示最后一条,向上滑,然后直接发送短信,短信发送成功,应该显示发送出去的短信
				listView_chat.setSelection(list.size() - 1);

			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (smsReceiver != null) {
			// 注销广播接收器,释放资源
			unregisterReceiver(smsReceiver);
		}
	}

	// 软键盘自动,开则关,关则开
	private void autoIMM() {
		// 判断软键盘是否打开
		boolean isOpen = getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE;
		if (isOpen) {
			showIMM();
		} else {
			hideIMM();
		}
	}

	// 显示软键盘
	private void showIMM() {
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	// 隐藏软键盘
	private void hideIMM() {
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	// 强制关闭软键盘
	private void closeIMM() {
		IMM.hideSoftInputFromWindow(editText_sms.getWindowToken(), 0);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {// 聊天记录的滑动事件
		closeIMM();
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
	}

	@Override
	// 长按删除一条短信
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		DialogManager.showDeleteSmsDialog(this, adapter.getItem(position),
				adapter);
		return false;
	}

	public class MyTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.length() == 0) {
				// button_send.setBackgroundColor(Color.WHITE);
				button_send.setBackgroundResource(R.drawable.button_bg);
			} else {
				// button_send.setBackgroundColor(Color.BLUE);
				button_send.setBackgroundResource(R.drawable.button_bg_ok);
			}
		}

	}
}
