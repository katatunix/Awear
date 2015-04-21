package com.example.nghiabuivan.awear.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class View {

	private byte[] m_background;
	private ArrayList<Item> m_items = new ArrayList<>();

	static View createFromJson(String json, DataSource dataSource) throws JSONException {
		UnthrownJsonObject obj = new UnthrownJsonObject( new JSONObject(json) );

		View view = new View();
		view.m_background = dataSource.get( obj.getString("backgroundImageKey") );

		Item item = new Item()
				.setName(obj.getString("name"))
				.setImage(dataSource.get(obj.getString("backIconImageKey")));
		view.m_items.add(item);

		JSONArray array = obj.getJSONArray("items");
		if (array != null) {
			int count = array.length();
			for (int i = 0; i < count; i++) {
				obj.set(array.getJSONObject(i));

				item = new Item()
						.setName( obj.getString("name") )
						.setImage( dataSource.get( obj.getString("imageKey") ) )
						.setNextViewKey( obj.getString("nextViewKey") )
						.setSendingKey( obj.getString("sendingKey") )
						.setSendingValue( obj.getString("sendingValue") );
				view.m_items.add(item);
			}
		}

		return view;
	}

	private View() {

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
