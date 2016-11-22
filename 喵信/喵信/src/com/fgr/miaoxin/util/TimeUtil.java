package com.fgr.miaoxin.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;

public class TimeUtil {

	/**
	 * 根据传入的时间值，得到一个具有描述性的字符串 </br>时间戳代表的时间为今天14:35 ---> 14:35</br>
	 * 时间戳代表的时间为昨天14:35 ---> 昨天14:35 </br>时间戳代表的时间为前天14:35 ---> 前天14:35
	 * </br>时间戳代表的时间为更久以前的时间 ---> 2016/XX/XX 14:35
	 * 
	 * @param timeStamp
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getTime(long timeStamp) {
		String result = "";
		// 当前时间时间戳
		long now = System.currentTimeMillis();

		// 计算当前时间与timeStamp之间查了多少天
		// 如果采用如下方式，得到的是now与timeStamp相差了多少个24小时
		// int day = (int) ((now - timeStamp)/1000/60/60/24);
		// 采用如下方式，得到的是now与timeStamp相差了多少天
		int day = (int) (now / 1000 / 60 / 60 / 24 - timeStamp / 1000 / 60 / 60
				/ 24);

		switch (day) {
		case 0:
			// timeStamp转成“时:分”
			result = new SimpleDateFormat("HH:mm").format(timeStamp);
			break;
		case 1:
			// timeStamp转成“昨天 时:分”
			result = "昨天 " + new SimpleDateFormat("HH:mm").format(timeStamp);
			break;
		case 2:
			// timeStamp转成“前天 时:分”
			result = "前天 " + new SimpleDateFormat("HH:mm").format(timeStamp);
			break;
		default:
			// timeStamp转成“xxxx/xx/xx 时:分”
			result = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(timeStamp);
			break;
		}

		return result;
	}

}
