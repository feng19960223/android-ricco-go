package com.fgr.bmobdemo.bean;

import cn.bmob.v3.BmobObject;

public class MyPost extends BmobObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String content;
	private MyUser user;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}

}
