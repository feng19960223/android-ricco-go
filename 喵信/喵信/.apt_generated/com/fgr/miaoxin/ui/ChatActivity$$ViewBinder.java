// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChatActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.ChatActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165210, "field 'voiceContainer'");
    target.voiceContainer = finder.castView(view, 2131165210, "field 'voiceContainer'");
    view = finder.findRequiredView(source, 2131165215, "field 'etContent'");
    target.etContent = finder.castView(view, 2131165215, "field 'etContent'");
    view = finder.findRequiredView(source, 2131165222, "field 'moreContainer'");
    target.moreContainer = finder.castView(view, 2131165222, "field 'moreContainer'");
    view = finder.findRequiredView(source, 2131165211, "field 'ivVolume'");
    target.ivVolume = finder.castView(view, 2131165211, "field 'ivVolume'");
    view = finder.findRequiredView(source, 2131165209, "field 'listView'");
    target.listView = finder.castView(view, 2131165209, "field 'listView'");
    view = finder.findRequiredView(source, 2131165219, "field 'btnSpeak' and method 'speak'");
    target.btnSpeak = finder.castView(view, 2131165219, "field 'btnSpeak'");
    view.setOnTouchListener(
      new android.view.View.OnTouchListener() {
        @Override public boolean onTouch(
          android.view.View p0,
          android.view.MotionEvent p1
        ) {
          return target.speak(p0, p1);
        }
      });
    view = finder.findRequiredView(source, 2131165217, "field 'voiceinputContainer'");
    target.voiceinputContainer = finder.castView(view, 2131165217, "field 'voiceinputContainer'");
    view = finder.findRequiredView(source, 2131165212, "field 'tvTip'");
    target.tvTip = finder.castView(view, 2131165212, "field 'tvTip'");
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
    view = finder.findRequiredView(source, 2131165213, "field 'textinputContainer'");
    target.textinputContainer = finder.castView(view, 2131165213, "field 'textinputContainer'");
    view = finder.findRequiredView(source, 2131165218, "method 'showTextInputContainer'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showTextInputContainer(p0);
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
    view = finder.findRequiredView(source, 2131165214, "method 'showVoiceInputContaienr'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.showVoiceInputContaienr(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.voiceContainer = null;
    target.etContent = null;
    target.moreContainer = null;
    target.ivVolume = null;
    target.listView = null;
    target.btnSpeak = null;
    target.voiceinputContainer = null;
    target.tvTip = null;
    target.btnSend = null;
    target.btnAdd = null;
    target.textinputContainer = null;
  }
}
