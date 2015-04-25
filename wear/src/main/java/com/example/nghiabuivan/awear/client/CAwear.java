package com.example.nghiabuivan.awear.client;

import android.util.Log;

import java.util.HashMap;

public class CAwear {

	private Storage m_storage;
	private HashMap<String, View> m_views = new HashMap<>();
	private final Messenger m_messenger;
	private Notifier m_notifier = null;
	private int m_sessionId = 0;
	private int m_sessionIdCounter = 0;

	private static final String TAG = "Awear";

	private static final String ROOT_VIEW_KEY = "view";
	private static final String START_SYNC_KEY = "start_sync";
	private static final String CANCEL_SYNC_KEY = "cancel_sync";
	private static final String FINISH_SYNC_KEY = "finish_sync";

	//===================================================================================================
	// Singleton
	private static CAwear s_instance = null;
	public static CAwear createInstance(String localDirPath, Object context) {
		s_instance = new CAwear(localDirPath, context);
		return s_instance;
	}
	public static CAwear getInstance() {
		return s_instance;
	}
	//===================================================================================================

	private CAwear(String localDirPath, Object context) {
		m_storage = new Storage(localDirPath);

		ClientListener listener = new ClientListener() {
			@Override
			public void onReceived(Message msg) {
				// TODO: timeout

				try {
					int sid = msg.getHeaderAsInt();
					Log.d(TAG, "received: sid = " + sid + ", m_sessionId = " + m_sessionId);
					if (sid != m_sessionId) return;
				} catch (Exception e) {
					return;
				}

				String key = msg.getKey();
				if (key.equals(FINISH_SYNC_KEY)) {
					m_storage.flush();
					buildViewsAndNotify();
				} else {
					m_storage.put(key, msg.getBody());
				}
			}
		};

		m_messenger = new GoogleMessenger(context);
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

	public boolean startRemoteSync(Notifier notifier) {
		m_notifier = notifier;
		m_sessionId = getNewSessionId();
		Log.d(TAG, "New sessionId: " + m_sessionId);
		return m_messenger.send(
				new Message.Builder()
						.setKey(START_SYNC_KEY)
						.setHeaderAsInt(m_sessionId)
						.build(),
				null
		);
	}

	public void cancelRemoteSync() {
		if (m_sessionId == 0) return;
		m_messenger.send(
				new Message.Builder()
						.setKey(CANCEL_SYNC_KEY)
						.setHeaderAsInt(m_sessionId)
						.build(),
				null
		);
		m_sessionId = 0;
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
		m_messenger.send(
				new Message.Builder()
						.setKey(key)
						.setBody( new Bytes(value.getBytes()) )
						.build(),
				notifier
		);
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
		if (!m_storage.hasKey(key)) return false;

		Bytes viewBytes = m_storage.get(key);
		String json = new String( viewBytes.data, viewBytes.offset, viewBytes.length );
		View view;
		try {
			view = View.createFromJson(json, m_storage);
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

	private int getNewSessionId() {
		return ++m_sessionIdCounter;
	}

}
