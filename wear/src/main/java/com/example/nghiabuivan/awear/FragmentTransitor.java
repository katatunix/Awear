package com.example.nghiabuivan.awear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

public class FragmentTransitor {

	private final FragmentManager m_fragmentManager;
	private final int m_containerViewId;

	public FragmentTransitor(FragmentManager fm, int containerViewId) {
		m_fragmentManager = fm;
		m_containerViewId = containerViewId;
	}

	public void goToWelcome() {
		Fragment fragment = new WelcomeFragment().setFragmentTransitor(this);
		go(fragment);
	}

	public void goToInApp() {
		Fragment fragment = new InAppFragment().setFragmentTransitor(this);
		go(fragment);
	}

	private void go(Fragment fragment) {
		FragmentTransaction transaction = m_fragmentManager.beginTransaction();
		transaction.replace(m_containerViewId, fragment);
		transaction.commit();
	}
}
