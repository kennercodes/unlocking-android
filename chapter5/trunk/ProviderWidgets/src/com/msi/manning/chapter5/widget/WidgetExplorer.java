package com.msi.manning.chapter5.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WidgetExplorer extends Activity {
   
    private EditText addName;
    private EditText addType;
    private EditText addCategory;
    private EditText editName;
    private EditText editType;
    private EditText editCategory;
    
    private Button addButton;
    private Button editButton;
    
    private long itemId;
   
    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.provider_explorer);

        this.addName = (EditText) this.findViewById(R.id.add_name);
        this.addType = (EditText) this.findViewById(R.id.add_type);
        this.addCategory = (EditText) this.findViewById(R.id.add_category);
        this.editName = (EditText) this.findViewById(R.id.edit_name);
        this.editType = (EditText) this.findViewById(R.id.edit_type);
        this.editCategory = (EditText) this.findViewById(R.id.edit_category);

        this.addButton = (Button) this.findViewById(R.id.add_button);
        this.addButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                WidgetExplorer.this.add();
            }
        });
        this.editButton = (Button) this.findViewById(R.id.edit_button);
        this.editButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                WidgetExplorer.this.edit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        List<WidgetBean> widgets = this.getWidgets();
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(200,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        if (widgets != null) {
            LinearLayout editLayout = (LinearLayout) this.findViewById(R.id.edit_buttons_layout);
            LinearLayout deleteLayout = (LinearLayout) this.findViewById(R.id.delete_buttons_layout);
            params.setMargins(10, 0, 0, 0);
            for (WidgetBean w : widgets) {
                WidgetButton widgetEditButton = new WidgetButton(this, w);
                widgetEditButton.setText(w.toString());
                editLayout.addView(widgetEditButton, params);
                widgetEditButton.setOnClickListener(new OnClickListener() {
                    public void onClick(final View v) {
                        WidgetButton view = (WidgetButton) v;
                        WidgetExplorer.this.editName.setText(view.widget.name);
                        WidgetExplorer.this.editType.setText(view.widget.type);
                        WidgetExplorer.this.editCategory.setText(view.widget.category);
                        WidgetExplorer.this.itemId = view.widget.id;
                    }
                });

                WidgetButton widgetDeleteButton = new WidgetButton(this, w);
                widgetDeleteButton.setText("Delete " + w.name);
                deleteLayout.addView(widgetDeleteButton, params);
                widgetDeleteButton.setOnClickListener(new OnClickListener() {
                    public void onClick(final View v) {
                        WidgetButton view = (WidgetButton) v;
                        WidgetExplorer.this.itemId = view.widget.id;
                        WidgetExplorer.this.delete();
                    }
                });
            }
        } else {
            LinearLayout layout = (LinearLayout) this.findViewById(R.id.edit_buttons_layout);
            TextView empty = new TextView(this);
            empty.setText("No current widgets");
            layout.addView(empty, params);
        }
    }
    
    @Override
    public void onPause() {
        super.onPause();
    }
    
    //
    // resolver methods
    //
    
    private void add() {
        ContentValues values = new ContentValues();
        values.put(Widget.NAME, this.addName.getText().toString());
        values.put(Widget.TYPE, this.addType.getText().toString());
        values.put(Widget.CATEGORY, this.addCategory.getText().toString());
        values.put(Widget.CREATED, System.currentTimeMillis());
        this.getContentResolver().insert(Widget.CONTENT_URI, values);

        this.startActivity(new Intent(this, WidgetExplorer.class));
    }

    private void delete() {
        Uri uri = Widget.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Long.toString(this.itemId)).build();
        this.getContentResolver().delete(uri, null, null);
        
        this.startActivity(new Intent(this, WidgetExplorer.class));
    }

    private void edit() {
        Uri uri = Widget.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Long.toString(this.itemId)).build();
        ContentValues values = new ContentValues();
        values.put(Widget.NAME, editName.getText().toString());
        values.put(Widget.TYPE, editType.getText().toString());
        values.put(Widget.CATEGORY, editCategory.getText().toString());
        values.put(Widget.UPDATED, System.currentTimeMillis());        
        this.getContentResolver().update(uri, values, null, null);
        
        this.startActivity(new Intent(this, WidgetExplorer.class));
    }

    private List<WidgetBean> getWidgets() {
        List<WidgetBean> results = null;
        long id = 0L;
        String name = null;
        String type = null;
        String category = null;
        long created = 0L;
        long updated = 0L;
        Uri uri = Widget.CONTENT_URI;
        Cursor cur = this.managedQuery(uri, null, null, null, null);
        if (cur != null) {
            while (cur.moveToNext()) {
                if (results == null) {
                    results = new ArrayList<WidgetBean>();
                }
                id = cur.getLong(cur.getColumnIndex(BaseColumns._ID));
                name = cur.getString(cur.getColumnIndex(Widget.NAME));
                type = cur.getString(cur.getColumnIndex(Widget.TYPE));
                category = cur.getString(cur.getColumnIndex(Widget.CATEGORY));
                created = cur.getLong(cur.getColumnIndex(Widget.CREATED));
                updated = cur.getLong(cur.getColumnIndex(Widget.UPDATED));
                results.add(new WidgetBean(id, name, type, category, created, updated));
            }
        }
        return results;
    }
    
    //
    // addtl classes
    //
    
    private class WidgetBean {
        public long id;
        public String name;
        public String type;
        public String category;
        long created;
        long updated;

        public WidgetBean(final long id, final String name, final String type, final String category,
                final long created, final long updated) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.category = category;
            this.created = created;
            this.updated = updated;
        }

        @Override
        public String toString() {
            return this.name + "\n" + this.type + " " + this.category;
        }
    }

    private class WidgetButton extends Button {
        public WidgetBean widget;

        public WidgetButton(final Context ctx, final WidgetBean w) {
            super(ctx);
            this.widget = w;
        }
    }
}
