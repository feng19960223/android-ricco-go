package com.tarena.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.tarena.entity.CityNameBean;
import com.tarena.groupon.R;

public class MyCityAdapter extends MyBaseAdapter<CityNameBean> implements
		SectionIndexer {

	public MyCityAdapter(Context context, List<CityNameBean> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.city_item_layout, parent,
					false);
			vh = new ViewHolder();
			vh.textView_city_pingying = (TextView) convertView
					.findViewById(R.id.textView_city_pingying);
			vh.textView_city_name = (TextView) convertView
					.findViewById(R.id.textView_city_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		CityNameBean cb = datasource.get(position);
		vh.textView_city_pingying.setText("" + cb.getLetter());
		vh.textView_city_name.setText(cb.getCityName());
		// 隐藏多余重复的字母
		if (getPositionForSection(getSectionForPosition(position)) == position) {
			vh.textView_city_pingying.setVisibility(View.VISIBLE);
		} else {
			vh.textView_city_pingying.setVisibility(View.GONE);
		}
		return convertView;
	}

	private class ViewHolder {
		TextView textView_city_pingying, textView_city_name;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			if (getItem(i).getLetter() == section) {
				return i;
			}
		}
		// return -1;//返回-1,可能是热门
		return -999;// 给一个不可能的值//uv没有分组
	}

	@Override
	public int getSectionForPosition(int position) {
		return getItem(position).getLetter();
	}

	@Override
	public Object[] getSections() {
		return null;
	}

}
