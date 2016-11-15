package com.fgr.miaoxin.bean;

import cn.bmob.im.bean.BmobChatUser;
import cn.bmob.v3.datatype.BmobGeoPoint;

public class MyUser extends BmobChatUser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 性别，位置，拼音名称和拼音首字母
	Boolean gender;// true 男生 false 女生
	BmobGeoPoint location;// 用户位置
	String pyName;// username属性的拼音格式
	Character letter;// 拼音名字的首字母

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

	public BmobGeoPoint getLocation() {
		return location;
	}

	public void setLocation(BmobGeoPoint location) {
		this.location = location;
	}

	public String getPyName() {
		return pyName;
	}

	public void setPyName(String pyName) {
		this.pyName = pyName;
	}

	public Character getLetter() {
		return letter;
	}

	public void setLetter(Character letter) {
		this.letter = letter;
	}

	@Override
	public String toString() {
		return "MyUser [gender=" + gender + ", location=" + location
				+ ", pyName=" + pyName + ", letter=" + letter + "]";
	}

}
