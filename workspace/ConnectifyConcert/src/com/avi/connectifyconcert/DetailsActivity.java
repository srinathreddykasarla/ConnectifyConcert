package com.avi.connectifyconcert;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.ListActivity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;



public class DetailsActivity extends ListActivity implements SearchView.OnQueryTextListener{
	
	public String detailsLIst[]={"Loading..."};
	public String IDList[];
	public ArrayList<String> refinedList;
	public final static String DETAIL = "com.avi.connectifyconcert.DETAIL";
	public final static String EXTRA_DETAIL = "com.avi.connectifyconcert.EXTRA_DETAIL";
	public final static int RESULT_DETAIL=0;
	public final static String ID_DETAIL = "com.avi.connectifyconcert.ID_DETAIL";

	private SearchView mSearchView;
	ListView list;
	int count = 0,flag = 0;
	String extra =  "com.avi.Developer.EXTRA" ;
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		String detail,ID = "";
		if(flag==1){
			 detail = refinedList.get(position);
		}
		else
		{
			detail= detailsLIst[position];
			ID= IDList[position];
		}
		Intent intent = new Intent();
		intent.putExtra(EXTRA_DETAIL, extra);
		intent.putExtra(DETAIL,detail);
	    intent.putExtra(ID_DETAIL, ID);
		setResult(RESULT_DETAIL, intent);
		finish();
	}

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_project);
		
		list = (ListView) findViewById(android.R.id.list);
		Intent intent = getIntent();
		 extra = intent.getStringExtra(AssignActivity.EXTRA_STRING);
		 setListAdapter(new ArrayAdapter<String>(DetailsActivity.this,android. R.layout.simple_list_item_1, detailsLIst));
		new DBAsyncTask().execute(extra);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.search_in_menu, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) searchItem.getActionView();
		setupSearchView(searchItem);
		return true;
	}
	 protected boolean isAlwaysExpanded() {
	        return false;
	    }
	private void setupSearchView(MenuItem searchItem) {
		
		if(isAlwaysExpanded()) {
			mSearchView.setIconifiedByDefault(false);
			}else {
				searchItem.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);
			}
		mSearchView.setOnQueryTextListener(this);
		}
	
	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		

		 count = 0;
		 flag = 1;
		 refinedList = new ArrayList<String>();

		

		
		for(int i=0;i<detailsLIst.length;i++) {
			if(detailsLIst[i].toLowerCase().contains(newText.toLowerCase()))
			{
				refinedList.add(detailsLIst[i]);
				count++;
				
			}
			
		}
		if(count>0)
		{
			list.setVisibility(View.VISIBLE);
			setListAdapter(new ArrayAdapter<String>(DetailsActivity.this,android. R.layout.simple_list_item_1,refinedList));

		}
		else
		{
			list.setVisibility(View.INVISIBLE);
			Toast.makeText(getApplicationContext(), "No Results", Toast.LENGTH_SHORT).show();
		}
		

		

		return false;
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
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
				if(params[0].equals("Project"))
				{
					 httppost = new HttpPost("http://testandroid.net46.net/getProjs.php");
				}
				else if(params[0].equals("Developer"))
				{
					 httppost = new HttpPost("http://testandroid.net46.net/getDevs.php");
					}
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
				IDList = new String[jArray.length()];
				for(int i=0; i<jArray.length();i++){
					JSONObject json = jArray.getJSONObject(i);
						if(extra.equals("Project"))
						{
							 detailsLIst[i] =  json.getString("Pname");
                             IDList[i]  = json.getString("Pid");
						}
						else if(extra.equals("Developer"))
						{
							 detailsLIst[i] =  json.getString("Dname");
							 IDList[i]  = json.getString("Did");
						}
					}
				setListAdapter(new ArrayAdapter<String>(DetailsActivity.this,android. R.layout.simple_list_item_1, detailsLIst));

			}catch(Exception e) {
				
				Log.e("log_tag","Error Parsing Data"+e.toString());
			}
			
			
		}
		
		
		
	}



}
