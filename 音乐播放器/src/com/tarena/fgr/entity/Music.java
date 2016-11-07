package com.tarena.fgr.entity;

import java.io.Serializable;

/**
 * 音乐实体类
 * 
 * @author 冯国芮
 * 
 */
public class Music implements Serializable {// Parcelable
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = 1L;
	private int id;// 音乐的编号
	private String name;// 歌曲的名称
	private String album;// 专辑的名称
	private String albumpic;// 专辑图片的路径
	private String author;// 作词
	private String composer;// 作曲
	private String downcount;// 下载次数
	private String durationtime;// 播放时长
	private String favcount;// 点赞
	private String musicpath;// 音乐文件路径
	private String singer;// 演唱者名称

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

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getAlbumpic() {
		return albumpic;
	}

	public void setAlbumpic(String albumpic) {
		this.albumpic = albumpic;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getDowncount() {
		return downcount;
	}

	public void setDowncount(String downcount) {
		this.downcount = downcount;
	}

	public String getDurationtime() {
		return durationtime;
	}

	public void setDurationtime(String durationtime) {
		this.durationtime = durationtime;
	}

	public String getFavcount() {
		return favcount;
	}

	public void setFavcount(String favcount) {
		this.favcount = favcount;
	}

	public String getMusicpath() {
		return musicpath;
	}

	public void setMusicpath(String musicpath) {
		this.musicpath = musicpath;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public Music(int id, String name, String album, String albumpic,
			String author, String composer, String downcount,
			String durationtime, String favcount, String musicpath,
			String singer) {
		super();
		this.id = id;
		this.name = name;
		this.album = album;
		this.albumpic = albumpic;
		this.author = author;
		this.composer = composer;
		this.downcount = downcount;
		this.durationtime = durationtime;
		this.favcount = favcount;
		this.musicpath = musicpath;
		this.singer = singer;
	}

	public Music() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((album == null) ? 0 : album.hashCode());
		result = prime * result
				+ ((albumpic == null) ? 0 : albumpic.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((composer == null) ? 0 : composer.hashCode());
		result = prime * result
				+ ((downcount == null) ? 0 : downcount.hashCode());
		result = prime * result
				+ ((durationtime == null) ? 0 : durationtime.hashCode());
		result = prime * result
				+ ((favcount == null) ? 0 : favcount.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((musicpath == null) ? 0 : musicpath.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((singer == null) ? 0 : singer.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Music other = (Music) obj;
		if (album == null) {
			if (other.album != null)
				return false;
		} else if (!album.equals(other.album))
			return false;
		if (albumpic == null) {
			if (other.albumpic != null)
				return false;
		} else if (!albumpic.equals(other.albumpic))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (composer == null) {
			if (other.composer != null)
				return false;
		} else if (!composer.equals(other.composer))
			return false;
		if (downcount == null) {
			if (other.downcount != null)
				return false;
		} else if (!downcount.equals(other.downcount))
			return false;
		if (durationtime == null) {
			if (other.durationtime != null)
				return false;
		} else if (!durationtime.equals(other.durationtime))
			return false;
		if (favcount == null) {
			if (other.favcount != null)
				return false;
		} else if (!favcount.equals(other.favcount))
			return false;
		if (id != other.id)
			return false;
		if (musicpath == null) {
			if (other.musicpath != null)
				return false;
		} else if (!musicpath.equals(other.musicpath))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (singer == null) {
			if (other.singer != null)
				return false;
		} else if (!singer.equals(other.singer))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Music [id=" + id + ", name=" + name + ", album=" + album
				+ ", albumpic=" + albumpic + ", author=" + author
				+ ", composer=" + composer + ", downcount=" + downcount
				+ ", durationtime=" + durationtime + ", favcount=" + favcount
				+ ", musicpath=" + musicpath + ", singer=" + singer + "]";
	}

	// @Override
	// public int describeContents() {
	// return 0;
	// }
	//
	// @Override
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeInt(id);
	// dest.writeString(name);
	// dest.writeString(album);
	// dest.writeString(albumpic);
	// dest.writeString(author);
	// dest.writeString(composer);
	// dest.writeString(downcount);
	// dest.writeString(durationtime);
	// dest.writeString(durationtime);
	// dest.writeString(favcount);
	// dest.writeString(musicpath);
	// dest.writeString(musicpath);
	// dest.writeString(singer);
	// }
	//
	// 进入Bundle类,查询到次方法,实现反序列化
	// public static final Parcelable.Creator<Music> CREATOR = new
	// Parcelable.Creator<Music>() {
	// public Music createFromParcel(Parcel in) {
	// return new Music(in.readInt(), in.readString(), in.readString(),
	// in.readString(), in.readString(), in.readString(),
	// in.readString(), in.readString(), in.readString(),
	// in.readString(), in.readString());
	// }
	//
	// public Music[] newArray(int size) {
	// return new Music[size];
	// }
	// };

}
