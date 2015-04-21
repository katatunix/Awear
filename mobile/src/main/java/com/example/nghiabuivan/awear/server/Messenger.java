package com.example.nghiabuivan.awear.server;

public interface Messenger {

	public void connect();
	public void disconnect();

	public void setTargetNodeId(String nodeId);

	public void send(String key, String value);
	public void send(String key, byte[] value);

	public void setListener(ActionListener listener);

}
