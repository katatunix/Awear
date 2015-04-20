package com.example.nghiabuivan.awear.client;

interface Messenger {

	public void connect();

	public void disconnect();

	public void send(String key, String value, Notifier notifier);

	public void setReceiveListener(CListener listener);
}
