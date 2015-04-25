package com.example.nghiabuivan.awear.client;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

class Storage {

	private String m_localDirPath;
	private HashMap<String, Bytes> m_map = new HashMap<>();
	private boolean m_hasNewData = false;

	public Storage(String localDirPath) {
		m_localDirPath = localDirPath;
	}

	public boolean hasKey(String key) {
		if ( m_map.containsKey(key) ) return true;
		File f = new File(m_localDirPath + key);
		return f.exists();
	}

	public Bytes get(String key) {
		if ( m_map.containsKey(key) ) return m_map.get(key);

		Bytes ret = null;

		String filePath = m_localDirPath + key;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			int count = fis.available();
			if (count > 0) {
				byte[] buffer = new byte[count];
				fis.read(buffer);
				m_map.put(key, ret = new Bytes(buffer));
				Log.d("Awear", "read file: " + filePath + ", data length: " + count);
			}
		} catch (IOException ignored) {
			ret = null;
		} finally {
			if (fis != null) try {
				fis.close();
			} catch (IOException ignored) {

			}
		}

		return ret;
	}

	public void put(String key, Bytes value) {
		m_map.put(key, value);
		m_hasNewData = true;
	}

	public void flush() {
		if (!m_hasNewData) return;
		try {
			emptyDir(new File(m_localDirPath));
			for (String name : m_map.keySet()) {
				saveFile(m_localDirPath + name, m_map.get(name));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void emptyDir(File dir) throws IOException {
		String[] files = dir.list();

		for (String f : files) {
			File temp = new File(dir.getAbsolutePath() + "/" + f);
			if (temp.isDirectory()) {
				emptyDir(temp);
				temp.delete();
				Log.d("Awear", "delete folder: " + f);
			} else {
				temp.delete();
				Log.d("Awear", "delete file: " + f);
			}
		}
	}

	private void saveFile(String name, Bytes bytes) throws IOException {
		Log.d("Awear", "save file: " + name + ", data length: " + bytes.length);
		FileOutputStream fos = new FileOutputStream(name);
		fos.write(bytes.data, bytes.offset, bytes.length);
		fos.close();
	}

}
