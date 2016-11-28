// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FindFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.FindFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165286, "field 'ptrListView'");
    target.ptrListView = finder.castView(view, 2131165286, "field 'ptrListView'");
  }

  @Override public void unbind(T target) {
    target.ptrListView = null;
  }
}
