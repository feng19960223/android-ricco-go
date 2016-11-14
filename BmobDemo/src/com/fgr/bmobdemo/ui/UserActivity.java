package com.fgr.bmobdemo.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.fgr.bmobdemo.R;
import com.fgr.bmobdemo.bean.MyUser;

public class UserActivity extends Activity {
	@Bind(R.id.listView_user)
	ListView listview;
	List<String> names;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		ButterKnife.bind(this);
		initListView();
	}

	private void initListView() {
		names = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, names);
		listview.setAdapter(adapter);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	private void refresh() {
		BmobQuery<MyUser> query = new BmobQuery<MyUser>();
		query.findObjects(this, new FindListener<MyUser>() {

			@Override
			public void onSuccess(List<MyUser> arg0) {
				List<String> list = new ArrayList<String>();
				for (MyUser user : arg0) {
					list.add(user.getUsername());
				}
				names.clear();
				adapter.addAll(list);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onError(int arg0, String arg1) {
				Toast.makeText(UserActivity.this, "≤È—Ø¥ÌŒÛ,«Î…‘∫Û÷ÿ ‘",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@OnItemClick(R.id.listView_user)
	public void selectUser(AdapterView<?> parent, View view, int position,
			long id) {
		String name = adapter.getItem(position);
		Intent data = new Intent();
		data.putExtra("name", name);
		setResult(RESULT_OK, data);
		finish();
	}
}
