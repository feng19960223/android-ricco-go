package com.fgr.bmobdemo.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.listener.DeleteListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyPost;
import com.fgr.bmobdemo.bean.MyUser;
import com.fgr.bmobdemo.ui.PostActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

public class PostAdapter extends BaseAdapter {

	Context context;
	List<MyPost> posts;
	LayoutInflater inflater;

	MyUser currentUser;

	public PostAdapter(Context context, List<MyPost> posts, MyUser currentUser) {
		super();
		this.context = context;
		this.posts = posts;
		this.inflater = LayoutInflater.from(context);
		this.currentUser = currentUser;

	}

	@Override
	public int getCount() {
		return posts.size();
	}

	@Override
	public MyPost getItem(int position) {
		return posts.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder vh;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_post_layout, parent,
					false);
			vh = new ViewHolder(convertView);
			// vh.ivAvatar = (ImageView)
			// convertView.findViewById(R.id.iv_item_avatar);
			// vh.tvUsername = (TextView)
			// convertView.findViewById(R.id.tv_item_username);
			// vh.tvTitle = (TextView)
			// convertView.findViewById(R.id.tv_item_title);
			// vh.tvTime = (TextView)
			// convertView.findViewById(R.id.tv_item_posttime);
			// vh.tvContent = (TextView)
			// convertView.findViewById(R.id.tv_item_content);
			// vh.tvDelete = (TextView)
			// convertView.findViewById(R.id.tv_item_delete);
			// vh.tvUpdate = (TextView)
			// convertView.findViewById(R.id.tv_item_update);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		final MyPost post = getItem(position);

		MyUser user = post.getUser();

		// 设定头像
		String avatar = user.getAvatar();
		if (TextUtils.isEmpty(avatar)) {
			// 用户注册时未设定头像
			vh.ivAvatar.setImageResource(R.drawable.ic_launcher);
		} else {
			// 用户注册时设定了头像
			ImageLoader.getInstance().displayImage(avatar, vh.ivAvatar);
		}

		// 用户名
		vh.tvUsername.setText(user.getUsername());

		// 帖子的标题
		vh.tvTitle.setText(post.getTitle());

		// 帖子的时间
		if (post.getCreatedAt().equals(post.getUpdatedAt())) {
			vh.tvTime.setText("发表于:" + post.getCreatedAt());
		} else {
			vh.tvTime.setText("编辑于：" + post.getUpdatedAt());
		}

		// 帖子的正文
		vh.tvContent.setText(post.getContent());

		// 更新和删除
		// 如果第position个位置上帖子的作者是当前登录用户
		// "更新"与"删除"才可见
		if (user.getUsername().equals(currentUser.getUsername())) {
			vh.tvDelete.setVisibility(View.VISIBLE);
			vh.tvUpdate.setVisibility(View.VISIBLE);
		} else {
			vh.tvDelete.setVisibility(View.INVISIBLE);
			vh.tvUpdate.setVisibility(View.INVISIBLE);
		}

		// 点击“删除”，将第position位置上的帖子删除掉
		vh.tvDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("提示");
				builder.setIcon(android.R.drawable.ic_menu_info_details);
				builder.setMessage("您确实要删除该帖子吗？");
				builder.setNegativeButton("取消", null);
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								MyPost delPost = new MyPost();

								// delPost.setObjectId(post.getObjectId());

								// 删除帖子
								delPost.delete(context, post.getObjectId(),
										new DeleteListener() {

											@Override
											public void onSuccess() {
												// 数据从服务器的MyPost数据表中删除成功
												Toast.makeText(context, "删除成功",
														Toast.LENGTH_SHORT)
														.show();
												// 从缓存中清除数据

												// 从ListView数据源中将对应的数据删除
												// 刷新ListView
												remove(post);

											}

											@Override
											public void onFailure(int arg0,
													String arg1) {
												Toast.makeText(
														context,
														"删除失败，请稍后重试。错误代码："
																+ arg0 + ","
																+ arg1,
														Toast.LENGTH_SHORT)
														.show();
											}
										});
							}
						});
				builder.create().show();
			}
		});

		vh.tvUpdate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 将第position个位置上的“帖子”传递到PostActivtiy
				// 在PostActivity中完成帖子的更新
				Intent intent = new Intent(context, PostActivity.class);
				intent.putExtra("from", "update");
				intent.putExtra("post", post);
				context.startActivity(intent);

			}
		});

		return convertView;
	}

	public void addAll(List<MyPost> list, boolean isClear) {
		if (isClear) {
			posts.clear();
		}
		posts.addAll(list);
		notifyDataSetChanged();
	}

	public void remove(MyPost post) {
		posts.remove(post);
		notifyDataSetChanged();
	}

	public class ViewHolder {
		@Bind(R.id.iv_item_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_title)
		TextView tvTitle;
		@Bind(R.id.tv_item_posttime)
		TextView tvTime;
		@Bind(R.id.tv_item_content)
		TextView tvContent;
		@Bind(R.id.tv_item_delete)
		TextView tvDelete;
		@Bind(R.id.tv_item_update)
		TextView tvUpdate;

		public ViewHolder(View convertView) {

			ButterKnife.bind(this, convertView);
		}

	}

}
