package com.tarena.fgr.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.tarena.fgr.entity.Music;
import com.tarena.fgr.utils.JSONParserUtils;

public class HttpMusicManager1 {
	public static List<Music> loadMusics(String path) {
		List<Music> musics = new ArrayList<Music>();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(path);
			HttpResponse response = client.execute(get);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				HttpEntity httpEntity = response.getEntity();
				String jsonStr = EntityUtils.toString(httpEntity);
				JSONObject jsonObject = new JSONObject(jsonStr);
				musics = JSONParserUtils.parseMusics(jsonObject);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return musics;
	}
}
