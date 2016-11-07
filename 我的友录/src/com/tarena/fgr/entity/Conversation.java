package com.tarena.fgr.entity;

/**
 * 封装会话数据 短信记录
 * 
 * @author 冯国芮
 * 
 */
public class Conversation {
	private int thread_id;// 会话的编号
	private String body;// 会话的内容
	private String phone;// 电话号码
	private String name;// 联系人姓名
	private long date;// 时间
	private String formatdate;// 格式化时间
	private int photoid;// 头像id
	private int read;// 是否已经阅读

	public int getThread_id() {
		return thread_id;
	}

	public void setThread_id(int thread_id) {
		this.thread_id = thread_id;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getFormatdate() {
		return formatdate;
	}

	public void setFormatdate(String formatdate) {
		this.formatdate = formatdate;
	}

	public int getPhotoid() {
		return photoid;
	}

	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public Conversation(int thread_id, String body, String phone, String name,
			long date, String formatdate, int photoid, int read) {
		super();
		this.thread_id = thread_id;
		this.body = body;
		this.phone = phone;
		this.name = name;
		this.date = date;
		this.formatdate = formatdate;
		this.photoid = photoid;
		this.read = read;
	}

	public Conversation() {
		super();
	}

	@Override
	public String toString() {
		return "Conversation [thread_id=" + thread_id + ", body=" + body
				+ ", phone=" + phone + ", name=" + name + ", date=" + date
				+ ", formatdate=" + formatdate + ", photoid=" + photoid
				+ ", read=" + read + "]";
	}

}
