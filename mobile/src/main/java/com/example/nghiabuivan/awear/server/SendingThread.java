package com.example.nghiabuivan.awear.server;

import android.util.Log;

class SendingThread extends Thread {

	private ViewPool m_pool;
	private ViewCreator m_creator;
	private String m_finishSyncKey;
	private String m_nodeId;
	private int m_sessionId;
	private Messenger m_messenger;

	private boolean m_isRunning = false;
	private boolean m_isForceStop = false;

	private static final String TAG = "Awear";

	public SendingThread(ViewPool pool, ViewCreator creator, String finishSyncKey,
						 String nodeId, int sessionId, Messenger messenger) {
		m_pool = pool; m_creator = creator; m_finishSyncKey = finishSyncKey;
		m_nodeId = nodeId; m_sessionId = sessionId; m_messenger = messenger;
	}

	public boolean isRunning() {
		synchronized (this) {
			return m_isRunning;
		}
	}

	public void forceStop() {
		synchronized (this) {
			m_isForceStop = true;
		}
		m_pool.stopSending();
	}

	private void setRunning(boolean val) {
		synchronized (this) {
			m_isRunning = val;
		}
	}

	private boolean isForceStop() {
		synchronized (this) {
			return m_isForceStop;
		}
	}

	@Override
	public void run() {
		setRunning(true);

		synchronized (SendingThread.class) {

			m_pool.clearViews();
			if (m_creator != null) {
				m_creator.createViews(m_pool);
			}

			if (isForceStop()) {
				setRunning(false);
				return;
			}

			m_pool.sendViews(m_nodeId, m_sessionId, m_messenger);

			if (isForceStop()) {
				setRunning(false);
				return;
			}

			m_messenger.send(
					new Message.Builder()
							.setKey(m_finishSyncKey)
							.setHeaderAsInt(m_sessionId)
							.build(),
					m_nodeId
			);

			Log.d(TAG, "Send FINISH_SYNC with sessionId = " + m_sessionId);
		}

		setRunning(false);
	}

}
