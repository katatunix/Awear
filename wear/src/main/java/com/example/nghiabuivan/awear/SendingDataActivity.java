package com.example.nghiabuivan.awear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.example.nghiabuivan.awear.client.CAwear;
import com.example.nghiabuivan.awear.client.Notifier;

public class SendingDataActivity extends Activity implements Notifier {

	private Handler m_handler;

	private TextView m_textStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sending);

		m_textStatus = (TextView) findViewById(R.id.text_sending_status);

		m_textStatus.setText("Sending...");

		Intent intent = getIntent();
		String sendingKey		= intent.getStringExtra("key");
		String sendingValue		= intent.getStringExtra("value");

		CAwear.getInstance().sendAction(sendingKey, sendingValue, this);

		m_handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what < 2) {
					m_textStatus.setText((String)msg.obj);

					// Wating for 3000 ms and then close activity
					sendEmptyMessageDelayed(2, 3000);
				} else {
					finish();
				}
			}
		};
	}

	public void onComplete(boolean success, String message) {
		// In another thread
		m_handler.obtainMessage(success ? 1 : 0, message).sendToTarget();
	}
}
