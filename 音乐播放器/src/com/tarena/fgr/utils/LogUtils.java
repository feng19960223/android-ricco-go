package com.tarena.fgr.utils;

import android.util.Log;

/**
 * 只需要修改LEVEL的常量的值，就可以自由地控制日志的打印行为了。 比如让LEVEL等于VERBOSE就可以把所有的日志都打印出来，
 * 让LEVEL等于WARN就可以把所以日志都屏蔽掉。
 * 
 * 在开发阶段将LEVEL指定成VERBOSE ，当项目正式上线的时候将LEVEL指定成NOTHING就可以了。 </br>2016年10月13日
 * 10:35:47 更新 采用true和flase修改,增加多种输出
 * 
 * @author anzhuo
 * 
 */
public class LogUtils {
	// public static final int VERBOSE = 1;
	// public static final int DEBUG = 2;
	// public static final int INFO = 3;
	// public static final int WARN = 4;
	// public static final int ERROR = 5;
	// public static final int NOTHING = 6;
	// public static int LEVEL = VERBOSE;// 日志的等级控制属性，通过改变值，进行日志输出控制

	public static boolean isDebug = true;// 是否需要打印bug，可以在application的onCreate函数里面初始化
	private static final String TAG = "-------------->"; // 默认的Tag

	private LogUtils() {
		/* cannot be instantiated,不能被实例化 */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	// 下面五个是默认tag的函数
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	public static void w(String msg) {
		// if (LEVEL <= WARN) {
		if (isDebug) {
			Log.w(TAG, msg);
		}
	}

	// 下面是传入自定义tag的函数
	public static void v(String tag, String msg) {
		// if (LEVEL <= VERBOSE) {
		if (isDebug) {
			Log.v(tag, msg);
		}
	}

	public static void d(String tag, String msg) {
		// if (LEVEL <= DEBUG) {
		if (isDebug) {
			Log.d(tag, msg);
		}
	}

	public static void i(String tag, String msg) {
		// if (LEVEL <= INFO) {
		if (isDebug) {
			Log.i(tag, msg);
		}
	}

	public static void e(String tag, String msg) {
		// if (LEVEL <= ERROR) {
		if (isDebug) {
			Log.e(tag, msg);
		}
	}
}
