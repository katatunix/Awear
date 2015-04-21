package com.example.nghiabuivan.awear.client;

import java.util.HashMap;

public class CAwear {

	private DataSource m_dataSource;
	private HashMap<String, View> m_views = new HashMap<>();
	private Messenger m_messenger;

	private Notifier m_notifier = null;

	private static final String ROOT_VIEW_KEY = "view";
	private static final String START_SYNC_KEY = "start_sync";
	private static final String FINISH_SYNC_KEY = "finish_sync";

	//===================================================================================================
	// Singleton
	private static CAwear s_instance = null;
	public static CAwear createInstance(String localDirPath, Object context) {
		if (s_instance == null) {
			s_instance = new CAwear(localDirPath, context);
		}
		return s_instance;
	}
	public static CAwear getInstance() {
		return s_instance;
	}
	//===================================================================================================

	private CAwear(String localDirPath, Object context) {
		m_dataSource = new DataSource(localDirPath);

		CListener listener = new CListener() {
			@Override
			public void onReceived(String key, byte[] value) {
				// TODO: timeout

				if (key.equals(FINISH_SYNC_KEY)) {
					// TODO: save to disk
					buildViewsAndNotify();
				} else {
					m_dataSource.put(key, value);
				}
			}
		};
		// TODO: MockMessenger
		m_messenger = new GoogleMessenger(context);
		//m_messenger = new MockMessenger();
		m_messenger.setReceiveListener(listener);
	}

	public void connect() {
		m_messenger.connect();
	}

	public void disconnect() {
		m_messenger.disconnect();
	}

	public void startLocalSync(Notifier notifier) {
		m_notifier = notifier;
		new Thread(m_localSyncThread).start();
	}

	public void startRemoteSync(Notifier notifier) {
		m_notifier = notifier;
		sendAction(START_SYNC_KEY, "", null);
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

	//===================================================================================================
	//===================================================================================================

	private void buildViews() {
		m_views.clear();
		if (!makeView(ROOT_VIEW_KEY)) {
			m_views.clear(); // so hasRootView() will return false
		}
	}

	private boolean makeView(String key) {
		// Sure: hasView(key) == false (this view has not been made)
		if (!m_dataSource.hasKey(key)) return false;

		String json = new String( m_dataSource.get(key) );
		View view;
		try {
			view = View.createFromJson(json, m_dataSource);
		} catch (Exception e) {
			return false;
		}
		if (view == null) return false;

		int count = view.getItemCount();
		for (int i = 0; i < count; i++) {
			Item item = view.getItem(i);
			String nvk = item.nextViewKey;
			if (nvk != null && !hasView(nvk) && !makeView(nvk)) {
				return false;
			}
		}

		m_views.put(key, view);
		return true;
	}

	private Runnable m_localSyncThread = new Runnable() {
		@Override
		public void run() {
			// TODO: just test Thread.sleep()
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			buildViewsAndNotify();
		}
	};

	private void buildViewsAndNotify() {
		buildViews();
		if (hasRootView()) {
			m_notifier.onComplete(true, "Success");
		} else {
			m_notifier.onComplete(false, "Failed");
		}
	}

}
