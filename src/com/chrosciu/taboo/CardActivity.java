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
			handleTimerTick();
		}
	}
	
	private static final String CARD_KEY = "card";
	private static final String TIME_KEY = "time";
	private static final String POINTS_KEY = "points";
	private Card card = null;
	private Handler timerHandler = new Handler();
	private Runnable timerRunnable = new TimerRunnable();
	private int time = 0;
	private int points = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (savedInstanceState != null) {
			card = (Card)(savedInstanceState.get(CARD_KEY));
			time = savedInstanceState.getInt(TIME_KEY, 0);
			points = savedInstanceState.getInt(POINTS_KEY, 0);
		} else {
			card = loadCard();
			SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
			time = Integer.parseInt(
					sharedPref.getString(
							getString(R.string.pref_timeout_key), 
							getString(R.string.settings_default_timeout_value)
							)
					);
			points = 0;
		}
		displayTimer();
		displayPoints();
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
		outState.putInt(TIME_KEY, time);
		outState.putInt(POINTS_KEY, points);
		super.onSaveInstanceState(outState);
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
	
	private void handleTimerTick() {
		if (time > 0) {
			displayTimer();
			timerHandler.postDelayed(timerRunnable, 1000);
			time--;
		} else {
			NavUtils.navigateUpFromSameTask(this);
		}
	}
	
	public void passed(View view) {
		points++;
		displayPoints();
		nextCard();
	}
	
	public void failed(View view) {
		nextCard();
	}
	
	private void nextCard() {
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
	
	private void displayPoints() {
		TextView pointsView = (TextView)(findViewById(R.id.points));
		pointsView.setText(getString(R.string.points, points));
	}
	
	private void displayTimer() {
		TextView timerView = (TextView)(findViewById(R.id.timer));
		timerView.setText(getString(R.string.time_remaining, time));
	}

}
