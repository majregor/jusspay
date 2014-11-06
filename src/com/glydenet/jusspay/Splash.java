package com.glydenet.jusspay;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Splash extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		getSupportActionBar().hide();
		StartActivityTimer activityTimer = new StartActivityTimer(5000, "android.intent.action.STARTINGPOINT", this);
		activityTimer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
