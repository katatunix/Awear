package com.example.nghiabuivan.awear;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nghiabuivan.awear.client.CAwear;
import com.example.nghiabuivan.awear.client.Notifier;

public class WelcomeFragment extends BaseFragment {

	private Button m_buttonEnter;
	private Button m_buttonSync;
	private Button m_buttonCancelSync;
	private TextView m_textSyncStatus;

	private enum State {
		IDLE,
		IN_LOCAL_SYNC,
		IN_REMOTE_SYNC
	}

	private State m_state;
	private Handler m_handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.welcome, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		m_state = State.IDLE;

		m_buttonEnter = (Button) getActivity().findViewById(R.id.button_enter);
		m_buttonSync = (Button) getActivity().findViewById(R.id.button_sync);
		m_buttonCancelSync = (Button) getActivity().findViewById(R.id.button_cancel_sync);
		m_textSyncStatus = (TextView) getActivity().findViewById(R.id.text_sync_status);

		m_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (m_state == State.IDLE) return;

				boolean success = msg.what != 0;

				if (m_state == State.IN_REMOTE_SYNC) {
					m_textSyncStatus.setVisibility(View.VISIBLE);
					m_textSyncStatus.setText((String) msg.obj);
				} else {
					m_textSyncStatus.setVisibility(View.GONE);
				}

				// For sure, success == true also means CAwear.getInstance().hasRootView() == true
				m_buttonEnter.setVisibility(success ? View.VISIBLE : View.GONE);
				m_buttonSync.setVisibility(View.VISIBLE);
				m_buttonCancelSync.setVisibility(View.GONE);
			}
		};

		//-----------------------------------------------------------------------------------
		m_buttonEnter.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				m_fragmentTransitor.goToInApp();
			}
		});

		m_buttonSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				m_buttonEnter.setVisibility(View.GONE);
				m_buttonSync.setVisibility(View.GONE);
				m_buttonCancelSync.setVisibility(View.VISIBLE);
				m_textSyncStatus.setVisibility(View.VISIBLE);
				m_textSyncStatus.setText("Synching...");

				m_state = State.IN_REMOTE_SYNC;
				if ( !CAwear.getInstance().startRemoteSync(m_notifier) ) {
					// TODO: sending is failed due to no connected node
				}
			}
		});

		m_buttonCancelSync.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				m_state = State.IDLE;
				m_buttonEnter.setVisibility(View.GONE);
				m_buttonSync.setVisibility(View.VISIBLE);
				m_buttonCancelSync.setVisibility(View.GONE);
				m_textSyncStatus.setVisibility(View.GONE);

				CAwear.getInstance().cancelRemoteSync();
			}
		});

		//-----------------------------------------------------------------------------------
		if (CAwear.getInstance().hasRootView()) {
			m_textSyncStatus.setVisibility(View.GONE);
		} else {
			m_buttonEnter.setVisibility(View.GONE);
			m_buttonSync.setVisibility(View.GONE);
			m_textSyncStatus.setText("Loading...");

			m_state = State.IN_LOCAL_SYNC;
			CAwear.getInstance().startLocalSync(m_notifier);
		}
	}

	private Notifier m_notifier = new Notifier() {
		@Override
		public void onComplete(boolean success, String message) {
			m_handler.obtainMessage(success ? 1 : 0, message).sendToTarget();
		}
	};

}
