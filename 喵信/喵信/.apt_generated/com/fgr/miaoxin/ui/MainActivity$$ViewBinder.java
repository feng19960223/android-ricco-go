// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MainActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.MainActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165234, "field 'mtiFind' and method 'setCurrentFragment'");
    target.mtiFind = finder.castView(view, 2131165234, "field 'mtiFind'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165235, "field 'mtiSetting' and method 'setCurrentFragment'");
    target.mtiSetting = finder.castView(view, 2131165235, "field 'mtiSetting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165230, "field 'mtiMessage' and method 'setCurrentFragment'");
    target.mtiMessage = finder.castView(view, 2131165230, "field 'mtiMessage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165233, "field 'ivNewInvitation'");
    target.ivNewInvitation = finder.castView(view, 2131165233, "field 'ivNewInvitation'");
    view = finder.findRequiredView(source, 2131165231, "field 'bvCount'");
    target.bvCount = finder.castView(view, 2131165231, "field 'bvCount'");
    view = finder.findRequiredView(source, 2131165232, "field 'mtiFriend' and method 'setCurrentFragment'");
    target.mtiFriend = finder.castView(view, 2131165232, "field 'mtiFriend'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setCurrentFragment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165229, "field 'viewPager'");
    target.viewPager = finder.castView(view, 2131165229, "field 'viewPager'");
  }

  @Override public void unbind(T target) {
    target.mtiFind = null;
    target.mtiSetting = null;
    target.mtiMessage = null;
    target.ivNewInvitation = null;
    target.bvCount = null;
    target.mtiFriend = null;
    target.viewPager = null;
  }
}
