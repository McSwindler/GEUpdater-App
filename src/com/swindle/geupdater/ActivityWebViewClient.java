package com.swindle.geupdater;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class ActivityWebViewClient extends WebViewClient {
	private ActivateActivity activity;
	
	public void onPageFinished(WebView view, String url) {
		
		CookieManager mgr = CookieManager.getInstance();
		Log.i("URL", url);
		Log.i("Cookie",mgr.getCookie("runescape.com"));
		boolean isFinished = false;
		if(url.contains("authorise.ws?done=true")){
			String cookieString = mgr.getCookie("runescape.com");
			String codeMatch = "toolbar_code=([0-9]+);";
			String hashMatch = "toolbar_hash=([0-9]+);";
			String toolbarCode = "";
			String toolbarHash = "";
			
			Matcher m = Pattern.compile(codeMatch).matcher(cookieString);
			
			while(m.find()){
				toolbarCode = m.group(1);
			}
			
			m = Pattern.compile(hashMatch).matcher(cookieString);
			
			while(m.find()){
				toolbarHash = m.group(1);
			}
			
			if(toolbarCode != null && toolbarHash != null){
				OptionsDatabaseHandler optionDB = new OptionsDatabaseHandler(MainActivity.getAppContext());
				optionDB.setToolbarCode(toolbarCode, toolbarHash);
				isFinished = true;
			}
		}
		
		
		super.onPageFinished(view, url);
		if(isFinished) {
			OptionsDatabaseHandler optionDB = new OptionsDatabaseHandler(MainActivity.getAppContext());
			if(optionDB.getToolbarCode().isEmpty()) {
				Toast.makeText(activity, "Error Authorizing", Toast.LENGTH_SHORT).show();
				activity.openMain();
			} else {
				Toast.makeText(activity, "Success!", Toast.LENGTH_SHORT).show();
				activity.openTabs();
			}
		}
	}
	
	public void setActivity(ActivateActivity activity) {
		this.activity = activity;
	}
}
