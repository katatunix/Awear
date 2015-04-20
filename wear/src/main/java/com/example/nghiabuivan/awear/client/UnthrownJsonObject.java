package com.example.nghiabuivan.awear.client;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

class UnthrownJsonObject {

	private JSONObject m_obj;

	public UnthrownJsonObject(JSONObject obj) {
		m_obj = obj;
	}

	public void set(JSONObject obj) {
		m_obj = obj;
	}

	public String getString(String name) {
		try {
			return m_obj.getString(name);
		} catch (JSONException e) {
			return null;
		}
	}

	public JSONArray getJSONArray(String name) {
		try {
			return m_obj.getJSONArray(name);
		} catch (JSONException e) {
			return null;
		}
	}
}
