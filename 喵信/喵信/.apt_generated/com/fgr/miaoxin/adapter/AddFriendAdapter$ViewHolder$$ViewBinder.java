// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddFriendAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.AddFriendAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165314, "field 'tvAdd'");
    target.tvAdd = finder.castView(view, 2131165314, "field 'tvAdd'");
    view = finder.findRequiredView(source, 2131165313, "field 'btnAdd'");
    target.btnAdd = finder.castView(view, 2131165313, "field 'btnAdd'");
    view = finder.findRequiredView(source, 2131165311, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165311, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165312, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165312, "field 'tvUsername'");
  }

  @Override public void unbind(T target) {
    target.tvAdd = null;
    target.btnAdd = null;
    target.ivAvatar = null;
    target.tvUsername = null;
  }
}
