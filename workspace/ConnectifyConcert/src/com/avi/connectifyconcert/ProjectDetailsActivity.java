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
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class ProjectDetailsActivity extends Activity {

	String PID,DID;
	TextView projectName,projectID,manager,project_status;
	public static final String PROJECT_ID = "com.avi.connectify.PID";
	public static final String DEV_ID = "com.avi.connectify.DID";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_details);
		Intent intent = getIntent();
		PID = intent.getStringExtra(ProjectActivity.PROJECT_ID);
		DID = intent.getStringExtra(ProjectActivity.DEV_ID);
		projectName = (TextView) findViewById(R.id.projectNameTV);
		projectID = (TextView) findViewById(R.id.projectIDTV);
		manager= (TextView) findViewById(R.id.managerTV);
		project_status = (TextView) findViewById(R.id.projectStatusTV);
		new GetDetails().execute(PID);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.project_details, menu);
		return true;
	}
	
	public void getTeamDetails(View v){
		Intent intent = new Intent(ProjectDetailsActivity.this,TeamDetailsActivity.class);
		intent.putExtra(PROJECT_ID,PID);
		intent.putExtra(DEV_ID, DID);
		startActivity(intent);
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
	           HttpPost httppost= new HttpPost("http://testandroid.net46.net/GetProjectdetails.php");
	           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();           
			   nameValuePairs.add(new BasicNameValuePair("Pid",params[0]));
			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	           HttpResponse response=httpclient.execute(httppost);
	           HttpEntity entity = response.getEntity();
	           isr = entity.getContent();
	        }catch(Exception e){
	            Log.e("log_tag", "Eror at httpost "+e.toString());
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
				String  projectNameStr = "",projectIDStr = "",managerStr = "",project_statusStr = "";
			
				for(int i=0; i<jArray.length();i++){
					JSONObject json = jArray.getJSONObject(i);
					
					projectNameStr = json.getString("Pname");
					projectIDStr = json.getString("Pid"); 
					managerStr= json.getString("Mname");
					project_statusStr= json.getString("status");
						
						
					}
				
				projectName.setText(projectNameStr);
				projectID.setText("Project ID:"+projectIDStr);
				manager.setText("Manager:"+managerStr);
				project_status.setText("Status:"+project_statusStr);
				

			}catch(Exception e) {
				
				Log.e("log_tag","Error Parsing Data"+e.toString());
			}
		
			
			
		}
		
	}

}
