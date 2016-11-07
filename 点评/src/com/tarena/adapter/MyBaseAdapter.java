package com.tarena.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

public abstract class MyBaseAdapter<T> extends BaseAdapter {

	Context context;
	LayoutInflater inflater;
	List<T> datasource;

	public MyBaseAdapter(Context context, List<T> datasource) {
		super();
		this.context = context;
		this.datasource = datasource;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return datasource.size();
	}

	@Override
	public T getItem(int position) {
		return datasource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void addAll(List<T> list, boolean isClear) {
		if (isClear) {
			datasource.clear();
		}
		datasource.addAll(list);
		notifyDataSetChanged();
	}

	public void add(T t) {
		datasource.add(t);
		notifyDataSetChanged();
	}

	public void remove(T t) {
		datasource.remove(t);
		notifyDataSetChanged();
	}

	public void clear() {
		datasource.clear();
		notifyDataSetChanged();
	}

}
