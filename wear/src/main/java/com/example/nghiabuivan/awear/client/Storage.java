package com.example.nghiabuivan.awear.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

class Storage {

	private String m_localDirPath;
	private HashMap<String, byte[]> m_map = new HashMap<>();

	public Storage(String localDirPath) {
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

		String filePath = m_localDirPath + key;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
			int count = fis.available();
			if (count > 0) {
				buffer = new byte[count];
				fis.read(buffer);
				m_map.put(key, buffer);
				K.log("read file: " + filePath + ", data length: " + count);
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

	public void flush() {
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
				K.log("delete folder: " + f);
			} else {
				temp.delete();
				K.log("delete file: " + f);
			}
		}
	}

	private void saveFile(String name, byte[] data) throws IOException {
		K.log("save file: " + name + ", data length: " + data.length);
		FileOutputStream fos = new FileOutputStream(name);
		fos.write(data);
		fos.close();

	}
}
