package com.example.nghiabuivan.awear;

import java.util.ArrayList;

/**
 * Created by nghia.buivan on 4/16/2015.
 */
public class ViewData {

	public ViewData() {
		m_array = new ArrayList<>();
	}

	public int getItemCount() {
		return m_array.size();
	}

	public ViewItemData getItem(int index) {
		return m_array.get(index);
	}

	public void addItem(ViewItemData vid) {
		m_array.add(vid);
	}

	private ArrayList<ViewItemData> m_array;
}
