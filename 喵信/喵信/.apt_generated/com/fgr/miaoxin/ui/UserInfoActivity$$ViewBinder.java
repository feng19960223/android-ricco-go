// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserInfoActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.UserInfoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165258, "field 'ivNicknameEditor' and method 'setNickname'");
    target.ivNicknameEditor = finder.castView(view, 2131165258, "field 'ivNicknameEditor'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165259, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165259, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165263, "field 'btnBlack'");
    target.btnBlack = finder.castView(view, 2131165263, "field 'btnBlack'");
    view = finder.findRequiredView(source, 2131165253, "field 'tvNickname'");
    target.tvNickname = finder.castView(view, 2131165253, "field 'tvNickname'");
    view = finder.findRequiredView(source, 2131165251, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165251, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165260, "field 'ivGender'");
    target.ivGender = finder.castView(view, 2131165260, "field 'ivGender'");
    view = finder.findRequiredView(source, 2131165254, "field 'llNicknameContainer'");
    target.llNicknameContainer = finder.castView(view, 2131165254, "field 'llNicknameContainer'");
    view = finder.findRequiredView(source, 2131165261, "field 'btnUpdate' and method 'update'");
    target.btnUpdate = finder.castView(view, 2131165261, "field 'btnUpdate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.update(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165257, "field 'ibCancel' and method 'cancelNickname'");
    target.ibCancel = finder.castView(view, 2131165257, "field 'ibCancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.cancelNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165252, "field 'ivAvatarEditor' and method 'setAvatar'");
    target.ivAvatarEditor = finder.castView(view, 2131165252, "field 'ivAvatarEditor'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAvatar(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165255, "field 'etNickname'");
    target.etNickname = finder.castView(view, 2131165255, "field 'etNickname'");
    view = finder.findRequiredView(source, 2131165256, "field 'ibConfirm' and method 'saveNickname'");
    target.ibConfirm = finder.castView(view, 2131165256, "field 'ibConfirm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165262, "field 'btnChat' and method 'chat'");
    target.btnChat = finder.castView(view, 2131165262, "field 'btnChat'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.chat(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivNicknameEditor = null;
    target.tvUsername = null;
    target.btnBlack = null;
    target.tvNickname = null;
    target.ivAvatar = null;
    target.ivGender = null;
    target.llNicknameContainer = null;
    target.btnUpdate = null;
    target.ibCancel = null;
    target.ivAvatarEditor = null;
    target.etNickname = null;
    target.ibConfirm = null;
    target.btnChat = null;
  }
}
