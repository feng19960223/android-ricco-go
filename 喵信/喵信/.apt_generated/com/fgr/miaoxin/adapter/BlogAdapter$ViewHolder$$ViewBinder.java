// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BlogAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.BlogAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165317, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131165317, "field 'tvContent'");
    view = finder.findRequiredView(source, 2131165321, "field 'tvLove'");
    target.tvLove = finder.castView(view, 2131165321, "field 'tvLove'");
    view = finder.findRequiredView(source, 2131165320, "field 'tvShare'");
    target.tvShare = finder.castView(view, 2131165320, "field 'tvShare'");
    view = finder.findRequiredView(source, 2131165322, "field 'tvComment'");
    target.tvComment = finder.castView(view, 2131165322, "field 'tvComment'");
    view = finder.findRequiredView(source, 2131165319, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131165319, "field 'tvTime'");
    view = finder.findRequiredView(source, 2131165318, "field 'imageContainer'");
    target.imageContainer = finder.castView(view, 2131165318, "field 'imageContainer'");
    view = finder.findRequiredView(source, 2131165316, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165316, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165323, "field 'commentContainer'");
    target.commentContainer = finder.castView(view, 2131165323, "field 'commentContainer'");
    view = finder.findRequiredView(source, 2131165315, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165315, "field 'ivAvatar'");
  }

  @Override public void unbind(T target) {
    target.tvContent = null;
    target.tvLove = null;
    target.tvShare = null;
    target.tvComment = null;
    target.tvTime = null;
    target.imageContainer = null;
    target.tvUsername = null;
    target.commentContainer = null;
    target.ivAvatar = null;
  }
}
