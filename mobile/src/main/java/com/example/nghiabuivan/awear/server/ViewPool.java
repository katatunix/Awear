package com.example.nghiabuivan.awear.server;

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

	void sendViews(String targetNodeId, Messenger sender) {
		m_sentKeys.clear();
		sendView(m_rootViewKey, targetNodeId, sender);
	}

	//====================================================================================================
	//====================================================================================================

	private void sendView(String key, String targetNodeId, Messenger sender) {
		if ( key == null || m_sentKeys.contains(key) || !m_views.containsKey(key) ) return;

		View view = m_views.get(key);
		String json;
		try {
			json = view.toJson();
		} catch (Exception e) {
			return;
		}

		sender.send(key, json, targetNodeId);
		m_sentKeys.add(key);

		checkAndSendImage( view.getBackgroundImageKey(), targetNodeId, sender );
		checkAndSendImage( view.getBackIconImageKey(), targetNodeId, sender) ;

		int count = view.getItemCount();
		for (int i = 0; i < count; i++) {
			checkAndSendImage( view.getItemImageKey(i), targetNodeId, sender) ;
			sendView( view.getItemNextViewKey(i), targetNodeId, sender) ;
		}
	}

	private void checkAndSendImage(String imageKey, String targetNodeId, Messenger sender) {
		if ( imageKey != null && !m_sentKeys.contains(imageKey) ) {
			byte[] data = getImage(imageKey);
			if (data != null) {
				sender.send(imageKey, data, targetNodeId);
				m_sentKeys.add(imageKey);
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

}
