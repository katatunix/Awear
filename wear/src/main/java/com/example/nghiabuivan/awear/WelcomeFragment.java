package com.example.nghiabuivan.awear;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WelcomeFragment extends BaseFragment {

	private Button m_buttonEnter;
	private Button m_buttonSync;
	private TextView m_textSyncResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.welcome, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		m_buttonEnter = (Button) getActivity().findViewById(R.id.button_enter);
		m_buttonSync = (Button) getActivity().findViewById(R.id.button_sync);
		m_textSyncResult = (TextView) getActivity().findViewById(R.id.text_sync_result);

		//-----------------------------------------------------------------------------------
		m_buttonEnter.setVisibility(View.GONE);
		m_textSyncResult.setVisibility(View.GONE);

		m_buttonSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				m_buttonEnter.setVisibility(View.GONE);
				m_buttonSync.setVisibility(View.GONE);
				m_textSyncResult.setVisibility(View.VISIBLE);
				m_textSyncResult.setText("Please wait...");

				m_viewDataSource.startLocalSync(m_notifier);
			}
		});
	}

	private Notifier m_notifier = new Notifier() {
		@Override
		public void onComplete(boolean success, String message) {
			m_textSyncResult.setVisibility(View.VISIBLE);
			m_textSyncResult.setText(success ? "Success!" : "Error!");

			// For sure, success == true also means m_viewDataSource.valid() == true
			m_buttonEnter.setVisibility(success ? View.VISIBLE : View.GONE);
		}
	};
}
