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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

	EditText etUser,etPass;
	Button bLogin;
	public static String USER_NAME = "com.avi.connectify.MainActivity.USER_NAME";
	public static String TYPE = "com.avi.connectify.MainActivity.TYPE";
	public String username;
	String password;
	String type;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);      
		
		
		etUser = (EditText) findViewById(R.id.etUser);
		etPass = (EditText) findViewById(R.id.etPass);
		bLogin = (Button) findViewById(R.id.bSubmit);
}

@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO){
			etUser.setTranslationY(-300);
			etPass.setTranslationY(-300);
			bLogin.setTranslationY(-300);
		}
		else if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES){
			etUser.setTranslationY(300);
			etPass.setTranslationY(300);
			bLogin.setTranslationY(300);
		}
	}


	class Connection extends AsyncTask<String, Void, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			
			
			String result ="";
	        InputStream isr = null;

	        try{
	           HttpClient httpclient=new DefaultHttpClient();
	           HttpPost httppost= new HttpPost("http://testandroid.net46.net/login.php");
	           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();           
			   nameValuePairs.add(new BasicNameValuePair("username",username));
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
			
			super.onPostExecute(result);
			String s = null;
			 try{
		        	JSONArray jArray = new JSONArray(result);
		            for(int i =0;i<jArray.length();i++){
		                JSONObject json = jArray.getJSONObject(i);
		                s = json.getString("password");		
		                type = json.getString("flag");
		            } 		
						if(s.equals(password)){
		       			 Intent intent = new Intent(MainActivity.this,DashBoardActivity.class);
		       			 intent.putExtra(USER_NAME, username);
		       			 intent.putExtra(TYPE, type);
		       			 startActivity(intent);
		    
		        		 }else{
		        			 Toast.makeText(getBaseContext(),"Invalid Login Details", Toast.LENGTH_SHORT).show(); 
		        		 }
		            		
		                 
		        }catch(Exception e){
		            Log.e("log_tag", "Error parsing data "+e.toString());
		            Toast.makeText(getBaseContext(),"Error In Connection", Toast.LENGTH_SHORT).show();
		        }
		}
		
	}
	public void loginfun(View v)
	{
		username = etUser.getText().toString();
		
		password = etPass.getText().toString();
		new Connection().execute();
       
        
	}
}
