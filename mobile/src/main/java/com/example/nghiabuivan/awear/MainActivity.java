package com.example.nghiabuivan.awear;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.nghiabuivan.awear.server.ActionListener;
import com.example.nghiabuivan.awear.server.K;
import com.example.nghiabuivan.awear.server.SAwear;
import com.example.nghiabuivan.awear.server.View;
import com.example.nghiabuivan.awear.server.ViewCreator;
import com.example.nghiabuivan.awear.server.ViewPool;

public class MainActivity extends ActionBarActivity implements ViewCreator, ActionListener {

	private SAwear m_awear;
	private TextView m_textView;
	private String m_text;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		m_textView = (TextView) findViewById(R.id.hello);

		m_awear = new SAwear("/sdcard/awear_dir/", this);
		m_awear.registerViewCreator(this);
		m_awear.registerActionListener(this);

		m_awear.connect();
	}

	@Override
	public void createViews(ViewPool pool) {
		View root = pool.createRootView();
		root.setName("MC 5").setBackgroundImageKey("bg_default.png").setBackIconImageKey("back_icon.png");
		root.addItem().setName("Events").setImageKey("icon.png").setNextViewKey("events");
		root.addItem().setName("This is item 2").setImageKey("icon.png");
		root.addItem().setName("This is item 3").setImageKey("icon.png");
		root.addItem().setName("This is item 4").setImageKey("icon.png");
		root.addItem().setName("This is item 5").setImageKey("icon.png");

		View events = pool.createView("events");
		events.setName("EVENTS").setBackgroundImageKey("bg_events.png").setBackIconImageKey("back_icon.png");
		events.addItem().setName("Event 1").setImageKey("icon.png").setSendingKey("get_event").setSendingValue("event_1");
		events.addItem().setName("Event 2").setImageKey("icon.png").setSendingKey("get_event").setSendingValue("event_2");
		events.addItem().setName("Event 3").setImageKey("icon.png");
		events.addItem().setName("Event 4").setImageKey("icon.png");
		events.addItem().setName("Event 5").setImageKey("icon.png");
		events.addItem().setName("Event 6").setImageKey("icon.png");
		events.addItem().setName("Event 7").setImageKey("icon.png");
	}

	@Override
	public void onActionReceived(String key, String value, String nodeId) {
		m_text = "onActionReceived: key=" + key + ", value=" + value + "; from: " + nodeId;
		K.log(m_text);
		runOnUiThread(m_run);
	}

	private Runnable m_run = new Runnable() {
		@Override
		public void run() {
			m_textView.setText(m_textView.getText() + "\n\n" + m_text);
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		m_awear.disconnect();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
