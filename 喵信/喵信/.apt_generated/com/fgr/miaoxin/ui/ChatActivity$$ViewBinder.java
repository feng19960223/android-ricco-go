// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChatActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.ChatActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165222, "field 'moreContainer'");
    target.moreContainer = finder.castView(view, 2131165222, "field 'moreContainer'");
    view = finder.findRequiredView(source, 2131165221, "field 'btnSend' and method 'sendTextMessage'");
    target.btnSend = finder.castView(view, 2131165221, "field 'btnSend'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.sendTextMessage(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165209, "field 'listView'");
    target.listView = finder.castView(view, 2131165209, "field 'listView'");
    view = finder.findRequiredView(source, 2131165215, "field 'etContent'");
    target.etContent = finder.castView(view, 2131165215, "field 'etContent'");
    view = finder.findRequiredView(source, 2131165220, "field 'btnAdd' and method 'addAddLayout'");
    target.btnAdd = finder.castView(view, 2131165220, "field 'btnAdd'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.addAddLayout(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165216, "method 'addEmoLayout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.addEmoLayout(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.moreContainer = null;
    target.btnSend = null;
    target.listView = null;
    target.etContent = null;
    target.btnAdd = null;
  }
}
