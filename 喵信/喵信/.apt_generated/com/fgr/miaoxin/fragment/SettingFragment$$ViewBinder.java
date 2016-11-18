// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.SettingFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165245, "field 'ivNotification' and method 'setNotification'");
    target.ivNotification = finder.castView(view, 2131165245, "field 'ivNotification'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNotification(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165244, "field 'tvNotification'");
    target.tvNotification = finder.castView(view, 2131165244, "field 'tvNotification'");
    view = finder.findRequiredView(source, 2131165247, "field 'ivSound' and method 'setSound'");
    target.ivSound = finder.castView(view, 2131165247, "field 'ivSound'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setSound(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165248, "field 'tvVibrate'");
    target.tvVibrate = finder.castView(view, 2131165248, "field 'tvVibrate'");
    view = finder.findRequiredView(source, 2131165246, "field 'tvSound'");
    target.tvSound = finder.castView(view, 2131165246, "field 'tvSound'");
    view = finder.findRequiredView(source, 2131165249, "field 'ivVibrate' and method 'setVibrate'");
    target.ivVibrate = finder.castView(view, 2131165249, "field 'ivVibrate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setVibrate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165242, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165242, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165250, "method 'logout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logout(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivNotification = null;
    target.tvNotification = null;
    target.ivSound = null;
    target.tvVibrate = null;
    target.tvSound = null;
    target.ivVibrate = null;
    target.tvUsername = null;
  }
}
