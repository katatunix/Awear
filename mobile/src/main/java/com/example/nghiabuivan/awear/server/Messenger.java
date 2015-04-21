package com.example.nghiabuivan.awear.server;

interface Messenger {

	public void connect();
	public void disconnect();

	public void send(String key, String value, String nodeId);
	public void send(String key, byte[] value, String nodeId);

	public void setListener(ActionListener listener);

}
