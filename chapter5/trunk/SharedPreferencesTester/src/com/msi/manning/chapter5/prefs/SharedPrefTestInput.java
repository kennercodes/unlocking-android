package com.msi.manning.chapter5.prefs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SharedPrefTestInput extends Activity {

    public static final String PREFS_PRIVATE = "PREFS_PRIVATE";
    public static final String PREFS_WORLD_READ = "PREFS_WORLD_READABLE";
    public static final String PREFS_WORLD_WRITE = "PREFS_WORLD_WRITABLE";
    public static final String PREFS_WORLD_READ_WRITE = "PREFS_WORLD_READABLE_WRITABLE";

    public static final String KEY_PRIVATE = "KEY_PRIVATE";
    public static final String KEY_WORLD_READ = "KEY_WORLD_READ";
    public static final String KEY_WORLD_WRITE = "KEY_WORLD_WRITE";
    public static final String KEY_WORLD_READ_WRITE = "KEY_WORLD_READ_WRITE";

    private EditText inputPrivate;
    private EditText inputWorldRead;
    private EditText inputWorldWrite;
    private EditText inputWorldReadWrite;
    private Button button;

    private SharedPreferences prefsPrivate;
    private SharedPreferences prefsWorldRead;
    private SharedPreferences prefsWorldWrite;
    private SharedPreferences prefsWorldReadWrite;

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.shared_preftest_input);

        this.inputPrivate = (EditText) this.findViewById(R.id.input_private);
        this.inputWorldRead = (EditText) this.findViewById(R.id.input_worldread);
        this.inputWorldWrite = (EditText) this.findViewById(R.id.input_worldwrite);
        this.inputWorldReadWrite = (EditText) this.findViewById(R.id.input_worldreadwrite);
        this.button = (Button) this.findViewById(R.id.prefs_test_button);
        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {

                boolean valid = SharedPrefTestInput.this.validate();
                if (valid) {
                    SharedPrefTestInput.this.prefsPrivate = SharedPrefTestInput.this.getSharedPreferences(
                            SharedPrefTestInput.PREFS_PRIVATE, Context.MODE_PRIVATE);
                    SharedPrefTestInput.this.prefsWorldRead = SharedPrefTestInput.this.getSharedPreferences(
                            SharedPrefTestInput.PREFS_WORLD_READ, Context.MODE_WORLD_READABLE);
                    SharedPrefTestInput.this.prefsWorldWrite = SharedPrefTestInput.this.getSharedPreferences(
                            SharedPrefTestInput.PREFS_WORLD_WRITE, Context.MODE_WORLD_WRITEABLE);
                    SharedPrefTestInput.this.prefsWorldReadWrite = SharedPrefTestInput.this.getSharedPreferences(
                            SharedPrefTestInput.PREFS_WORLD_READ_WRITE, Context.MODE_WORLD_READABLE
                                    + Context.MODE_WORLD_WRITEABLE);

                    Editor prefsPrivateEditor = SharedPrefTestInput.this.prefsPrivate.edit();
                    Editor prefsWorldReadEditor = SharedPrefTestInput.this.prefsWorldRead.edit();
                    Editor prefsWorldWriteEditor = SharedPrefTestInput.this.prefsWorldWrite.edit();
                    Editor prefsWorldReadWriteEditor = SharedPrefTestInput.this.prefsWorldReadWrite.edit();

                    prefsPrivateEditor.putString(SharedPrefTestInput.KEY_PRIVATE, SharedPrefTestInput.this.inputPrivate
                            .getText().toString());
                    prefsWorldReadEditor.putString(SharedPrefTestInput.KEY_WORLD_READ,
                            SharedPrefTestInput.this.inputWorldRead.getText().toString());
                    prefsWorldWriteEditor.putString(SharedPrefTestInput.KEY_WORLD_WRITE,
                            SharedPrefTestInput.this.inputWorldWrite.getText().toString());
                    prefsWorldReadWriteEditor.putString(SharedPrefTestInput.KEY_WORLD_READ_WRITE,
                            SharedPrefTestInput.this.inputWorldReadWrite.getText().toString());

                    prefsPrivateEditor.commit();
                    prefsWorldReadEditor.commit();
                    prefsWorldWriteEditor.commit();
                    prefsWorldReadWriteEditor.commit();

                    Intent intent = new Intent(SharedPrefTestInput.this, SharedPrefTestOutput.class);
                    SharedPrefTestInput.this.startActivity(intent);
                }
            }
        });
    }

    private boolean validate() {
        boolean valid = true;
        StringBuffer sb = new StringBuffer();
        sb.append("Validation failed: \n");

        if ((this.inputPrivate == null) || (this.inputPrivate.getText().toString() == null)
                || this.inputPrivate.getText().toString().equals("")) {
            sb.append("First input, private pref, must be present.\n");
            valid = false;
        }
        if ((this.inputWorldRead == null) || (this.inputWorldRead.getText().toString() == null)
                || this.inputWorldRead.getText().toString().equals("")) {
            sb.append("Second input, world read pref, must be present.\n");
            valid = false;
        }
        if ((this.inputWorldWrite == null) || (this.inputWorldWrite.getText().toString() == null)
                || this.inputWorldWrite.getText().toString().equals("")) {
            sb.append("Third input, world write pref, must be present.\n");
            valid = false;
        }
        if ((this.inputWorldReadWrite == null) || (this.inputWorldReadWrite.getText().toString() == null)
                || this.inputWorldReadWrite.getText().toString().equals("")) {
            sb.append("Fourth input, world read write pref, must be present.\n");
            valid = false;
        }

        if (!valid) {
            Toast.makeText(SharedPrefTestInput.this, sb.toString(), Toast.LENGTH_SHORT).show();
        }
        return valid;
    }
}