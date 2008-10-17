package com.msi.manning.chapter5.filestorage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class CreateFile extends Activity {

    private static final String LOGTAG = "FileStorage";

    private EditText createInput;
    private Button createButton;

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.create_file);

        this.createInput = (EditText) this.findViewById(R.id.create_input);
        this.createButton = (Button) this.findViewById(R.id.create_button);

        this.createButton.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                FileOutputStream fos = null;
                try {
                    fos = CreateFile.this.openFileOutput("filename.txt", Context.MODE_PRIVATE);
                    fos.write(CreateFile.this.createInput.getText().toString().getBytes());
                } catch (FileNotFoundException e) {
                    Log.e(CreateFile.LOGTAG, e.getLocalizedMessage());
                } catch (IOException e) {
                    Log.e(CreateFile.LOGTAG, e.getLocalizedMessage());
                } finally {

                    if (fos != null) {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            // swallow
                        }
                    }
                }
                CreateFile.this.startActivity(new Intent(CreateFile.this, ReadFile.class));
            }
        });
    }
}