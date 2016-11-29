package com.fgr.miaoxin.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.Blog;
import com.fgr.miaoxin.bean.Comment;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.bean.Zan;
import com.fgr.miaoxin.listener.OnCommentBlogListener;
import com.fgr.miaoxin.listener.OnDatasLoadFinishListener;
import com.fgr.miaoxin.util.DBUtil;
import com.fgr.miaoxin.util.TimeUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.im.BmobUserManager;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.sharesdk.onekeyshare.OnekeyShare;

public class BlogAdapter extends MyBaseAdapter<Blog> {

	DBUtil dbUtil;
	OnCommentBlogListener listener;

	public BlogAdapter(Context context, List<Blog> datasource,
			OnCommentBlogListener listener) {
		super(context, datasource);
		dbUtil = new DBUtil(context);
		this.listener = listener;
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
	private void showBlogComments(int position,
			final LinearLayout commentContainer) {
		// 从服务器Comment数据表中，将针对position位置的blog的所有评论都查询出来

		BmobQuery<Comment> query = new BmobQuery<Comment>();
		query.addWhereEqualTo("blogId", getItem(position).getObjectId());
		// query.order("-createdAt");//越晚发布的评论呈现时越靠上
		query.findObjects(context, new FindListener<Comment>() {

			@Override
			public void onSuccess(List<Comment> arg0) {
				// 每一条评论放入一个TextView，然后TextView放入到commentContainer中
				for (Comment comment : arg0) {
					TextView tv = new TextView(context);
					// abc评论：xxxxxxx
					String username = comment.getUsername();
					String content = comment.getContent();
					// 评论内容是否需要添加时间，以及时间的格式自己看着办
					tv.setText(username + "评论：" + content);
					tv.setTextColor(Color.BLUE);
					int padding = (int) TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 3, context
									.getResources().getDisplayMetrics());
					tv.setPadding(padding, padding, padding, padding);
					tv.setGravity(Gravity.CENTER_VERTICAL);
					commentContainer.addView(tv);
				}

			}

			@Override
			public void onError(int arg0, String arg1) {
				Log.d("TAG", "查询评论失败，错误代码：" + arg0 + ":" + arg1);

			}
		});
	}

	/**
	 * 评论一个blog
	 * 
	 * @param position
	 */
	protected void commentBlog(int position) {
		Blog blog = getItem(position);
		listener.onComment(position, blog);
	}

	/**
	 * 为blog点赞
	 * 
	 * @param position
	 */
	protected void loveBlog(final int position) {
		final String blogId = getItem(position).getObjectId();
		final String userId = BmobUserManager.getInstance(context)
				.getCurrentUserObjectId();
		// 查询服务器Zan数据表
		BmobQuery<Zan> query = new BmobQuery<Zan>();
		query.addWhereEqualTo("blogId", blogId);
		query.addWhereEqualTo("userId", userId);
		query.findObjects(context, new FindListener<Zan>() {

			@Override
			public void onError(int arg0, String arg1) {

				if (arg0 == 101) {
					// 此时服务器的数据库中根本还未创建Zan数据表
					saveZan(position, blogId, userId);
				} else {
					Log.d("TAG", "查询失败，错误代码：" + arg0 + "," + arg1);
				}

			}

			@Override
			public void onSuccess(List<Zan> arg0) {
				if (arg0 != null && arg0.size() > 0) {
					// 说明当前登录用户已经为第position位置上的blog点过赞了
					Toast.makeText(context, "已经点过赞了", Toast.LENGTH_SHORT)
							.show();
				} else {
					// 进行“点赞”操作
					saveZan(position, blogId, userId);
				}
			}
		});
	}

	protected void saveZan(final int position, String blogId, String userId) {
		Zan zan = new Zan();
		zan.setBlogId(blogId);
		zan.setUserId(userId);
		zan.save(context, new SaveListener() {

			@Override
			public void onSuccess() {

				Blog blog = getItem(position);
				blog.setLove(blog.getLove() + 1);
				blog.update(context, new UpdateListener() {

					@Override
					public void onSuccess() {
						// 刷新ListView
						notifyDataSetChanged();
					}

					@Override
					public void onFailure(int arg0, String arg1) {
						Log.d("TAG", "更新Blog失败，错误代码：" + arg0 + "," + arg1);

					}
				});
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				Log.d("TAG", "保存赞失败，错误代码：" + arg0 + "," + arg1);

			}
		});

	}

	/**
	 * 分享blog到第三方社交平台
	 * 
	 * @param position
	 */
	protected void shareBlog(int position) {

		Blog blog = getItem(position);

		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle("来自喵信的分享");
		// titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
		oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段

		if (TextUtils.isEmpty(blog.getContent())) {
			oks.setText("来自喵信的分享");
		} else {
			oks.setText(blog.getContent());
		}
		// 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
		if (!TextUtils.isEmpty(blog.getImgUrls())) {
			// 通过ShareSDK分享图片时，目前最多只能分享一幅图片
			String[] imgUrls = blog.getImgUrls().split("&");
			oks.setImageUrl(imgUrls[0]);
			// siteUrl是分享此内容的网站地址，仅在QQ空间使用
			oks.setSiteUrl(imgUrls[0]);
		}
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		// oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("http://sharesdk.cn");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite("ShareSDK");
		// 启动分享GUI
		oks.show(context);
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

			final ImageView iv = new ImageView(context);
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

			// ImageLoader.getInstance().displayImage(urls[i], iv);

			String url = urls[i];

			if (dbUtil.isExist(url)) {
				// Log.d("TAG", "图片从数据库加载");
				// 本地数据库中已经缓存了url所对应的图片
				dbUtil.get(url, new OnDatasLoadFinishListener<Bitmap>() {

					@Override
					public void onLoadFinish(List<Bitmap> datas) {
						Bitmap bitmap = datas.get(0);
						iv.setImageBitmap(bitmap);
					}
				});
			} else {
				// 从网络中加载图片
				// Log.d("TAG", "图片从网络加载");
				ImageLoader.getInstance().displayImage(url, iv,
						new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// 将这幅图片放到本地数据库中缓存
								iv.setImageBitmap(loadedImage);
								dbUtil.save(imageUri, loadedImage);

							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							}
						});

			}

			imageContainer.addView(iv);
		}

	}

	class ViewHolder {
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
