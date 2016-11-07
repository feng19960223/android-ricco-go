package com.tarena.fgr.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author 冯国芮 2016年9月30日 14:08:04
 * 
 * @param <T>
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	private List<T> datas = new ArrayList<T>();
	protected Context context = null;
	protected LayoutInflater layoutInflater = null;

	public MyBaseAdapter(Context context) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	// 添加数据
	/**
	 * @param t添加一个数据
	 */
	public void addItem(T t) {
		if (t != null) {
			datas.add(t);
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
	public void addItems(List<T> ts, boolean isClean) {
		if (isClean) {
			datas.clear();
		}
		datas.addAll(ts);
		notifyDataSetChanged();
	}

	/**
	 * 删除一条数据
	 * 
	 * @param t要删除的数据
	 */
	public void removeData(T t) {
		if (t != null) {
			datas.remove(t);
		}
		notifyDataSetChanged();
	}

	/**
	 * 获得所以的数据
	 * 
	 * @return list
	 */
	public List<T> getDatas() {
		return this.datas;
	}

	@Override
	/** 返回数据集中item数据的个数 */
	public int getCount() {
		return datas.size();
	}

	@Override
	/** 返回对应位置的item中的数据 */
	public T getItem(int position) {
		return datas.get(position);
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

}
