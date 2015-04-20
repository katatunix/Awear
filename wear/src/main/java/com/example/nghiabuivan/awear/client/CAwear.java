package com.example.nghiabuivan.awear.client;

import android.content.Context;

import java.util.HashMap;

public class CAwear {

	private DataSource m_dataSource;
	private HashMap<String, View> m_views = new HashMap<>();
	private Messenger m_messenger;

	private Notifier m_notifier = null;

	private static final String ROOT_VIEW_KEY = "view";
	private static final String SYNC_ACTION_KEY = "sync";

	public CAwear(String localDirPath, Context context) {
		m_dataSource = new DataSource(localDirPath);
		m_messenger = new Messenger(m_listener, context);
	}

	public void connect() {
		m_messenger.connect();
	}

	public void disconnect() {
		m_messenger.disconnect();
	}

	public boolean isConnected() {
		return m_messenger.isConnected();
	}

	public void startLocalSync(Notifier notifier) {
		m_notifier = notifier;
	}

	public void startRemoteSync(Notifier notifier) {
		m_notifier = notifier;
	}

	public boolean hasRootView() {
		return m_views.size() > 0;
	}

	public View getRootView() {
		return m_views.get(ROOT_VIEW_KEY);
	}

	public boolean hasView(String key) {
		return m_views.containsKey(key);
	}

	public View getView(String key) {
		return m_views.get(key);
	}

	public void sendAction(String key, String value, Notifier notifier) {
		m_messenger.send(key, value, notifier);
	}

	private void buildViews() {
		// TODO
	}

	private boolean makeView(String key) {
		// TODO
		return false;
	}

	private CListener m_listener = new CListener() {
		@Override
		public void onReceived(String key, byte[] value) {
			// TODO
		}
	};

}
