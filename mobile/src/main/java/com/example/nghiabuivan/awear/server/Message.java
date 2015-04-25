package com.example.nghiabuivan.awear.server;

public class Message {
	private String m_key = null;
	private byte[] m_data = null;

	private static final int s_offset = 1;
	private static final int s_sizeofint = 4;

	private Message() {

	}

	public Message(String key, byte[] data) {
		m_key = key;
		m_data = data;
	}

	public String getKey() {
		return m_key;
	}

	public Bytes getHeader() {
		if (!isValid()) return null;
		return new Bytes(m_data, s_offset, m_data[0]);
	}

	private boolean isValid() {
		return m_data != null && m_data.length > 0 && s_offset + m_data[0] <= m_data.length;
	}

	public int getHeaderAsInt() throws Exception {
		if (!isValid() || m_data[0] != s_sizeofint) throw new Exception();
		return (int) m_data[4]
				| ( (int) m_data[3] << 8 )
				| ( (int) m_data[2] << 16 )
				| ( (int) m_data[1] << 24 );
	}

	public Bytes getBody() {
		if (!isValid()) return null;
		return new Bytes(m_data, s_offset + m_data[0]);
	}

	public byte[] getData() {

		return m_data;
	}

	public static class Builder {

		private String m_key = null;
		private Bytes m_header = null;
		private Bytes m_body = null;

		public Builder setKey(String key) {
			m_key = key;
			return this;
		}

		public Builder setHeader(Bytes header) {
			m_header = header;
			return this;
		}

		public Builder setHeaderAsInt(int header) {
			byte[] h = new byte[4];
			h[3] = (byte) ( header & 0xFF );
			h[2] = (byte) ( (header >> 8) & 0xFF );
			h[1] = (byte) ( (header >> 16) & 0xFF );
			h[0] = (byte) ( (header >> 24) & 0xFF );
			m_header = new Bytes(h);
			return this;
		}

		public Builder setBody(Bytes body) {
			m_body = body;
			return this;
		}

		public Message build() {
			Message msg = new Message();
			msg.m_key = m_key;

			int headerLength = m_header == null ? 0 : m_header.length;
			int bodyLength = m_body == null ? 0 : m_body.length;

			byte[] buffer = new byte[s_offset + headerLength + bodyLength];
			int offset = 0;

			buffer[offset] = (byte) headerLength;
			offset++;

			if (headerLength > 0) {
				System.arraycopy(m_header.data, m_header.offset, buffer, offset, headerLength);
				offset += headerLength;
			}

			if (bodyLength > 0) {
				System.arraycopy(m_body.data, m_body.offset, buffer, offset, bodyLength);
			}

			msg.m_data = buffer;
			return msg;
		}

	}

}
