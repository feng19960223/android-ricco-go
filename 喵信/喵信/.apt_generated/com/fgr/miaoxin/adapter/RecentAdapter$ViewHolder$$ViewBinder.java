// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecentAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.RecentAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165325, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165325, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165326, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165326, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165328, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131165328, "field 'tvContent'");
    view = finder.findRequiredView(source, 2131165329, "field 'bvCount'");
    target.bvCount = finder.castView(view, 2131165329, "field 'bvCount'");
    view = finder.findRequiredView(source, 2131165327, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131165327, "field 'tvTime'");
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.tvUsername = null;
    target.tvContent = null;
    target.bvCount = null;
    target.tvTime = null;
  }
}
