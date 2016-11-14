// Generated code from Butter Knife. Do not modify!
package com.fgr.bmobdemo.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PostActivity$$ViewBinder<T extends com.fgr.bmobdemo.ui.PostActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131230725, "field 'etContent' and method 'showUsers'");
    target.etContent = finder.castView(view, 2131230725, "field 'etContent'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.showUsers(finder.<android.text.Editable>castParam(p0, "onTextChanged", 0, "showUsers", 0));
        }
        @Override public void beforeTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          
        }
        @Override public void afterTextChanged(
          android.text.Editable p0
        ) {
          
        }
      });
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
    view = finder.findRequiredView(source, 2131230724, "field 'etTitle'");
    target.etTitle = finder.castView(view, 2131230724, "field 'etTitle'");
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
    view = finder.findRequiredView(source, 2131230737, "field 'ivBack' and method 'backTo'");
    target.ivBack = finder.castView(view, 2131230737, "field 'ivBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.backTo(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.etContent = null;
    target.btnUpate = null;
    target.etTitle = null;
    target.btnPost = null;
    target.ivBack = null;
  }
}
