package com.swindle.geupdater;


import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

public class OfferService extends IntentService {
	


  	public OfferService() {
		super("OfferService");
	}

	/**
     * The IntentService calls this method from the default worker thread with
     * the intent that started the service. When this method returns, IntentService
     * stops the service, as appropriate.
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // Normally we would do some work here, like download a file.
        // For our sample, we just sleep for 5 seconds.
        long endTime = System.currentTimeMillis() + 5*1000;
        while (System.currentTimeMillis() < endTime) {
            synchronized (this) {
                try {
                    wait(endTime - System.currentTimeMillis());
                } catch (Exception e) {
                }
            }
        }
    }
	
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent,flags,startId);
    }
	
	  
	@Override
	public void onDestroy() {
		Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show(); 
	}
}