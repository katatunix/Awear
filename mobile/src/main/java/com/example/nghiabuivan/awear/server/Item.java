package com.example.nghiabuivan.awear.server;

public class Item {

	public String name;
	public String imageKey;
	public String nextViewKey;
	public String sendingKey;
	public String sendingValue;

	public Item setName(String name) { this.name = name; return this; }
	public Item setImageKey(String k) { this.imageKey = k; return this; }
	public Item setNextViewKey(String k) { this.nextViewKey = k; return this; }
	public Item sendingKey(String k) { this.sendingKey = k; return this; }
	public Item sendingValue(String k) { this.sendingValue = k; return this; }

}
