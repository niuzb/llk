
package com.dozingcatsoftware.bouncy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;

public class BouncyAndroid extends AndroidApplication {
	public static final int EIGHT_ID = Menu.FIRST+1;
	public static final int TWO_ID = Menu.FIRST+4;
	public static final int ONE_ID = Menu.FIRST+7;
	
	public static final String BouncyAndroid = "BouncyAndroid";
	public static final String NO1 = "no1";
	public static final String NO2 = "no2";
	public static final String NO3 = "no3";
	
	private long no1, no2, no3;
	private SharedPreferences.Editor mPrefsEditor;
	
	/** Called when the activity is first created. */
	@Override public void onCreate (Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		restoreScore();
		initialize(new Bouncy(), false, new FillResolutionStrategy(), 16);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return(super.onCreateOptionsMenu(menu));
	}
	public void reload() {
		    onStop();
		    onCreate(getIntent().getExtras());
	}
	private void populateMenu(Menu menu) {
			menu.add(Menu.NONE, ONE_ID, Menu.NONE, "声音开关");
			menu.add(Menu.NONE, TWO_ID, Menu.NONE, "退出");
			menu.add(Menu.NONE, EIGHT_ID, Menu.NONE, "排行榜");			
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	return(applyMenuChoice(item) ||
	super.onOptionsItemSelected(item));
	}
	private boolean applyMenuChoice(MenuItem item) {
		switch (item.getItemId()) {
		case ONE_ID:
			Bouncy.soundEnabled=!Bouncy.soundEnabled;
		return(true);
		case TWO_ID:
			saveScore();
			this.finish();
			return(true);
		case EIGHT_ID:
			saveScore();
			showScoreDialog();
			return(true);

		}
		return(false);
	}
	
	private void restoreScore() {
		SharedPreferences prefs = this.getApplicationContext().getSharedPreferences(BouncyAndroid, MODE_PRIVATE);
		
		no1 = prefs.getLong("NO1", 0);
		no2 = prefs.getLong("NO2", 0);
		no3 = prefs.getLong("NO3", 0);
	}
	
	private void saveScore() {
		SharedPreferences prefs = this.getApplicationContext().getSharedPreferences(BouncyAndroid, MODE_PRIVATE);
		mPrefsEditor = prefs.edit();
		long score = GameState.score;
		if (score > no1) no1 = score;
		else if (score > no2)  no2 = score;
		else if (score > no3)  no3 = score;
		mPrefsEditor.putLong("NO1", no1);
		mPrefsEditor.putLong("NO2", no2);
		mPrefsEditor.putLong("NO3", no3);
		mPrefsEditor.commit();
	}
	 @Override
	 protected void onStop() {	    	
		 saveScore();
	     super.onStop();	    	
	 }
	 
	 private void showScoreDialog() {
		 final String score[] = new String[] {""+no1, ""+no2,""+no3,};
		 
		 AlertDialog dialog =new AlertDialog.Builder(this).setTitle("排行榜")
		 .setItems(score, new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int which) {
				 
			 }
		 }).create();
		 dialog.show();
		 
	 }
}
