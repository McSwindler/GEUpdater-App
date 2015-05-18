package com.swindle.geupdater;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class TabHostActivity extends TabActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabhost_main);

		TabHost tabHost = getTabHost();

		// Tab for D&Ds
		TabSpec activitySpec = tabHost.newTabSpec("D&Ds");
		// setting Title and Icon for the Tab
		activitySpec.setIndicator("D&Ds", getResources().getDrawable(R.drawable.ic_tab_diversions));
		Intent activityIntent = new Intent(this, DiversionsActivity.class);
		activitySpec.setContent(activityIntent);

		// Tab for GEUpdate
		TabSpec geUpdateSpec = tabHost.newTabSpec("GE");
		geUpdateSpec.setIndicator("GE", getResources().getDrawable(R.drawable.ic_action_search));
		Intent geIntent = new Intent(this, OffersActivity.class);
		geUpdateSpec.setContent(geIntent);

		// Adding all TabSpec to TabHost
		tabHost.addTab(activitySpec);
		tabHost.addTab(geUpdateSpec); 

//		populateTabs();
//	}
//
//	private void populateTabs() {
//		populateDiversions();
//		populateOffers();
//	}
//
//	private void populateDiversions() {
//		String cookie = "toolbar_remember=true; toolbar_hash=" + toolbar.get("toolbarHash") + "; toolbar_code=" + toolbar.get("toolbarCode");
//		new performBackgroundTask(Constants.DIVERSIONS_URL, cookie, DiversionsActivity.class).execute();
//	}
//
//	private void populateOffers() {
//		String cookie = "toolbar_remember=true; toolbar_hash=" + toolbar.get("toolbarHash") + "; toolbar_code=" + toolbar.get("toolbarCode");
//		Thread thread = new Thread(new GEDownloader(Constants.OFFERS_URL, cookie, OffersActivity.class));
//		thread.start();
	}
}
