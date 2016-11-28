// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddFriendActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.AddFriendActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165205, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131165205, "field 'etUsername'");
    view = finder.findRequiredView(source, 2131165208, "field 'ptrListView'");
    target.ptrListView = finder.castView(view, 2131165208, "field 'ptrListView'");
    view = finder.findRequiredView(source, 2131165206, "method 'serarch'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.serarch(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165207, "method 'searchMore'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.searchMore(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.etUsername = null;
    target.ptrListView = null;
  }
}
