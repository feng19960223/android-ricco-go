package com.tarena.fgr.fragment;

import com.tarena.fgr.youlu.R;

import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 基础的fragment的父类,所有fragment都要继承自该类,在类中封装公有的标题栏初始化方法,
 * 
 * 提供代码的重用性,降低冗余
 * 
 * @author 冯国芮
 * 
 *         2016年9月29日 14:49:53
 */
public class BaseFragment extends Fragment {
	protected View contentView = null;
	// 自定义标题
	protected LinearLayout actionbar = null;

	/**
	 * 通过此方法可以对actionbar进行设置,实现自定义的actionbar的初始设置<br>
	 * <br>
	 * leftId和rightId 如果传值为-1则表示隐藏<br>
	 * title 为null或""时表示隐藏
	 * 
	 * @param leftId
	 *            左边图片资源id
	 * @param title
	 *            中间标题的内容文本
	 * @param rightId
	 *            右边图片资源id
	 */
	protected void initialActionbar(int leftId, String title, int rightId) {
		if (actionbar == null) {// 如果没有actionbar直接返回,不进行设置
			return;
		}
		// 获得该标题栏中所有的控件
		ImageView imageViewLeft = (ImageView) actionbar
				.findViewById(R.id.imageView_actionbar_left);
		TextView textViewTitle = (TextView) actionbar
				.findViewById(R.id.textView_actionbar_title);
		ImageView imageViewRight = (ImageView) actionbar
				.findViewById(R.id.imageView_actionbar_right);
		// 左图片,如果传值为-1则表示隐藏
		if (leftId == -1) {
			imageViewLeft.setVisibility(View.INVISIBLE);
			// 2016年9月29日 19:32:02
			// 冯国芮:我对INVISIBLE和GONE进行了实际代码分析
			// 发现INVISIBLE只是隐藏掉了,android:layout_weight="1"这条属性,不会占用掉他的控件
			// 而GONE,不只是隐藏,而且android:layout_weight="1"这条属性,会占用掉他的空间
			// 所以GONE应该谨慎使用,又可能会出现布局title混乱的情况
		} else {
			imageViewLeft.setVisibility(View.VISIBLE);
			imageViewLeft.setImageResource(leftId);
		}
		// 中间文字,如果传空或""则隐藏
		if (TextUtils.isEmpty(title)) {
			textViewTitle.setVisibility(View.INVISIBLE);
		} else {
			textViewTitle.setVisibility(View.VISIBLE);
			textViewTitle.setText(title);
		}
		// 右图片,如果传值为-1则表示隐藏
		if (rightId == -1) {
			imageViewRight.setVisibility(View.INVISIBLE);
		} else {
			imageViewRight.setVisibility(View.VISIBLE);
			imageViewRight.setImageResource(rightId);
		}

	}
}
