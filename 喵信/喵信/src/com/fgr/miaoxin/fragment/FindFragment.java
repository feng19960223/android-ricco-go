package com.fgr.miaoxin.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.BlogAdapter;
import com.fgr.miaoxin.bean.Blog;
import com.fgr.miaoxin.bean.Comment;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.listener.OnCommentBlogListener;
import com.fgr.miaoxin.ui.PostBlogActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class FindFragment extends BaseFragment implements OnCommentBlogListener {

	@Bind(R.id.ptr_find_blogs)
	PullToRefreshListView ptrListView;
	ListView listView;
	List<Blog> blogs;
	BlogAdapter adapter;

	@Bind(R.id.ll_find_commentcontainer)
	LinearLayout commentContainer;
	@Bind(R.id.et_find_comment)
	EditText etComment;

	Blog blog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public View createMyView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_find, container, false);
		return view;
	}

	@Override
	public void init() {
		super.init();
		initHeaderView();
		initListView();
	}

	private void initListView() {
		listView = ptrListView.getRefreshableView();
		blogs = new ArrayList<Blog>();
		adapter = new BlogAdapter(getActivity(), blogs, this);
		listView.setAdapter(adapter);

	}

	private void initHeaderView() {
		setHeaderTitle("喵圈", Position.CENTER);
		setHeaderImage(Position.END, R.drawable.ic_new_blog, true,
				new OnClickListener() {

					@Override
					public void onClick(View v) {

						jumpTo(PostBlogActivity.class, false);

					}
				});

	}

	@Override
	public void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		BmobQuery<Blog> query = new BmobQuery<Blog>();
		query.include("author");
		query.order("-createdAt");
		query.findObjects(getActivity(), new FindListener<Blog>() {

			@Override
			public void onSuccess(List<Blog> arg0) {
				adapter.addItems(arg0, true);
			}

			@Override
			public void onError(int arg0, String arg1) {
				toastAndLog("加载博客失败，稍后重试", arg0, arg1);

			}
		});

	}

	@Override
	public void onComment(int position, Blog blog) {
		// 显示 / 隐藏供用户输入评论内容视图
		if (commentContainer.getVisibility() == View.VISIBLE) {
			commentContainer.setVisibility(View.INVISIBLE);
			this.blog = null;
		} else {
			commentContainer.setVisibility(View.VISIBLE);
			this.blog = blog;
		}

	}

	@OnClick(R.id.btn_find_send)
	public void sendComment(View view) {

		String content = etComment.getText().toString();
		if (TextUtils.isEmpty(content)) {
			return;
		}
		Comment comment = new Comment();
		comment.setBlogId(blog.getObjectId());
		comment.setContent(content);
		comment.setUsername(bmobUserManager.getCurrentUserName());
		comment.save(getActivity(), new SaveListener() {

			@Override
			public void onSuccess() {
				etComment.setText("");
				commentContainer.setVisibility(View.INVISIBLE);
				blog = null;

				refresh();

			}

			@Override
			public void onFailure(int arg0, String arg1) {
				toastAndLog("发布评论失败，稍后重试", arg0, arg1);

			}
		});

	}
}
