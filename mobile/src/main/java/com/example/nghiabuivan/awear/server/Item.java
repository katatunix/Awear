package com.example.nghiabuivan.awear.server;

public class Item {

	public String name = null;
	public String imageKey = null;
	public String nextViewKey = null;
	public String sendingKey = null;
	public String sendingValue = null;

	public Item setName(String name) { this.name = name; return this; }
	public Item setImageKey(String k) { this.imageKey = k; return this; }
	public Item setNextViewKey(String k) { this.nextViewKey = k; return this; }
	public Item sendingKey(String k) { this.sendingKey = k; return this; }
	public Item sendingValue(String v) { this.sendingValue = v; return this; }

}
