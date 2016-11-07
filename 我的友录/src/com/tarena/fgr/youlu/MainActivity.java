package com.tarena.fgr.youlu;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.tarena.fgr.adapter.MyFragmentPagerAdapter;
import com.tarena.fgr.fragment.CalllogFragment;
import com.tarena.fgr.fragment.ContactFragment;
import com.tarena.fgr.fragment.DialpadFragment;
import com.tarena.fgr.fragment.SmsFragment;

// 继承FragmentActivity
public class MainActivity extends FragmentActivity implements
		OnPageChangeListener, OnCheckedChangeListener {
	private ViewPager viewPager = null;
	private MyFragmentPagerAdapter pageradapter = null;// 数据适配器
	private Fragment calllogFragment = null;// 通话记录
	private Fragment smsFragment = null;// 发信息
	private Fragment contactFragment = null;// 联系人
	private Fragment dialpadFragment = null;// 拨号
	private RadioGroup bottomRadioGroup = null;// 选择按钮

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化所有的Fragment
		initialFragments();
		// 初始化RadioGroup
		initialRadioGroup();

		ActivityManager aManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		// 获得手机为应用程序分配的堆内存大小
		MEMORY_MAX_SIZE = aManager.getMemoryClass() * 1024 * 1024 / 8;
	}

	public static int MEMORY_MAX_SIZE = 0;

	private void initialRadioGroup() {
		bottomRadioGroup = (RadioGroup) findViewById(R.id.radioGroup_bottom);
		// 给RadioGroup添加选择发生变化时的监听事件
		// 2016年9月29日 17:26:55
		// 冯国芮:设置监听的时候,请注意:会有两个,选择RadioGroup的那个,输入是个小问题,但出错了会很丢人的...
		bottomRadioGroup.setOnCheckedChangeListener(this);
	}

	private void initialFragments() {
		// 初始化viewpage
		viewPager = (ViewPager) findViewById(R.id.viewPager_main);
		// 创建数据适配器
		FragmentManager fm = getSupportFragmentManager();
		pageradapter = new MyFragmentPagerAdapter(fm);

		// 创建4个Fragment对象
		calllogFragment = new CalllogFragment();
		smsFragment = new SmsFragment();
		contactFragment = new ContactFragment();
		dialpadFragment = new DialpadFragment();

		// 将4个Fragment对象添加到适配器的集合中
		// 2016年9月29日 16:43:16
		// 冯国芮:这个地方我认为是不合适的,如果添加顺序出错,会出现页面不对的情况
		pageradapter.addFragment(calllogFragment);
		pageradapter.addFragment(contactFragment);
		pageradapter.addFragment(smsFragment);
		pageradapter.addFragment(dialpadFragment);

		// 将数据适配器和viewpager绑定
		viewPager.setAdapter(pageradapter);
		viewPager.setCurrentItem(1);// 默认显示第1个界面
		// 给viewpager设置滑动时的监听事件
		viewPager.setOnPageChangeListener(this);
	}

	/**
	 * RaidoGroup的选择事件监听
	 * */
	@Override
	// RadioGroup的选项发生改变时
	// 当我们选择不同的单项按钮时,改变viewpager中当前显示的fragment
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// 2016年9月29日 19:22:04
		// 冯国芮:对switch的代码大量重复问题,我有了新的解释,对于RadioGroup来说,选项是不会太多的,所以可以接收switch
		// 如果选项很多,我们可以选择ListView来实现
		switch (checkedId) {
		case R.id.radio_calllog:// 通话记
			// viewPager.setCurrentItem(0);
			viewPager.setCurrentItem(0, false);
			// 2016年9月29日 17:35:08
			// 冯国芮:设置他的滑动方式,false没有滑动感
			// true会有滑动感,我认为设置false是好的,没有动画效果,但这样反而显的高大上,冷酷
			// 而且市场上大多是false*****
			break;
		case R.id.radio_contact:// 联系人
			viewPager.setCurrentItem(1, false);
			break;
		case R.id.radio_sms:// 短信息
			viewPager.setCurrentItem(2, false);
			break;
		case R.id.radio_dialpad:// 拨号
			viewPager.setCurrentItem(3, false);
			break;
		default:
			break;
		}
		// 2016年9月29日 19:17:10
		// 冯国芮:结合同桌的想法,可以通过RadioBtuuon的位置,确定ViewPager的位置,而且代码根据阶段
		// 但我发现,这个方法会新构建Button,这有可能销毁内存...出现了findViewById这样不利于程序的优化
		RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
		int index = group.indexOfChild(radioButton);
		// indexofchild和getchildat()常用
		viewPager.setCurrentItem(index, false);

		// if (checkedId == R.id.radio_calllog) {// 通话记录
		// viewPager.setCurrentItem(0);
		// } else if (checkedId == R.id.radio_contact) {// 联系人
		// viewPager.setCurrentItem(1);
		// } else if (checkedId == R.id.radio_sms) {// 短信息
		// viewPager.setCurrentItem(2);
		// } else if (checkedId == R.id.radio_dialpad) {// 拨号
		// viewPager.setCurrentItem(3);
		// }
	}

	/**
	 * viewpager的滑动事件监听
	 * */
	@Override
	// 选择页面时执行
	public void onPageScrolled(int position, float positionOffset,
			int positionOffsetPixels) {
		// 2016年9月29日 17:03:02
		// 冯国芮:我在这里设置了下面这条语句,如果发现很好玩的事情,页面点击按钮是无法滚动的
		// ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
	}

	@Override
	// 滑动完毕执行
	// 当我们在viewpager中选择不同的页面时,会回调此方法
	// 方法被回调的时候会把页面在集合中的索引传回来
	public void onPageSelected(int position) {
		// 2016年9月29日 17:10:48
		// 冯国芮:下面的这句是我写的滑动监听,但老师写成了swith,我写了一行,而老师却写了多行,不知道为什么?
		// 难度是后面还要加代码?方便以后扩展?
		// 而我认为我的代码是优秀的,因为以后如果添加或删除了radiobutton,我不需要修改这里的代码
		// ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
		switch (position) {
		case 0:
			bottomRadioGroup.check(R.id.radio_calllog);
			break;
		case 1:
			bottomRadioGroup.check(R.id.radio_contact);
			break;
		case 2:
			bottomRadioGroup.check(R.id.radio_sms);
			break;
		case 3:
			bottomRadioGroup.check(R.id.radio_dialpad);
			break;
		default:
			break;
		}
	}

	@Override
	// 正在滑动改变时执行
	public void onPageScrollStateChanged(int state) {
	}
}
