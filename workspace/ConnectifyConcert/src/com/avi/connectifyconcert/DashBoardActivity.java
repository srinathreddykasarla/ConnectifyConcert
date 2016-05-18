package com.avi.connectifyconcert;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class DashBoardActivity extends ListActivity {
	static final String SENDER_ID = "1089043749159"; 
	AsyncTask<Void, Void, Void> mRegisterTask;
	String menu_dev[] = {"My Projects","My Teams","Upload Progress","Log out"};
	String menu_mgr[] = {"My Projects","My Teams","Member Details","Assign Activity","Log out"};
	String menu_sh[] = {"My Projects","My Team","Project Status","Log out"};
	public final static String USER_NAME = "com.avi.connectify.USER_NAME";
	public static final String TAG = "log_tag";
	public final static String TYPE = "com.avi.connectify.TYPE";

	 String username;
	 String type;
	 String regId;
	private static String getClassName(int position,String post)
	{
		String result = "";
		if(post.equals("dev")){
			switch(position)
			{
			case 0: result = "ProjectActivity";
			break;
			case 1: result = "TeamActivity";
			break;
			case 2: result = "UploadProgressActivity";
			break;
			
			case 3: result = "Logout";
			break;
			
			}
		}
		else if(post.equals("mgr"))
		{
			switch(position)
			{
			case 0: result = "ProjectActivity";
			break;
			case 1: result = "TeamActivity";
			break;
			
			case 2: result = "MemberDetails";
			break;
			case 3: result = "AssignActivity";
			break;
			case 4: result = "Logout";
			break;
			
			}
		}
		else if(post.equals("sh"))
		{
			switch(position)
			{
			case 0: result = "ProjectActivity";
			break;
			case 1: result = "TeamActivity";
			break;
			case 2: result = "ProjectStatusActivity";
			break;
			
			case 3: result = "Logout";
			break;
			
			}
		}
	
		return result;
	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		String item = getClassName(position,type);
		try {
			@SuppressWarnings("rawtypes")
			Class ourClass = Class.forName("com.avi.connectifyconcert."+item);
			Intent intent = new Intent(DashBoardActivity.this,ourClass);
			intent.putExtra(USER_NAME, username);
			intent.putExtra(TYPE, type);
			Log.d(TAG,username);
			startActivity(intent);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		Intent getintent = getIntent();
		regId = GCMRegistrar.getRegistrationId(this);
		 username = getintent.getStringExtra(MainActivity.USER_NAME);
		 type = getintent.getStringExtra(MainActivity.TYPE);
		if(type.contentEquals("dev")){
			setListAdapter(new ArrayAdapter<String>(DashBoardActivity.this,android. R.layout.simple_list_item_1, menu_dev));
		}
		else if(type.contentEquals("mgr")){
			setListAdapter(new ArrayAdapter<String>(DashBoardActivity.this,android. R.layout.simple_list_item_1, menu_mgr));
		}
		else if(type.contentEquals("sh")){
			setListAdapter(new ArrayAdapter<String>(DashBoardActivity.this,android. R.layout.simple_list_item_1, menu_sh));
		}
		
		GCMRegistrar.checkDevice(this);
		GCMRegistrar.checkManifest(this);
		 regId = GCMRegistrar.getRegistrationId(this);
		if (regId.equals("")) {
			  GCMRegistrar.register(this, SENDER_ID);
		} else {
			// Device is already registered on GCM
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.				
				
				Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
			
			}else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {
						// Register on our server
						// On server creates a new user
						ServerUtilities.register(context, username, "dummy place", regId);
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
	
		
	}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
