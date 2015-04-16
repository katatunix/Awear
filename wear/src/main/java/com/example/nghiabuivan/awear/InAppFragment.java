package com.example.nghiabuivan.awear;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InAppFragment extends BaseFragment implements WearableListView.ClickListener {

	private Context m_context;

	private ListAdapter m_adapter;

	private ViewData m_currentViewData;
	private ViewData m_previousViewData;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		WearableListView listView = (WearableListView) inflater.inflate(R.layout.list, container, false);

		// ASSERT( m_viewDataSource.valid() );
		m_currentViewData = m_viewDataSource.getRoot();
		m_previousViewData = null;

		m_context = container.getContext();
		m_adapter = new ListAdapter(m_context, m_currentViewData);

		listView.setAdapter(m_adapter);
		listView.setClickListener(this);

		return listView;
	}

	@Override
	public void onClick(WearableListView.ViewHolder v) {
		int position = (Integer) v.itemView.getTag();
		ViewData nextViewData = null;

		if (position == 0) { // BACK
			if (m_previousViewData == null) {
				m_fragmentTransitor.goToWelcome();
				return;

			} else {
				nextViewData = m_previousViewData;
			}
		} else {
			ViewItemData vid = m_currentViewData.getItem(position);
			if (vid.sendingData != null) {
				Intent intent = new Intent(m_context, SendingDataActivity.class);
				intent.putExtra("key", vid.sendingData.key);
				intent.putExtra("value", vid.sendingData.value);

				startActivity(intent);
				return;

			} else if (vid.nextViewKey != null) {
				nextViewData = m_viewDataSource.get(vid.nextViewKey);
			}
		}

		if (nextViewData != null) {
			m_previousViewData = m_currentViewData;
			m_currentViewData = nextViewData;

			m_adapter.setViewData(m_currentViewData);
			m_adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onTopEmptyRegionClick() {
	}
}
