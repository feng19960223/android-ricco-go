package com.tarena.fgr.entity;

/**
 * 封装联系人的信息的实体类
 * 
 * @author 冯国芮 2016年9月30日 09:19:32
 * 
 */
public class Contact {
	private int id;// 联系人的编号
	private String name;// 姓名
	private String phone;// 电话号码
	private String address;// 通讯地址
	private String email;// 电子邮箱
	private int photoid;// 联系人的头像编号

	// 数据库中可以通过编号,查找到图片
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

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPhotoid() {
		return photoid;
	}

	public void setPhotoid(int photoid) {
		this.photoid = photoid;
	}

	/**
	 * @param id联系人的编号
	 * @param name姓名
	 * @param phone电话号码
	 * @param address通讯地址
	 * @param email电子邮箱
	 * @param photoid联系人的头像编号
	 */
	public Contact(int id, String name, String phone, String address,
			String email, int photoid) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.address = address;
		this.email = email;
		this.photoid = photoid;
	}

	public Contact() {
		super();
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", name=" + name + ", phone=" + phone
				+ ", address=" + address + ", email=" + email + ", photoid="
				+ photoid + "]";
	}

}
