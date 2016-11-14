package com.fgr.miaoxin.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.fgr.miaoxin.R;
import com.fgr.miaoxin.app.MyApp;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toast.makeText(
				this,
				MyApp.lastPoint.getLatitude() + " / "
						+ MyApp.lastPoint.getLongitude(), Toast.LENGTH_SHORT)
				.show();
	}

}
