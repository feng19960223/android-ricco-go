// Generated code from Butter Knife. Do not modify!
package com.fgr.bmobdemo.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.fgr.bmobdemo.ui.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230720, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131230720, "field 'etUsername'");
    view = finder.findRequiredView(source, 2131230721, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131230721, "field 'etPassword'");
    view = finder.findRequiredView(source, 2131230722, "method 'login'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.login(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.etUsername = null;
    target.etPassword = null;
  }
}
