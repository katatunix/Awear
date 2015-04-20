package com.example.nghiabuivan.awear;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.example.nghiabuivan.awear.client.CAwear;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// TODO: get local dir path of the app
		CAwear.createInstance("/sdcard/awear_dir/", this);

		FragmentTransitor ft = new FragmentTransitor(getFragmentManager(), R.id.activity_main);
		ft.goToWelcome();
	}

	@Override
	protected void onStart() {
		super.onStart();
		CAwear.getInstance().connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
		CAwear.getInstance().disconnect();
	}
}
