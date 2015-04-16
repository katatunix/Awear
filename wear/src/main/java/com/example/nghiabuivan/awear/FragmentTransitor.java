package com.example.nghiabuivan.awear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentTransitor {

	private ViewDataSource m_viewDataSource;
	private final FragmentManager m_fragmentManager;

	private boolean m_first;

	public FragmentTransitor(FragmentManager fm) {
		m_viewDataSource = new ViewDataSource();
		m_fragmentManager = fm;
		m_first = true;
	}

	public void goToWelcome() {
		Fragment fragment = new WelcomeFragment()
				.setFragmentTransitor(this)
				.setViewDataSource(m_viewDataSource);
		go(fragment);
	}

	public void goToInApp() {
		Fragment fragment = new InAppFragment()
				.setFragmentTransitor(this)
				.setViewDataSource(m_viewDataSource);
		go(fragment);
	}

	private void go(Fragment fragment) {
		FragmentTransaction transaction = m_fragmentManager.beginTransaction();

		if (m_first) {
			transaction.add(R.id.activity_main, fragment);
			m_first = false;
		} else {
			transaction.replace(R.id.activity_main, fragment);
		}

		transaction.commit();
	}
}
