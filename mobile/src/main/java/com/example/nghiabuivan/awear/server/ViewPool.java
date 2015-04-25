package com.example.nghiabuivan.awear.server;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewPool {

	private String m_dir;
	private String m_rootViewKey;

	private HashMap<String, View> m_views = new HashMap<>();
	private HashMap<String, byte[]> m_imageCache = new HashMap<>();
	private ArrayList<String> m_sentKeys = new ArrayList<>();

	private boolean m_isStopSending = false;

	private static final String TAG = "Awear";

	ViewPool(String dir, String rootViewKey) {
		m_dir = dir;
		m_rootViewKey = rootViewKey;
	}

	public View createView(String key) {
		View view = new View();
		m_views.put(key, view);
		return view;
	}

	public View createRootView() {
		return createView(m_rootViewKey);
	}

	//====================================================================================================
	//====================================================================================================

	void clearViews() {
		m_views.clear();
	}

	void sendViews(String nodeId, int sessionId, Messenger sender) {
		synchronized (this) {
			m_isStopSending = false;
		}

		m_sentKeys.clear();
		sendView(m_rootViewKey, nodeId, sessionId, sender);
	}

	public void stopSending() {
		synchronized (this) {
			m_isStopSending = true;
		}
	}

	//====================================================================================================
	//====================================================================================================

	private void sendView(String key, String nodeId, int sessionId, Messenger sender) {
		if ( key == null || m_sentKeys.contains(key) || !m_views.containsKey(key) ) return;

		if (isStopSending()) return;

		View view = m_views.get(key);
		String json;
		try {
			json = view.toJson();
		} catch (Exception e) {
			return;
		}

		sender.send(
				new Message.Builder()
						.setKey(key)
						.setHeaderAsInt(sessionId)
						.setBody( new Bytes(json.getBytes()) )
						.build()
				, nodeId
		);
		m_sentKeys.add(key);

		Log.d(TAG, "Send view: key = " + key + ", sessionId = " + sessionId + ", length = " + json.length());

		checkAndSendImage(view.getBackgroundImageKey(), nodeId, sessionId, sender);
		checkAndSendImage( view.getBackIconImageKey(), nodeId, sessionId, sender );

		int count = view.getItemCount();
		for (int i = 0; i < count; i++) {
			checkAndSendImage( view.getItemImageKey(i), nodeId, sessionId, sender) ;
			sendView( view.getItemNextViewKey(i), nodeId, sessionId, sender );
		}
	}

	private void checkAndSendImage(String imageKey, String nodeId, int sessionId, Messenger sender) {
		if (isStopSending()) return;

		if ( imageKey != null && !m_sentKeys.contains(imageKey) ) {
			byte[] data = getImage(imageKey);
			if (data != null) {
				sender.send(
						new Message.Builder()
								.setKey(imageKey)
								.setHeaderAsInt(sessionId)
								.setBody( new Bytes(data) )
								.build(),
						nodeId
				);
				m_sentKeys.add(imageKey);

				Log.d(TAG, "Send image: key = " + imageKey + ", sessionId = " + sessionId + ", length = " + data.length);
			}
		}
	}

	private byte[] getImage(String imageKey) {
		if (m_imageCache.containsKey(imageKey)) {
			return m_imageCache.get(imageKey);
		}

		byte[] buffer = null;
		File f = new File(m_dir + imageKey);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			int count = fis.available();
			if (count > 0) {
				buffer = new byte[count];
				fis.read(buffer);
				m_imageCache.put(imageKey, buffer);
			}
		} catch (IOException ignored) {
			buffer = null;
		} finally {
			if (fis != null) try {
				fis.close();
			} catch (IOException ignored) {

			}
		}

		return buffer;
	}

	private boolean isStopSending() {
		synchronized (this) {
			return m_isStopSending;
		}
	}
}
