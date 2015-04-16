package com.example.nghiabuivan.awear;

public class DataSender {

	private static DataSender m_instance = null;

	private DataSender() {

	}

	public static DataSender getInstance() {
		if (m_instance == null) {
			m_instance = new DataSender();
		}
		return m_instance;
	}

	public void startSend(String key, String value, Notifier notifier) {
		// TODO
	}

}
