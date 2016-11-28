package com.fgr.miaoxin.bean;

import cn.bmob.v3.BmobObject;

public class Blog extends BmobObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyUser author;// blog的作者
	String content;// blog的文本内容
	// blog配图的地址
	// 多幅配图地址之间用"&"分隔
	String imgUrls;
	Integer love;// 被点赞的数量

	public MyUser getAuthor() {
		return author;
	}

	public void setAuthor(MyUser author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImgUrls() {
		return imgUrls;
	}

	public void setImgUrls(String imgUrls) {
		this.imgUrls = imgUrls;
	}

	public Integer getLove() {
		return love;
	}

	public void setLove(Integer love) {
		this.love = love;
	}

}
