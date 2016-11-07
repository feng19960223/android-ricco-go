package com.tarena.fgr.entity;

public class BlackSMS {
	private int _id;
	private String number;
	private String body;
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

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public BlackSMS(int _id, String number, String body, long date) {
		super();
		this._id = _id;
		this.number = number;
		this.body = body;
		this.date = date;
	}

	public BlackSMS() {
		super();
	}

	@Override
	public String toString() {
		return "BlackSMS [_id=" + _id + ", number=" + number + ", body=" + body
				+ ", date=" + date + "]";
	}

}
