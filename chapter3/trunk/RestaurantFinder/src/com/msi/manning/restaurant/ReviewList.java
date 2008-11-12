package com.msi.manning.restaurant;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.msi.manning.restaurant.data.Review;
import com.msi.manning.restaurant.data.ReviewFetcher;

/**
 * "List" of reviews screen - show reviews that match Criteria user selected.
 * Users ReviewFetcher which makes a Google Base call via Rome.
 * 
 * @author charliecollins
 * 
 */
public class ReviewList extends ListActivity {
    private static final String CLASSTAG = ReviewList.class.getSimpleName();
    private static final int NUM_RESULTS_PER_PAGE = 8;
    private static final int MENU_GET_NEXT_PAGE = Menu.FIRST;
    private static final int MENU_CHANGE_CRITERIA = Menu.FIRST + 1;
    private ProgressDialog progressDialog;
    private List<Review> reviews;
    private ReviewAdapter reviewAdapter;
    private TextView empty;

    // use a Handler in order to update UI thread after worker done
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            Log.v(Constants.LOGTAG, " " + ReviewList.CLASSTAG + " worker thread done, setup ReviewAdapter");
            ReviewList.this.progressDialog.dismiss();
            if ((ReviewList.this.reviews == null) || (ReviewList.this.reviews.size() == 0)) {
                ReviewList.this.empty.setText("No Data");
            } else {
                ReviewList.this.reviewAdapter = new ReviewAdapter(ReviewList.this, ReviewList.this.reviews);
                ReviewList.this.setListAdapter(ReviewList.this.reviewAdapter);
            }
        }
    };    

    // onCreate is called when Activity is initialized
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(Constants.LOGTAG, " " + ReviewList.CLASSTAG + " onCreate");

        // NOTE* This Activity MUST contain a ListView named "@android:id/list"
        // (or "list" in code) in order to be customized
        // http://code.google.com/android/reference/android/app/ListActivity.html
        this.setContentView(R.layout.review_list);

        this.empty = (TextView) this.findViewById(R.id.empty);

        // set list properties
        final ListView listView = this.getListView();
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setEmptyView(this.empty);
    }
    
    // onResume is called when Activity hits foreground
    @Override
    protected void onResume() {
        super.onResume();
        Log.v(Constants.LOGTAG, " " + ReviewList.CLASSTAG + " onResume");
        // get the current review criteria from the Application (global state placed there)
        RestaurantFinderApplication application = (RestaurantFinderApplication) this.getApplication();
        String criteriaCuisine = application.getReviewCriteriaCuisine();
        String criteriaLocation = application.getReviewCriteriaLocation();

        // get start from, an int, from extras
        int startFrom = this.getIntent().getIntExtra(Constants.STARTFROM_EXTRA, 1);

        this.loadReviews(criteriaLocation, criteriaCuisine, "ALL", startFrom);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, ReviewList.MENU_GET_NEXT_PAGE, 0, R.string.menu_get_next_page).setIcon(
                android.R.drawable.ic_menu_more);
        menu.add(0, ReviewList.MENU_CHANGE_CRITERIA, 0, R.string.menu_change_criteria).setIcon(
                android.R.drawable.ic_menu_edit);
        return true;
    }

    @Override
    protected void onListItemClick(final ListView l, final View v, final int position, final long id) {
        // set the current review to the Application (global state placed there)
        RestaurantFinderApplication application = (RestaurantFinderApplication) this.getApplication();
        application.setCurrentReview(this.reviews.get(position));

        // startFrom page is not stored in application, for example purposes it's a simple "extra"
        Intent intent = new Intent(Constants.INTENT_ACTION_VIEW_DETAIL);
        intent.putExtra(Constants.STARTFROM_EXTRA, this.getIntent().getIntExtra(Constants.STARTFROM_EXTRA, 1));
        this.startActivity(intent);
    }

    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {
        case MENU_GET_NEXT_PAGE:
            // increment the startFrom value and call this Activity again
            intent = new Intent(Constants.INTENT_ACTION_VIEW_LIST);
            intent.putExtra(Constants.STARTFROM_EXTRA, this.getIntent().getIntExtra(Constants.STARTFROM_EXTRA, 1)
                    + ReviewList.NUM_RESULTS_PER_PAGE);
            this.startActivity(intent);
            return true;
        case MENU_CHANGE_CRITERIA:
            intent = new Intent(this, ReviewCriteria.class);
            this.startActivity(intent);
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }
    
    private void loadReviews(final String location, final String cuisine, final String rating, final int startFrom) {

        Log.v(Constants.LOGTAG, " " + ReviewList.CLASSTAG + " loadReviews");

        final ReviewFetcher rf = new ReviewFetcher(location, cuisine, rating, startFrom,
                ReviewList.NUM_RESULTS_PER_PAGE);

        this.progressDialog = ProgressDialog.show(this, " Working...", " Retrieving reviews", true, false);

        // get reviews in a separate thread for ProgressDialog/Handler
        // when complete send "empty" message to handler 
        new Thread() {
            @Override
            public void run() {
                ReviewList.this.reviews = rf.getReviews();
                ReviewList.this.handler.sendEmptyMessage(0);
            }
        }.start();
    }

   
}
