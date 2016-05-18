package com.avi.connectifyconcert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;





import android.os.AsyncTask;
import android.os.Bundle;

import android.app.ListActivity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.NavUtils;

public class ProjectActivity extends ListActivity {
	public String detailsLIst[]={"Loading..."};
	public String idList[];
	private String username;
	private String type;
    String[] params = new String[2];
	public static final String PROJECT_ID = "com.avi.connectify.PROJECT_ID";
	public static final String DEV_ID = "com.avi.connectify.DID";
	public static final String TAG = "log_tag";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project);
		// Show the Up button in the action bar.
		Intent intent = getIntent();
		username = intent.getStringExtra(DashBoardActivity.USER_NAME);
		type = intent.getStringExtra(DashBoardActivity.TYPE);
		intent.removeExtra(DashBoardActivity.USER_NAME);
		Log.d(TAG,"Project:"+username);
		Log.d(TAG,"Type:"+type);
		params[0] = username;
		params[1] = type;
		setupActionBar();
		setListAdapter(new ArrayAdapter<String>(ProjectActivity.this,android. R.layout.simple_list_item_1, detailsLIst));
		new DBAsyncTask().execute(params);

	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		
		
		Intent intent = new Intent(ProjectActivity.this,ProjectDetailsActivity.class);
		intent.putExtra(PROJECT_ID, idList[position]);
		intent.putExtra(DEV_ID, username);
		startActivity(intent);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	class DBAsyncTask extends AsyncTask<String, Void, String> {
	

			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				
				String result = "";
				InputStream isr = null;
				try{
					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = null;
					httppost = new HttpPost("http://testandroid.net46.net/myprojects.php");
					ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();           
					   nameValuePairs.add(new BasicNameValuePair("Did",params[0]));
					   nameValuePairs.add(new BasicNameValuePair("type",params[1]));
					   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					HttpResponse response = httpclient.execute(httppost);
					HttpEntity entity = response.getEntity();
					isr = entity.getContent();
				}catch(Exception e){
					Log.e("log_tag","Error in http connection"+e.toString());
				}
				
				try{
					BufferedReader reader = new BufferedReader(new InputStreamReader(isr,"iso-8859-1"),8);
					StringBuilder sb = new StringBuilder();
					String line = null;
					while((line = reader.readLine()) != null){
						sb.append(line+ "\n");
					}
					isr.close();
					
					result = sb.toString();
				}catch(Exception e){
					Log.e("log_tag","Error converting result "+e.toString());
				}
				return result;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				try{
					
					JSONArray jArray =new JSONArray(result);
					detailsLIst = new String[jArray.length()];
					idList = new String[jArray.length()];
				
					for(int i=0; i<jArray.length();i++){
						JSONObject json = jArray.getJSONObject(i);
							
								 detailsLIst[i] =  json.getString("Pname");
								 idList[i] = json.getString("Pid");

							
						}
					setListAdapter(new ArrayAdapter<String>(ProjectActivity.this,android. R.layout.simple_list_item_1, detailsLIst));

				}catch(Exception e) {
					
					Log.e("log_tag","Error Parsing Data"+e.toString());
				}
				
				
			}
	}

}
