package com.chrosciu.taboo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class CardActivity extends SherlockActivity {
	
	public static final String TEAM_KEY = "com.chrosciu.taboo.TEAM";
	
	private static final String CARD_KEY = "card";
	private static final String TIME_KEY = "time";
	private static final String POINTS_KEY = "points";
	
	private Card card = null;
	private Handler timerHandler = new Handler();
	private Runnable timerRunnable = new Runnable() {
		@Override
        public void run() {
			handleTimerTick();
		}
	};
	private int time = 0;
	private int points = 0;
	private boolean team = true;
	private boolean sound = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		team = getIntent().getBooleanExtra(TEAM_KEY, true);
		setContentView(R.layout.card_activity);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setTitle(getString(R.string.team, team ? 1 : 2));
		if (savedInstanceState != null) {
			restoreData(savedInstanceState);
		} else {
			initData();
		}
		displayTimer();
		displayPoints();
		displayCard();
	}

	private void initData() {
		card = loadCard();
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
		time = Integer.parseInt(
				sharedPref.getString(
						getString(R.string.pref_timeout_key), 
						getString(R.string.settings_default_timeout_value)
						)
				);
		sound = sharedPref.getBoolean(getString(R.string.pref_sound_key), true);
		points = 0;
	}

	private void restoreData(Bundle savedInstanceState) {
		card = (Card)(savedInstanceState.get(CARD_KEY));
		time = savedInstanceState.getInt(TIME_KEY, 0);
		points = savedInstanceState.getInt(POINTS_KEY, 0);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		scheduleTimer();
	}

	private void scheduleTimer() {
		timerHandler.postDelayed(timerRunnable, 1000);
	}
	
	@Override
	protected void onPause() {
		descheduleTimer();
		super.onPause();
	}

	private void descheduleTimer() {
		timerHandler.removeCallbacks(timerRunnable);
	}
	
	@Override 
	public void onSaveInstanceState(Bundle outState) {
		storeData(outState);
		super.onSaveInstanceState(outState);
	}

	private void storeData(Bundle outState) {
		outState.putSerializable(CARD_KEY, card);
		outState.putInt(TIME_KEY, time);
		outState.putInt(POINTS_KEY, points);
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
			displayAbortConfirmationDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onBackPressed() {
		displayAbortConfirmationDialog();
	}
	
	private void handleTimerTick() {
		time--;
		displayTimer();
		if (time > 0) {
			scheduleTimer();
		} else {
			playSound(R.raw.failure);
			displaySummaryDialog();
		}
	}
	
	public void passed(View view) {
		points++;
		displayPoints();
		playSound(R.raw.success);
		nextCard();
	}
	
	public void failed(View view) {
		playSound(R.raw.failure);
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
	
	private void displaySummaryDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.end));
		alertDialog.setMessage(getString(R.string.points, points));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   updateScore();
			   NavUtils.navigateUpFromSameTask(CardActivity.this);
		   }
		});
		alertDialog.setIcon(android.R.drawable.ic_dialog_info);
		alertDialog.show();
	}
	
	private void displayAbortConfirmationDialog() {
		descheduleTimer();
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.confirmation));
		alertDialog.setMessage(getString(R.string.end_confirmation_question));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   NavUtils.navigateUpFromSameTask(CardActivity.this);
		   }
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   dialog.dismiss();
			   scheduleTimer();
		   }
		});
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.show();
	}
	
	private void updateScore() {
		Score.add(team, points);
	}
	
	private void playSound(int resourceId) {
		if (!sound) {
			return;
		}
		MediaPlayer player = MediaPlayer.create(this, resourceId);
		player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
			public void onCompletion(MediaPlayer mp) {
				mp.release();
			}
		});
		player.start();
	}

}
