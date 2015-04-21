package com.example.nghiabuivan.awear.server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class View {

	private String m_name;
	private String m_backgroundImageKey;
	private String m_backIconImageKey;
	private ArrayList<Item> m_items = new ArrayList<>();

	public View setName(String name) { m_name = name; return this; }

	public View setBackgroundImageKey(String imageKey) { m_backgroundImageKey = imageKey; return this; }

	public View setBackIconImageKey(String imageKey) { m_backIconImageKey = imageKey; return this; }

	public Item addItem() {
		Item item = new Item();
		m_items.add(item);
		return item;
	}

	String toJson() throws JSONException {
		JSONObject view = new JSONObject();
		view.put("backgroundImageKey", m_backgroundImageKey);
		view.put("backIconImageKey", m_backIconImageKey);
		view.put("name", m_name);

		JSONArray items = new JSONArray();
		for (Item item : m_items) {
			JSONObject obj = new JSONObject();

			obj.put("name", item.name);
			obj.put("imageKey", item.imageKey);
			obj.put("nextViewKey", item.nextViewKey);
			obj.put("sendingKey", item.sendingKey);
			obj.put("sendingValue", item.sendingValue);

			items.put(obj);
		}

		view.put("items", items);
		return view.toString();
	}

	String getBackgroundImageKey() {
		return m_backgroundImageKey;
	}

	String getBackIconImageKey() {
		return m_backIconImageKey;
	}

	int getItemCount() {
		return m_items.size();
	}

	String getItemImageKey(int index) {
		return m_items.get(index).imageKey;
	}

	String getItemNextViewKey(int index) {
		return m_items.get(index).nextViewKey;
	}

}
