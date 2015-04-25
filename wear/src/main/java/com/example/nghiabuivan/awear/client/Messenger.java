package com.example.nghiabuivan.awear.client;

interface Messenger {

	public void connect();

	public void disconnect();

	public boolean send(Message msg, Notifier notifier);

	public void setReceiveListener(ClientListener listener);
}
