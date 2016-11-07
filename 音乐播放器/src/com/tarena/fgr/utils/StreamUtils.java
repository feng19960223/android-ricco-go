package com.tarena.fgr.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class StreamUtils {
	public static byte[] streamToByteArray(InputStream is) {
		byte[] data = null;
		ByteArrayOutputStream os = null;
		try {
			os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			while (is.read(buffer, 0, buffer.length) != -1) {
				os.write(buffer, 0, buffer.length);
			}
			data = os.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return data;
	}

	/**
	 * ·µ»Øjson×Ö·û´®
	 * 
	 * @param is
	 * @return
	 */
	public static String streamToString(InputStream is) {
		BufferedReader reader = null;
		StringBuilder builder = null;
		try {
			reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
			builder = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return builder.toString();// JSONµÄ×Ö·û´®
	}
}
