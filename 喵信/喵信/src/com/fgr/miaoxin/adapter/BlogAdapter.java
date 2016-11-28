package com.fgr.miaoxin.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.Blog;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.util.TimeUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

@SuppressLint("SimpleDateFormat")
public class BlogAdapter extends MyBaseAdapter<Blog> {

	public BlogAdapter(Context context, List<Blog> datasource) {
		super(context, datasource);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		try {
			ViewHolder vh;
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.item_blog_layout,
						parent, false);
				vh = new ViewHolder(convertView);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}

			Blog blog = getItem(position);
			MyUser author = blog.getAuthor();
			setAvatar(author.getAvatar(), vh.ivAvatar);
			vh.tvUsername.setText(author.getUsername());

			vh.tvContent.setText(blog.getContent());

			vh.imageContainer.removeAllViews();

			String imgUrls = blog.getImgUrls();

			if (!TextUtils.isEmpty(imgUrls)) {
				showBlogImages(imgUrls, vh.imageContainer);
			}
			// 2016-11-28 15:45:14--TimeUtils.getTime(时间戳)--->15:45
			String time = blog.getCreatedAt();
			Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
			String descTime = TimeUtil.getTime(date.getTime());
			vh.tvTime.setText(descTime);

			vh.tvShare.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					shareBlog(position);

				}
			});

			vh.tvLove.setText(blog.getLove() + " 赞");

			vh.tvLove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					loveBlog(position);

				}
			});

			vh.tvComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					commentBlog(position);
				}
			});

			vh.commentContainer.removeAllViews();
			showBlogComments(position, vh.commentContainer);

			return convertView;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("不正确的时间格式");
		}
	}

	/**
	 * 显示对某一个blog的所有评论内容
	 * 
	 * @param position
	 * 
	 * @param commentContainer
	 */
	private void showBlogComments(int position, LinearLayout commentContainer) {
		// TODO Auto-generated method stub

	}

	/**
	 * 评论一个blog
	 * 
	 * @param position
	 */
	protected void commentBlog(int position) {
		// TODO Auto-generated method stub

	}

	/**
	 * 为blog点赞
	 * 
	 * @param position
	 */
	protected void loveBlog(int position) {
		// TODO Auto-generated method stub

	}

	/**
	 * 分享blog到第三方社交平台
	 * 
	 * @param position
	 */
	protected void shareBlog(int position) {
		// TODO Auto-generated method stub

	}

	/**
	 * 用来显示blog的所有配图
	 * 
	 * @param imgUrls
	 *            所有配图的网址
	 * @param imageContainer
	 *            用来装载显示blog配图的ImageView的容器
	 */
	private void showBlogImages(String imgUrls, RelativeLayout imageContainer) {

		String[] urls = imgUrls.split("&");

		// 每一个ImageView的尺寸
		// 整个屏幕的宽度-左外边距15dp-右外边距15dp-两个ImageView之间的距离15dp

		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;

		int imgWidth = (int) ((screenWidth - TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 45, context.getResources()
						.getDisplayMetrics())) / 2);

		int imgHeight = imgWidth;

		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources()
						.getDisplayMetrics());

		for (int i = 0; i < urls.length; i++) {

			ImageView iv = new ImageView(context);
			iv.setId(i + 1);

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					imgWidth, imgHeight);

			if (i % 2 != 0) {
				params.addRule(RelativeLayout.RIGHT_OF, i);
				params.leftMargin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources()
								.getDisplayMetrics());
			}

			if (i >= 2) {
				params.addRule(RelativeLayout.BELOW, i - 1);
				params.topMargin = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 15, context.getResources()
								.getDisplayMetrics());
			}

			iv.setLayoutParams(params);

			iv.setBackgroundResource(R.drawable.input_bg);
			iv.setPadding(padding, padding, padding, padding);
			iv.setScaleType(ScaleType.FIT_XY);

			ImageLoader.getInstance().displayImage(urls[i], iv);

			imageContainer.addView(iv);

		}

	}

	public class ViewHolder {
		@Bind(R.id.iv_item_blog_avatar)
		ImageView ivAvatar;
		@Bind(R.id.tv_item_blog_username)
		TextView tvUsername;
		@Bind(R.id.tv_item_blog_content)
		TextView tvContent;
		@Bind(R.id.rl_item_blog_imagecontainer)
		RelativeLayout imageContainer;
		@Bind(R.id.tv_item_blog_time)
		TextView tvTime;
		@Bind(R.id.tv_item_blog_share)
		TextView tvShare;
		@Bind(R.id.tv_item_blog_love)
		TextView tvLove;
		@Bind(R.id.tv_item_blog_comment)
		TextView tvComment;
		@Bind(R.id.ll_item_blog_commentcontainer)
		LinearLayout commentContainer;

		public ViewHolder(View convertView) {
			ButterKnife.bind(this, convertView);
		}

	}

}
