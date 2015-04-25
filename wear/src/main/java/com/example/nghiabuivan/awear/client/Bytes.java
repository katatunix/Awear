package com.example.nghiabuivan.awear.client;

public class Bytes {

	public byte[] data;
	public int offset, length;

	public Bytes(byte[] data) {
		this(data, 0, data.length);
	}

	public Bytes(byte[] data, int offset) {
		this(data, offset, data.length - offset);
	}

	public Bytes(byte[] data, int offset, int length) {
		this.data = data;
		this.offset = offset;
		this.length = length;
	}

}
