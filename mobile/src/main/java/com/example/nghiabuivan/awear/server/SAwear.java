package com.example.nghiabuivan.awear.server;

public class SAwear {

	private ViewCreator m_creator = null;
	private ActionListener m_userListener = null;
	private Messenger m_messenger;
	private ViewPool m_pool;

	private static final String ROOT_VIEW_KEY = "view";
	private static final String START_SYNC_KEY = "start_sync";
	private static final String FINISH_SYNC_KEY = "finish_sync";

	public SAwear(String dir, Object context) {
		m_messenger = new GoogleMessenger(context);
		m_pool = new ViewPool(dir, ROOT_VIEW_KEY);

		ActionListener selfListener = new ActionListener() {
			@Override
			public void onActionReceived(String key, String value, String nodeId) {
				if (key.equals(START_SYNC_KEY)) {
					m_pool.clearViews();
					if (m_creator != null) {
						m_creator.createViews(m_pool);
					}

					// TODO: cache this thread???
					new SendingThread(m_pool, nodeId, m_messenger, FINISH_SYNC_KEY).start();

				} else if (m_userListener != null) {
					m_userListener.onActionReceived(key, value, nodeId);
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

}
