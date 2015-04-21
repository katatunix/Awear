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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		TextView textView = (TextView) findViewById(R.id.hello);

		K.log("local dir path: " + getFilesDir().getAbsolutePath());
		m_awear = new SAwear(getFilesDir().getAbsolutePath() + '/', this);
		m_awear.registerViewCreator(this);
		m_awear.registerActionListener(this);
	}

	@Override
	public void createViews(ViewPool pool) {
		View root = pool.createRootView();
		root.setName("Main Menu");
		root.addItem().setName("This is item 1");
		root.addItem().setName("This is item 2");
		root.addItem().setName("This is item 3");
		root.addItem().setName("This is item 4");
		root.addItem().setName("This is item 5");
	}

	@Override
	public void onActionReceived(String key, String value, String nodeId) {
		K.log("onActionReceived: " + key + ", " + value + "; from: " + nodeId);
	}

	@Override
	protected void onStart() {
		super.onStart();
		m_awear.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();
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
