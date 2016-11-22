// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RobotAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.RobotAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165296, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131165296, "field 'tvContent'");
    view = finder.findRequiredView(source, 2131165294, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131165294, "field 'tvTime'");
    view = finder.findRequiredView(source, 2131165295, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165295, "field 'ivAvatar'");
  }

  @Override public void unbind(T target) {
    target.tvContent = null;
    target.tvTime = null;
    target.ivAvatar = null;
  }
}
