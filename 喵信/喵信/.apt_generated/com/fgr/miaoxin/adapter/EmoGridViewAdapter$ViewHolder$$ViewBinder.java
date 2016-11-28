// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class EmoGridViewAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.EmoGridViewAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165330, "field 'ivEmo'");
    target.ivEmo = finder.castView(view, 2131165330, "field 'ivEmo'");
  }

  @Override public void unbind(T target) {
    target.ivEmo = null;
  }
}
