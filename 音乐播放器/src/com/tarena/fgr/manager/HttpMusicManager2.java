package com.tarena.fgr.manager;

import java.util.List;

import org.apache.http.Header;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tarena.fgr.entity.Music;
import com.tarena.fgr.utils.JSONParserUtils;
import com.tarena.fgr.utils.LogUtils;

public class HttpMusicManager2 {
	public static LoadMusicsListener loadlistener = null;

	public static void loadMusics(String path, LoadMusicsListener listener) {
		loadlistener = listener;
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(path, new MyResponseHandler());
	}

	public static class MyResponseHandler extends JsonHttpResponseHandler {
		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			super.onFailure(statusCode, headers, throwable, errorResponse);
			LogUtils.e(statusCode + "");
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			super.onSuccess(statusCode, headers, response);
			// json½âÎö
			List<Music> musics = JSONParserUtils.parseMusics(response);
			loadlistener.onMusicsLoadEnd(musics);
		}
	}

	public interface LoadMusicsListener {
		public void onMusicsLoadEnd(List<Music> musics);
	}
}
