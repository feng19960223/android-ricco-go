package com.tarena.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tarena.entity.TuanGou.Deals;
import com.tarena.groupon.R;
import com.tarena.utils.HttpUtil;

public class DealsAdapter extends MyBaseAdapter<Deals> {

	public DealsAdapter(Context context, List<Deals> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Deals Deals = getItem(position);

		ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.main_item_layout, parent,
					false);
			vh = new ViewHolder();
			vh.imageView1 = (ImageView) convertView
					.findViewById(R.id.imageView1);
			vh.imageView2 = (ImageView) convertView
					.findViewById(R.id.imageView2);
			vh.imageView3 = (ImageView) convertView
					.findViewById(R.id.imageView3);
			vh.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			vh.textView2 = (TextView) convertView.findViewById(R.id.textView2);
			vh.textView3 = (TextView) convertView.findViewById(R.id.textView3);
			vh.textView4 = (TextView) convertView.findViewById(R.id.textView4);
			vh.textView5 = (TextView) convertView.findViewById(R.id.textView5);
			vh.textView6 = (TextView) convertView.findViewById(R.id.textView6);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		String url = Deals.getS_image_url();
		HttpUtil.displayImage(url, vh.imageView1);
		if (Deals.getRestrictions().getIs_reservation_required() == 1) {// 是免预约
			vh.imageView2.setVisibility(View.VISIBLE);
		} else {
			vh.imageView2.setVisibility(View.INVISIBLE);
		}
		if (Deals.getIs_popular() == 1) {// 是热门
			vh.imageView3.setVisibility(View.VISIBLE);
		} else {
			vh.imageView3.setVisibility(View.INVISIBLE);
		}
		vh.textView1.setText(Deals.getTitle());
		vh.textView2.setText(Deals.getDescription());
		// TODO
		vh.textView3.setText((new Random().nextInt(900) + 100) + "米");
		vh.textView4.setText("￥" + Deals.getCurrent_price() + "");
		vh.textView5.setText("已售 " + (new Random().nextInt(2000) + 500));
		if (TextUtils.isEmpty(Deals.getBusinesses().get(0).getAddress())) {
			vh.textView6.setVisibility(View.GONE);
		} else {
			vh.textView6.setText(Deals.getBusinesses().get(0).getName());
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView1, imageView2, imageView3;
		TextView textView1, textView2, textView3, textView4, textView5,
				textView6;
	}
}
