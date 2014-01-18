package com.chrosciu.taboo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;

import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;

public class CardActivity extends ActionBarActivity {
	
	private static final String TAG = "CardActivity";
	private static Random random = new Random();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		// Show the Up button in the action bar.
		setupActionBar();
		//Log.i(TAG, "onCreate");
		loadCard();
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.card, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		NavUtils.navigateUpFromSameTask(this);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	public void nextCard(View view) {
    	Intent intent = new Intent(this, CardActivity.class);
    	startActivity(intent);
    }
	
	private void loadCard() {
		AssetManager assetManager = getAssets();
		try {
			
			/* 
			String tabooFiles[] = assetManager.list("taboos");
			int taboosCount = tabooFiles.length;
			//Log.i(TAG, "taboosCount -> " + taboosCount);
			if (0 == taboosCount) {
				return;
			}
			
			int tabooNumber = random.nextInt(taboosCount);
			String tabooFile = "taboos/" + tabooFiles[tabooNumber];
			//Log.i(TAG, "tabooFile -> " + tabooFile);
			 
			*/
			
			int taboosCount = 907;
			int tabooNumber = random.nextInt(taboosCount) + 1;
			String tabooFile = "taboos/taboo" + tabooNumber + ".txt";
			
			InputStream inputStream = assetManager.open(tabooFile, AssetManager.ACCESS_BUFFER);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			String line = bufferedReader.readLine();
			//Log.i(TAG, "line -> " + line);
			
			String[] taboos = line.split(";");
			String word = taboos[0].toUpperCase();
			
			TextView wordView = (TextView)findViewById(R.id.word);
			wordView.setText(word);
			
			TextView tabooViews[] = {
				(TextView)findViewById(R.id.taboo1),
				(TextView)findViewById(R.id.taboo2),
				(TextView)findViewById(R.id.taboo3),
				(TextView)findViewById(R.id.taboo4),
				(TextView)findViewById(R.id.taboo5)
			};
			for (int i = 0; i < 5; ++i) {
				tabooViews[i].setText(taboos[i + 1]);
			}
			
		} catch (Exception e) {
			Log.e(TAG, "", e);
		}
	}

}