package com.tarena.fgr.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;

import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.manager.HttpMusicManager;
import com.tarena.fgr.manager.HttpMusicManager.LoadMusicListener;
import com.tarena.fgr.utils.LogUtils;

public class MusicService extends Service {
	MediaPlayer player = null;
	MusicReceiver musicReceiver = null;
	int seekTime;// 记录播放器的暂停位置
	public static boolean isPause = false;// 记录音乐的正在播放状态

	@Override
	public void onCreate() {
		super.onCreate();
		LogUtils.i("音乐播放服务启动");
		player = new MediaPlayer();
		// 为播放器设置监听器
		player.setOnPreparedListener(new OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				LogUtils.i("音乐加载完毕,回调此方法");
				mp.start();
			}
		});
		player.setOnErrorListener(new OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				LogUtils.i("音乐加载出错时执行,错误状态码:" + what);
				return false;
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				LogUtils.i("音乐播放完毕");
				mp.stop();
				// 播放下一首歌由控制界面的进度条SeekBar控制,当进度满了
				// 给服务发送一个新的广播,播放一个新的音乐
				// 而不是在音乐停的时候播放下一首
				isPause = false;
				// 发广播,给控制界面,seekber归0
				Intent intent = new Intent();
				intent.setAction(IURL.MUSIC_NEXT_ACTION);
				sendBroadcast(intent);
			}
		});
		// 注册播放控制界面发来的广播
		registMusicReceiver();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		thread.interrupt();// 线程停止
		player.release();
		player = null;
		unregisterReceiver(musicReceiver);
	}

	Thread thread = null;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					if (player.isPlaying()) {
						// 发广播,传进度
						Intent intent = new Intent();
						// 当前播放时间
						int currentPosition = player.getCurrentPosition();
						intent.putExtra("currentPositionKey", currentPosition);
						intent.setAction(IURL.MUSIC_UP_PROGRESS_ACTION);
						sendBroadcast(intent);
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		thread.start();

		HttpMusicManager.asyncLoadMusics(IURL.LOADMUSICS_URL,
				new LoadMusicListener() {

					@Override
					public void onMusicsLoadEnd(List<Music> musics) {
						// 把加载完毕的音乐的信息通过发广播的方式转给小组件
						Intent intent = new Intent();
						intent.setAction(IURL.MUSIC_LIST_ACTION);
						intent.putExtra("musiclistKey",
								(ArrayList<Music>) musics);
						sendBroadcast(intent);
					}
				});

		return super.onStartCommand(intent, flags, startId);
	}

	private void registMusicReceiver() {
		musicReceiver = new MusicReceiver();
		IntentFilter filter = new IntentFilter();// 过滤器
		filter.addAction(IURL.MUSIC_PLAY_ACTION);
		filter.addAction(IURL.MUSIC_PAUSE_ACTION);
		filter.addAction(IURL.MUSIC_PROGRESS_ACTION);
		filter.addAction(IURL.WIDGET_NEXT_ACTION);
		filter.addAction(IURL.WIDGET_PAUSE_ACTION);
		filter.addAction(IURL.WIDGET_PLAY_ACTION);
		filter.addAction(IURL.WIDGET_PRE_ACTION);
		registerReceiver(musicReceiver, filter);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	public class MusicReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			// 如果是要播放音乐的广播
			if (IURL.MUSIC_PLAY_ACTION.equals(action)) {
				// 获得被播放的音乐的URL
				String musicPath = intent.getStringExtra("musicPathKey");
				// 加载音乐实现音乐播放
				play(musicPath);
			} else if (IURL.MUSIC_PAUSE_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicPathKey");
				pause(musicPath);
			} else if (IURL.MUSIC_PROGRESS_ACTION.equals(action)) {
				int progress = intent.getIntExtra("progressKey", 0);
				// 更改播放进度
				seekToProgress(progress);
			} else if (IURL.WIDGET_NEXT_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				play(musicPath);
			} else if (IURL.WIDGET_PAUSE_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				pause(musicPath);
			} else if (IURL.WIDGET_PLAY_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				play(musicPath);
			} else if (IURL.WIDGET_PRE_ACTION.equals(action)) {
				String musicPath = intent.getStringExtra("musicpath");
				play(musicPath);
			}
		}

	}

	public void play(String musicPath) {// 播放穿来路径的音乐
		try {
			if (isPause) {
				player.seekTo(seekTime);// 从记录时间唱
				player.start();
				seekTime = 0;
			} else {
				// 重置音乐播放器
				player.reset();
				// 设置音乐数据源
				player.setDataSource(musicPath);
				// 异步加载音乐资源
				player.prepareAsync();
			}
			isPause = false;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void seekToProgress(int progress) {
		seekTime = player.getDuration() * progress / 100;
		// 跳转播放进度
		player.seekTo(seekTime);
		// player.start();
	}

	public void pause(String musicPath) {// 暂停正在播放的音乐
		if (player.isPlaying()) {
			isPause = true;
			seekTime = player.getCurrentPosition();
			player.pause();
		}
	}

}
