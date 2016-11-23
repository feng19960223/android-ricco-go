// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewFriendActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.NewFriendActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165237, "field 'listView'");
    target.listView = finder.castView(view, 2131165237, "field 'listView'");
  }

  @Override public void unbind(T target) {
    target.listView = null;
  }
}
