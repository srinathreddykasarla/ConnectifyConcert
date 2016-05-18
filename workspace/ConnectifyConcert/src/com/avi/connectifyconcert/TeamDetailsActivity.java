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

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamDetailsActivity extends Activity {

	TextView teamnameTV,teamMembernameTV,workingPartTV,progressTV;
	Button uploadDocs;
	public String PID,DID;
	public static final String TAG = "log_tag_teamDetails";

	public Typeface roboto;
	
	
	LinearLayout scrollLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_details);
		teamnameTV = (TextView) findViewById(R.id.teamNameTV);
		
		scrollLayout = (LinearLayout) findViewById(R.id.scrollLayout);
		Intent intent = getIntent();
		PID = intent.getStringExtra(ProjectDetailsActivity.PROJECT_ID);
	    intent.removeExtra(ProjectDetailsActivity.PROJECT_ID);
		new GetDetails().execute(PID);
	    

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team_details, menu);
		return true;
	}
	
	
	class GetDetails extends AsyncTask<String, Void, String>
	{

		public static final  String PROJECT_IDT = "com.avi.connectify.PID";
		public  static final String DEV_IDT = "com.avi.connectify.DID";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result ="";
	        InputStream isr = null;
	        
	        try{
	           HttpClient httpclient=new DefaultHttpClient();
	           HttpPost httppost= new HttpPost("http://testandroid.net46.net/GetTeamDetails.php");
	           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();           
			   nameValuePairs.add(new BasicNameValuePair("Pid",params[0]));
			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	           HttpResponse response=httpclient.execute(httppost);
	           HttpEntity entity = response.getEntity();
	           isr = entity.getContent();
	        }catch(Exception e){
	            Log.e("log_tag", "Error at httpost "+e.toString());
	        }
	        try{        
	            BufferedReader reader = new BufferedReader(new InputStreamReader(isr, "iso-8859-1"),8);
	            StringBuilder sb = new StringBuilder();
	            String line = null;
	            while((line = reader.readLine())!=null){
	                sb.append(line+"\n");
	            }
	            isr.close();
	            result=sb.toString();
	        }catch(Exception e){
	            Log.e("log_tag", "Error converting "+e.toString());
	        }
			return result;
		}
			
			

	   
	        

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
try{
				
				JSONArray jArray =new JSONArray(result);
				ArrayList<String> teamMembersArray = new ArrayList<String>(); 
				ArrayList<String> workingPartArray = new ArrayList<String>(); 
				ArrayList<String> progressArray = new ArrayList<String>();
				final ArrayList<String> devIDArray = new ArrayList<String>();
				String teamMembersString = "";
				int counter = 0;
			
				for(int i=0; i<jArray.length();i++){
					JSONObject json = jArray.getJSONObject(i);
					
					teamMembersArray.add(json.getString("Dname"));
					workingPartArray.add(json.getString("working_part"));
					progressArray.add(json.getString("progress"));
					devIDArray.add(json.getString("Did"));	
					}
				roboto.createFromFile("/ConnectifyConcert/res/fonts/Roboto-Thin.ttf");
				teamnameTV.setText("Team Members");
				for(int i=0; i<jArray.length();i++){
					teamMembersString = teamMembersString+counter+") " + teamMembersArray.get(i)+"\n";
					teamMembernameTV = new TextView(TeamDetailsActivity.this);
					teamMembernameTV.setText("Developer: "+teamMembersArray.get(i) );
					teamMembernameTV.setTextSize(30);
					
					teamMembernameTV.setTypeface(roboto, Typeface.BOLD_ITALIC);
					scrollLayout.addView(teamMembernameTV);
					
					workingPartTV = new TextView(TeamDetailsActivity.this);
					workingPartTV.setText("Working Part: "+workingPartArray.get(i) +"\n");
					workingPartTV.setTypeface(roboto, Typeface.BOLD_ITALIC);
					workingPartTV.setTextSize(20);
					scrollLayout.addView(workingPartTV);
					
					progressTV = new TextView(TeamDetailsActivity.this);
					progressTV .setText("Progress: "+progressArray.get(i) + "\n\n");
					progressTV .setTextSize(20);
					scrollLayout.addView(progressTV );
					
					
					
					
						uploadDocs = new Button(TeamDetailsActivity.this);
						uploadDocs.setText("Uploaded Documents");
						uploadDocs.setId(counter);
						scrollLayout.addView(uploadDocs);
						uploadDocs.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {

								Button button = (Button) v;
								DID = devIDArray.get(button.getId());
					        	 Intent intent = new Intent(TeamDetailsActivity.this,FileDetailsActivity.class);
								
									intent.putExtra(PROJECT_IDT, PID);
									intent.putExtra(DEV_IDT, DID);
									Log.d(TAG, "PID:"+PID);
									Log.d(TAG, "DID:"+DID);
									startActivity(intent); 
								
							}
						});
					
				
					counter++;		
					}
				
				
				

			}catch(Exception e) {
				
				Log.e("log_tag","Error Parsing Data"+e.toString());
			}
		
			
			
		}
	
}
	
}
