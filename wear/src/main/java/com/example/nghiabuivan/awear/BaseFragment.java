package com.example.nghiabuivan.awear;

import android.app.Fragment;

public abstract class BaseFragment extends Fragment {

	protected ViewDataSource m_viewDataSource;
	protected FragmentTransitor m_fragmentTransitor;

	public BaseFragment setViewDataSource(ViewDataSource vds) {
		m_viewDataSource = vds;
		return this;
	}

	public BaseFragment setFragmentTransitor(FragmentTransitor ft) {
		m_fragmentTransitor = ft;
		return this;
	}
}
