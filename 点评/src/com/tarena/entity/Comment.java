package com.tarena.entity;

import java.util.Arrays;

/**
 * 评论实体类
 * 
 * @author 冯国芮
 * 
 */
public class Comment {
	private String avatar;// 头像
	private String username;// 用户名
	private String rating;// 打分
	private String avgPrice;// 人均价格,(并不是所以网友评论时都提供了价格)
	private String content;// 评论正文
	private String[] imgs;// 评论配图(并不是所以网页提供了配图,如果提供了配图最多取三幅)

	public Comment(String avatar, String username, String rating,
			String avgPrice, String content, String[] imgs) {
		super();
		this.avatar = avatar;
		this.username = username;
		this.rating = rating;
		this.avgPrice = avgPrice;
		this.content = content;
		this.imgs = imgs;
	}

	public Comment() {
		super();
	}

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

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(String avgPrice) {
		this.avgPrice = avgPrice;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getImgs() {
		return imgs;
	}

	public void setImgs(String[] imgs) {
		this.imgs = imgs;
	}

	@Override
	public String toString() {
		return "Comment [avatar=" + avatar + ", username=" + username
				+ ", rating=" + rating + ", avgPrice=" + avgPrice
				+ ", content=" + content + ", imgs=" + Arrays.toString(imgs)
				+ "]";
	}

}
