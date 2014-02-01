package com.chrosciu.taboo;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.TextView;

public class CardActivity extends SherlockActivity {
	
	class TimerRunnable implements Runnable {
		@Override
        public void run() {
			if (counter > 0) {
				TextView timerView = (TextView)(findViewById(R.id.timer));
				timerView.setText(getString(R.string.time_remaining, counter));
				timerHandler.postDelayed(this, 1000);
				counter--;
			} else {
				NavUtils.navigateUpFromSameTask(CardActivity.this);
			}
		}
	}
	
	private static final String CARD_KEY = "card";
	private static final String COUNTER_KEY = "counter";
	private Card card = null;
	private Handler timerHandler = new Handler();
	private Runnable timerRunnable = new TimerRunnable();
	private int counter = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		setupActionBar();
		if (savedInstanceState != null) {
			card = (Card)(savedInstanceState.get(CARD_KEY));
			counter = savedInstanceState.getInt(COUNTER_KEY, 0);
		} else {
			card = loadCard();
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			counter = Integer.parseInt(
					sharedPref.getString(
							getString(R.string.pref_timeout_key), 
							getString(R.string.settings_default_timeout_value)
							)
					);
		}
		displayCard();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		timerHandler.postDelayed(timerRunnable, 0);
	}
	
	@Override
	protected void onPause() {
		timerHandler.removeCallbacks(timerRunnable);
		super.onPause();
	}
	
	@Override 
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(CARD_KEY, card);
		outState.putInt(COUNTER_KEY, counter);
		super.onSaveInstanceState(outState);
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.empty, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void nextCard(View view) {
		card = loadCard();
		displayCard();
    }
	
	private Card loadCard() {
		return CardLoader.loadCard(getAssets());
	}
	
	private void displayCard() {
		TextView wordView = (TextView)findViewById(R.id.word);
		wordView.setText(card.getWord());
		
		TextView tabooViews[] = {
			(TextView)findViewById(R.id.taboo1),
			(TextView)findViewById(R.id.taboo2),
			(TextView)findViewById(R.id.taboo3),
			(TextView)findViewById(R.id.taboo4),
			(TextView)findViewById(R.id.taboo5)
		};
		for (int i = 0; i < 5; ++i) {
			tabooViews[i].setText(card.getTaboos()[i]);
		}
	}

}
