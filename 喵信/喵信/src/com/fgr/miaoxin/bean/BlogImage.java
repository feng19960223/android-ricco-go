package com.fgr.miaoxin.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class BlogImage {
	@DatabaseField(id = true)
	String imgUrl;
	@DatabaseField
	String bitmap;// BitmapÍ¼ÐÎ--´òÉ¢-->byte[]--BASE64±àÂë-->String

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getBitmap() {
		return bitmap;
	}

	public void setBitmap(String bitmap) {
		this.bitmap = bitmap;
	}

}
