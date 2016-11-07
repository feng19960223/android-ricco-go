package com.tarena.fgr.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tarena.fgr.entity.Music;

public class JSONParserUtils {
	public static List<Music> parseMusics(JSONObject jsonObject) {
		List<Music> musics = new ArrayList<Music>();
		try {
			// 实现json的解析
			String result = jsonObject.getString("result");
			if ("ok".equals(result)) {
				JSONArray jsonArray = jsonObject.getJSONArray("data");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonMusic = jsonArray.getJSONObject(i);
					int id = jsonMusic.getInt("id");
					String album = jsonMusic.getString("album");
					String albumpic = jsonMusic.getString("albumpic");
					String author = jsonMusic.getString("author");
					String composer = jsonMusic.getString("composer");
					String downcount = jsonMusic.getString("downcount");
					String durationtime = jsonMusic.getString("durationtime");
					String favcount = jsonMusic.getString("favcount");
					String musicpath = jsonMusic.getString("musicpath");
					String name = jsonMusic.getString("name");
					String singer = jsonMusic.getString("singer");
					musics.add(new Music(id, name, album, albumpic, author,
							composer, downcount, durationtime, favcount,
							musicpath, singer));
				}
			}
		} catch (Exception e) {
		}

		// 对List进行简单的排序
		// Collections.sort(musics, new Comparator<Music>() {
		// @Override
		// public int compare(Music lhs, Music rhs) {
		// // return 0;
		// return lhs.getName().compareTo(rhs.getName());
		// }
		// });
		return musics;
	}

}
