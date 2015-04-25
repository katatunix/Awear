package com.example.nghiabuivan.awear;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.example.nghiabuivan.awear.client.CAwear;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Log.d("AWear", "local dir path: " + getFilesDir().getAbsolutePath());
		CAwear.createInstance(getFilesDir().getAbsolutePath() + '/', this);

		FragmentTransitor ft = new FragmentTransitor(getFragmentManager(), R.id.activity_main);
		ft.goToWelcome();

		CAwear.getInstance().connect();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		CAwear.getInstance().disconnect();
	}
}
