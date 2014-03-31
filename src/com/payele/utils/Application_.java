package com.payele.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;


public class Application_ extends Application{
	private List<Activity> activities = new LinkedList<Activity>();
	private static Application_ instance;
	private Application_ () {}
	
	public static Application_ getInstatnce() {
	    if (instance == null)
	    	instance = new Application_();
	    
	    return instance;
    }
	
	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	public void exit() {
		for (Activity activity:activities) {
			activity.finish();
		}
		
		System.exit(0);
	}
}
