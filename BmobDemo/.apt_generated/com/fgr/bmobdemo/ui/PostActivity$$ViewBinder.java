// Generated code from Butter Knife. Do not modify!
package com.fgr.bmobdemo.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PostActivity$$ViewBinder<T extends com.fgr.bmobdemo.ui.PostActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230726, "field 'btnPost' and method 'post'");
    target.btnPost = finder.castView(view, 2131230726, "field 'btnPost'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.post(p0);
        }
      });
    view = finder.findRequiredView(source, 2131230736, "field 'ivBack' and method 'backTo'");
    target.ivBack = finder.castView(view, 2131230736, "field 'ivBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.backTo(p0);
        }
      });
    view = finder.findRequiredView(source, 2131230724, "field 'etTitle'");
    target.etTitle = finder.castView(view, 2131230724, "field 'etTitle'");
    view = finder.findRequiredView(source, 2131230725, "field 'etContent'");
    target.etContent = finder.castView(view, 2131230725, "field 'etContent'");
    view = finder.findRequiredView(source, 2131230727, "field 'btnUpate' and method 'updatePost'");
    target.btnUpate = finder.castView(view, 2131230727, "field 'btnUpate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.updatePost(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.btnPost = null;
    target.ivBack = null;
    target.etTitle = null;
    target.etContent = null;
    target.btnUpate = null;
  }
}
