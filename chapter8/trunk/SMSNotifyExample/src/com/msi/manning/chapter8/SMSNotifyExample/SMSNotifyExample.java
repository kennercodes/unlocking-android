package com.msi.manning.chapter8.SMSNotifyExample;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSNotifyExample extends BroadcastReceiver {

    private static final String LOG_TAG = "SMSReceiver";
    public static final int NOTIFICATION_ID_RECEIVED = 0x1221;
    static final String ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public void onReceive(Context context, Intent intent) {
           
       NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (intent.getAction().equals(SMSNotifyExample.ACTION)) {

            StringBuilder sb = new StringBuilder();
            String from = new String();
            String body = new String();
            
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                for (Object pdu : pdus){
                SmsMessage messages = SmsMessage.createFromPdu((byte[]) pdu);
                sb.append("Received compressed SMS\nFrom: ");
                sb.append(messages.getDisplayOriginatingAddress());
                from = messages.getDisplayOriginatingAddress();
                sb.append("\n----Message----\n");
                sb.append("body -" + messages.getDisplayMessageBody());
                body= messages.getDisplayMessageBody();

          Log.i(SMSNotifyExample.LOG_TAG, "[SMSApp] onReceiveIntent: " + sb);
            abortBroadcast();
                }}
                
                int icon = R.drawable.chat;
                CharSequence tickerText = "New Message From " + from + ": " + body;
                long when = System.currentTimeMillis();

                Notification notification = new Notification(icon, tickerText, when);
                CharSequence contentTitle = "New SMS Message";
                CharSequence contentText = sb.toString();
                Intent notificationIntent = new Intent();
                PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
                
                notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
                notification.vibrate = new long[] { 100, 250, 100, 500};

                mNotificationManager.notify(NOTIFICATION_ID_RECEIVED, notification);
   
       }
    }
}
