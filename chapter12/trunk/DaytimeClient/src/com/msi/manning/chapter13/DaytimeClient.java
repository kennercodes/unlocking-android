/*
 * UnlockingAndroid
 * 
 * Author: Frank Ableson
 * fableson@msiservices.com
 * 
 * 
 */


package com.msi.manning.chapter13;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.os.Handler;
import android.os.Message;
import java.io.*;
import java.net.*;



import android.util.Log;

public class DaytimeClient extends Activity 
{
	Handler h;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) 
    {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
        
        
        final TextView statuslabel = (TextView) findViewById(R.id.statuslabel);
        
        h = new Handler()
        {
    		@Override
            public void handleMessage(Message msg) 
            {
                // process incoming messages here
            	switch (msg.what)
            	{
            		case 0:
            			// update progress bar
            			Log.d("CH13","data [" + (String) msg.obj + "]");
            			statuslabel.setText((String) msg.obj);
            			break;
            	}
            	super.handleMessage(msg); 
            }
        	
        };
        
        
        
        
        
        Button test = (Button) findViewById(R.id.testit);
        test.setOnClickListener(new Button.OnClickListener() 
        {
            public void onClick(View v) 
            {
            	try
            	{
            		// 	Perform action on click
            		Requester r = new Requester();
            		r.start();
            	}
            	catch (Exception e)
            	{
            		Log.d("CH13 exception caught : ",e.getMessage());
            	}
            }
        });            
    }
    
    
    public class Requester extends Thread
    {
    	Socket requestSocket;
    	OutputStream out;
    	InputStream in;
     	String message;
    	StringBuilder returnStringBuffer = new StringBuilder();
    	Message lmsg;
    	int ch;
    	
    	public void run()
    	{
    		try
    		{
    			
    			
    			requestSocket = new Socket("127.0.0.1", 1024);
                InputStreamReader isr = new InputStreamReader(requestSocket.getInputStream(),"ISO-8859-1");
                while ((ch = isr.read()) != -1) 
                {
                    returnStringBuffer.append((char) ch);
                }
                message = returnStringBuffer.toString();
                lmsg = new Message();
                lmsg.obj = (Object) message;
                lmsg.what = 0;
                h.sendMessage(lmsg);
                requestSocket.close();
    		}
    		catch (Exception ee)
    		{
    			Log.d("CH13","failed to read data" + ee.getMessage());
    		}
    	}
    }
}
