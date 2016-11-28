package com.fgr.miaoxin.ui;

import java.util.ArrayList;
import java.util.List;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.bean.Blog;
import com.fgr.miaoxin.bean.MyUser;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.view.NumberProgressBar;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;

public class PostBlogActivity extends BaseActivity {

	@Bind(R.id.et_postblog_content)
	EditText etContent;// 帖子正文内容

	@Bind(R.id.ll_postblog_imagecontainer)
	LinearLayout llImageContainer;

	@Bind(R.id.iv_postblog_del1)
	ImageView ivBlogDel1;
	@Bind(R.id.iv_postblog_del2)
	ImageView ivBlogDel2;
	@Bind(R.id.iv_postblog_del3)
	ImageView ivBlogDel3;
	@Bind(R.id.iv_postblog_del4)
	ImageView ivBlogDel4;
	@Bind(R.id.iv_postblog_img1)
	ImageView ivBlogImg1;
	@Bind(R.id.iv_postblog_img2)
	ImageView ivBlogImg2;
	@Bind(R.id.iv_postblog_img3)
	ImageView ivBlogImg3;
	@Bind(R.id.iv_postblog_img4)
	ImageView ivBlogImg4;

	@Bind(R.id.tv_postblog_imagenumber)
	TextView tvImageNumber;

	@Bind(R.id.npb_postblog_progress)
	NumberProgressBar npbProgressBar;

	@Bind(R.id.iv_postblog_plus)
	ImageView ivPlus;
	@Bind(R.id.iv_postblog_picture)
	ImageView ivPicture;
	@Bind(R.id.iv_postblog_camera)
	ImageView ivCamera;
	@Bind(R.id.iv_postblog_location)
	ImageView ivLocation;

	List<ImageView> blogImages;// 4个用来显示blog图片的ImageView
	List<ImageView> blogDels;// 4个用户来删除blog配图的小红叉

	boolean isExpaned;// 底部的三个按钮是否可见true 可见 false 不可见
	boolean isPosting;// 当前是否有blog正处于上传的状态: true，有blog正在上传；false，没有blog正在上传

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void setMyContentView() {
		setContentView(R.layout.activity_post_blog);

	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initView();

	}

	private void initHeaderView() {
		setHeaderTitle("");
		setHeaderImage(Position.START, R.drawable.back_arrow_2, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						finish();

					}
				});

		setHeaderImage(Position.END, R.drawable.ic_upload, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						// 如果用户即没有输入文字也没有任何配图
						String content = etContent.getText().toString();
						if (TextUtils.isEmpty(content)
								&& blogImages.get(0).getVisibility() == View.INVISIBLE) {
							return;
						}
						if (isPosting) {
							return;
						}
						isPosting = true;
						// 先上传Blog的配图
						// 获得配图的地址后，在开始上传Blog
						postBlogImages();

					}
				});
	}

	/**
	 * 上传blog的配图
	 */
	protected void postBlogImages() {
		// 如果此时用户比没有为发布的blog配图
		if (blogImages.get(0).getVisibility() == View.INVISIBLE) {
			postBlog("");
		} else {
			// 利用BmobFile的批量上传方法上传配图到服务器，并获得配图在服务器上的地址
			// 要上传的图片路径就保存在显示图片的ImageView的tag属性中

			List<String> list = new ArrayList<String>();
			for (int i = 0; i < blogImages.size(); i++) {
				ImageView iv = blogImages.get(i);
				if (iv.getVisibility() == View.VISIBLE) {
					String tag = (String) iv.getTag();
					list.add(tag);
				}
			}

			final String[] filePaths = list.toArray(new String[list.size()]);
			npbProgressBar.setVisibility(View.VISIBLE);
			npbProgressBar.setProgress(0);
			BmobFile.uploadBatch(this, filePaths, new UploadBatchListener() {

				@Override
				public void onSuccess(List<BmobFile> arg0, List<String> arg1) {
					// 上传成功

					if (arg0.size() == filePaths.length) {
						// 此时图片全部上传成功
						StringBuilder sb = new StringBuilder();
						for (String string : arg1) {
							sb.append(string).append("&");
						}

						npbProgressBar.setVisibility(View.INVISIBLE);

						postBlog(sb.substring(0, sb.length() - 1));

					}

				}

				@Override
				public void onProgress(int arg0, int arg1, int arg2, int arg3) {
					// arg3 上传的总进度
					npbProgressBar.setProgress(arg3);

				}

				@Override
				public void onError(int arg0, String arg1) {
					toastAndLog("blog发布失败，请稍后重试", arg0, arg1);
					// TODO

				}
			});
		}

	}

	/**
	 * 在blog配图上传完毕后，开始上传blog
	 * 
	 * @param imgUrls
	 *            blog配图的地址
	 */
	private void postBlog(String imgUrls) {

		Blog blog = new Blog();
		blog.setAuthor(bmobUserManager.getCurrentUser(MyUser.class));
		blog.setContent(etContent.getText().toString());
		blog.setImgUrls(imgUrls);
		blog.setLove(0);
		// 把blog上传到服务器
		blog.save(this, new SaveListener() {

			@Override
			public void onSuccess() {
				toast("博客发布成功");
				etContent.setText("");
				for (int i = 0; i < blogImages.size(); i++) {
					blogImages.get(i).setVisibility(View.INVISIBLE);
					blogDels.get(i).setVisibility(View.INVISIBLE);
				}
				tvImageNumber.setText("");
				isPosting = false;
			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("博客发布失败，稍后重试", arg0, arg1);
				isPosting = false;

			}
		});

	}

	private void initView() {

		blogImages = new ArrayList<ImageView>();
		blogImages.add(ivBlogImg1);
		blogImages.add(ivBlogImg2);
		blogImages.add(ivBlogImg3);
		blogImages.add(ivBlogImg4);

		blogDels = new ArrayList<ImageView>();
		blogDels.add(ivBlogDel1);
		blogDels.add(ivBlogDel2);
		blogDels.add(ivBlogDel3);
		blogDels.add(ivBlogDel4);

		llImageContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@SuppressWarnings("deprecation")
					@Override
					public void onGlobalLayout() {
						// llImageContainer的宽度=屏幕宽度-30dp
						// 或者调用llImageContainer的getWidth方法
						int containerWidth = llImageContainer.getWidth();
						// framelayout的宽度=(llImageContainer的宽度-30dp)/4
						int rightMargin = (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 10, getResources()
										.getDisplayMetrics());
						int frameWidth = (containerWidth - 3 * rightMargin) / 4;
						// framelayout的高度=framelayout的宽度
						int frameHeight = frameWidth;
						// 请求llImageContainer重写再进行一次布局
						for (int i = 0; i < llImageContainer.getChildCount(); i++) {
							View frame = llImageContainer.getChildAt(i);
							LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
									frameWidth, frameHeight);
							if (i != llImageContainer.getChildCount() - 1) {
								params.setMargins(0, 0, rightMargin, 0);
							}
							frame.setLayoutParams(params);
						}

						llImageContainer.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);

						llImageContainer.requestLayout();
					}
				});

	}

	@OnClick(R.id.iv_postblog_plus)
	public void setButtonsVisible(View view) {
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.button_press_anim);
		ivPlus.startAnimation(anim);
		if (isExpaned) {
			// 隐藏底部三个按钮
			closeButtons();
		} else {
			// 显示底部三个按钮
			expandButtons();
		}
	}

	/**
	 * 显示底部三个按钮
	 */
	private void expandButtons() {
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.button_expand_anim);
		ivPicture.startAnimation(anim);
		ivCamera.startAnimation(anim);
		ivLocation.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ivPicture.setVisibility(View.VISIBLE);
				ivCamera.setVisibility(View.VISIBLE);
				ivLocation.setVisibility(View.VISIBLE);
				isExpaned = true;

			}
		});
	}

	private void closeButtons() {

		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.button_close_anim);
		ivPicture.startAnimation(anim);
		ivCamera.startAnimation(anim);
		ivLocation.startAnimation(anim);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				ivPicture.setVisibility(View.INVISIBLE);
				ivCamera.setVisibility(View.INVISIBLE);
				ivLocation.setVisibility(View.INVISIBLE);
				isExpaned = false;

			}
		});

	}

	/**
	 * 为blog增加配图
	 * 
	 * @param view
	 */
	@OnClick(R.id.iv_postblog_picture)
	public void selectPicture(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, "image/*");
		startActivityForResult(intent, 101);
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg1 == RESULT_OK) {
			if (arg0 == 101) {
				// 获得用户选择的图片在SD上的地址
				Uri uri = arg2.getData();
				Cursor cursor = getContentResolver().query(uri,
						new String[] { Images.Media.DATA }, null, null, null);
				cursor.moveToNext();
				String filePath = cursor.getString(0);
				cursor.close();
				showBlogImage(filePath);
			}
		}
	}

	/**
	 * "预览"博客的配图
	 * 
	 * @param filePath
	 *            blog配图在SD卡上的地址
	 */

	private void showBlogImage(String filePath) {
		Bitmap bm = BitmapFactory.decodeFile(filePath);
		for (int i = 0; i < blogImages.size(); i++) {
			ImageView iv = blogImages.get(i);
			if (iv.getVisibility() == View.INVISIBLE) {
				iv.setImageBitmap(bm);
				iv.setTag(filePath);
				iv.setVisibility(View.VISIBLE);
				blogDels.get(i).setVisibility(View.VISIBLE);

				tvImageNumber.setText((i + 1) + " / 4");

				return;
			}
		}

		toast("最多只能添加四幅图片");
	}

	@OnClick({ R.id.iv_postblog_del1, R.id.iv_postblog_del2,
			R.id.iv_postblog_del3, R.id.iv_postblog_del4 })
	public void deletBlogImages(View view) {
		switch (view.getId()) {
		case R.id.iv_postblog_del1:
			removeBlogImage(0);
			break;
		case R.id.iv_postblog_del2:
			removeBlogImage(1);
			break;
		case R.id.iv_postblog_del3:
			removeBlogImage(2);
			break;
		default:
			removeBlogImage(3);
			break;
		}
	}

	/**
	 * "删除"已经添加的blog配图
	 * 
	 * @param i
	 */
	private void removeBlogImage(int idx) {
		// 1)一共有多少幅配图
		int count = 0;
		for (int i = 0; i < blogImages.size(); i++) {
			if (blogImages.get(i).getVisibility() == View.VISIBLE) {
				count += 1;
			}
		}
		// 2)根据用户点击的是否是最后一幅配图分别处理
		if (idx == count - 1) {
			// 2.1)如果是，直接隐藏最后一幅配图
			blogImages.get(idx).setVisibility(View.INVISIBLE);
			blogDels.get(idx).setVisibility(View.INVISIBLE);
		} else {
			// 2.2)如果不是，就进行递补，最后再将最后一幅图隐藏
			for (int i = idx; i < count; i++) {
				if (i == count - 1) {
					// 如果已经到了最后一幅图
					// 将最后一幅图隐藏
					blogImages.get(i).setVisibility(View.INVISIBLE);
					blogDels.get(i).setVisibility(View.INVISIBLE);
				} else {
					// 递补
					// 将第i+1个ImageView中的图片取出，放到第i个ImageView中
					Drawable drawable = blogImages.get(i + 1).getDrawable();
					blogImages.get(i).setImageDrawable(drawable);
					// 将第i+1个ImageView中的图片在SD卡上的路径取出，放到第i个ImageView的tag属性中
					String filePath = (String) blogImages.get(i + 1).getTag();
					blogImages.get(i).setTag(filePath);

				}
			}
		}

		if (count == 1) {
			tvImageNumber.setText("");
		} else {
			tvImageNumber.setText((count - 1) + " / 4");
		}

	}
}
