package com.fgr.miaoxin.util;

import java.util.Locale;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {

	/**
	 * 将参数中的文字转为拼音格式
	 * 
	 * 老王 -- LAOWANG
	 * 
	 * 老王a -- LAOWANGA
	 * 
	 * 老王8 -- LAOWANG8
	 * 
	 * 8老王 -- #LAOWANG
	 * 
	 * 
	 * @param string
	 * @return
	 */
	public static String getPinYin(String string) {
		try {
			String result = "";
			// 1)设定拼音格式
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			// 2)按照设定好的格式，逐字进行转换
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < string.length(); i++) {
				// 判断第i个位置上的字是不是中文
				// "京"
				String s = string.substring(i, i + 1);
				if (s.matches("[\u4e00-\u9fff]")) {
					// 如果是中文
					char c = string.charAt(i);
					String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c,
							format);
					sb.append(pinyin[0]);
				} else if (s.matches("[a-zA-Z]")) {
					// 如果不是中文是英文字母
					sb.append(s.toUpperCase(Locale.US));
				} else {
					// 即不是中文又不是英文字母
					if (i == 0) {
						sb.append("#");
					} else {
						sb.append(s);
					}
				}
			}

			result = sb.toString();

			return result;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			throw new RuntimeException("拼音格式异常");
		}
	}

	public static char getFirstLetter(String string) {
		return getPinYin(string).charAt(0);
	}

}
