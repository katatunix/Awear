package com.example.nghiabuivan.awear;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Stack;

public class InAppFragment extends BaseFragment implements WearableListView.ClickListener {

	private Context m_context;
	private ListAdapter m_adapter;
	WearableListView m_listView;
	private Stack<ViewData> m_stack = new Stack<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		m_listView = (WearableListView) inflater.inflate(R.layout.list, container, false);

		// ASSERT( m_viewDataSource.valid() );
		ViewData root = m_viewDataSource.getRoot();
		m_stack.clear();
		m_stack.push(root);

		m_context = container.getContext();
		m_adapter = new ListAdapter(m_context, root);
		m_listView.setAdapter(m_adapter);

		m_listView.setBackground( root.getBackground() );

		m_listView.setClickListener(this);

		return m_listView;
	}

	@Override
	public void onClick(WearableListView.ViewHolder v) {
		int position = (Integer) v.itemView.getTag();
		ViewData nextViewData = null;

		if (position == 0) { // BACK
			if (m_stack.size() == 1) {
				m_fragmentTransitor.goToWelcome();
				return;

			} else {
				m_stack.pop();
				nextViewData = m_stack.lastElement();
			}
		} else {
			ViewItemData vid = m_stack.lastElement().getItem(position);
			if (vid.sendingData != null) {
				Intent intent = new Intent(m_context, SendingDataActivity.class);
				intent.putExtra("key", vid.sendingData.key);
				intent.putExtra("value", vid.sendingData.value);

				startActivity(intent);
				return;

			} else if (vid.nextViewKey != null) {
				nextViewData = m_viewDataSource.get(vid.nextViewKey);
				m_stack.push(nextViewData);
			}
		}

		if (nextViewData != null) {
			m_listView.setBackground( nextViewData.getBackground() );
			m_adapter.setViewData(nextViewData);
			m_adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onTopEmptyRegionClick() {
	}
}
