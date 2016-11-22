// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserInfoActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.UserInfoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165248, "field 'btnBlack'");
    target.btnBlack = finder.castView(view, 2131165248, "field 'btnBlack'");
    view = finder.findRequiredView(source, 2131165241, "field 'ibConfirm' and method 'saveNickname'");
    target.ibConfirm = finder.castView(view, 2131165241, "field 'ibConfirm'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.saveNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165244, "field 'tvUsername'");
    target.tvUsername = finder.castView(view, 2131165244, "field 'tvUsername'");
    view = finder.findRequiredView(source, 2131165237, "field 'ivAvatarEditor' and method 'setAvatar'");
    target.ivAvatarEditor = finder.castView(view, 2131165237, "field 'ivAvatarEditor'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAvatar(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165242, "field 'ibCancel' and method 'cancelNickname'");
    target.ibCancel = finder.castView(view, 2131165242, "field 'ibCancel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.cancelNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165238, "field 'tvNickname'");
    target.tvNickname = finder.castView(view, 2131165238, "field 'tvNickname'");
    view = finder.findRequiredView(source, 2131165239, "field 'llNicknameContainer'");
    target.llNicknameContainer = finder.castView(view, 2131165239, "field 'llNicknameContainer'");
    view = finder.findRequiredView(source, 2131165246, "field 'btnUpdate' and method 'update'");
    target.btnUpdate = finder.castView(view, 2131165246, "field 'btnUpdate'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.update(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165236, "field 'ivAvatar'");
    target.ivAvatar = finder.castView(view, 2131165236, "field 'ivAvatar'");
    view = finder.findRequiredView(source, 2131165245, "field 'ivGender'");
    target.ivGender = finder.castView(view, 2131165245, "field 'ivGender'");
    view = finder.findRequiredView(source, 2131165243, "field 'ivNicknameEditor' and method 'setNickname'");
    target.ivNicknameEditor = finder.castView(view, 2131165243, "field 'ivNicknameEditor'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setNickname(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165240, "field 'etNickname'");
    target.etNickname = finder.castView(view, 2131165240, "field 'etNickname'");
    view = finder.findRequiredView(source, 2131165247, "field 'btnChat' and method 'chat'");
    target.btnChat = finder.castView(view, 2131165247, "field 'btnChat'");
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
    target.btnBlack = null;
    target.ibConfirm = null;
    target.tvUsername = null;
    target.ivAvatarEditor = null;
    target.ibCancel = null;
    target.tvNickname = null;
    target.llNicknameContainer = null;
    target.btnUpdate = null;
    target.ivAvatar = null;
    target.ivGender = null;
    target.ivNicknameEditor = null;
    target.etNickname = null;
    target.btnChat = null;
  }
}
