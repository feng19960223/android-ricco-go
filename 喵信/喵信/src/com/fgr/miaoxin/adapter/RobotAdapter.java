package com.fgr.miaoxin.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.MyRobot;
import com.fgr.miaoxin.view.XCRoundImageView;

public class RobotAdapter extends MyBaseAdapter<MyRobot> {

	public RobotAdapter(Context context, List<MyRobot> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		int type = getItemViewType(position);
		if (convertView == null) {
			if (type == 0) {// 接收
				convertView = layoutInflater.inflate(R.layout.item_robot_left,
						parent, false);
			}
			if (type == 1) {// 发送
				convertView = layoutInflater.inflate(R.layout.item_robot_right,
						parent, false);
			}
			viewHolder = new ViewHolder(convertView);
			// 设置聊天字体
			stetFontType(context, viewHolder.tvContent);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		MyRobot robot = getItem(position);

		viewHolder.tvContent.setText(robot.getContent());
		viewHolder.tvTime.setText(robot.getTime());
		if (TextUtils.isEmpty(viewHolder.tvTime.getText().toString())) {
			viewHolder.tvTime.setVisibility(View.GONE);
		}
		if (robot.getFlag() == 2) {
			// 自己的头像
			// TODO
		}
		return convertView;
	}

	@Override
	// 改变数据适配时采用的布局的类型
	public int getItemViewType(int position) {
		MyRobot robot = getItem(position);
		int type = robot.getFlag();
		return type - 1;// 0和1
	}

	@Override
	// 改变数据适配时采用的布局的个数
	public int getViewTypeCount() {
		return 2;
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_robot_avatar)
		XCRoundImageView ivAvatar;
		@Bind(R.id.tv_item_robot_content)
		TextView tvContent;
		@Bind(R.id.tv_item_robot_time)
		TextView tvTime;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}
	}
}
