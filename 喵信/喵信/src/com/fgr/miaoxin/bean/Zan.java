package com.fgr.miaoxin.bean;

import cn.bmob.v3.BmobObject;

public class Zan extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String blogId;// 为那篇blog点的赞
	String userId;// 谁点的赞

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
