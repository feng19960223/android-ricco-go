// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165218, "field 'ivNewInvitation'");
    target.ivNewInvitation = finder.castView(view, 2131165218, "field 'ivNewInvitation'");
    view = finder.findRequiredView(source, 2131165219, "field 'mtiFind' and method 'setCurrentFragment'");
    target.mtiFind = finder.castView(view, 2131165219, "field 'mtiFind'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165215, "field 'mtiMessage' and method 'setCurrentFragment'");
    target.mtiMessage = finder.castView(view, 2131165215, "field 'mtiMessage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165214, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131165214, "field 'viewPager'");
    view = finder.findRequiredView(source, 2131165217, "field 'mtiFriend' and method 'setCurrentFragment'");
    target.mtiFriend = finder.castView(view, 2131165217, "field 'mtiFriend'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165220, "field 'mtiSetting' and method 'setCurrentFragment'");
    target.mtiSetting = finder.castView(view, 2131165220, "field 'mtiSetting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165216, "field 'bvCount'");
    target.bvCount = finder.castView(view, 2131165216, "field 'bvCount'");
  }

  @Override public void unbind(T target) {
    target.ivNewInvitation = null;
    target.mtiFind = null;
    target.mtiMessage = null;
    target.viewPager = null;
    target.mtiFriend = null;
    target.mtiSetting = null;
    target.bvCount = null;
  }
}
