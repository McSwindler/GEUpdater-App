package com.swindle.geupdater;

import android.os.Bundle;


public class DiversionsActivity extends ToolbarActivity {
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isResetable = true;
		new performBackgroundTask().execute(Constants.DIVERSIONS_URL);
    }
	
}
