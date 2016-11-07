package com.tarena.fgr.entity;

/**
 * 封装通话记录的实体类
 * 
 * @author 冯国芮
 * 
 */
public class Calllog {

	private int id;// 通话记录的编号
	private String name;// 联系人的姓名
	private int type;// 通话的类型
	private String phone;// 电话号码
	private int photoid;// 头像id
	private long callTime;// 时间戳,数据库里存储的是1970/1/1--当前的时间的毫秒值;
	private String formatCallTimeString;// 格式化后的时间

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getPhotoid() {
		return photoid;
	}

	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}

	public long getCallTime() {
		return callTime;
	}

	public void setCallTime(long callTime) {
		this.callTime = callTime;
	}

	public String getFormatCallTimeString() {
		return formatCallTimeString;
	}

	public void setFormatCallTimeString(String formatCallTimeString) {
		this.formatCallTimeString = formatCallTimeString;
	}

	/**
	 * @param id
	 *            通话记录的编号
	 * @param name
	 *            联系人的姓名
	 * @param type
	 *            通话的类型
	 * @param phone
	 *            电话号码
	 * @param photoid
	 *            头像id
	 * @param callTime
	 *            时间戳 数据库里存储的是1970/1/1--当前的时间的毫秒值;
	 * @param formatCallTimeString
	 *            格式化后的时间
	 */
	public Calllog(int id, String name, int type, String phone, int photoid,
			long callTime, String formatCallTimeString) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.phone = phone;
		this.photoid = photoid;
		this.callTime = callTime;
		this.formatCallTimeString = formatCallTimeString;
	}

	public Calllog() {
		super();
	}

	@Override
	public String toString() {
		return "Calllog [id=" + id + ", name=" + name + ", type=" + type
				+ ", phone=" + phone + ", photoid=" + photoid + ", callTime="
				+ callTime + ", formatCallTimeString=" + formatCallTimeString
				+ "]";
	}

}
