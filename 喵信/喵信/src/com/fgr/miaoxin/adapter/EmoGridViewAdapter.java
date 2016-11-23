package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.fgr.miaoxin.R;

public class EmoGridViewAdapter extends MyBaseAdapter<String> {

	public EmoGridViewAdapter(Context context, List<String> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh;

		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.item_emo_layout,
					parent, false);
			vh = new ViewHolder(convertView);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// ue056
		String name = getItem(position);
		// ue056--->R.drawable.ue056
		int resId = context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
		vh.ivEmo.setImageResource(resId);
		return convertView;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_emo)
		ImageView ivEmo;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}
	}

}
