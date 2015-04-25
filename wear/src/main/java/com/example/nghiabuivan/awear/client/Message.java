package com.example.nghiabuivan.awear.client;

public class Message {

	public String getKey() {
		return null;
	}

	public Bytes getHeader() {
		return null;
	}

	public int getHeaderAsInt() throws Exception {
		return 0;
	}

	public Bytes getBody() {
		return null;
	}

	/**
	 * Never return null, return new byte[0] instead
	 * @return The raw data of the whole message, except the key
	 */
	public byte[] getData() {

		return new byte[0];
	}

	public static class Builder {

		public Builder setKey(String key) {
			return this;
		}

		public Builder setHeader(Bytes header) {
			return this;
		}

		public Builder setHeaderAsInt(int header) {
			return this;
		}

		public Builder setBody(Bytes body) {
			return this;
		}

		public Message build() {
			return null;
		}

	}

}
