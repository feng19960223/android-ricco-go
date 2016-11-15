// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SplashActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.SplashActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165218, "field 'tvMiao'");
    target.tvMiao = finder.castView(view, 2131165218, "field 'tvMiao'");
    view = finder.findRequiredView(source, 2131165219, "field 'tvXin'");
    target.tvXin = finder.castView(view, 2131165219, "field 'tvXin'");
  }

  @Override public void unbind(T target) {
    target.tvMiao = null;
    target.tvXin = null;
  }
}
