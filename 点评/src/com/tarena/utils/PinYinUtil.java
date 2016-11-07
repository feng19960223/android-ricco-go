package com.tarena.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinYinUtil {
	/**
	 * 返回首字母
	 * 
	 * @param string
	 * @return
	 */
	public static char getFirstLetter(String string) {
		return getPinYin(string).charAt(0);
	}

	/**
	 * 将参数中的文字转为拼音格式 <br>
	 * 例如"北京"-->"BEIJING"
	 * 
	 * @param string
	 * @return
	 */
	public static String getPinYin(String string) {
		try {
			String result = "";
			// 1.设置拼音转换格式
			// 2.按照设定好的格式,逐字进行转换
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setCaseType(HanyuPinyinCaseType.UPPERCASE);// 大写
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有语音语调
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < string.length(); i++) {
				// 判断第i个位置上的字是否是中文,有的地名,可能有特殊字符
				String s = string.substring(i, i + 1);
				if (s.matches("[\u4e00-\u9fff]")) {// 中文编码在期中
					char c = string.charAt(i);
					String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c,
							format);// 为什么返回数组?多音字
					// '重'-->[ZHONG,CHONG]//重庆又肯能被放到最后
					sb.append(pinyin[0]);
				} else {
					sb.append(s);
				}
			}
			result = sb.toString();
			return result;
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
			throw new RuntimeException("拼音格式异常");
		}
	}
}
