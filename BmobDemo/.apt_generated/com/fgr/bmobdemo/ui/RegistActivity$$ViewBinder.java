// Generated code from Butter Knife. Do not modify!
package com.fgr.bmobdemo.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegistActivity$$ViewBinder<T extends com.fgr.bmobdemo.ui.RegistActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230728, "field 'ivAvatar' and method 'setAvatar'");
    target.ivAvatar = finder.castView(view, 2131230728, "field 'ivAvatar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAvatar(p0);
        }
      });
    view = finder.findRequiredView(source, 2131230729, "field 'etUsername'");
    target.etUsername = finder.castView(view, 2131230729, "field 'etUsername'");
    view = finder.findRequiredView(source, 2131230731, "field 'rgGender'");
    target.rgGender = finder.castView(view, 2131230731, "field 'rgGender'");
    view = finder.findRequiredView(source, 2131230730, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131230730, "field 'etPassword'");
    view = finder.findRequiredView(source, 2131230734, "method 'regist'");
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
    target.ivAvatar = null;
    target.etUsername = null;
    target.rgGender = null;
    target.etPassword = null;
  }
}
