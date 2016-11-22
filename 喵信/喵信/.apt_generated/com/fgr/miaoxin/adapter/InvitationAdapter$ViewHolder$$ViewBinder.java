// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class InvitationAdapter$ViewHolder$$ViewBinder<T extends com.fgr.miaoxin.adapter.InvitationAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165284, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165284, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165286, "field 'ibAgree'");
    target.ibAgree = finder.castView(view, 2131165286, "field 'ibAgree'");
    view = finder.findRequiredView(source, 2131165285, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165285, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165288, "field 'tvAdd'");
    target.tvAdd = finder.castView(view, 2131165288, "field 'tvAdd'");
    view = finder.findRequiredView(source, 2131165287, "field 'ibReject'");
    target.ibReject = finder.castView(view, 2131165287, "field 'ibReject'");
  }

  @Override public void unbind(T target) {
    target.ivAvatar = null;
    target.ibAgree = null;
    target.tvUsername = null;
    target.tvAdd = null;
    target.ibReject = null;
  }
}
