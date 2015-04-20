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

public class Messenger implements
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		MessageApi.MessageListener {

	private CListener m_listener;
	private GoogleApiClient m_googleClient;
	private Notifier m_notifier = null;

	Messenger(CListener listener, Context context) {
		m_listener = listener;

		m_googleClient = new GoogleApiClient.Builder(context)
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

	public boolean isConnected() {
		return m_googleClient.isConnected();
	}

	@Override
	public void onConnected(Bundle bundle) {
		Wearable.MessageApi.addListener(m_googleClient, this);
	}

	public void send(String key, String value, Notifier notifier) {
		if (!isConnected()) return;

		NodeApi.GetConnectedNodesResult nodes =
				Wearable.NodeApi.getConnectedNodes(m_googleClient).await();

		for (Node node : nodes.getNodes()) {
			PendingResult<MessageApi.SendMessageResult> pendingResult = Wearable.MessageApi.sendMessage(
					m_googleClient, node.getId(),
					key, value.getBytes()
			);

			m_notifier = notifier;

			pendingResult.setResultCallback(m_sendMessageResultCallback);

			// TODO
			break;
		}
	}

	private ResultCallback<MessageApi.SendMessageResult> m_sendMessageResultCallback = new ResultCallback<MessageApi.SendMessageResult>() {
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
