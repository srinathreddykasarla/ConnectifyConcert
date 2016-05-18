package com.avi.connectifyconcert;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;


public class SplashActivity extends Activity {

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Thread timer = new Thread(){

			@Override
			public void run() {
				
				// TODO Auto-generated method stub
				super.run();
				try{
					sleep(2000);
					Intent intent = new Intent(SplashActivity.this,MainActivity.class);
					startActivity(intent);
				}
				catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			
		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	

}
