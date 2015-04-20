package com.example.nghiabuivan.awear.client;

public class Item {

	public String name;
	public byte[] image;
	public String nextViewKey;
	public String sendingKey;
	public String sendingValue;

	public Item setName(String name) { this.name = name; return this; }
	public Item setImage(byte[] image) { this.image = image; return this; }
	public Item setNextViewKey(String key) { this.nextViewKey = key; return this; }
	public Item setSendingKey(String key) { this.sendingKey = key; return this; }
	public Item setSendingValue(String value) { this.sendingValue = value; return this; }

}
