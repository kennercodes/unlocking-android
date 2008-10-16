package com.msi.manning.restaurant.data;

import java.util.ArrayList;

import android.test.ApplicationTestCase;
import android.test.mock.MockContext;

import com.msi.manning.restaurant.RestaurantFinderApplication;

public class ReviewFetcherTest extends ApplicationTestCase {
    
    public ReviewFetcherTest() {
        super(RestaurantFinderApplication.class);
        setContext(new MockContext());
        this.createApplication();
    }
    
    String exampleQuery = 
        "http://www.google.com/base/feeds/snippets/-/reviews?bq=[review%20type:restaurant][location:tampa%2C+fl]&start-index=1&max-results=8";

    
    public void testReviewFetcher() {
        
        ReviewFetcher fetcher = new ReviewFetcher("atlanta, ga", "ALL", "ALL", 1, 8);
        
        ArrayList<Review> reviews = fetcher.getReviews();
        
        for (Review r: reviews) {
            System.out.println("review - " + r);
        }
        
    }
    
    
}