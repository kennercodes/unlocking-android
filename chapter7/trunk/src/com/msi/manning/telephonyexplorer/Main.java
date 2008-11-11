package com.msi.manning.telephonyexplorer;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * Explore telephony related functionality and APIs in Android.
 * 
 * @author charliecollins
 * 
 */
public class Main extends Activity {

    public static final String NUMBER = "555-123-1234";

    private Button dialintent;
    private Button callintent;
    private Button gotoTelMgrExample;
    private Button gotoSmsExample;
    private Button gotoPnUtilsExample;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);

        gotoTelMgrExample = (Button) findViewById(R.id.gototelmgr_button);
        gotoTelMgrExample.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, TelephonyManagerExample.class);
                startActivity(intent);
            }
        });
        
        gotoSmsExample = (Button) findViewById(R.id.gotosms_button);
        gotoSmsExample.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, SmsExample.class);
                startActivity(intent);
            }
        });
        
        gotoPnUtilsExample = (Button) findViewById(R.id.gotopnutils_button);
        gotoPnUtilsExample.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Main.this, PhoneNumberUtilsExample.class);
                startActivity(intent);
            }
        });
        
        dialintent = (Button) findViewById(R.id.dialintent_button);
        dialintent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + NUMBER));
                startActivity(intent);
            }
        });

        callintent = (Button) findViewById(R.id.callintent_button);
        callintent.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + NUMBER));
                startActivity(intent);
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
