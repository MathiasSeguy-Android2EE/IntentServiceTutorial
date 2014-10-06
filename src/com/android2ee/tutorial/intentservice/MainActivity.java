package com.android2ee.tutorial.intentservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.com.android2ee.intentservice.R;

/**
 * @author florian
 * 
 */
public class MainActivity extends Activity {
	Button btnToulouse, btnLondon;
	String strTlseForecast, strLondonForecast;
	boolean tlseVisible = true;
	TextView result;
	private static final String LONDON = "London";
	private static final String TOULOUSE = "Toulouse";

	public class MyReceiver extends BroadcastReceiver {
		public static final String ACTION_RESP = "com.myapp.intent.action.TEXT_TO_DISPLAY";

		@Override
		public void onReceive(Context context, Intent intent) {
			String text = intent
					.getStringExtra(DownloadSourceService.SOURCE_URL);
			// send text to display
			if (intent.getStringExtra(DownloadSourceService.CITY).equals(
					TOULOUSE)) {
				strTlseForecast = text;
			} else if (intent.getStringExtra(DownloadSourceService.CITY)
					.equals(LONDON)) {
				strLondonForecast = text;
			}
			changeCity(tlseVisible);

		}
	}

	private MyReceiver receiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		result = (TextView) findViewById(R.id.text_result);
		btnLondon = (Button) findViewById(R.id.btnLondon);
		btnToulouse = (Button) findViewById(R.id.btnToulouse);
		// ajout des listeners
		btnLondon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeCity(false);
			}
		});
		btnToulouse.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeCity(true);
			}
		});
		// on initialise notre broadcast
		receiver = new MyReceiver();
		// on lance le service
		Intent msgIntent = new Intent(this, DownloadSourceService.class);
		msgIntent
				.putExtra(DownloadSourceService.URL,
						"http://api.openweathermap.org/data/2.5/weather?q=London&mode=xml");

		msgIntent.putExtra(DownloadSourceService.CITY, LONDON);
		startService(msgIntent);
		msgIntent
				.putExtra(DownloadSourceService.URL,
						"http://api.openweathermap.org/data/2.5/weather?q=Toulouse&mode=xml");
		msgIntent.putExtra(DownloadSourceService.CITY, TOULOUSE);
		startService(msgIntent);

	}

	/**
	 * @param isTlseVisible
	 */
	private void changeCity(boolean isTlseVisible) {
		tlseVisible = isTlseVisible;
		if (isTlseVisible) {
			result.setText(strTlseForecast);
		} else {
			result.setText(strLondonForecast);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		// on déclare notre Broadcast Receiver
		IntentFilter filter = new IntentFilter(MyReceiver.ACTION_RESP);
		filter.addCategory(Intent.CATEGORY_DEFAULT);
		registerReceiver(receiver, filter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// on désenregistre notre broadcast
		unregisterReceiver(receiver);
	}

}
