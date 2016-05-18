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



import android.app.DownloadManager;
import android.app.ListActivity;
import android.app.DownloadManager.Request;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class FileDetailsActivity extends ListActivity {
    private long enqueue;
    private DownloadManager dm;
	public String detailsLIst[]={"Loading..."};
	public String file_paths[];
	public String PID,DID;
	ListView list;
	public static final int VISIBILITY_VISIBLE_NOTIFY_COMPLETED = 1;
	String[] params = new String[2];
	public static final String TAG = "log_tag_fileDetails";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_file_details);
		
		Intent intent = getIntent();
	
			PID = intent.getStringExtra(TeamDetailsActivity.GetDetails.PROJECT_IDT);
			DID = intent.getStringExtra(TeamDetailsActivity.GetDetails.DEV_IDT);
			list = (ListView) findViewById(android.R.id.list);
		
			Log.d(TAG, "DID: "+DID);	
		
	
		setListAdapter(new ArrayAdapter<String>(FileDetailsActivity.this,android. R.layout.simple_list_item_1, detailsLIst));
		params[0] = DID;
		params[1] = PID;
		Log.d(TAG, "DID:"+params[0]);
		Log.d(TAG, "PID:"+params[1]);
		new DBAsyncTask().execute(params);
	}

	protected void onListItemClick(ListView l, View v, int position, long id) {
		
		super.onListItemClick(l, v, position, id);
		 dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
	     DownloadManager.Request request = new Request(	Uri.parse(file_paths[position]));
	     request.setNotificationVisibility(VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
	     enqueue = dm.enqueue(request);
	     
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.file_details, menu);
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
				httppost = new HttpPost("http://testandroid.net46.net/ConnectifyConcert/getFiles.php");
				ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();           
				   nameValuePairs.add(new BasicNameValuePair("Did",params[0]));
				   nameValuePairs.add(new BasicNameValuePair("Pid",params[1]));
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
				
				
				
					Log.d("result_value", result);
					JSONArray jArray =new JSONArray(result);
					if(jArray.length()>0){
						detailsLIst = new String[jArray.length()];
						file_paths = new String[jArray.length()];
					
						for(int i=0; i<jArray.length();i++){
							JSONObject json = jArray.getJSONObject(i);
									
										 detailsLIst[i] =  json.getString("file_name");
										 file_paths[i] = json.getString("file_path");
									}
						if(detailsLIst[0].contentEquals("NULL"))
						{
							if(file_paths[0].contentEquals("NULL"))
							{
								detailsLIst = new String[1];
								detailsLIst[0] = "No Files Uploaded";
								list.setEnabled(false);
								Toast.makeText(getApplicationContext(), "No Files Uploaded", Toast.LENGTH_SHORT).show();
							}
						}
					}
				
				
				setListAdapter(new ArrayAdapter<String>(FileDetailsActivity.this,android. R.layout.simple_list_item_1, detailsLIst));

			}catch(Exception e) {
				
				Log.e("log_tag","Error Parsing Data"+e.toString());
			}
			
			
		}
}

}
