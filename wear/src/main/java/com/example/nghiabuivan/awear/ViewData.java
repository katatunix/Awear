package com.example.nghiabuivan.awear;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;

/**
 * Created by nghia.buivan on 4/16/2015.
 */
public class ViewData {

	private BitmapDrawable m_background = null;
	private ArrayList<ViewItemData> m_array = new ArrayList<>();

	public ViewData() {
	}

	public ViewData(BitmapDrawable background) {
		m_background = background;
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

	public void setBackground(BitmapDrawable background) {
		m_background = background;
	}

	public BitmapDrawable getBackground() {
		return m_background;
	}

}
