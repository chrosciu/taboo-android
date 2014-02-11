package com.chrosciu.taboo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class MainActivity extends SherlockActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        displayScore();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
	public void onBackPressed() {
		displayAbortConfirmationDialog();
	}
    
    public void team1(View view) {
    	Intent intent = new Intent(this, CardActivity.class);
    	intent.putExtra(CardActivity.TEAM_KEY, true);
    	startActivity(intent);
    }
    
    public void team2(View view) {
    	Intent intent = new Intent(this, CardActivity.class);
    	intent.putExtra(CardActivity.TEAM_KEY, false);
    	startActivity(intent);
    }
    
    private void openSettings() {
    	Intent intent = new Intent(this, SettingsActivity.class);
    	startActivity(intent);
    }
    
    public void resetScore(View view) {
    	displayResetConfirmationDialog();
    	
    }
    
    private void displayScore() {
    	TextView team1Score = (TextView)findViewById(R.id.team1_score);
    	TextView team2Score = (TextView)findViewById(R.id.team2_score);
    	team1Score.setText(getString(R.string.team_score, 1, Score.get(true)));
    	team2Score.setText(getString(R.string.team_score, 2, Score.get(false)));
    }
    
    private void displayResetConfirmationDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.confirmation));
		alertDialog.setMessage(getString(R.string.reset_confirmation_question));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   Score.reset();
			   displayScore();
			   dialog.dismiss();
		   }
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   dialog.dismiss();
		   }
		});
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.show();
	}
    
    private void displayAbortConfirmationDialog() {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle(getString(R.string.confirmation));
		alertDialog.setMessage(getString(R.string.end_confirmation_question));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.ok), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   Score.reset();
			   MainActivity.this.finish();
		   }
		});
		alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
			   dialog.dismiss();
		   }
		});
		alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
		alertDialog.show();
	}
    
}
