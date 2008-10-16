package com.msi.manning.restaurant;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.msi.manning.restaurant.data.Review;

/**
 * Show Review detail for review item user selected.
 * 
 * @author charliecollins
 * 
 */
public class ReviewDetail extends Activity {

   private static final String CLASSTAG = ReviewDetail.class.getSimpleName();
   private static final int MENU_WEB_REVIEW = Menu.FIRST;
   private static final int MENU_MAP_REVIEW = Menu.FIRST + 1;
   private static final int MENU_CALL_REVIEW = Menu.FIRST + 2;

   private TextView name;
   private TextView rating;
   private TextView review;
   private TextView location;
   private TextView phone;
   private ImageView reviewImage;

   private String link;
   private String imageLink;

   private Handler handler = new Handler() {
      @Override
      public void handleMessage(final Message msg) {
         if ((ReviewDetail.this.imageLink != null) && !ReviewDetail.this.imageLink.equals("")) {
            try {
               ///Log.v(Constants.LOGTAG, " " + ReviewDetail.CLASSTAG + " get image imageLink = " + imageLink);
               URL url = new URL(ReviewDetail.this.imageLink);
               URLConnection conn = url.openConnection();
               conn.connect();
               BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
               Bitmap bm = BitmapFactory.decodeStream(bis);
               bis.close();
               ReviewDetail.this.reviewImage.setImageBitmap(bm);
            }
            catch (IOException e) {
               Log.e(Constants.LOGTAG, " " + CLASSTAG, e);
            }
         }
         else {
            ReviewDetail.this.reviewImage.setImageResource(R.drawable.no_review_image);
         }
      }
   };

   @Override
   public void onCreate(final Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      Log.v(Constants.LOGTAG, " " + ReviewDetail.CLASSTAG + " onCreate");

      // inflate layout
      this.setContentView(R.layout.review_detail);

      // reference XML defined views that we will touch in code
      this.name = (TextView) this.findViewById(R.id.name_detail);
      this.name.startAnimation(AnimationUtils.loadAnimation(this, R.anim.scaler));
      this.rating = (TextView) this.findViewById(R.id.rating_detail);
      this.location = (TextView) this.findViewById(R.id.location_detail);
      this.phone = (TextView) this.findViewById(R.id.phone_detail);
      this.review = (TextView) this.findViewById(R.id.review_detail);      
      this.reviewImage = (ImageView) this.findViewById(R.id.review_image);

      // get the current review from the Application (global state placed there)
      RestaurantFinderApplication application = (RestaurantFinderApplication) this.getApplication();
      Review currentReview = application.getCurrentReview();

      this.link = currentReview.link;
      this.imageLink = currentReview.imageLink;
      this.name.setText(currentReview.name);
      this.rating.setText(currentReview.rating);
      this.location.setText(currentReview.location);
      this.review.setText(currentReview.content);
      if ((currentReview.phone != null) && !currentReview.phone.equals("")) {
         this.phone.setText(currentReview.phone);
      }
      else {
         this.phone.setText("NA");
      }
   }

   @Override
   protected void onResume() {
      super.onResume();
      Log.v(Constants.LOGTAG, " " + CLASSTAG + " onResume");

      // tell handler to load image
      this.handler.sendEmptyMessage(1);
   }

   @Override
   public boolean onCreateOptionsMenu(final Menu menu) {
      super.onCreateOptionsMenu(menu);
      menu.add(0, ReviewDetail.MENU_WEB_REVIEW, 0, R.string.menu_web_review).setIcon(
               android.R.drawable.ic_menu_info_details);
      menu.add(0, ReviewDetail.MENU_MAP_REVIEW, 1, R.string.menu_map_review)
               .setIcon(android.R.drawable.ic_menu_mapmode);
      menu.add(0, ReviewDetail.MENU_CALL_REVIEW, 2, R.string.menu_call_review).setIcon(android.R.drawable.ic_menu_call);
      return true;
   }

   @Override
   public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
      Intent intent = null;
      switch (item.getItemId()) {
      case MENU_WEB_REVIEW:
         Log.v(Constants.LOGTAG, " " + ReviewDetail.CLASSTAG + " WEB - " + this.link);
         if ((this.link != null) && !this.link.equals("")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(this.link));
            this.startActivity(intent);
         }
         else {
            new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.alert_label)).setMessage(
                     R.string.no_link_message).setPositiveButton("Continue", new OnClickListener() {
               public void onClick(final DialogInterface dialog, final int arg1) {
               }
            }).show();
         }

         return true;
      case MENU_MAP_REVIEW:
         Log.v(Constants.LOGTAG, " " + ReviewDetail.CLASSTAG + " MAP ");
         if ((this.location.getText() != null) && !this.location.getText().equals("")) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + this.location.getText().toString()));
            this.startActivity(intent);
         }
         else {
            new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.alert_label)).setMessage(
                     R.string.no_location_message).setPositiveButton("Continue", new OnClickListener() {
               public void onClick(final DialogInterface dialog, final int arg1) {
               }
            }).show();
         }

         return true;
      case MENU_CALL_REVIEW:
         Log.v(Constants.LOGTAG, " " + ReviewDetail.CLASSTAG + " PHONE ");
         if ((this.phone.getText() != null) && !this.phone.getText().equals("") && !this.phone.getText().equals("NA")) {
            Log.v(Constants.LOGTAG, " " + ReviewDetail.CLASSTAG + " phone - " + this.phone.getText().toString());
            String phoneString = ReviewDetail.parsePhone(this.phone.getText().toString());
            intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneString));
            this.startActivity(intent);
         }
         else {
            new AlertDialog.Builder(this).setTitle(this.getResources().getString(R.string.alert_label)).setMessage(
                     R.string.no_phone_message).setPositiveButton("Continue", new OnClickListener() {
               public void onClick(final DialogInterface dialog, final int arg1) {
               }
            }).show();
         }
         return true;
      }
      return super.onMenuItemSelected(featureId, item);
   }

   public static String parsePhone(final String p) {
      String tempP = p;
      tempP = tempP.replaceAll("\\D", "");
      tempP = tempP.replaceAll("\\s", "");
      return tempP.trim();
   }
}
