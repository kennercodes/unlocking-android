package com.msi.manning.chapter8;


import android.app.Activity;
import android.app.NotificationManager;
import android.os.Bundle;

public class SMSNotifyActivity extends Activity {

	    /** Called when the activity is first created. */

	    public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        setContentView(R.layout.main);
	        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        nm.cancel(R.string.app_name);
	       
	    }
	}
	
	