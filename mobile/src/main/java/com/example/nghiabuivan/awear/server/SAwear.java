package com.example.nghiabuivan.awear.server;

public class SAwear {

	private ViewCreator m_creator = null;
	private ActionListener m_userListener = null;
	private Messenger m_messenger;
	private ViewPool m_pool;

	private static final String ROOT_VIEW_KEY = "view";
	private static final String START_SYNC_KEY = "start_sync";
	private static final String CANCEL_SYNC_KEY = "start_sync";
	private static final String FINISH_SYNC_KEY = "finish_sync";

	public SAwear(String dir, Object context) {
		m_messenger = new GoogleMessenger(context);
		m_pool = new ViewPool(dir, ROOT_VIEW_KEY);

		ActionListener selfListener = new ActionListener() {
			@Override
			public void onActionReceived(Message msg, String nodeId) {
				int sessionId;
				String key = msg.getKey();

				if (key.equals(START_SYNC_KEY)) {
					try { sessionId = msg.getHeaderAsInt(); } catch (Exception e) { return; }

					m_pool.clearViews();
					if (m_creator != null) {
						m_creator.createViews(m_pool);
					}

					m_pool.sendViews(nodeId, sessionId, m_messenger); // slow and block
					m_messenger.send(
							new Message.Builder()
									.setKey(FINISH_SYNC_KEY)
									.setHeaderAsInt(sessionId)
									.build(),
							nodeId
					);

				} else if (key.equals(CANCEL_SYNC_KEY)) {
					//try { sessionId = msg.getHeaderAsInt(); } catch (Exception e) { return; }
					// TODO: CANCEL_SYNC_KEY
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

}
