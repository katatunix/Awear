package com.example.nghiabuivan.awear.client;

import java.util.ArrayList;

public class View {

	private byte[] m_background;
	private ArrayList<Item> m_items = new ArrayList<>();

	static View createFromJson(String json) {
		// TODO
		return null;
	}

	public byte[] getBackground() {
		return m_background;
	}

	public int getItemCount() {
		return m_items.size();
	}

	public Item getItem(int index) {
		return m_items.get(index);
	}
}
