package com.example.nghiabuivan.awear.server;

class SendingThread extends Thread {

	private ViewPool m_pool;
	private String m_nodeId;
	private Messenger m_sender;
	private String m_finishKey;

	public SendingThread(ViewPool pool, String nodeId, Messenger sender, String finishKey) {
		m_pool = pool;
		m_nodeId = nodeId;
		m_sender = sender;
		m_finishKey = finishKey;
	}

	public void run() {
		m_pool.sendViews(m_nodeId, m_sender);
		m_sender.send(m_finishKey, "", m_nodeId);
	}

}
