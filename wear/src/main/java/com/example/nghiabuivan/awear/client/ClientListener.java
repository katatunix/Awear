package com.example.nghiabuivan.awear.client;

interface ClientListener {
	public void onReceived(String key, byte[] value);
}
