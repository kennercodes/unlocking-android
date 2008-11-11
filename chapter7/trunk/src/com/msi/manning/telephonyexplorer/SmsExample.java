package com.msi.manning.telephonyexplorer;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Demonstrate sending and receiving SMS messages.
 * 
 * SEND: Use emulator (telnet localhost 5554) to issue
 * "sms send 5551231234 testing" (or DDMS tool to send).
 *  
 * RECEIVE: Use form to send messsage.
 * 
 * @author charliecollins
 * 
 */
public class SmsExample extends Activity {
	private EditText smsInputText;
	private EditText smsInputDest;
	private Button smsSend;

	private SmsManager smsManager;

	@Override
	public void onCreate(Bundle icicle) {
		Log.d("TelExp ******", "SmsExample onCreate");

		super.onCreate(icicle);
		setContentView(R.layout.smsexample);

		smsInputDest = (EditText) findViewById(R.id.smsinputdest);
		smsInputText = (EditText) findViewById(R.id.smsinputtext);
		smsSend = (Button) findViewById(R.id.smssend_button);

		smsManager = SmsManager.getDefault();

		// pending intent request code NOT USED
		final PendingIntent sentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, SmsExample.class), 0);
		
		smsSend.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.d("TelExp ******", "sending SMS message via manager");

				String dest = smsInputDest.getText().toString();
				if (PhoneNumberUtils.isWellFormedSmsAddress(dest)) {

					// dest, serviceCenter (null for default), message,
					// sentIntent, deliveryIntent
					//
					// Set the second parameter to null. The scAddress relates
					// to the address of the server on the cellular network that will handle
					// the message, it is not the address of the sender.
					smsManager.sendTextMessage(smsInputDest.getText()
							.toString(), null, smsInputText.getText()
							.toString(), sentIntent, null);
					
					Toast.makeText(SmsExample.this,
							"SMS message sent",
							Toast.LENGTH_LONG).show();

				} else {
					Toast.makeText(SmsExample.this,
							"SMS destination invalid - try again",
							Toast.LENGTH_LONG).show();
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
}