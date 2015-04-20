package com.example.nghiabuivan.awear.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

class DataSource {

	private String m_localDirPath;
	private HashMap<String, byte[]> m_map = new HashMap<>();

	public DataSource(String localDirPath) {
		m_localDirPath = localDirPath;
	}

	public boolean hasKey(String key) {
		if ( m_map.containsKey(key) ) return true;
		File f = new File(m_localDirPath + key);
		return f.exists();
	}

	public byte[] get(String key) {
		if ( m_map.containsKey(key) ) return m_map.get(key);

		byte[] buffer = null;
		File f = new File(m_localDirPath + key);

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			int count = fis.available();
			if (count > 0) {
				buffer = new byte[count];
				fis.read(buffer);
				m_map.put(key, buffer);
			}
		} catch (IOException ignored) {
			buffer = null;
		} finally {
			if (fis != null) try {
				fis.close();
			} catch (IOException ignored) {

			}
		}

		return buffer;
	}

	public void put(String key, byte[] value) {
		m_map.put(key, value);
	}

}
