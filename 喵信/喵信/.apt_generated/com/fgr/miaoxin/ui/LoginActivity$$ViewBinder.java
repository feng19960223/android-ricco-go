// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165206, "field 'etUsername' and method 'recover2'");
    target.etUsername = finder.castView(view, 2131165206, "field 'etUsername'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.recover2(finder.<android.text.Editable>castParam(p0, "onTextChanged", 0, "recover2", 0));
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
    view = finder.findRequiredView(source, 2131165207, "field 'etPassword' and method 'recover1'");
    target.etPassword = finder.castView(view, 2131165207, "field 'etPassword'");
    ((android.widget.TextView) view).addTextChangedListener(
      new android.text.TextWatcher() {
        @Override public void onTextChanged(
          java.lang.CharSequence p0,
          int p1,
          int p2,
          int p3
        ) {
          target.recover1(finder.<android.text.Editable>castParam(p0, "onTextChanged", 0, "recover1", 0));
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
    view = finder.findRequiredView(source, 2131165208, "field 'btnLogin' and method 'login'");
    target.btnLogin = finder.castView(view, 2131165208, "field 'btnLogin'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.login(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165209, "method 'regist'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.regist(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.etUsername = null;
    target.etPassword = null;
    target.btnLogin = null;
  }
}
