package com.example.nghiabuivan.awear.server;

import android.util.Log;

public class SAwear {

	private ViewCreator m_creator = null;
	private ActionListener m_userListener = null;
	private Messenger m_messenger;
	private ViewPool m_pool;
	private SendingThread m_sendingThread = null;

	private static final String TAG = "Awear";

	private static final String ROOT_VIEW_KEY = "view";
	private static final String START_SYNC_KEY = "start_sync";
	private static final String CANCEL_SYNC_KEY = "cancel_sync";
	private static final String FINISH_SYNC_KEY = "finish_sync";

	public SAwear(String dir, Object context) {
		m_messenger = new GoogleMessenger(context);
		m_pool = new ViewPool(dir, ROOT_VIEW_KEY);

		ActionListener selfListener = new ActionListener() {
			@Override
			public void onActionReceived(Message msg, String nodeId) {
				String key = msg.getKey();

				if (key.equals(START_SYNC_KEY)) {
					int sessionId;
					try { sessionId = msg.getHeaderAsInt(); } catch (Exception e) { return; }

					if (m_sendingThread != null && m_sendingThread.isRunning()) {
						Log.d(TAG, "Too busy, please wait...");
						try {
							m_sendingThread.join();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					m_sendingThread = new SendingThread(m_pool, m_creator, nodeId, sessionId, m_messenger);
					m_sendingThread.start();

				} else if (key.equals(CANCEL_SYNC_KEY)) {
					if (m_sendingThread != null && m_sendingThread.isRunning()) {
						m_sendingThread.forceStop();
					}
				} else if (m_userListener != null) {
					m_userListener.onActionReceived(msg, nodeId);
				}
			}
		};

		m_messenger.setListener(selfListener);
	}

	public void registerViewCreator(ViewCreator creator) {
		m_creator = creator;
	}

	public void registerActionListener(ActionListener listener) {
		m_userListener = listener;
	}

	public void connect() {
		m_messenger.connect();
	}

	public void disconnect() {
		m_messenger.disconnect();
	}

	private static class SendingThread extends Thread {

		private ViewPool m_pool;
		private ViewCreator m_creator;
		private String m_nodeId;
		private int m_sessionId;
		private Messenger m_messenger;

		private boolean m_isRunning = false;

		public SendingThread(ViewPool pool, ViewCreator creator, String nodeId, int sessionId, Messenger messenger) {
			m_pool = pool; m_creator = creator; m_nodeId = nodeId; m_sessionId = sessionId; m_messenger = messenger;
		}

		public boolean isRunning() {
			synchronized (this) {
				return m_isRunning;
			}
		}

		public void forceStop() {
			// TODO: forceStop()
		}

		private void setRunning(boolean val) {
			synchronized (this) {
				m_isRunning = val;
			}
		}

		@Override
		public void run() {
			setRunning(true);

			m_pool.clearViews();
			if (m_creator != null) {
				m_creator.createViews(m_pool);
			}

			m_pool.sendViews(m_nodeId, m_sessionId, m_messenger); // slow and block
			m_messenger.send(
					new Message.Builder()
							.setKey(FINISH_SYNC_KEY)
							.setHeaderAsInt(m_sessionId)
							.build(),
					m_nodeId
			);

			setRunning(false);
		}

	}
}
