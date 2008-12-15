package com.msi.manning.chapter8.simpleAlarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import java.util.Calendar;

public class GenerateAlarm extends Activity
{
    Toast mToast;

    @Override
	protected void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        Button button = (Button)findViewById(R.id.set_alarm_button);
        button.setOnClickListener(mOneShotListener);
     }

    private OnClickListener mOneShotListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            
            Intent intent = new Intent(GenerateAlarm.this, AlarmReceiver.class);

            PendingIntent appIntent = PendingIntent.getBroadcast(GenerateAlarm.this, 0, intent, 0);
            
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.add(Calendar.SECOND, 30);

            AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
            am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), appIntent);

            if (mToast != null) {mToast.cancel();
            }
            mToast = Toast.makeText(GenerateAlarm.this, R.string.alarm_message, Toast.LENGTH_LONG);
            mToast.show();
        }
    };
    
}