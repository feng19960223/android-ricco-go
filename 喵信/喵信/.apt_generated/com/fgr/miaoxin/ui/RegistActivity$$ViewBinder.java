// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegistActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.RegistActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165223, "field 'etRePassword'");
    target.etRePassword = finder.castView(view, 2131165223, "field 'etRePassword'");
    view = finder.findRequiredView(source, 2131165221, "field 'etUsername' and method 'recover'");
    target.etUsername = finder.castView(view, 2131165221, "field 'etUsername'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.recover(finder.<android.text.Editable>castParam(p0, "onTextChanged", 0, "recover", 0));
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
    view = finder.findRequiredView(source, 2131165227, "field 'btnRegist' and method 'regist'");
    target.btnRegist = finder.castView(view, 2131165227, "field 'btnRegist'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.regist(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165224, "field 'rgGender'");
    target.rgGender = finder.castView(view, 2131165224, "field 'rgGender'");
    view = finder.findRequiredView(source, 2131165222, "field 'etPassword'");
    target.etPassword = finder.castView(view, 2131165222, "field 'etPassword'");
  }

  @Override public void unbind(T target) {
    target.etRePassword = null;
    target.etUsername = null;
    target.btnRegist = null;
    target.rgGender = null;
    target.etPassword = null;
  }
}
