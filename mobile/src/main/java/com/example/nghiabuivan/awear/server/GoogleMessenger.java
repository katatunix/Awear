package com.example.nghiabuivan.awear.server;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

class GoogleMessenger implements
		Messenger,
		GoogleApiClient.ConnectionCallbacks,
		GoogleApiClient.OnConnectionFailedListener,
		MessageApi.MessageListener {

	private ActionListener m_listener = null;
	private GoogleApiClient m_googleClient;

	private static final String TAG = "Awear";

	public GoogleMessenger(Object context) {
		m_googleClient = new GoogleApiClient.Builder((Context)context)
				.addApi(Wearable.API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
	}

	@Override
	public void connect() {
		if (!isConnected()) {
			m_googleClient.connect();
		}
	}

	@Override
	public void disconnect() {
		if (isConnected()) {
			m_googleClient.disconnect();
			Wearable.MessageApi.removeListener(m_googleClient, this);
		}
	}

	@Override
	public void send(Message msg, String nodeId) {
		Log.d(TAG, "send: " + msg.getKey() + ", " + msg.getData().length + "; to: " + nodeId);
		Wearable.MessageApi.sendMessage(m_googleClient, nodeId, msg.getKey(), msg.getData()).await();
	}

	@Override
	public void setListener(ActionListener listener) {
		m_listener = listener;
	}

	private boolean isConnected() {
		return m_googleClient.isConnected();
	}

	@Override
	public void onConnected(Bundle bundle) {
		Log.d(TAG, "onConnected");
		Wearable.MessageApi.addListener(m_googleClient, this);
	}

	@Override
	public void onMessageReceived(MessageEvent me) {
		if (m_listener != null) {
			Log.d(TAG, "onMessageReceived: " + me.getPath() + ", length: " + me.getData().length + "; from: " + me.getSourceNodeId());
			m_listener.onActionReceived( new Message(me.getPath(), me.getData()), me.getSourceNodeId() );
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {

	}

}
