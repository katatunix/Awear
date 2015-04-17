package com.example.nghiabuivan.awear;

import android.graphics.Bitmap;

import java.util.HashMap;

public class ViewDataSource {

	private Notifier m_notifier;
	private HashMap<String, ViewData> m_viewDataList;

	private static final String ROOT_VIEW_KEY = "view";

	public ViewDataSource() {
		m_viewDataList = new HashMap<>();
	}

	public void startLocalSync(Notifier notifier) {
		m_notifier = notifier;

		Bitmap dummyBitmap = Bitmap.createBitmap(32, 32, Bitmap.Config.ARGB_8888);
		for (int i = 0; i < 32; i++) for (int j = 0; j < 32; j++) {
			dummyBitmap.setPixel(i, j, 0xFFFF00FF);
		}

		// Mock data
		ViewData vd;
		m_viewDataList.clear();

		// 1
		vd = new ViewData();
		vd.addItem(new ViewItemData(dummyBitmap, "Back", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Cong hoa xa hoi chu nghia Viet Nam", "view_loadout", null));
		vd.addItem(new ViewItemData(dummyBitmap, "Hello World 2", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Hello World 3", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Hello World 4", null, null));
		m_viewDataList.put(ROOT_VIEW_KEY, vd);

		// 2
		vd = new ViewData();
		vd.addItem(new ViewItemData(dummyBitmap, "Back", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Loadout 1", null, null));
		vd.addItem(new ViewItemData(dummyBitmap, "Loadout 2", null, null));
		m_viewDataList.put("view_loadout", vd);

		// TODO
		m_notifier.onComplete(true, "OK");
	}

	public void startRemoteSync(Notifier notifier) {
		m_notifier = notifier;
		// TODO
		m_notifier.onComplete(true, "OK");
	}

	public boolean valid() {
		// TODO
		return m_viewDataList.size() > 0;
	}

	public ViewData getRoot() {
		if (!valid()) return null;
		return m_viewDataList.get(ROOT_VIEW_KEY);
	}

	public ViewData get(String key) {
		if (!valid()) return null;
		if (!m_viewDataList.containsKey(key)) return null;
		return m_viewDataList.get(key);
	}

	//-------------------------------------------------------------
	//-------------------------------------------------------------

	private void checkValid() {
		// TODO
	}

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
