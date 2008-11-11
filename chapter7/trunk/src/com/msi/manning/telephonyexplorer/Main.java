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
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      this.setContentView(R.layout.main);

      this.gotoTelMgrExample = (Button) this.findViewById(R.id.gototelmgr_button);
      this.gotoTelMgrExample.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            Intent intent = new Intent(Main.this, TelephonyManagerExample.class);
            Main.this.startActivity(intent);
         }
      });

      this.gotoSmsExample = (Button) this.findViewById(R.id.gotosms_button);
      this.gotoSmsExample.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            Intent intent = new Intent(Main.this, SmsExample.class);
            Main.this.startActivity(intent);
         }
      });

      this.gotoPnUtilsExample = (Button) this.findViewById(R.id.gotopnutils_button);
      this.gotoPnUtilsExample.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            Intent intent = new Intent(Main.this, PhoneNumberUtilsExample.class);
            Main.this.startActivity(intent);
         }
      });

      this.dialintent = (Button) this.findViewById(R.id.dialintent_button);
      this.dialintent.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Main.NUMBER));
            Main.this.startActivity(intent);
         }
      });

      this.callintent = (Button) this.findViewById(R.id.callintent_button);
      this.callintent.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Main.NUMBER));
            Main.this.startActivity(intent);
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
