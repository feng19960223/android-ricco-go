// Generated code from Butter Knife. Do not modify!
package com.fgr.bmobdemo.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PostAdapter$ViewHolder$$ViewBinder<T extends com.fgr.bmobdemo.adapter.PostAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230747, "field 'tvUpdate'");
    target.tvUpdate = finder.castView(view, 2131230747, "field 'tvUpdate'");
    view = finder.findRequiredView(source, 2131230748, "field 'tvDelete'");
    target.tvDelete = finder.castView(view, 2131230748, "field 'tvDelete'");
    view = finder.findRequiredView(source, 2131230743, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131230743, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131230745, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131230745, "field 'tvTime'");
    view = finder.findRequiredView(source, 2131230746, "field 'tvContent'");
    target.tvContent = finder.castView(view, 2131230746, "field 'tvContent'");
    view = finder.findRequiredView(source, 2131230742, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131230742, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131230744, "field 'tvTitle'");
    target.tvTitle = finder.castView(view, 2131230744, "field 'tvTitle'");
  }

  @Override public void unbind(T target) {
    target.tvUpdate = null;
    target.tvDelete = null;
    target.tvUsername = null;
    target.tvTime = null;
    target.tvContent = null;
    target.ivAvatar = null;
    target.tvTitle = null;
  }
}
