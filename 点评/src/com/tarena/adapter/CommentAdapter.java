package com.tarena.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tarena.entity.Comment;
import com.tarena.groupon.R;
import com.tarena.utils.HttpUtil;
import com.tarena.view.XCRoundImageView;

public class CommentAdapter extends MyBaseAdapter<Comment> {

	public CommentAdapter(Context context, List<Comment> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Comment comment = getItem(position);
		ViewHolder vh;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.include_detail_item,
					parent, false);
			vh = new ViewHolder();
			vh.xCRoundImageView1 = (XCRoundImageView) convertView
					.findViewById(R.id.xCRoundImageView1);
			vh.linearLayout1 = (LinearLayout) convertView
					.findViewById(R.id.linearLayout1);
			vh.imageView_rating = (ImageView) convertView
					.findViewById(R.id.imageView_rating);
			vh.imageView1 = (ImageView) convertView
					.findViewById(R.id.imageView1);
			vh.imageView2 = (ImageView) convertView
					.findViewById(R.id.imageView2);
			vh.imageView3 = (ImageView) convertView
					.findViewById(R.id.imageView3);
			vh.textView_name = (TextView) convertView
					.findViewById(R.id.textView_name);
			vh.textView_avgPrice = (TextView) convertView
					.findViewById(R.id.textView_avgPrice);
			vh.textView_content = (TextView) convertView
					.findViewById(R.id.textView_content);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		String XCurl = comment.getAvatar();
		HttpUtil.displayImage(XCurl, vh.xCRoundImageView1);
		vh.textView_name.setText(comment.getUsername());
		vh.textView_content.setText(comment.getContent());
		vh.textView_avgPrice.setText(getAvgPrice(comment.getAvgPrice()));
		vh.imageView_rating
				.setImageResource(resIds[getID(comment.getRating())]);
		if (comment.getImgs() != null && comment.getImgs().length > 0) {
			// 有图片
			int count = comment.getImgs().length;
			if (count == 1) {// 使用invisible
				vh.imageView2.setVisibility(View.INVISIBLE);
				vh.imageView3.setVisibility(View.INVISIBLE);
				HttpUtil.displayImage(comment.getImgs()[0], vh.imageView1);
			} else if (count == 2) {
				vh.imageView3.setVisibility(View.INVISIBLE);
				HttpUtil.displayImage(comment.getImgs()[0], vh.imageView1);
				HttpUtil.displayImage(comment.getImgs()[1], vh.imageView2);
			} else {
				HttpUtil.displayImage(comment.getImgs()[0], vh.imageView1);
				HttpUtil.displayImage(comment.getImgs()[1], vh.imageView2);
				HttpUtil.displayImage(comment.getImgs()[2], vh.imageView2);
			}
		} else {// 没图片,隐藏
			vh.linearLayout1.setVisibility(View.GONE);// 使用gone
		}
		return convertView;
	}

	int[] resIds = new int[] { R.drawable.star0, R.drawable.star10,
			R.drawable.star20, R.drawable.star30, R.drawable.star35,
			R.drawable.star40, R.drawable.star45, R.drawable.star50 };

	private int getID(String rating) {
		if ("star10".equals(rating)) {
			return 1;
		} else if ("star20".equals(rating)) {
			return 2;
		} else if ("star30".equals(rating)) {
			return 3;
		} else if ("star35".equals(rating)) {
			return 4;
		} else if ("star40".equals(rating)) {
			return 5;
		} else if ("star45".equals(rating)) {
			return 6;
		} else if ("star50".equals(rating)) {
			return 7;
		} else {
			return 0;
		}
	}

	private String getAvgPrice(String string) {
		if (TextUtils.isEmpty(string)) {
			return "";
		} else {
			return string + "/人";
		}
	}

	class ViewHolder {
		XCRoundImageView xCRoundImageView1;
		LinearLayout linearLayout1;
		ImageView imageView_rating, imageView1, imageView2, imageView3;
		TextView textView_name, textView_avgPrice, textView_content;
	}

}
