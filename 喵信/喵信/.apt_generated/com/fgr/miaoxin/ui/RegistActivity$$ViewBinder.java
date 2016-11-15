// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegistActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.RegistActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165213, "field 'etRePassword'");
    target.etRePassword = finder.castView(view, 2131165213, "field 'etRePassword'");
    view = finder.findRequiredView(source, 2131165214, "field 'rgGender'");
    target.rgGender = finder.castView(view, 2131165214, "field 'rgGender'");
    view = finder.findRequiredView(source, 2131165211, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131165211, "field 'etUsername'");
    view = finder.findRequiredView(source, 2131165212, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131165212, "field 'etPassword'");
    view = finder.findRequiredView(source, 2131165217, "method 'regist'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.regist(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.etRePassword = null;
    target.rgGender = null;
    target.etUsername = null;
    target.etPassword = null;
  }
}
