package com.swindle.geupdater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class ToolbarActivity extends ListActivity {
	protected SimpleAdapter adapter;
	protected boolean isResetable = false;
	protected static Map<String, String> toolbar = null;
	protected List<Map<String,String>> activitiesList = new ArrayList<Map<String,String>>();
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListView list = new ListView(this);
        list.setId(android.R.id.list);
        //adapter = new ArrayAdapter<String>(this, R.layout.activities_list_item);
        //adapter.setNotifyOnChange(false);
        adapter = new SimpleAdapter(this, activitiesList, 
    			R.layout.activities_list_item, 
    			new String[] { Parser.CAPTION, Parser.ICON, Parser.HEADER, Parser.PERCENT },
    			new int[] { R.id.activityText, R.id.activityIcon, R.id.listHeader, R.id.progressBar });
        adapter.setViewBinder(new CustomViewBinder());
        setListAdapter(adapter);
        setContentView(list);
        
        getToolbar();
	}
	
	protected void getToolbar() {
		if(toolbar == null || toolbar.isEmpty()){
			OptionsDatabaseHandler optionsDB = new OptionsDatabaseHandler(this);
			toolbar = optionsDB.getToolbarCode();
		}
	}
	
	protected void resetToolbar() {
		OptionsDatabaseHandler db = new OptionsDatabaseHandler(this);
		db.setToolbarCode("", "");
    	Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
    }
	
	protected class performBackgroundTask extends AsyncTask <String, Void, String> {
		private ProgressDialog dialog = new ProgressDialog(ToolbarActivity.this);
		private String url;

		protected void onPreExecute() {
			dialog.setMessage("Loading Data...");
			dialog.show();
		}

		protected void onPostExecute(String data) {
			if(data != null && data.length() > 0){
				List<Map<String,String>> items = Parser.parseXML(data);
				if((items == null || items.isEmpty()) && isResetable){
					Log.e("Diversions", "No Activities");
					resetToolbar();
				} else {
					/*for(Map<String,String> map : items) {
						//TextView tv = new TextView(list.getContext());
						//tv.setText(map.get(Parser.CAPTION));
						adapter.add(map.get(Parser.CAPTION));
					}*/
					/*Map<String,String> header = items.get(0);
					if(header.size() < 2){
						items.remove(0);
					}*/
					activitiesList.clear();
					activitiesList.addAll(items); 
			    	adapter.notifyDataSetChanged();
				}
		    	
		    	//Remove the loading screen
			} else {
				Map<String,String> error = new HashMap<String,String>();
				error.put(Parser.ICON, null);
				error.put(Parser.CAPTION, "Error loading data");
				activitiesList.add(error);
				//adapter.add("Error loading data");
				adapter.notifyDataSetChanged();
			}
			
			try {
				if(dialog.isShowing()) {
					dialog.dismiss();
				}
			} catch(Exception e) {}
		}

		@Override
		protected String doInBackground(String... urls) {
			url = urls[0];
			try {
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpGet httpGet = new HttpGet(url);
				String cookie = "toolbar_remember=true; toolbar_hash=" + toolbar.get("toolbarHash") + "; toolbar_code=" + toolbar.get("toolbarCode");
				httpGet.setHeader("Cookie", cookie);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				
				return EntityUtils.toString(httpEntity);
			} catch (Exception e) {
				return null;
			}
		}
	}

}
