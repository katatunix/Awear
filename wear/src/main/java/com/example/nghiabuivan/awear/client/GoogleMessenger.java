package com.example.nghiabuivan.awear.client;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

class GoogleMessenger implements
		Messenger,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		MessageApi.MessageListener {

	private ClientListener m_listener = null;
	private final GoogleApiClient m_googleClient;
	private Notifier m_notifier = null;

	private String m_currentNodeId = null;

	private static final String TAG = "Awear";

	public GoogleMessenger(Object context) {
		m_googleClient = new GoogleApiClient.Builder((Context)context)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
	}

	public void connect() {
		if (!isConnected()) {
			m_googleClient.connect();
		}
	}

	public void disconnect() {
		if (isConnected()) {
			m_googleClient.disconnect();
			Wearable.MessageApi.removeListener(m_googleClient, this);
		}
	}

	public boolean send(Message msg, Notifier notifier) {
		if (!isConnected() || m_currentNodeId == null) {
			if (notifier != null) {
				notifier.onComplete(false, "Not connected");
			}
			return false;
		}

		m_notifier = notifier;

		PendingResult<MessageApi.SendMessageResult> pendingResult = Wearable.MessageApi.sendMessage(
				m_googleClient, m_currentNodeId,
				msg.getKey(), msg.getData()
		);
		pendingResult.setResultCallback(m_sendMessageResultCallback);

		Log.d(TAG, "send: " + msg.getKey() + ", length: " + msg.getData().length + "; to: " + m_currentNodeId);
		return true;
	}

	@Override
	public void setReceiveListener(ClientListener listener) {
		m_listener = listener;
	}

	private boolean isConnected() {
		return m_googleClient.isConnected();
	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.MessageApi.addListener(m_googleClient, this);

		m_currentNodeId = null;
		PendingResult<NodeApi.GetConnectedNodesResult> pendingResult = Wearable.NodeApi.getConnectedNodes(m_googleClient);
		pendingResult.setResultCallback(m_getConnectedNodesResultCallback);
	}

	private ResultCallback<MessageApi.SendMessageResult> m_sendMessageResultCallback
			= new ResultCallback<MessageApi.SendMessageResult>() {
		@Override
		public void onResult(MessageApi.SendMessageResult result) {
			if (m_notifier == null) return;

			if (result.getStatus().isSuccess()) {
				Log.d(TAG, "send success");
				m_notifier.onComplete(true, "Success");
			} else {
				Log.d(TAG, "send failed");
				m_notifier.onComplete(false, "Failed");
			}
		}
	};

	private ResultCallback<NodeApi.GetConnectedNodesResult> m_getConnectedNodesResultCallback
			= new ResultCallback<NodeApi.GetConnectedNodesResult>() {
		@Override
		public void onResult(NodeApi.GetConnectedNodesResult result) {
			if (result.getNodes().size() > 0) {
				// TODO: get a specific node
				m_currentNodeId = result.getNodes().get(0).getId();
				Log.d(TAG, "get a connected node: " + m_currentNodeId);
			}
		}
	};

	@Override
	public void onMessageReceived(MessageEvent me) {
		if (m_listener == null) return;
		Log.d(TAG, "onMessageReceived: " + me.getPath() + ", " + me.getData().length + "; from: " + me.getSourceNodeId());

		/**
		 * Structure of a message:
		 * 		First byte is the length of the header
		 * 		The header itself
		 *		The body
		 */

		byte[] data = me.getData();
		final int offset = 1;
		if (data.length < offset) return;

		int headerLength = data[0];
		if (offset + headerLength > data.length) return;

		Message msg = new Message.Builder()
				.setKey( me.getPath() )
				.setHeader( new Bytes(data, offset, headerLength) )
				.setBody( new Bytes(data, offset + headerLength) )
				.build();

		m_listener.onReceived(msg);
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

}
