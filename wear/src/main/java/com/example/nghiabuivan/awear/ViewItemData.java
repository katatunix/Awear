package com.example.nghiabuivan.awear;

import android.graphics.Bitmap;

/**
 * Created by nghia.buivan on 4/16/2015.
 */
public class ViewItemData {
	public Bitmap image;
	public String text;
	public String nextViewKey;
	public KeyValue sendingData;

	public ViewItemData(Bitmap image, String text, String nextViewKey, KeyValue sendingData) {
		this.image = image;
		this.text = text;
		this.nextViewKey = nextViewKey;
		this.sendingData = sendingData;
	}
}
