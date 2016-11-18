// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.FriendAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165262, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165262, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165263, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165263, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165261, "field 'tvLetter'");
    target.tvLetter = finder.castView(view, 2131165261, "field 'tvLetter'");
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.tvUsername = null;
    target.tvLetter = null;
  }
}
