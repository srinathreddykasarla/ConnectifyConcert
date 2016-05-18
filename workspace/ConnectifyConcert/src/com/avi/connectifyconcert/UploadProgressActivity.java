package com.avi.connectifyconcert;

import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class UploadProgressActivity extends Activity {

	EditText projectnameET;
	EditText devNameET;
	EditText progressET;
	AlertDialogManager alert = new AlertDialogManager();
	AsyncTask<Void, Void, Void> mRegisterTask;
	static final String EXTRA_STRING  = "com.avi.SendAlerts.EXTRA_STRING";
	
	String regId;
	String devName;
	String projectName;
	String progress;
	String project_name;
	String dev_name;
	String DID,PID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendalerts);
		projectnameET = (EditText) findViewById(R.id.crimnameEditText);
		devNameET = (EditText) findViewById(R.id.toEditText);
		progressET = (EditText) findViewById(R.id.descriptionEditText);
		
		
		
	
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		
		if(requestCode==0)
		{
			if(resultCode==DetailsActivity.RESULT_DETAIL)
			{
				if(intent.hasExtra(DetailsActivity.EXTRA_DETAIL)){
					if(intent.getStringExtra(DetailsActivity.EXTRA_DETAIL).equals("Project")){
						 project_name = intent.getStringExtra(DetailsActivity.DETAIL);
						projectnameET.setText(project_name);
						PID = intent.getStringExtra(DetailsActivity.ID_DETAIL);
						}
					else if(intent.getStringExtra(DetailsActivity.EXTRA_DETAIL).equals("Developer"))
					{
						 dev_name = intent.getStringExtra(DetailsActivity.DETAIL); 
						devNameET.setText(dev_name);
						DID = intent.getStringExtra(DetailsActivity.ID_DETAIL);
				}
				
			}
			}
		}
	}
	public void searchProject(View v)
	{
		Intent intent = new Intent(UploadProgressActivity.this,DetailsActivity.class);
		intent.putExtra(EXTRA_STRING, "Project");
		startActivityForResult(intent, 0);
	}
	
	public void searchDev(View v)
	{
		Intent intent = new Intent(UploadProgressActivity.this,DetailsActivity.class);
		intent.putExtra(EXTRA_STRING, "Developer");
		startActivityForResult(intent,0);
	}
	
	public void uploadFunc(View v)
	{
		 devName = devNameET.getText().toString();
		 projectName = projectnameET.getText().toString();
		 progress = progressET.getText().toString();
		if(devName.trim().length()>0 && projectName.trim().length()>0)
		{
			new SendMessage().execute();
		}
		else{
			
			alert.showAlertDialog(UploadProgressActivity.this, "Error!", "Please enter Developer Name and Project Name", false);
		}
		
	}
	
	class SendMessage extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			
			

	        try{
	           HttpClient httpclient=new DefaultHttpClient();
	           HttpPost httppost= new HttpPost("http://testandroid.net46.net/ConnectifyConcert/upload_progress.php");
	           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
	           nameValuePairs.add(new BasicNameValuePair("dev_ID",DID));
	           nameValuePairs.add(new BasicNameValuePair("project_ID",PID));
			   nameValuePairs.add(new BasicNameValuePair("progress",progress));
			   httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
	           HttpResponse response=httpclient.execute(httppost);
	           HttpEntity entity = response.getEntity();
	          
	        }catch(Exception e){
	            Log.e("log_tag", "Error at httpost "+e.toString());
	        }
	        
	        return regId;
		}
	        

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			
			Toast.makeText(getApplicationContext(), "Message: " + progress+" to: "+DID+" about: "+PID, Toast.LENGTH_LONG).show();
			
		}
		
	}

}
