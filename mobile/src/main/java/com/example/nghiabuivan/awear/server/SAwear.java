package com.example.nghiabuivan.awear.server;

public class SAwear {

	private ViewCreator m_creator = null;
	private ActionListener m_selfListener = null;
	private ActionListener m_userListener = null;
	private Messenger m_messenger;
	private ViewPool m_pool;
	private String m_dir;

	private static final String ROOT_VIEW_KEY = "view";
	private static final String START_SYNC_KEY = "start_sync";
	private static final String FINISH_SYNC_KEY = "finish_sync";

	public SAwear(String dir) {
		m_dir = dir;
	}

	public void registerViewCreator(ViewCreator creator) {
		m_creator = creator;
		m_selfListener = new ActionListener() {
			@Override
			public void onActionReceived(String key, String value) {
				// TODO
				if (key.equals(START_SYNC_KEY)) {

				}
			}
		};
		m_messenger = new GoogleMessenger();
		m_pool = new ViewPool(m_dir);

		m_messenger.setListener(m_selfListener);
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
