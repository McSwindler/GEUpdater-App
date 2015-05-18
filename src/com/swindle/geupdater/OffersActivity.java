package com.swindle.geupdater;

import android.os.Bundle;


public class OffersActivity extends ToolbarActivity {

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
		new performBackgroundTask().execute(Constants.OFFERS_URL);
    }
}
