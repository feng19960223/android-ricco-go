// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NearFriendActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.NearFriendActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165221, "field 'mMapView'");
    target.mMapView = finder.castView(view, 2131165221, "field 'mMapView'");
  }

  @Override public void unbind(T target) {
    target.mMapView = null;
  }
}
