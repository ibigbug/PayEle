package com.payele.ui;

import com.payele.utils.Application_;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/** 
 * @ClassName: OnLoading 
 * @Description: App Loading
 * @author Yuwei Ba <i@xiaoba.me>
 * @date Mar 6, 2014 5:21:02 PM 
 *  
 */

public class OnLoading extends Activity {
	
	private static final long SPLASH_DELAY_MILLIS = 2000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.on_loading);
		
		Application_.getInstatnce().addActivity(this);
		
		new Handler().postDelayed(new Runnable() {
			public void run() {
				goHome();
			}
		}, SPLASH_DELAY_MILLIS);
		
		
	}

	private void goHome() {
		Intent intent = new Intent(OnLoading.this, MainActivity.class);
		OnLoading.this.startActivity(intent);
		OnLoading.this.finish();
	}
}
