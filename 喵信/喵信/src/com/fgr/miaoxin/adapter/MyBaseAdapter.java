package com.fgr.miaoxin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fgr.miaoxin.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	private List<T> datasource = new ArrayList<T>();
	protected Context context = null;
	protected LayoutInflater layoutInflater = null;

	public MyBaseAdapter(Context context, List<T> datasource) {
		super();
		this.context = context;
		this.datasource = datasource;
		this.layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public MyBaseAdapter(Context context) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);// from内部代码和上面一样
	}

	@Override
	/** 返回数据集中item数据的个数 */
	public int getCount() {
		return datasource.size();
	}

	@Override
	/** 返回对应位置的item中的数据 */
	public T getItem(int position) {
		return datasource.get(position);
	}

	@Override
	/** 暂且直接返回position */
	public long getItemId(int position) {
		return position;
	}

	@Override
	/** 此方法用于构建item,每显示一个item都要执行一次此方法 */
	// 只有继承此类,就要重写此方法
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

	// 添加数据
	/**
	 * @param t添加一个数据
	 */
	public void addItem(T t) {
		if (t != null) {
			datasource.add(t);
		}
		notifyDataSetChanged();// 通知更新数据
	}

	/**
	 * 如果isClean为true,需要清除原来的数据
	 * 
	 * @param ts
	 *            添加多个数据
	 * @param isClean
	 *            是否需要将原来的数据清除掉
	 */
	public void addItems(List<T> ts, boolean isClear) {
		if (isClear) {
			datasource.clear();
		}
		datasource.addAll(ts);
		notifyDataSetChanged();
	}

	/**
	 * 删除一条数据
	 * 
	 * @param t要删除的数据
	 */
	public void removeData(T t) {
		if (t != null) {
			datasource.remove(t);
		}
		notifyDataSetChanged();
	}

	/**
	 * 清除所以数据
	 */
	public void clear() {
		datasource.clear();
		notifyDataSetChanged();
	}

	/**
	 * 获得所以的数据
	 * 
	 * @return list
	 */
	public List<T> getDatas() {
		return this.datasource;
	}

	/**
	 * 设置图片
	 * 
	 * @param url
	 * @param iv
	 */
	public void setAvatar(String url, ImageView iv) {
		if (TextUtils.isEmpty(url)) {
			iv.setImageResource(R.drawable.ic_launcher);
		} else {
			ImageLoader.getInstance().displayImage(url, iv);
		}
	}

	// 自定义的字体
	Typeface Fonts;

	public void stetFontType(Context context, TextView textView) {
		if (Fonts == null) {
			AssetManager manager = context.getAssets();
			String path = "fonts/customfont.ttf";// 自定义的字体
			Fonts = Typeface.createFromAsset(manager, path);
		}
		// 将字体应用到控件上
		textView.setTypeface(Fonts);
	}
}
