package com.fgr.miaoxin.listener;

import java.util.List;

public interface OnDatasLoadFinishListener<T> {
	void onLoadFinish(List<T> datas);
}
