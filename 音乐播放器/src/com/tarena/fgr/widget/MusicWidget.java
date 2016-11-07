package com.tarena.fgr.widget;

import java.util.List;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RemoteViews;

import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.music.R;
import com.tarena.fgr.utils.LogUtils;

/**
 * 桌面小组件,其实是一个广播接收器
 * 
 * @author 冯国芮
 */
public class MusicWidget extends AppWidgetProvider {
	// 在小组件中维护一个音乐播放的音乐列表
	// 并且维护当前正在播放的音乐的索引
	// 因为service的生命周期比较短,如果不理service,大约10就会被销毁,所以定义成静态
	private static List<Music> musics = null;
	private static int position = 0;

	// 当控制mediaplayer的服务启动时候,异步加载音乐列表
	// 并且将加载完毕的音乐列表,通过发广播的方式,传给小组件
	// 在小组件当中接收到广播后从意图中将音乐列表的信息获取过来,并设置给小组件中的musics
	// 当小组件被添加到桌面时判断当前的音乐列表是否为空,如果不为空,对组件上的控件进行初始化设置,并且为组件进行监听器的设置:
	// 点击播放的时候向服务发送播放广播...点击暂停的时候向服务发送暂停广播,点击下一首和上一首的时候发送广播..

	@SuppressWarnings("unchecked")
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);// 父类有一个分发机制,所以一定要写到第一行
		String action = intent.getAction();
		if (action.equals(IURL.MUSIC_LIST_ACTION)) {
			LogUtils.i("音乐列表加载完成");
			musics = (List<Music>) intent.getSerializableExtra("musiclistKey");
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		if (musics != null && musics.size() > 0) {
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_music);
			views.setViewVisibility(R.id.imageView_pre, View.VISIBLE);
			views.setViewVisibility(R.id.imageView_play, View.VISIBLE);
			views.setViewVisibility(R.id.imageView_next, View.VISIBLE);
			setWidgets(context, views);
			appWidgetManager.updateAppWidget(appWidgetIds, views);
		}
	}

	private void setWidgets(Context context, RemoteViews views) {
		// 获得集合中的当前包播放的音乐对象
		Music music = musics.get(position);
		LogUtils.i(music.toString());
		// 要播放的音乐对象的url
		String songname = music.getName();
		String singer = music.getSinger();
		views.setTextViewText(R.id.textView_name, songname);
		views.setTextViewText(R.id.textView_singer, singer);

		String musicpath = IURL.ROOT + music.getMusicpath();
		// 为组件上的四个按钮添加监听事件
		Intent intentPlay = new Intent(IURL.WIDGET_PLAY_ACTION);
		intentPlay.putExtra("musicpath", musicpath);
		PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context,
				100, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageView_play, pendingIntentPlay);

		Intent intentPause = new Intent(IURL.WIDGET_PAUSE_ACTION);
		intentPause.putExtra("musicpath", musicpath);
		PendingIntent pendingIntentPause = PendingIntent.getBroadcast(context,
				100, intentPause, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageview_pause, pendingIntentPause);

		// 下一首
		String next = IURL.ROOT + musics.get(music.getId()).getMusicpath();
		LogUtils.i("-----------" + next);
		Intent intentNext = new Intent(IURL.WIDGET_NEXT_ACTION);
		intentPause.putExtra("musicpath", next);
		PendingIntent pendingIntentNext = PendingIntent.getBroadcast(context,
				100, intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageView_next, pendingIntentNext);

		// 上一首
		String pre = IURL.ROOT + musics.get(music.getId()).getMusicpath();// -2
		Intent intentPre = new Intent(IURL.WIDGET_PRE_ACTION);
		intentPause.putExtra("musicpath", pre);
		PendingIntent pendingIntentPre = PendingIntent.getBroadcast(context,
				100, intentPre, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.imageView_pre, pendingIntentPre);

	}

	@Override
	public void onDisabled(Context context) {
	}

	@Override
	public void onEnabled(Context context) {
	}
}
