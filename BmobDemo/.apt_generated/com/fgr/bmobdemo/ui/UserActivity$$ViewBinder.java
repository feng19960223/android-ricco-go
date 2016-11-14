// Generated code from Butter Knife. Do not modify!
package com.fgr.bmobdemo.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserActivity$$ViewBinder<T extends com.fgr.bmobdemo.ui.UserActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230736, "field 'listview' and method 'selectUser'");
    target.listview = finder.castView(view, 2131230736, "field 'listview'");
    ((android.widget.AdapterView<?>) view).setOnItemClickListener(
      new android.widget.AdapterView.OnItemClickListener() {
        @Override public void onItemClick(
          android.widget.AdapterView<?> p0,
          android.view.View p1,
          int p2,
          long p3
        ) {
          target.selectUser(p0, p1, p2, p3);
        }
      });
  }

  @Override public void unbind(T target) {
    target.listview = null;
  }
}
