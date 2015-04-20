package com.example.nghiabuivan.awear.client;

import java.util.HashMap;

public class DataSource {

	private String m_localDirPath;
	private HashMap<String, byte[]> m_map = new HashMap<>();

	DataSource(String localDirPath) {
		m_localDirPath = localDirPath;
	}

	public boolean hasKey(String key) {
		return m_map.containsKey(key);
	}

	public byte[] get(String key) {
		// TODO
		return m_map.get(key);
	}

	public void put(String key, byte[] value) {
		m_map.put(key, value);
	}

}
