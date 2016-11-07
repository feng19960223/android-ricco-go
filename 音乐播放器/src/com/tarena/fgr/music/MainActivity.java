package com.tarena.fgr.music;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.tarena.fgr.adapter.MusicAdapter;
import com.tarena.fgr.constant.IURL;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.manager.HttpMusicManager2;
import com.tarena.fgr.manager.HttpMusicManager2.LoadMusicsListener;
import com.tarena.fgr.service.MusicService;
import com.tarena.fgr.utils.LogUtils;

//import com.tarena.fgr.manager.HttpMusicManager.LoadMusicListener;

public class MainActivity extends Activity implements LoadMusicsListener,
		OnItemClickListener {
	private ListView listView = null;
	private MusicAdapter adapter = null;
	// private ProgressDialog progressDialog = null;// 进度条对话框
	private MyDialog dialog = null;
	List<Music> musics = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().hide();
		listView = (ListView) findViewById(R.id.listView_musics);

		// progressDialog = ProgressDialog.show(this, "数据加载", "一大波音乐正在赶来的路上");
		dialog = new MyDialog(this,
				android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth);
		ImageView imageView = new ImageView(this);
		imageView.setImageResource(R.drawable.animation_splash);
		AnimationDrawable animationDrawable = (AnimationDrawable) imageView
				.getDrawable();
		animationDrawable.start();
		dialog.setTitle("天籁正在飞来的路上");
		dialog.setContentView(imageView);
		dialog.show();

		adapter = new MusicAdapter(MainActivity.this);
		listView.setAdapter(adapter);

		initDatas();

		listView.setOnItemClickListener(this);

		// 启动音乐播放的服务
		Intent service = new Intent(this, MusicService.class);
		startService(service);
		LogUtils.i("+++++++++++++++++++++");
	}

	private void initDatas() {
		// HttpMusicManager.asyncLoadMusics(IURL.LOADMUSICS_URL, this);
		HttpMusicManager2.loadMusics(IURL.LOADMUSICS_URL, this);
	}

	@Override
	public void onMusicsLoadEnd(List<Music> musics) {
		adapter.addMusics(musics);
		// progressDialog.dismiss();
		dialog.dismiss();
		this.musics = musics;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// Music music = adapter.getItem(position);
		// Bundle bundle = new Bundle();
		// bundle.putSerializable("music", music);
		// intent.putExtras(bundle);
		// intent.putExtra("musicKey", music);

		Intent intent = new Intent(this, PlayActivity.class);
		intent.putExtra("musicsKey", (ArrayList<Music>) musics);
		intent.putExtra("positionKey", position);
		startActivity(intent);
	}

	class MyDialog extends Dialog {

		public MyDialog(Context context, int theme) {
			super(context, theme);
		}
	}

}
