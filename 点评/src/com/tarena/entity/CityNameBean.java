package com.tarena.entity;

import com.j256.ormlite.field.DatabaseField;

public class CityNameBean {
	@DatabaseField(id = true)
	private String cityName;// 城市名
	@DatabaseField
	private String pyName;// 城市拼音,排序
	@DatabaseField
	private char letter;// 第一个拼音字符,首字母

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getPyName() {
		return pyName;
	}

	public void setPyName(String pyName) {
		this.pyName = pyName;
	}

	public char getLetter() {
		return letter;
	}

	public void setLetter(char letter) {
		this.letter = letter;
	}

	public CityNameBean(String cityName, String pyName, char letter) {
		super();
		this.cityName = cityName;
		this.pyName = pyName;
		this.letter = letter;
	}

	public CityNameBean() {
		super();
	}

	@Override
	public String toString() {
		return "CityNameBean [cityName=" + cityName + ", pyName=" + pyName
				+ ", letter=" + letter + "]";
	}

}
