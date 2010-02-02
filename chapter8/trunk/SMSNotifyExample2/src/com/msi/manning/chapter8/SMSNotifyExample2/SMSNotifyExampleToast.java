package com.msi.manning.chapter8.SMSNotifyExample2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSNotifyExampleToast extends BroadcastReceiver {

    private static final String LOG_TAG = "SMSReceiver";
    public static final int NOTIFICATION_ID_RECEIVED = 0x1221;
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        
        if (intent.getAction().equals(SMSNotifyExampleToast.ACTION)) {
            StringBuilder sb = new StringBuilder();

            Bundle bundle = intent.getExtras();
            if (bundle != null) {

                Object[] pdus = (Object[]) bundle.get("pdus");
                for (Object pdu : pdus){
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdu);
                sb.append("Received SMS\nFrom: ");
                sb.append(messages.getDisplayOriginatingAddress());
                sb.append("\n----Message----\n");
                sb.append( messages.getDisplayMessageBody());
                }
            }
            Log.i(SMSNotifyExampleToast.LOG_TAG, "[SMSApp] onReceiveIntent: " + sb);
            Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
