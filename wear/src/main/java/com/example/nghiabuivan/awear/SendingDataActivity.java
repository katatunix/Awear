package com.example.nghiabuivan.awear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.nghiabuivan.awear.client.Notifier;

public class SendingDataActivity extends Activity implements Notifier {

	private Handler m_handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		// TODO
//		sendingTextView.setVisible(true);
//		resultTextView.setVisible(false);

		Intent intent = getIntent();
		String sendingKey		= intent.getStringExtra("key");
		String sendingValue		= intent.getStringExtra("value");

		DataSender.getInstance().startSend(sendingKey, sendingValue, this);

		m_handler = new MyHandler(this);
	}

	public void onComplete(boolean success, String message) {
		// In another thread
		m_handler.obtainMessage(success ? 1 : 0, message).sendToTarget();
	}

	//====================================================================================================
	//====================================================================================================

	private static class MyHandler extends Handler {
		private Activity m_activity;

		public MyHandler(Activity activity) {
			m_activity = activity;
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0) { // error
				// Show error text: msg.obj TODO
			} else if (msg.what == 1) { // success
				// Show success text: msg.obj TODO
			} else {
				m_activity.finish();
			}

			if (msg.what < 2) {
				// Wating for 100 ms and then close activity
				sendEmptyMessageDelayed(2, 100);
			}
		}
	}
}
