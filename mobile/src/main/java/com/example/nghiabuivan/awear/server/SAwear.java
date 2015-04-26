package com.example.nghiabuivan.awear.server;

import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class SAwear {

	private ViewCreator m_creator = null;
	private ActionListener m_actionListener = null;
	private Messenger m_messenger;
	private ViewPool m_pool;
	private HashMap<Integer, SendingThread> m_sendingThreads = new HashMap<>();

	private static final String TAG = "Awear";

	private static final String ROOT_VIEW_KEY = "view";
	private static final String START_SYNC_KEY = "start_sync";
	private static final String CANCEL_SYNC_KEY = "cancel_sync";
	private static final String FINISH_SYNC_KEY = "finish_sync";

	public SAwear(String dir, Object context) {
		m_messenger = new GoogleMessenger(context);
		m_pool = new ViewPool(dir, ROOT_VIEW_KEY);

		ServerListener serverListener = new ServerListener() {
			@Override
			public void onReceived(Message msg, String nodeId) {
				String key = msg.getKey();

				if (key.equals(START_SYNC_KEY)) {
					int sessionId;
					try { sessionId = msg.getHeaderAsInt(); } catch (Exception e) { return; }
					Log.d(TAG, "Receive a START_SYNC request with sessionId = " + sessionId);

					cleanSendingThreads();
					SendingThread thread = new SendingThread(m_pool, m_creator, FINISH_SYNC_KEY,
							nodeId, sessionId, m_messenger);
					m_sendingThreads.put(sessionId, thread);
					thread.start();

				} else if (key.equals(CANCEL_SYNC_KEY)) {
					int sessionId;
					try { sessionId = msg.getHeaderAsInt(); } catch (Exception e) { return; }
					Log.d(TAG, "Receive a CANCEL_SYNC request with sessionId = " + sessionId);

					if (m_sendingThreads.containsKey(sessionId)) {
						m_sendingThreads.get(sessionId).forceStop();
					}
				} else if (m_actionListener != null) {
					m_actionListener.onActionReceived( msg.getKey(), msg.getBodyAsString() );
				}
			}
		};

		m_messenger.setListener(serverListener);
	}

	public void registerViewCreator(ViewCreator creator) {
		m_creator = creator;
	}

	public void registerActionListener(ActionListener listener) {
		m_actionListener = listener;
	}

	public void connect() {
		m_messenger.connect();
	}

	public void disconnect() {
		m_messenger.disconnect();
	}

	private void cleanSendingThreads() {
		Iterator<Map.Entry<Integer, SendingThread>> iter = m_sendingThreads.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, SendingThread> entry = iter.next();
			if ( !entry.getValue().isRunning() ) {
				iter.remove();
			}
		}
	}

}
