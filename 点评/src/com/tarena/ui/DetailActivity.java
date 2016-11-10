package com.tarena.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.Listener;
import com.tarena.adapter.CommentAdapter;
import com.tarena.entity.Business.Businesses;
import com.tarena.entity.Comment;
import com.tarena.groupon.R;
import com.tarena.utils.HttpUtil;

public class DetailActivity extends Activity {
	private Businesses businesses;
	private ImageView imageView_back = null;// 返回
	private ListView listView_detail = null;
	private CommentAdapter adapter = null;
	int[] resIds = new int[] { R.drawable.star0, R.drawable.star10,
			R.drawable.star20, R.drawable.star30, R.drawable.star35,
			R.drawable.star40, R.drawable.star45, R.drawable.star50 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);
		businesses = (Businesses) getIntent().getSerializableExtra("business");
		listView_detail = (ListView) findViewById(R.id.listView_detail);
		initData();
		imageView_back = (ImageView) findViewById(R.id.imageView_back);
		imageView_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initData() {
		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.include_detail_pull1,
				listView_detail, false);
		LinearLayout ll = (LinearLayout) view
				.findViewById(R.id.linearlayout_address);
		ll.setOnClickListener(new OnClickListener() {// 地图
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DetailActivity.this,
						FindActivity.class);
				intent.putExtra("business", businesses);
				intent.putExtra("from", "detail");// 标记,从商户页面跳转的,有一个界面,是从发现跳转的
				startActivity(intent);
			}
		});
		initView(view);
		listView_detail.addHeaderView(view);
		adapter = new CommentAdapter(this, new ArrayList<Comment>());
		listView_detail.setAdapter(adapter);
		refresh();
	}

	private void refresh() {
		String url = businesses.getReview_list_url();
		HttpUtil.getComments(url, new Listener<String>() {
			@Override
			public void onResponse(String arg0) {
				// try {
				// FileWriter fw = new FileWriter(
				// new File(
				// Environment
				// .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
				// "dianping.text"));
				// fw.write(arg0);
				// fw.flush();
				// fw.close();
				// Toast.makeText(DetailActivity.this, "ok",
				// Toast.LENGTH_SHORT).show();
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
				Document document = Jsoup.parse(arg0);
				List<Comment> comments = new ArrayList<Comment>();
				Elements elements = document
						.select(".comment-list ul li[data-id]");
				for (Element element : elements) {
					Comment comment = new Comment();
					// 从element中取出相应的内容作为commment的属性
					// 取网友的用户名,头像
					Elements imgElements = element.select(".pic a img");
					Element imgElement = imgElements.get(0);
					comment.setUsername(imgElement.attr("title"));
					comment.setAvatar(imgElement.attr("src"));
					// 选取为商家的打分
					Elements spanElements = element
							.select(".user-info span[title]");
					Element spanElement = spanElements.get(0);
					String rat = spanElement.attr("class");
					// sml-rank-stars sml-str40
					String rating = rat.split(" ")[1].split("-")[1];// 40
					comment.setRating(rating);
					// 选取评论的正文
					Elements contentElements = element.select(".J_brief-cont");
					Element contentElement = contentElements.get(0);
					comment.setContent(contentElement.text());
					// 选取软件消费价格
					Elements spans = element.select(".comm-per");
					if (spans != null && spans.size() > 0) {// 人均 ￥40
						// 提供了人均价格
						comment.setAvgPrice(spans.get(0).text().split(" ")[1]);
					} else {
						// 没有提供人均价格
						comment.setAvatar("");
					}
					// 选取评论的配图
					// 选取评论的配图
					Elements imgs = element.select(".shop-photo img");
					if (imgs != null && imgs.size() > 0) {
						// 评论中有配图
						int num = imgs.size();
						if (num > 3) {
							// 配图多于3张，最多就取3张
							num = 3;
						}
						String[] pics = new String[num];
						for (int i = 0; i < num; i++) {
							Element img = imgs.get(i);
							pics[i] = img.attr("src");
						}
						comment.setImgs(pics);
					} else {
						// 评论中没有配图
						comment.setImgs(null);
					}
					comments.add(comment);
				}
				adapter.addAll(comments, true);
			}
		});

	}

	private void initView(View view) {
		ImageView imageView_main = (ImageView) view
				.findViewById(R.id.imageView_main);// 商店图片
		TextView textView_name = (TextView) view
				.findViewById(R.id.textView_name);// 商店名字
		ImageView imageView_star = (ImageView) view
				.findViewById(R.id.imageView_star);// 评分图片
		TextView textView_pl = (TextView) view.findViewById(R.id.textView_pl);// 评论数
		TextView textView_jg = (TextView) view.findViewById(R.id.textView_jg);// 价格
		TextView textView_wz = (TextView) view.findViewById(R.id.textView_wz);// 地点
		TextView textView_lx = (TextView) view.findViewById(R.id.textView_lx);// 类型
		TextView textView_xxwz = (TextView) view
				.findViewById(R.id.textView_xxwz);// 详细地点
		TextView textView_phone = (TextView) view
				.findViewById(R.id.textView_phone);// 电话
		String url = businesses.getS_photo_url();
		HttpUtil.displayImage(url, imageView_main);
		String name = businesses.getName().substring(0,// 店名称
				businesses.getName().indexOf("("));
		String branchName = businesses.getBranch_name();// 分店名称
		if (!TextUtils.isEmpty(branchName)) {
			name += "(" + businesses.getBranch_name() + ")";
		}
		textView_name.setText(name);// 餐厅名称+分店名称
		imageView_star.setImageResource(resIds[new Random()
				.nextInt(resIds.length)]);// 评分无法获取,随机
		textView_jg.setText("￥" + (new Random().nextInt(200) + 50) + "/人");// 人均价格无法得到,随机
		textView_lx.setText(businesses.getCategories().get(0)); // 餐厅类型
		List<String> regions = businesses.getRegions();
		StringBuilder sb = new StringBuilder();
		for (String string : regions) {
			sb.append(string).append("/");
		}
		textView_wz.setText(sb.deleteCharAt(sb.length() - 1).toString());// 地点
		textView_pl.setText(new Random().nextInt(2000) + "条");
		textView_xxwz.setText(businesses.getAddress());
		textView_phone.setText(businesses.getTelephone());
	}
}
