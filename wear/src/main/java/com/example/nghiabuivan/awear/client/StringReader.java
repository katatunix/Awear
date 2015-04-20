package com.example.nghiabuivan.awear.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StringReader {
	String m_path;

	StringReader(String path) {
		m_path = path;
	}

	public String read() throws Exception {
		File f = new File(m_path);
		FileInputStream fin = new FileInputStream(f);
		String ret = convertStreamToString(fin);
		fin.close();
		return ret;
	}

	private String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		return sb.toString();
	}

}
