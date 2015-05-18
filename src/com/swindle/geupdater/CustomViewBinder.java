package com.swindle.geupdater;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.channels.FileChannel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

class CustomViewBinder implements SimpleAdapter.ViewBinder {
	
    public boolean setViewValue(View view, Object data, String textRepresentation) {
    	int id = view.getId();
    	String str = (String) data;
        switch(id) {
        	case R.id.activityIcon:
        		ImageView i = (ImageView) view;
        		
        		/*if((str == null || str.length() < 1)) {
        			i.setVisibility(ImageView.GONE);
        		}
        		if(i.getDrawable() != null){
        			return true;
        		}*/
        		
        		try {
        			URL url = new URL(str);
        			//i.setDrawingCacheEnabled(true);
        			new ImageDownloader().fetchDrawableOnThread(url.toString(), i);
        			//i.setImageURI(Uri.parse(str));
        			//i.setImageDrawable(new ImageDownloader().fetchDrawable(url.toString()));
					//GEDownloader.setImage(i, url.toString());
        		} catch (Exception e) {
        			i.setVisibility(ImageView.GONE);
        		}
        		break;
        	case R.id.activityText:
        		TextView text = (TextView) view;
        		text.setText(str);
        		break;
        	case R.id.listHeader:
        		TextView header = (TextView) view;
        		View parent = (View) header.getParent();
        		if(str != null) {
	        		parent.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.darker_gray));
	        		header.setText(str);
	        		header.setVisibility(TextView.VISIBLE);
        		} else {
	        		parent.setBackgroundColor(parent.getContext().getResources().getColor(android.R.color.transparent));
	        		header.setVisibility(TextView.GONE);
        		}
        		break;
        	case R.id.progressBar:
        		ProgressBar bar = (ProgressBar) view;
        		if(str != null){
	        		int progress;
	        		try {
	        			progress = Integer.parseInt(str);
	        		} catch (Exception e){
	        			progress = 0;
	        		}
	        		bar.setProgress(progress);
	        		bar.setVisibility(ProgressBar.VISIBLE);
        		} else {
        			bar.setVisibility(ProgressBar.GONE);
        		}
        		break;
        }
        
        return true;
    }
    
    public static Bitmap loadBitmap(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream());

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = 1;

            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
        } catch (IOException e) {
            Log.e("Icon", "Could not load Bitmap from: " + url);
        } finally {
            try {
				in.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }

        return bitmap;
    }
    
    public static void copy(InputStream input, OutputStream output) throws IOException {
        // if both are file streams, use channel IO
        if ((output instanceof FileOutputStream) && (input instanceof FileInputStream)) {
          try {
            FileChannel target = ((FileOutputStream) output).getChannel();
            FileChannel source = ((FileInputStream) input).getChannel();

            source.transferTo(0, Integer.MAX_VALUE, target);

            source.close();
            target.close();

            return;
          } catch (Exception e) { /* failover to byte stream version */
          }
        }

        byte[] buf = new byte[8192];
        while (true) {
          int length = input.read(buf);
          if (length < 0)
            break;
          output.write(buf, 0, length);
        }

        try {
          input.close();
        } catch (IOException ignore) {
        }
        try {
          output.close();
        } catch (IOException ignore) {
        }
      }
}
