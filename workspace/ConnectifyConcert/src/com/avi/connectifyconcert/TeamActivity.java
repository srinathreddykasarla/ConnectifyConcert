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
import android.widget.LinearLayout;
import android.widget.TextView;

public class TeamActivity extends Activity {
	public String detailsLIst[]={"Loading..."};
	public String idList[];
	TextView teamnameTV,teamMembersTV;
	String username,teamMembers="";
	int count=0;
	private String type;
	String[] params = new String[2];
	LinearLayout scrollLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_details);
		scrollLayout = (LinearLayout) findViewById(R.id.scrollLayout);
		teamnameTV = (TextView) findViewById(R.id.teamNameTV);
		teamMembersTV = new TextView(TeamActivity.this);
		teamMembersTV.setTextSize(30);
		
		teamMembersTV.setTypeface(null, Typeface.BOLD_ITALIC);
		scrollLayout.addView(teamMembersTV);
		Intent intent = getIntent();
		username = intent.getStringExtra(DashBoardActivity.USER_NAME);
		type = intent.getStringExtra(DashBoardActivity.TYPE);
		params[0] = username;
		params[1] = type;
		
		new DBAsyncTask().execute(params);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.team, menu);
		return true;
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
                             Log.d("log_tag",idList[i]);
						
					}
				for(int i =0;i<idList.length;i++)
				{
					new GetDetails().execute(idList[i]);
				}

			}catch(Exception e) {
				
				Log.e("log_tag","Error Parsing Data"+e.toString());
			}
			
			
		}
}
	
	class GetDetails extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String result ="";
	        InputStream isr = null;
	        
	        try{
	           HttpClient httpclient=new DefaultHttpClient();
	           HttpPost httppost= new HttpPost("http://testandroid.net46.net/GetTeamDetails.php");
	           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();   
	           Log.d("GetDetails",params[0]);
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
	        Log.d("GetDetails",result);
			return result;
		}
			
			

	   
	        

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
try{
				
				JSONArray jArray =new JSONArray(result);
				 teamMembers = teamMembers+"Project Name: "+detailsLIst[count]+"\n";
				int counter = 1;
			
				for(int i=0; i<jArray.length();i++){
					JSONObject json = jArray.getJSONObject(i);
					
					teamMembers = teamMembers+counter+") "+json.getString("Dname")+"\n";
					counter++;	
						
					}
				
				teamMembers = teamMembers+"\n\n\n";
				teamnameTV.setText("All Project Team Members");
				teamMembersTV.setText(teamMembers);
				count++;

			}catch(Exception e) {
				e.printStackTrace();
				Log.e("log_tag","Error Parsing Data "+ e.toString());
			}
		
			
			
		}
		
	}

}
