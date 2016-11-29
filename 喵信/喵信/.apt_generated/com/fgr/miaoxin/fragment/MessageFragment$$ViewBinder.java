// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MessageFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.MessageFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165293, "field 'listView'");
    target.listView = finder.castView(view, 2131165293, "field 'listView'");
  }

  @Override public void unbind(T target) {
    target.listView = null;
  }
}
