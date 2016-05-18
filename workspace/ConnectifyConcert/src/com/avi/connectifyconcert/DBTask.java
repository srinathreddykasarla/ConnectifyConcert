package com.avi.connectifyconcert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


import android.util.Log;


public class DBTask {
	public String detailsLIst[]={"Loading..."};


	protected String doInBackground(String params) {
		// TODO Auto-generated method stub
		
		String result = "";
		InputStream isr = null;
		try{
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = null;
			httppost = new HttpPost("http://testandroid.net46.net/"+params);
			
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


	protected String[] onPostExecute(String result,String getString) {
		// TODO Auto-generated method stub
		
		try{
			
			JSONArray jArray =new JSONArray(result);
			detailsLIst = new String[jArray.length()];
		
			for(int i=0; i<jArray.length();i++){
				JSONObject json = jArray.getJSONObject(i);
					
						 detailsLIst[i] =  json.getString(getString);

					
				}
			
			
		}catch(Exception e) {
			
			Log.e("log_tag","Error Parsing Data"+e.toString());
		}
		
		return detailsLIst;
	}

}
