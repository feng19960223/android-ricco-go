// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RobotAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.RobotAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165312, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165312, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165311, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131165311, "field 'tvTime'");
    view = finder.findRequiredView(source, 2131165315, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131165315, "field 'tvContent'");
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.tvTime = null;
    target.tvContent = null;
  }
}
