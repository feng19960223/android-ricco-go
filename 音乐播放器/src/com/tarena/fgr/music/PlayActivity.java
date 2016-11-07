package com.tarena.fgr.music;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.manager.DownLoadMannger;
import com.tarena.fgr.manager.ImageLoader;
import com.tarena.fgr.service.MusicService;
import com.tarena.fgr.utils.LogUtils;
import com.tarena.fgr.view.MyDiskView;
import com.tarena.fgr.view.XCRoundImageView;

public class PlayActivity extends Activity implements OnClickListener,
		OnSeekBarChangeListener {
	private ImageView imageView_back = null;// 返回
	private TextView textView_name = null;// 歌曲,头标题
	private ImageView imageView_music_playing = null;// 音乐正在播放动画
	private XCRoundImageView xCRoundImageView1 = null;// 唱片图片
	private ImageView imageView_favcount = null;// 点赞
	private ImageView imageView_download = null;// 下载
	private SeekBar seekBar1 = null;// 播放进度
	private TextView textView_start = null;// 开始时间
	private TextView textView_end = null;// 结束时间
	private ImageView imageView_pre = null;// 上一首
	private ImageView imageView_play = null;// 播放暂停
	private ImageView imageView_next = null;// 下一首
	private ArrayList<Music> musics = null;
	private AnimationDrawable animationDrawable = null;// 音乐播放中
	private MyDiskView myDiskView = null;// 唱片和底片和指针动画
	private Music music = null;
	private int position;// 播放第几首音乐
	private int progress;// 当前音乐播放进度

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		getActionBar().hide();
		initViews();
		initListener();
		registUpProgress();
	}

	private void registUpProgress() {
		myReceiver = new UpProgressReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(IURL.MUSIC_UP_PROGRESS_ACTION);
		filter.addAction(IURL.MUSIC_NEXT_ACTION);
		registerReceiver(myReceiver, filter);
	}

	private UpProgressReceiver myReceiver = null;

	class UpProgressReceiver extends BroadcastReceiver {
		@SuppressLint("SimpleDateFormat")
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (IURL.MUSIC_UP_PROGRESS_ACTION.equals(action)) {
				// 当前音乐播放到第几毫秒
				int upProgress = intent.getIntExtra("currentPositionKey", 0);
				SimpleDateFormat format = new SimpleDateFormat("mm:ss");
				String time = format.format(upProgress);
				textView_start.setText(time);
				try {
					long up = upProgress * 100
							/ format.parse(music.getDurationtime()).getTime();
					seekBar1.setProgress((int) up);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else if (IURL.MUSIC_NEXT_ACTION.equals(action)) {
				nextMusic();
			}
		}
	}

	private void initListener() {// 设置监听事件
		imageView_back.setOnClickListener(this);
		imageView_favcount.setOnClickListener(this);
		imageView_download.setOnClickListener(this);
		imageView_pre.setOnClickListener(this);
		imageView_play.setOnClickListener(this);
		imageView_next.setOnClickListener(this);
		seekBar1.setOnSeekBarChangeListener(this);
	}

	@SuppressWarnings("unchecked")
	private void initDatas() {// 初始化数据
		musics = (ArrayList<Music>) getIntent().getSerializableExtra(
				"musicsKey");
		position = getIntent().getIntExtra("positionKey", 0);
		music = musics.get(position);
		// music = (Music) getIntent().getExtras().get("music");
		// music = getIntent().getParcelableExtra("musicKey");
		initMusicDatas();
		// imageView_favcount.setColorFilter(Color.parseColor("#CC0033"),
		// Mode.SRC_ATOP);
		imageView_download.setColorFilter(Color.parseColor("#0099CC"),
				Mode.SRC_ATOP);
	}

	public static final String MUSIC_PLAY = "com.tarena.fgr.music.musicplay.action";

	@Override
	protected void onResume() {// 点击进来就开始播放
		super.onResume();
		initDatas();
		// ()()()(()()()()()()()()()()()()()()()()()()())
		// 如果播放的是当前音乐,不应该重新播放
		goPlayMusic();
		// 默认开始动画
		startAnim();
	}

	private void initViews() {// 初始化view
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		textView_name = (TextView) findViewById(R.id.textView_name);
		imageView_music_playing = (ImageView) findViewById(R.id.imageView_music_playing);
		xCRoundImageView1 = (XCRoundImageView) findViewById(R.id.xCRoundImageView1);
		imageView_favcount = (ImageView) findViewById(R.id.imageView_favcount);
		imageView_download = (ImageView) findViewById(R.id.imageView_download);
		seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
		textView_start = (TextView) findViewById(R.id.textView_start);
		textView_end = (TextView) findViewById(R.id.textView_end);
		imageView_pre = (ImageView) findViewById(R.id.imageView_pre);
		imageView_play = (ImageView) findViewById(R.id.imageView_play);
		imageView_next = (ImageView) findViewById(R.id.imageView_next);
		myDiskView = (MyDiskView) findViewById(R.id.myDiskView1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageView_back:// 返回
			finish();
			break;
		case R.id.imageView_favcount:// 点赞
			isFavorite();
			break;
		case R.id.imageView_download:// 下载
			download();
			break;
		case R.id.imageView_pre:// 上一首
			preMusic();
			break;
		case R.id.imageView_play:// 播放中,暂停
			playMusic();
			break;
		case R.id.imageView_next:// 下一首
			nextMusic();
			break;
		default:
			break;
		}

	}

	private void download() {
		// 下载前应该在去查询一下本地文件夹,是否已经下载,如果有则不下载
		final String path = IURL.ROOT + music.getMusicpath();
		final String name = music.getName();

		AlertDialog.Builder builder = new AlertDialog.Builder(this,
				AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
		builder.setTitle("系统提示");
		builder.setIcon(R.drawable.download);
		builder.setMessage("确定要下载" + name + "吗?");// API要求14
		builder.setNegativeButton("再想想?", null);
		builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				DownLoadMannger.downloadSong(PlayActivity.this, path, name);
			}
		});
		// 显示对话框
		AlertDialog alertDialog = builder.create();
		alertDialog.show();
		// 改变按钮颜色
		// 一定要写在show方法后面
		Button btn = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
		btn.setTextColor(Color.WHITE);
		btn.setBackgroundColor(Color.parseColor("#FF8080"));
		// 单纯设置一个btn有可能会有高度偏移,所以直接设置了2个
		// 不使用AlertDialog.THEME_DEVICE_DEFAULT_LIGHT这句话就会发生偏移
		Button btn2 = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
		btn2.setTextColor(Color.BLACK);
		btn2.setBackgroundColor(Color.WHITE);
	}

	private boolean isPlaying = true;

	private void playMusic() {
		if (isPlaying) {
			imageView_play.setImageResource(R.drawable.selector_play);
			animationDrawable.stop();
			myDiskView.stopRotation();
			// 播放暂停
			goPauseMusic();
		} else {
			startAnim();
			// 播放音乐
			goPlayMusic();
		}
		isPlaying = !isPlaying;
	}

	private void startAnim() {
		imageView_play.setImageResource(R.drawable.selector_pause);
		animationDrawable.start();
		myDiskView.startRotation();
	}

	private void goPauseMusic() {
		// 向音乐服务发送广播,让音乐器暂停播放
		Intent intent = new Intent();
		intent.setAction(IURL.MUSIC_PAUSE_ACTION);
		String musicPath = IURL.ROOT + music.getMusicpath();
		LogUtils.i(musicPath);
		intent.putExtra("musicPathKey", musicPath);
		sendBroadcast(intent);
	}

	private void goPlayMusic() {
		// 向音乐播放的服务发广播的指令(发广播)
		Intent intent = new Intent();
		// 要播放歌曲的URL
		String musicPath = IURL.ROOT + music.getMusicpath();
		LogUtils.i(musicPath);
		intent.putExtra("musicPathKey", musicPath);
		intent.setAction(IURL.MUSIC_PLAY_ACTION);
		sendBroadcast(intent);
	}

	private void nextMusic() {
		position++;
		if (position == musics.size()) {// 如果后面没有歌曲,返回第一首
			position = 0;
		}
		music = musics.get(position);
		MusicService.isPause = false;
		isPlaying = true;
		goPlayMusic();
		initMusicDatas();
		startAnim();
		initFavorite();
	}

	private void preMusic() {
		// position控制在另一个按钮,有循环,顺序,随机控制position就可以实现播放的模式
		// 顺序播放
		position--;
		// 随机播放
		// position = new Random().nextInt(musics.size());
		if (position == -1) {// 如果上面没有歌曲,播放最下面的歌曲
			position = musics.size() - 1;
		}
		music = musics.get(position);
		isPlaying = true;
		MusicService.isPause = false;
		goPlayMusic();
		initMusicDatas();
		startAnim();
		initFavorite();
	}

	private void initMusicDatas() {
		initFavorite();
		textView_name.setText(music.getName() + "-" + music.getSinger() + "-"
				+ music.getAlbum());
		textView_start.setText("00:00");// ?得到当前播放的时间
		textView_end.setText(music.getDurationtime());
		if (music.getDurationtime().length() == 4) {
			textView_end.setText("0" + music.getDurationtime());
		}
		animationDrawable = (AnimationDrawable) imageView_music_playing
				.getDrawable();
		String imageviewUrl = IURL.ROOT + music.getAlbumpic();
		ImageLoader.setBitmapFromCache(this, xCRoundImageView1, imageviewUrl);
	}

	private void isFavorite() {// 喜欢是红的还是黑的??,点击事件用
		SharedPreferences sharedPreferences = getSharedPreferences("favorite",
				MODE_PRIVATE);
		isFavorite = sharedPreferences.getBoolean(
				"isFavorite" + music.getName(), false);
		Editor editor = sharedPreferences.edit();
		if (isFavorite) {
			imageView_favcount.setColorFilter(Color.parseColor("#00000000"),
					Mode.SRC_ATOP);
			editor.putBoolean("isFavorite" + music.getName(), false);
		} else {
			imageView_favcount.setColorFilter(Color.parseColor("#CC0033"),
					Mode.SRC_ATOP);
			editor.putBoolean("isFavorite" + music.getName(), true);
		}
		editor.commit();// 提交修改
	}

	private void initFavorite() {// 初始化,和音乐切换时用
		SharedPreferences sharedPreferences = getSharedPreferences("favorite",
				MODE_PRIVATE);
		isFavorite = sharedPreferences.getBoolean(
				"isFavorite" + music.getName(), false);
		if (isFavorite) {
			imageView_favcount.setColorFilter(Color.parseColor("#CC0033"),
					Mode.SRC_ATOP);
		} else {
			imageView_favcount.setColorFilter(Color.parseColor("#00000000"),
					Mode.SRC_ATOP);
		}
	}

	// 通过isFirstIn判断要发那种消息,此值要存储起来
	private boolean isFavorite = false;

	@SuppressLint("SimpleDateFormat")
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		this.progress = progress;
		String duration = textView_end.getText().toString();
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		Date dateDuration;
		try {
			// 把日期对象转换成时间戳
			dateDuration = format.parse(duration);
			long currentTime = dateDuration.getTime() * progress / 100;
			textView_start.setText(format.format(currentTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// 发送通知给服务
		Intent intent = new Intent();
		intent.putExtra("progressKey", progress);
		intent.setAction(IURL.MUSIC_PROGRESS_ACTION);
		sendBroadcast(intent);
	}
}
