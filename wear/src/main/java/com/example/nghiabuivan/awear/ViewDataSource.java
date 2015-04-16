package com.example.nghiabuivan.awear;

/**
 * Created by nghia.buivan on 4/16/2015.
 */
public class ViewDataSource {

	private Notifier m_notifier;

	public ViewDataSource() {

	}

	public void startLocalSync(Notifier notifier) {
		m_notifier = notifier;
		// TODO
	}

	public void startRemoteSync(Notifier notifier) {
		m_notifier = notifier;
		// TODO
	}

	public boolean valid() {
		return false;
		// TODO
	}

	public ViewData getRoot() {
		return null;
		// TODO
	}

	public ViewData get(String key) {
		return null;
		// TODO
	}

	//-------------------------------------------------------------
	//-------------------------------------------------------------

	private String getValueAsString(String key) {
		return null;
		// TODO
	}

	private byte[] getValueAsBinary(String key) {
		return null;
		// TODO
	}

	private boolean hasKey(String key) {
		return false;
		// TODO
	}
}
