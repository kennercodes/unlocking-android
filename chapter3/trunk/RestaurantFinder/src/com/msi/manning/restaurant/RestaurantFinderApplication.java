package com.msi.manning.restaurant;

import android.app.Application;

import com.msi.manning.restaurant.data.Review;

/**
 * Extend Application for global state information for an application. Access
 * the application via Activity.getApplication().
 * 
 * There are several ways to store global state information, this is one of
 * them. Another is to create a class with static members and just access it
 * from Activities.
 * 
 * Either approach works, and there is debate about which is better. Either way,
 * make sure to clean up in life-cycle pause or destroy methods if you use
 * resources that need cleaning up (static maps, etc).
 * 
 * @author charliecollins
 * 
 */
public class RestaurantFinderApplication extends Application {
   private String reviewCriteriaLocation;
   private String reviewCriteriaCuisine;
   private Review currentReview;

   public RestaurantFinderApplication() {
      super();
   }

   @Override
   public void onCreate() {
      super.onCreate();
   }

   @Override
   public void onTerminate() {
      super.onTerminate();
   }

   public String getReviewCriteriaLocation() {
      return this.reviewCriteriaLocation;
   }

   public void setReviewCriteriaLocation(String reviewCriteriaLocation) {
      this.reviewCriteriaLocation = reviewCriteriaLocation;
   }

   public String getReviewCriteriaCuisine() {
      return this.reviewCriteriaCuisine;
   }

   public void setReviewCriteriaCuisine(String reviewCriteriaCuisine) {
      this.reviewCriteriaCuisine = reviewCriteriaCuisine;
   }

   public Review getCurrentReview() {
      return this.currentReview;
   }

   public void setCurrentReview(Review currentReview) {
      this.currentReview = currentReview;
   }

}
