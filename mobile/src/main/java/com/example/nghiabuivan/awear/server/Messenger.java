package com.example.nghiabuivan.awear.server;

interface Messenger {

	public void connect();
	public void disconnect();

	public void send(Message msg, String nodeId);

	public void setListener(ServerListener listener);

}
