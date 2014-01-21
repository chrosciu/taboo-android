package com.chrosciu.taboo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;

public class CardActivity extends ActionBarActivity {
	
	private static final String CARD_KEY = "card";
	private Card card = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_card);
		setupActionBar();
		if (savedInstanceState != null) {
			card = (Card)(savedInstanceState.get(CARD_KEY));
		}
		if (null == card) {
			card = loadCard();
		}
		displayCard();
	}

	private void setupActionBar() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.card, menu);
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
	
	@Override 
	public void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(CARD_KEY, card);
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
