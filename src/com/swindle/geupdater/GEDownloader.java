package com.swindle.geupdater;

import java.lang.reflect.Method;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.util.Log;

public class GEDownloader implements Runnable {
	private String url;
	private String cookie;
	private int httpCode;
	private String content;
	private Method callbackMethod = null;
	
	public GEDownloader(String url, Class<? extends Activity> callback) {
		this(url, null, callback);
	}
	public GEDownloader(String url, String cookie, Class<? extends Activity> callback) {
		this.url = url;
		this.cookie = cookie;
		if(callback != null){
			try {
				callbackMethod = callback.getMethod("parseData", String.class);
			} catch(Exception e) {
				Log.e("Exception", "MethodException: " + e.toString());
			}
		}
	}
	
	public void run() {
		download();
	}
	
/*	public static void setImage(ImageView view, String url) {
		try {
			Thread thread = new Thread(new ImageDownloader(view, url));
			thread.start();
    
		} catch (Exception e) {
			return;
		}
	}*/
	
	private void download() {
		
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpGet httpGet = new HttpGet(url);
		    if(cookie != null && cookie.length() != 0){
		    	httpGet.setHeader("Cookie", cookie);
		    }
		
		    HttpResponse httpResponse = httpClient.execute(httpGet);
		    httpCode = httpResponse.getStatusLine().getStatusCode();
		    HttpEntity httpEntity = httpResponse.getEntity();
		    
		    content = EntityUtils.toString(httpEntity);
		    if(callbackMethod != null){
		    	callbackMethod.invoke(null, content);
		    }
        
		} catch (Exception e) {
			//Sad face
			httpCode = 0;
			content = null;
		}
	}
	
	public int getStatusCode() {
		return httpCode;
	}
	
	public String getContent() {
		return content;
	}

}

/*class ImageDownloader implements Runnable {
	private String url;
	private ImageView img;
	private static Map<String,Bitmap> imageCache = new HashMap<String,Bitmap>(); 
	
	public ImageDownloader(ImageView view, String url) {
		this.url = url;
		this.img = view;
	}
	
	public void run() {
		Bitmap bitmap = imageCache.get(url);
		if(bitmap != null){
			//img.setImageBitmap(bitmap);
			return;
		}
		
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(url).getContent());
			imageCache.put(url, bitmap);
			img.setImageBitmap(bitmap);
			img.setAdjustViewBounds(true);
			img.setMaxWidth(bitmap.getWidth());
			img.setMaxHeight(bitmap.getHeight());
			img.setVisibility(ImageView.VISIBLE);
			img.buildDrawingCache();
			//ToolbarActivity.updateAdatper();
		} catch (Exception e) {
			Log.e("Exception", "bitmapException: " + e.toString());
		}
		
	}
}*/
