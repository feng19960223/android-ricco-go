// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RobotActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.RobotActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165228, "field 'listview'");
    target.listview = finder.castView(view, 2131165228, "field 'listview'");
    view = finder.findRequiredView(source, 2131165229, "field 'etContent'");
    target.etContent = finder.castView(view, 2131165229, "field 'etContent'");
    view = finder.findRequiredView(source, 2131165230, "method 'send'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.send(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.listview = null;
    target.etContent = null;
  }
}
