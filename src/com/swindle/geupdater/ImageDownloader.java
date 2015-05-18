package com.swindle.geupdater;

/*
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.    
*/
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

public class ImageDownloader {
   private final Map<String, Drawable> drawableMap;

   public ImageDownloader() {
       drawableMap = new HashMap<String, Drawable>();
   }

   public Drawable fetchDrawable(String urlString) {
	   
       if (drawableMap.containsKey(urlString)) {
           return drawableMap.get(urlString);
       }

       Log.d(this.getClass().getSimpleName(), "image url:" + urlString);
       try {
           InputStream is = fetch(urlString);
           Drawable drawable = Drawable.createFromStream(is, "src");


           if (drawable != null) {
               drawableMap.put(urlString, drawable);
               Log.d(this.getClass().getSimpleName(), "got a thumbnail drawable: " + drawable.getBounds() + ", "
                       + drawable.getIntrinsicHeight() + "," + drawable.getIntrinsicWidth() + ", "
                       + drawable.getMinimumHeight() + "," + drawable.getMinimumWidth());
           } else {
             Log.w(this.getClass().getSimpleName(), "could not get thumbnail");
           }

           return drawable;
       } catch (MalformedURLException e) {
           Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
           return null;
       } catch (IOException e) {
           Log.e(this.getClass().getSimpleName(), "fetchDrawable failed", e);
           return null;
       }
   }

   public void fetchDrawableOnThread(final String urlString, final ImageView imageView) {
       if (drawableMap.containsKey(urlString)) {
           imageView.setImageDrawable(drawableMap.get(urlString));
       }

       Thread thread = new Thread() {
           @Override
           public void run() {
               Drawable drawable = fetchDrawable(urlString);
               ImageDisplayer display = new ImageDisplayer(drawable, imageView);
               ((Activity) imageView.getContext()).runOnUiThread(display);
           }
       };
       thread.start();
   }

   private InputStream fetch(String urlString) throws MalformedURLException, IOException {
       DefaultHttpClient httpClient = new DefaultHttpClient();
       HttpGet request = new HttpGet(urlString);
       HttpResponse response = httpClient.execute(request);
       return response.getEntity().getContent();
   }
   
   private class ImageDisplayer implements Runnable {
	   Drawable image;
	   ImageView view;
	   
       public ImageDisplayer(Drawable d, ImageView i){ image = d; view = i; }
       public void run()
       {
          // if(imageViewReused(photoToLoad))
          //     return;
           if(image != null){
        	   	view.setImageDrawable(image);
        	   	view.setAdjustViewBounds(true);
        	   	//imageView.setMaxWidth(image.getMinimumWidt());
        	   	//imageView.setMaxHeight(image.getMinimumHeight());
        	   	view.setVisibility(ImageView.VISIBLE);
        	   	view.buildDrawingCache();
           }
       }
   }

}