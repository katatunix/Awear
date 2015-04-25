package com.example.nghiabuivan.awear;

import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.nghiabuivan.awear.client.Bytes;
import com.example.nghiabuivan.awear.client.CAwear;
import com.example.nghiabuivan.awear.client.Item;
import com.example.nghiabuivan.awear.client.View;

import java.util.Stack;

public class InAppFragment extends BaseFragment implements WearableListView.ClickListener {

	private Context m_context;
	private ListAdapter m_adapter;
	WearableListView m_listView;
	private Stack<View> m_stack = new Stack<>();

	@Override
	public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_listView = (WearableListView) inflater.inflate(R.layout.list, container, false);

		View root = CAwear.getInstance().getRootView();
		m_stack.push(root);

		m_context = container.getContext();
		m_adapter = new ListAdapter(m_context, root);
		m_listView.setAdapter(m_adapter);

		setBackground( root.getBackground() );

		m_listView.setClickListener(this);

		return m_listView;
	}

	@Override
	public void onClick(WearableListView.ViewHolder v) {
		int position = (Integer) v.itemView.getTag();
		View nextView = null;

		if (position == 0) { // BACK
			if (m_stack.size() == 1) {
				m_fragmentTransitor.goToWelcome();
				return;

			} else {
				m_stack.pop();
				nextView = m_stack.lastElement();
			}
		} else {
			Item item = m_stack.lastElement().getItem(position);

			if (item.sendingKey != null) {
				Intent intent = new Intent(m_context, SendingDataActivity.class);
				intent.putExtra("key", item.sendingKey);
				intent.putExtra("value", item.sendingValue);

				startActivity(intent);
				return;

			} else if (item.nextViewKey != null) {
				nextView = CAwear.getInstance().getView(item.nextViewKey);
				m_stack.push(nextView);
			}
		}

		if (nextView != null) {
			setBackground( nextView.getBackground() );
			m_adapter.setView(nextView);
			m_adapter.notifyDataSetChanged();
		}
	}

	private void setBackground(Bytes bgr) {
		if (bgr == null || bgr.data == null || bgr.length <= 0) {
			m_listView.setBackground(null);
		} else {
			// TODO: cache these BitmapDrawable
			Bitmap bmp = BitmapFactory.decodeByteArray(bgr.data, bgr.offset, bgr.length);
			m_listView.setBackground( new BitmapDrawable(getResources(), bmp) );
		}
	}

	@Override
	public void onTopEmptyRegionClick() {
	}
}
