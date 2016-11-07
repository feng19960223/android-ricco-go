package com.tarena.fgr.constant;

public interface IURL {
	public static final String ROOT = "http://10.8.52.92:8080/MusicServer/";
	public static final String LOADMUSICS_URL = ROOT + "loadMusics.jsp";
	// 播放控制界面发给服务的广播意图动作
	public static final String MUSIC_PLAY_ACTION = "com.tarena.fgr.music.action.play";// 播放
	public static final String MUSIC_PAUSE_ACTION = "com.tarena.fgr.music.action.pause";// 暂停
	public static final String MUSIC_PROGRESS_ACTION = "com.tarena.fgr.music.action.progress";// 进度
	public static final String MUSIC_UP_PROGRESS_ACTION = "com.tarena.fgr.music.action.upprogress";// 更新进度
	public static final String MUSIC_NEXT_ACTION = "com.tarena.fgr.music.action.next";// 下一首
	public static final String MUSIC_LIST_ACTION = "android.appwidget.action.MUSIC_LIST";// 音乐列表

	public static final String WIDGET_NEXT_ACTION = "com.tarena.widgetnext";// 必须再写一个ACTION,否则会冲突
	public static final String WIDGET_PRE_ACTION = "com.tarena.widgetpre";
	public static final String WIDGET_PLAY_ACTION = "com.tarena.widgetplay";
	public static final String WIDGET_PAUSE_ACTION = "com.tarena.widgetpause";

}
