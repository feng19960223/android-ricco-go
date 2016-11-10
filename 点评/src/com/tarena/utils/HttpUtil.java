package com.tarena.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tarena.app.MyApp;
import com.tarena.groupon.R;

/**
 * @author 冯国芮
 * 
 */
public class HttpUtil {
	private static final String APPKEY = "49814079";
	private static final String APP_SECRET = "90e3438a41d646848033b6b9d461ed54";

	// Volley请求队列
	private static RequestQueue queue = Volley.newRequestQueue(MyApp.context);

	private static ImageLoader imageLoader = new ImageLoader(queue,
			new ImageCache() {
				LruCache<String, Bitmap> lruCache = new LruCache<String, Bitmap>(
						(int) Runtime.getRuntime().maxMemory() / 8) {
					@Override
					protected int sizeOf(String key, Bitmap value) {
						return value.getByteCount();
					}
				};

				@Override
				public void putBitmap(String arg0, Bitmap arg1) {
					lruCache.put(arg0, arg1);
				}

				@Override
				public Bitmap getBitmap(String arg0) {
					return lruCache.get(arg0);
				}
			});

	/**
	 * 获得评论
	 * 
	 * @param url
	 *            评论地址
	 * @param listener
	 */
	public static void getComments(String url, Listener<String> listener) {
		StringRequest request = new StringRequest(url, listener, null);
		queue.add(request);
	}

	/**
	 * 通过Volley的ImageLoader在指定的ImageView中显示网络图片
	 * 
	 * @param url
	 *            要显示图片的网络地址
	 * @param imageView
	 *            显示图片的ImageView
	 */
	public static void displayImage(String url, ImageView imageView) {
		ImageListener listener = ImageLoader.getImageListener(imageView,
				R.drawable.my_default, R.drawable.bucket_no_picture);
		imageLoader.get(url, listener);
	}

	public static void getRegions(String city, Listener<String> listener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("city", city);
		StringRequest request = new StringRequest(
				getUrl("http://api.dianping.com/v1/metadata/get_regions_with_businesses",
						params), listener, null);
		queue.add(request);
	}

	/**
	 * 得到所以的地名
	 * 
	 * @param listener
	 */
	public static void getCity(Listener<String> listener) {
		StringRequest request = new StringRequest(
				getUrl("http://api.dianping.com/v1/metadata/get_cities_with_businesses",
						new HashMap<String, String>()), listener, null);
		queue.add(request);
	}

	/**
	 * 得到所以团购信息
	 * 
	 * @param city
	 *            城市
	 * @param listener
	 */
	@SuppressLint("SimpleDateFormat")
	public static void getTuanInfo(String city, final Listener<String> listener) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("city", "北京");
		String timeFormat = new SimpleDateFormat("yyyy-MM-dd")
				.format(new Date());
		params.put("date", timeFormat);
		// 获取每日新增的团购ID列表
		StringRequest request = new StringRequest(
				getUrl("http://api.dianping.com/v1/deal/get_daily_new_id_list",
						params), new Listener<String>() {
					@Override
					public void onResponse(String arg0) {
						// arg0代表每日新增团购ID的JSON列表
						try {
							JSONObject jsonObject = new JSONObject(arg0);
							JSONArray jsonArray = jsonObject
									.getJSONArray("id_list");
							StringBuilder sb = new StringBuilder();
							int count = jsonArray.length();
							if (count > 40) {
								count = 40;
							}
							for (int i = 0; i < count; i++) {
								sb.append(jsonArray.getString(i)).append(",");
							}
							String string = sb.deleteCharAt(sb.length() - 1)
									.toString();
							Map<String, String> params2 = new HashMap<String, String>();
							params2.put("deal_ids", string);
							StringRequest request2 = new StringRequest(
									getUrl("http://api.dianping.com/v1/deal/get_batch_deals_by_id",
											params2), listener, null);
							queue.add(request2);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, null);
		queue.add(request);
	}

	/**
	 * 根据访问参数获得大众点评服务器返回的商户信息
	 * 
	 * @param city
	 *            城市名称
	 * @param region
	 *            城市区域名,如传入城市区域名，则城市名称必须传入,可以传入null
	 * @param category
	 *            分类名,支持同时输入多个分类，以逗号分隔，最大不超过5个。
	 * @param listener
	 *            监听器,当服务器返回成功时,会被调用
	 */
	public static void getBusines(String city, String region, String category,
			Listener<String> listener) {

		Map<String, String> params = new HashMap<String, String>();
		params.put("city", city);
		if (region != null) {// 区域名是可选的
			params.put("region", region);
		}
		params.put("category", category);// 在前面拆分一下字符串,看看有几个,保证数据访问不会出错
		StringRequest request = new StringRequest(getUrl(
				"http://api.dianping.com/v1/business/find_businesses", params),
				listener, null);
		queue.add(request);
	}

	public static String getUrl(String url, Map<String, String> params) {
		String result = "";
		String sign = getSign(APPKEY, APP_SECRET, params);
		String query = getQuery(APPKEY, sign, params);
		result = url + "?" + query;
		return result;
	}

	private static String getQuery(String appKey, String sign,
			Map<String, String> params) {
		try {
			// 添加签名
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("appkey=").append(appKey).append("&sign=")
					.append(sign);
			for (Entry<String, String> entry : params.entrySet()) {
				stringBuilder.append('&').append(entry.getKey()).append('=')
						.append(URLEncoder.encode(entry.getValue(), "UTF-8"));// 同时UTF-8,将中文转码,
			}
			String queryString = stringBuilder.toString();
			return queryString;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("不正确的解码格式");
			// 1.return null下一个方法报错
			// 2.throw new RuntimeException("出现异常");比第一个好,提前结束程序
			// 3.return "xxxx默认地址xxxx";即使程序出错,但不影响程序运行
		}
	}

	private static String getSign(String appKey, String appSecret,
			Map<String, String> params) {
		StringBuilder stringBuilder = new StringBuilder();
		// 对参数名进行字典排序
		String[] keyArray = params.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);
		// 拼接有序的参数名-值串
		stringBuilder.append(appKey);
		for (String key : keyArray) {
			stringBuilder.append(key).append(params.get(key));
		}
		String codes = stringBuilder.append(appSecret).toString();
		String sign = new String(Hex.encodeHex(DigestUtils.sha(codes)))// 生成MD5
				.toUpperCase();// 全部转大写
		return sign;
	}
}
