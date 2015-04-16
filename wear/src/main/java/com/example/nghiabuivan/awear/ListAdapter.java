package com.example.nghiabuivan.awear;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by nghia.buivan on 4/15/2015.
 */
public class ListAdapter extends WearableListView.Adapter {

	private final LayoutInflater m_inflater;
	private ViewData m_viewData;

	public ListAdapter(Context context, ViewData vd) {
		m_inflater = LayoutInflater.from(context);
		m_viewData = vd;
	}

	public void setViewData(ViewData vd) {
		m_viewData = vd;
	}

	@Override
	public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// Return layout of an item in the list
		return new WearableListView.ViewHolder(m_inflater.inflate(R.layout.item, null));
	}

	@Override
	public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
		ViewItemData vid = m_viewData.getItem(position);

		( (TextView) holder.itemView.findViewById(R.id.name) ).setText(vid.text);
		( (ImageView) holder.itemView.findViewById(R.id.image) ).setImageBitmap(vid.image);

		// Tag for this item, useful for onClick() event
		holder.itemView.setTag(position);
	}

	@Override
	public int getItemCount() {
		return m_viewData.getItemCount();
	}
}
