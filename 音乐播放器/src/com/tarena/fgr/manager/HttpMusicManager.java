package com.tarena.fgr.manager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.AsyncTask;

import com.tarena.fgr.entity.Music;
import com.tarena.fgr.utils.JSONParserUtils;
import com.tarena.fgr.utils.LogUtils;
import com.tarena.fgr.utils.StreamUtils;

public class HttpMusicManager {
	/**
	 * GET方式实现音乐加载
	 * 
	 * @param path
	 * @return
	 */
	private static List<Music> loadMusics(String path) {
		List<Music> musics = new ArrayList<Music>();
		try {
			URL url = new URL(path);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("GET");
			connection.setConnectTimeout(5000);
			connection.setDoInput(true);
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {// 200
				InputStream is = connection.getInputStream();
				String jsonStr = StreamUtils.streamToString(is);
				JSONObject jsonObject = new JSONObject(jsonStr);
				musics = JSONParserUtils.parseMusics(jsonObject);
			} else {
				LogUtils.i("" + responseCode);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (Music music : musics) {
			LogUtils.i(music.toString());
		}
		LogUtils.i("加载完毕");
		return musics;
	}

	public static LoadMusicListener loadMusicListener = null;

	/**
	 * 异步任务实现加载音乐
	 * 
	 * @param path
	 */
	public static void asyncLoadMusics(String path, LoadMusicListener listener) {
		loadMusicListener = listener;
		MyAsyncTask asyncTask = new MyAsyncTask();
		asyncTask.execute(path);
	}

	// execute传的值,加载的进度,返回类型
	public static class MyAsyncTask extends
			AsyncTask<String, Void, List<Music>> {

		@Override
		// 该方法在后台执行,执行的是工作线程的内容
		// 把加载完成的数据返回给UI主线程
		protected List<Music> doInBackground(String... params) {
			return loadMusics(params[0]);// onPostExecute();
		}

		@Override
		protected void onPostExecute(List<Music> result) {
			loadMusicListener.onMusicsLoadEnd(result);
			super.onPostExecute(result);
		}

		@Override
		// 在doInBackground之前执行,预处理操作
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		// 进度更新
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}
	}

	public interface LoadMusicListener {
		public void onMusicsLoadEnd(List<Music> musics);
	}

}
