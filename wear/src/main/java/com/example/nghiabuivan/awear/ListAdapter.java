package com.example.nghiabuivan.awear;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nghiabuivan.awear.client.Bytes;
import com.example.nghiabuivan.awear.client.Item;
import com.example.nghiabuivan.awear.client.View;

public class ListAdapter extends WearableListView.Adapter {

	private final LayoutInflater m_inflater;
	private View m_view;

	public ListAdapter(Context context, View view) {
		m_inflater = LayoutInflater.from(context);
		m_view = view;
	}

	public void setView(View view) {
		m_view = view;
	}

	@Override
	public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// Return layout of an item in the list
		return new WearableListView.ViewHolder(m_inflater.inflate(R.layout.item, null));
	}

	@Override
	public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
		Item item = m_view.getItem(position);
		Bytes img = item.image;

		( (TextView) holder.itemView.findViewById(R.id.name) ).setText(item.name);
		( (ImageView) holder.itemView.findViewById(R.id.image) ).setImageBitmap(
				img == null || img.data == null || img.length <= 0 ? null :
						BitmapFactory.decodeByteArray(img.data, img.offset, img.length)

		);

		// Tag for this item, useful for the onClick() event
		holder.itemView.setTag(position);
	}

	@Override
	public int getItemCount() {
		return m_view.getItemCount();
	}
}
