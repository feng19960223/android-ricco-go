// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class InvitationAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.InvitationAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165325, "field 'tvAdd'");
    target.tvAdd = finder.castView(view, 2131165325, "field 'tvAdd'");
    view = finder.findRequiredView(source, 2131165322, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165322, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165321, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165321, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165323, "field 'ibAgree'");
    target.ibAgree = finder.castView(view, 2131165323, "field 'ibAgree'");
    view = finder.findRequiredView(source, 2131165324, "field 'ibReject'");
    target.ibReject = finder.castView(view, 2131165324, "field 'ibReject'");
  }

  @Override public void unbind(T target) {
    target.tvAdd = null;
    target.tvUsername = null;
    target.ivAvatar = null;
    target.ibAgree = null;
    target.ibReject = null;
  }
}
