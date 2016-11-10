package com.tarena.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.tarena.app.MyApp;
import com.tarena.entity.Business.Businesses;
import com.tarena.groupon.R;
import com.tarena.utils.HttpUtil;

public class BusinessAdapter extends MyBaseAdapter<Businesses> {
	int[] resIds = new int[] { R.drawable.star0, R.drawable.star10,
			R.drawable.star20, R.drawable.star30, R.drawable.star35,
			R.drawable.star40, R.drawable.star45, R.drawable.star50 };

	public BusinessAdapter(Context context, List<Businesses> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Businesses businesses = getItem(position);
		ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.business_item_layout,
					parent, false);
			vh = new ViewHolder();
			vh.imageView1 = (ImageView) convertView
					.findViewById(R.id.imageView1);
			vh.imageView2 = (ImageView) convertView
					.findViewById(R.id.imageView2);
			vh.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			vh.textView2 = (TextView) convertView.findViewById(R.id.textView2);
			vh.textView3 = (TextView) convertView.findViewById(R.id.textView3);
			vh.textView4 = (TextView) convertView.findViewById(R.id.textView4);
			vh.textView5 = (TextView) convertView.findViewById(R.id.textView5);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		String url = businesses.getS_photo_url();
		HttpUtil.displayImage(url, vh.imageView1);
		vh.imageView2.setImageResource(resIds[new Random()
				.nextInt(resIds.length)]);// 评分无法获取,随机
		String name = businesses.getName().substring(0,// 店名称
				businesses.getName().indexOf("("));
		String branchName = businesses.getBranch_name();// 分店名称
		if (!TextUtils.isEmpty(branchName)) {
			name += "(" + businesses.getBranch_name() + ")";
		}
		vh.textView1.setText(name);// 餐厅名称+分店名称
		vh.textView2.setText("￥" + (new Random().nextInt(200) + 50) + "/人");// 人均价格无法得到,随机
		List<String> regions = businesses.getRegions();
		StringBuilder sb = new StringBuilder();
		for (String string : regions) {
			sb.append(string).append("/");
		}
		vh.textView3.setText(sb.deleteCharAt(sb.length() - 1).toString());// 地点
		vh.textView4.setText(businesses.getCategories().get(0)); // 餐厅类型
		// TODO
		if (MyApp.lastpoint != null) {
			// 商户经纬度,来自大众点评
			double lat1 = businesses.getLatitude();
			double lng1 = businesses.getLongitude();
			// vh.textView5.setText(""+ DistanceUtil.getDistance(lat1, lng1,
			// lat2, lng2) + "米");
			// 商户来自大众点评,用户百度地图,所以有误差
			double d = Math.round(DistanceUtil.getDistance(new LatLng(lat1,
					lng1), MyApp.lastpoint) * 10000) / 10000;// 四舍五入
			vh.textView5.setText("" + d + "米");
		} else {
			vh.textView5.setText("未定位");
		}
		return convertView;
	}

	private class ViewHolder {
		ImageView imageView1, imageView2;// 图片,评分
		TextView textView1, textView2, textView3, textView4, textView5;// 名称,人均价格,地点,餐厅类型,距离
	}
}
