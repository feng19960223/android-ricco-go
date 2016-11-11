package com.fgr.bmobdemo.bean;

import cn.bmob.v3.BmobObject;

public class MyUser extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String avatar;// 用户头像图片在服务器上的地址
	private String username;
	private String password;
	// MyUser对象的内容要保存到Bmob服务器的数据表中
	// Bmob服务器数据表不支持Java的8种基本数据类型
	// 使用时要将基本数据类型转为包装类型
	private Boolean gender;// true 男,false 女

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getGender() {
		return gender;
	}

	public void setGender(Boolean gender) {
		this.gender = gender;
	}

}
