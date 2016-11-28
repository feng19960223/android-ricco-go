// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PostBlogActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.PostBlogActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165242, "field 'ivBlogImg2'");
    target.ivBlogImg2 = finder.castView(view, 2131165242, "field 'ivBlogImg2'");
    view = finder.findRequiredView(source, 2131165245, "field 'ivBlogDel3' and method 'deletBlogImages'");
    target.ivBlogDel3 = finder.castView(view, 2131165245, "field 'ivBlogDel3'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deletBlogImages(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165243, "field 'ivBlogDel2' and method 'deletBlogImages'");
    target.ivBlogDel2 = finder.castView(view, 2131165243, "field 'ivBlogDel2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deletBlogImages(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165238, "field 'etContent'");
    target.etContent = finder.castView(view, 2131165238, "field 'etContent'");
    view = finder.findRequiredView(source, 2131165240, "field 'ivBlogImg1'");
    target.ivBlogImg1 = finder.castView(view, 2131165240, "field 'ivBlogImg1'");
    view = finder.findRequiredView(source, 2131165248, "field 'tvImageNumber'");
    target.tvImageNumber = finder.castView(view, 2131165248, "field 'tvImageNumber'");
    view = finder.findRequiredView(source, 2131165244, "field 'ivBlogImg3'");
    target.ivBlogImg3 = finder.castView(view, 2131165244, "field 'ivBlogImg3'");
    view = finder.findRequiredView(source, 2131165252, "field 'ivCamera'");
    target.ivCamera = finder.castView(view, 2131165252, "field 'ivCamera'");
    view = finder.findRequiredView(source, 2131165253, "field 'ivLocation'");
    target.ivLocation = finder.castView(view, 2131165253, "field 'ivLocation'");
    view = finder.findRequiredView(source, 2131165250, "field 'ivPlus' and method 'setButtonsVisible'");
    target.ivPlus = finder.castView(view, 2131165250, "field 'ivPlus'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setButtonsVisible(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165249, "field 'npbProgressBar'");
    target.npbProgressBar = finder.castView(view, 2131165249, "field 'npbProgressBar'");
    view = finder.findRequiredView(source, 2131165251, "field 'ivPicture' and method 'selectPicture'");
    target.ivPicture = finder.castView(view, 2131165251, "field 'ivPicture'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.selectPicture(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165239, "field 'llImageContainer'");
    target.llImageContainer = finder.castView(view, 2131165239, "field 'llImageContainer'");
    view = finder.findRequiredView(source, 2131165246, "field 'ivBlogImg4'");
    target.ivBlogImg4 = finder.castView(view, 2131165246, "field 'ivBlogImg4'");
    view = finder.findRequiredView(source, 2131165247, "field 'ivBlogDel4' and method 'deletBlogImages'");
    target.ivBlogDel4 = finder.castView(view, 2131165247, "field 'ivBlogDel4'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deletBlogImages(p0);
        }
      });
    view = finder.findRequiredView(source, 2131165241, "field 'ivBlogDel1' and method 'deletBlogImages'");
    target.ivBlogDel1 = finder.castView(view, 2131165241, "field 'ivBlogDel1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.deletBlogImages(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.ivBlogImg2 = null;
    target.ivBlogDel3 = null;
    target.ivBlogDel2 = null;
    target.etContent = null;
    target.ivBlogImg1 = null;
    target.tvImageNumber = null;
    target.ivBlogImg3 = null;
    target.ivCamera = null;
    target.ivLocation = null;
    target.ivPlus = null;
    target.npbProgressBar = null;
    target.ivPicture = null;
    target.llImageContainer = null;
    target.ivBlogImg4 = null;
    target.ivBlogDel4 = null;
    target.ivBlogDel1 = null;
  }
}
