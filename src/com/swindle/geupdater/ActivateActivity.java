package com.swindle.geupdater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class ActivateActivity extends Activity {
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                
        WebView webview = new WebView(this);
        ActivityWebViewClient webClient = new ActivityWebViewClient();
        webClient.setActivity(this);
        webview.setWebViewClient(webClient);
    	
		webview.loadUrl(Constants.AUTH_URL);
        setContentView(webview);
        
    }
	
	public void openMain() {
		Intent intent = new Intent(this, MainActivity.class);
    	startActivity(intent);
	}
	
	public void openTabs() {
		Intent intent = new Intent(this, TabHostActivity.class);
    	startActivity(intent);
	}
    
}
