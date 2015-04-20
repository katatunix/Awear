package com.example.nghiabuivan.awear;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemLayout extends LinearLayout implements WearableListView.OnCenterProximityListener {

	private final float m_fadedTextAlpha;
	private ImageView m_image;
	private TextView m_name;

	public ItemLayout(Context context) {
		this(context, null);
	}

	public ItemLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ItemLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		m_fadedTextAlpha = 0.4f;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		m_image = (ImageView) findViewById(R.id.image);
		m_name = (TextView) findViewById(R.id.name);
	}

	@Override
	public void onCenterPosition(boolean animate) {
		m_image.setAlpha(1.0f);
		m_name.setAlpha(1.0f);
	}

	@Override
	public void onNonCenterPosition(boolean animate) {
		m_image.setAlpha(m_fadedTextAlpha);
		m_name.setAlpha(m_fadedTextAlpha);
	}
}
