// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.SettingFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165278, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165278, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165282, "field 'tvSound'");
    target.tvSound = finder.castView(view, 2131165282, "field 'tvSound'");
    view = finder.findRequiredView(source, 2131165283, "field 'ivSound' and method 'setSound'");
    target.ivSound = finder.castView(view, 2131165283, "field 'ivSound'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setSound(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165280, "field 'tvNotification'");
    target.tvNotification = finder.castView(view, 2131165280, "field 'tvNotification'");
    view = finder.findRequiredView(source, 2131165281, "field 'ivNotification' and method 'setNotification'");
    target.ivNotification = finder.castView(view, 2131165281, "field 'ivNotification'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNotification(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165284, "field 'tvVibrate'");
    target.tvVibrate = finder.castView(view, 2131165284, "field 'tvVibrate'");
    view = finder.findRequiredView(source, 2131165285, "field 'ivVibrate' and method 'setVibrate'");
    target.ivVibrate = finder.castView(view, 2131165285, "field 'ivVibrate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setVibrate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165286, "method 'logout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.logout(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165279, "method 'setUserInfo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setUserInfo(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.tvUsername = null;
    target.tvSound = null;
    target.ivSound = null;
    target.tvNotification = null;
    target.ivNotification = null;
    target.tvVibrate = null;
    target.ivVibrate = null;
  }
}
