// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FindFragment$$ViewBinder<T extends com.fgr.miaoxin.fragment.FindFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165288, "field 'etComment'");
    target.etComment = finder.castView(view, 2131165288, "field 'etComment'");
    view = finder.findRequiredView(source, 2131165287, "field 'commentContainer'");
    target.commentContainer = finder.castView(view, 2131165287, "field 'commentContainer'");
    view = finder.findRequiredView(source, 2131165286, "field 'ptrListView'");
    target.ptrListView = finder.castView(view, 2131165286, "field 'ptrListView'");
    view = finder.findRequiredView(source, 2131165289, "method 'sendComment'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendComment(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.etComment = null;
    target.commentContainer = null;
    target.ptrListView = null;
  }
}
