package com.tarena.fgr.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.tarena.fgr.music.R;

public class DownLoadMannger {
	// 下载前发一个通知给用户
	// 下载过程中发通知给用户(显示文件当前下载的百分比)
	// 下载完成发通知给用户下载完毕
	/**
	 * @param context
	 * @param ticker
	 *            状态栏内容
	 * @param title
	 *            标题
	 * @param content
	 *            内容
	 */
	public static void sendNotification(Context context, String ticker,
			String title, String content) {
		// 获得通知管理的系统服务
		NotificationManager manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// 创建通知的构建器
		Notification.Builder builder = new Notification.Builder(context);
		builder.setTicker(ticker);
		builder.setContentTitle(title);
		builder.setContentText(content);
		builder.setSmallIcon(R.drawable.ic_launcher);
		Notification notification = builder.build();
		manager.notify(100, notification);
	}

	public static void downloadSong(final Context context, String url,
			final String name) {
		String downloadDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
		final File file = new File(downloadDir + File.separatorChar + name);
		// 使用异步任务类实现下载
		new AsyncTask<String, Void, File>() {
			protected void onPreExecute() {
				// 在执行异步处理之前先发一个通知,要执行文件下载了
				sendNotification(context, "准备下载" + name, "准备下载", "准备中");
			};

			@Override
			protected File doInBackground(String... params) {
				String path = params[0];
				URL url;
				try {
					url = new URL(path);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setDoInput(true);
					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						InputStream is = connection.getInputStream();
						BufferedInputStream bufferedInputStream = new BufferedInputStream(
								is);
						byte[] buffer = new byte[1024 * 8];
						OutputStream os = new FileOutputStream(file);
						BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
								os);
						int downloadCount = 0;// 当前已经下载的数据的长度
						int len = 0;// 每次下载的数据的长度
						int connentLength = connection.getContentLength();// 要下载文件的总大小
						while ((len = bufferedInputStream.read(buffer, 0,
								buffer.length)) != -1) {
							downloadCount += len;
							sendNotification(context, "下载中", "已经下载了:",
									downloadCount / 1000 + "/" + connentLength
											/ 1000 + "  KB");
							// 把读到的数据写到指定位置
							bufferedOutputStream.write(buffer);
						}
						bufferedOutputStream.flush();
						bufferedInputStream.close();
						bufferedOutputStream.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return file;
			}

			protected void onPostExecute(File result) {
				sendNotification(context, "下载完毕", result.getName(), "下载完毕");
			};

		}.execute(url);
	}

}
