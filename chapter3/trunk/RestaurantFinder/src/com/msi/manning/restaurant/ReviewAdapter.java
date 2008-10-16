package com.msi.manning.restaurant;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.msi.manning.restaurant.data.Review;

/**
 * Custom adapter for "Review" model objects.
 * 
 * @author charliecollins
 */
public class ReviewAdapter extends BaseAdapter {
    
    private static final String CLASSTAG = ReviewAdapter.class.getSimpleName();
    private final Context context;

    private final List<Review> reviews;

    public ReviewAdapter(final Context context, final List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        Log.v(Constants.LOGTAG, " " + ReviewAdapter.CLASSTAG + " reviews size - " + this.reviews.size());
    }

    public int getCount() {
        return this.reviews.size();
    }

    public Object getItem(final int position) {
        return this.reviews.get(position);
    }

    public long getItemId(final int position) {
        return position;
    }

    public View getView(final int position, final View convertView, final ViewGroup parent) {
        Review review = this.reviews.get(position);
        return new ReviewListView(this.context, review.name, review.rating);
    }
    
    /**
     * ReviewListView that adapter returns as it's view item per row.
     * 
     * @author charliecollins
     * 
     */
    private final class ReviewListView extends LinearLayout {
        private final TextView name;
        private final TextView rating;

        public ReviewListView(final Context context, final String name, final String rating) {

            super(context);
            this.setOrientation(LinearLayout.VERTICAL);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(5, 3, 5, 0);

            this.name = new TextView(context);
            this.name.setText(name);
            this.name.setTextSize(16f);
            this.name.setTextColor(Color.WHITE);
            this.addView(this.name, params);

            this.rating = new TextView(context);
            this.rating.setText(rating);
            this.rating.setTextSize(16f);
            this.rating.setTextColor(Color.GRAY);
            this.addView(this.rating, params);
        }
    }
}
