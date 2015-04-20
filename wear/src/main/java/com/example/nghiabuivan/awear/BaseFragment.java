package com.example.nghiabuivan.awear;

import android.app.Fragment;

public abstract class BaseFragment extends Fragment {

	protected FragmentTransitor m_fragmentTransitor;

	public BaseFragment setFragmentTransitor(FragmentTransitor ft) {
		m_fragmentTransitor = ft;
		return this;
	}
}
