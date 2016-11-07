package com.tarena.fgr.biz;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;

/**
 * 一个小音效,用于点击拨号按钮
 * 
 * @author 冯国芮
 * 
 */
public class MediaManager {
	// 声明音效池对象
	public static SoundPool soundPool = null;

	/**
	 * 音效
	 * 
	 * @param context
	 * @param resId
	 *            资源id
	 */
	public static void playMusic(Context context, int resId) {
		if (soundPool == null) {
			// 支持多少个声音,资源类型,资源的质量(预留)[0]
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		}
		// 音乐加载完毕时执行
		soundPool.setOnLoadCompleteListener(new OnLoadCompleteListener() {

			@Override
			public void onLoadComplete(SoundPool soundPool, int sampleId,
					int status) {
				// Load返回的resid,左声道音量,右声道音量,优先级,是否循环-1无限循环0不循环其他为循环次数,播放速率
				soundPool.play(sampleId, 1.0f, 1.0f, 1, 0, 1.0f);
			}
		});
		// 上下文,资源id,priority 优先级(预留)[1]
		soundPool.load(context, resId, 1);
	}

	// 释放音乐资源
	public static void release() {
		if (soundPool != null) {
			soundPool.release();
		}
	}
}
