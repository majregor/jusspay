package com.glydenet.jusspay;

import android.app.Activity;
import android.content.Intent;

public class StartActivityTimer extends Thread{

	private int interval;
	private String activityIdentifier;
	private Activity parentActivity;
	
	public StartActivityTimer(int interval, String activityIdentifier, Activity parent){
		this.interval = interval;
		this.activityIdentifier = activityIdentifier;
		this.parentActivity = parent;
	}
	
	public void run(){
		try{
			sleep(interval);
			Intent intent = new Intent(activityIdentifier);
			parentActivity.startActivity(intent);
		}
		catch(InterruptedException e){
			e.printStackTrace();
		}
		finally{
			
		}
	}
}
