// Generated code from Butter Knife. Do not modify!
package com.fgr.miaoxin.ui;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LocationActivity$$ViewBinder<T extends com.fgr.miaoxin.ui.LocationActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131165223, "field 'mapView'");
    target.mapView = finder.castView(view, 2131165223, "field 'mapView'");
  }

  @Override public void unbind(T target) {
    target.mapView = null;
  }
}
