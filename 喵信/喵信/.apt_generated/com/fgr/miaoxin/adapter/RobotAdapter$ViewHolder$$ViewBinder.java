// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RobotAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.RobotAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165265, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165265, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165266, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131165266, "field 'tvContent'");
    view = finder.findRequiredView(source, 2131165264, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131165264, "field 'tvTime'");
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.tvContent = null;
    target.tvTime = null;
  }
}
