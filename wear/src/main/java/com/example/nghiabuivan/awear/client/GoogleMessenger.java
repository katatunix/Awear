package com.example.nghiabuivan.awear.client;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

class GoogleMessenger implements
		Messenger,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		MessageApi.MessageListener {

	private CListener m_listener = null;
	private GoogleApiClient m_googleClient;
	private Notifier m_notifier = null;

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

	public void send(String key, String value, Notifier notifier) {
		if (!isConnected()) {
			notifier.onComplete(false, "Not connected");
			return;
		}

		if (value == null) value = "";
		byte[] data = value.getBytes();

		NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(m_googleClient).await();
		if (nodes.getNodes().size() <= 0) {
			notifier.onComplete(false, "No connected nodes");
			return;
		}

		m_notifier = notifier;

		// TODO: send to a specific node
		Node node = nodes.getNodes().get(0);
		PendingResult<MessageApi.SendMessageResult> pendingResult = Wearable.MessageApi.sendMessage(
				m_googleClient, node.getId(),
				key, data
		);
		pendingResult.setResultCallback(m_sendMessageResultCallback);
	}

	@Override
	public void setReceiveListener(CListener listener) {
		m_listener = listener;
	}

	private boolean isConnected() {
		return m_googleClient.isConnected();
	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.MessageApi.addListener(m_googleClient, this);
	}

	private ResultCallback<MessageApi.SendMessageResult> m_sendMessageResultCallback
			= new ResultCallback<MessageApi.SendMessageResult>() {
		@Override
		public void onResult(MessageApi.SendMessageResult result) {
			if (m_notifier == null) return;

			if (result.getStatus().isSuccess()) {
				m_notifier.onComplete(true, "Success");
			} else {
				m_notifier.onComplete(false, "Failed");
			}
		}
	};

	@Override
	public void onMessageReceived(MessageEvent me) {
		if (m_listener != null) {
			m_listener.onReceived(me.getPath(), me.getData());
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

}
