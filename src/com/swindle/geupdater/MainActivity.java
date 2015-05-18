package com.swindle.geupdater;

import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	
	private static Context context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
        
        MainActivity.context = getApplicationContext();
        
        OptionsDatabaseHandler optionsDB = new OptionsDatabaseHandler(this);
        
        Map<String, String> toolbar = optionsDB.getToolbarCode();
        if(!toolbar.isEmpty() && 
        		toolbar.get("toolbarCode").length() != 0 &&
        		toolbar.get("toolbarHash").length() != 0) {
        	Log.i("toolbarCode", toolbar.get("toolbarCode"));
        	startOfferService();
        	openTabs();
        }
        
        setContentView(R.layout.activity_main);
                
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    public void startOfferService() {
    	Intent intent = new Intent(this, OfferService.class);
    	startService(intent);
    }
    
    public void openAuth(View view) {
    	Intent intent = new Intent(this, ActivateActivity.class);
    	startActivity(intent);
    }
    
    public void openTabs() {
    	Intent intent = new Intent(this, TabHostActivity.class);
    	startActivity(intent);
    }
    
    public static Context getAppContext() {
        return MainActivity.context;
    }

}
