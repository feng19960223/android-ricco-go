// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.SettingFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165235, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165235, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165240, "field 'ivEditorSound' and method 'setSound'");
    target.ivEditorSound = finder.castView(view, 2131165240, "field 'ivEditorSound'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setSound(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165238, "field 'ivEditorNotification' and method 'setNotification'");
    target.ivEditorNotification = finder.castView(view, 2131165238, "field 'ivEditorNotification'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNotification(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165237, "field 'tvNotification'");
    target.tvNotification = finder.castView(view, 2131165237, "field 'tvNotification'");
    view = finder.findRequiredView(source, 2131165242, "field 'ivEditorVibrate' and method 'setVibrate'");
    target.ivEditorVibrate = finder.castView(view, 2131165242, "field 'ivEditorVibrate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setVibrate(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165241, "field 'tvVibrate'");
    target.tvVibrate = finder.castView(view, 2131165241, "field 'tvVibrate'");
    view = finder.findRequiredView(source, 2131165239, "field 'tvSound'");
    target.tvSound = finder.castView(view, 2131165239, "field 'tvSound'");
    view = finder.findRequiredView(source, 2131165243, "method 'logout'");
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
    target.tvUsername = null;
    target.ivEditorSound = null;
    target.ivEditorNotification = null;
    target.tvNotification = null;
    target.ivEditorVibrate = null;
    target.tvVibrate = null;
    target.tvSound = null;
  }
}
