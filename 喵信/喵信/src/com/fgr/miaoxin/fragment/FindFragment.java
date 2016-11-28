package com.fgr.miaoxin.fragment;

import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.fgr.miaoxin.R;
import com.fgr.miaoxin.adapter.BlogAdapter;
import com.fgr.miaoxin.bean.Blog;
import com.fgr.miaoxin.constant.Constant.Position;
import com.fgr.miaoxin.ui.PostBlogActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import butterknife.Bind;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class FindFragment extends BaseFragment {

	@Bind(R.id.ptr_find_blogs)
	PullToRefreshListView ptrListView;
	ListView listView;
	List<Blog> blogs;
	BlogAdapter adapter;

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
		adapter = new BlogAdapter(getActivity(), blogs);
		listView.setAdapter(adapter);

	}

	private void initHeaderView() {
		setHeaderTitle("ﬂ˜»¶", Position.CENTER);
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
				toastAndLog("º”‘ÿ≤©øÕ ß∞‹£¨…‘∫Û÷ÿ ‘", arg0, arg1);

			}
		});

	}
}
