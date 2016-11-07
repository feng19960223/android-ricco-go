package com.tarena.fgr.entity;

public class BlackPhone {
	private int _id;
	private String number;
	private int type;
	private long date;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public BlackPhone(int _id, String number, int type, long date) {
		super();
		this._id = _id;
		this.number = number;
		this.type = type;
		this.date = date;
	}

	public BlackPhone() {
		super();
	}

	@Override
	public String toString() {
		return "BlackPhone [_id=" + _id + ", number=" + number + ", type="
				+ type + ", date=" + date + "]";
	}

}
