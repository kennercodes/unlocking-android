package com.msi.manning.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Android Apache HTTP example demonstrating using the ClientLogin feature of
 * the Google Data APIs (obtain a token, and send it as a header with subsequent requests).
 * 
 * TODO - this is just a stub NOT DONE YET
 * need to make POST to https://www.google.com/accounts/ClientLogin
 * then get back token and set as "Authorization: GoogleLogin auth=yourAuthToken"
 * then call calendar API or such
 * 
 * @author charliecollins
 * 
 */
public class GoogleClientLogin extends Activity {

    private static final String CLASSTAG = GoogleClientLogin.class.getSimpleName();
    private static final String URL_GET_GTOKEN = "https://www.google.com/accounts/ClientLogin";

    private EditText user;
    private EditText pass;
    private TextView output;
    private Button button;
    
    private ProgressDialog progressDialog;
    
    // use a handler to update the UI (send the handler messages from other threads)
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            progressDialog.dismiss();
            String bundleResult = msg.getData().getString("RESPONSE");
            GoogleClientLogin.this.output.setText(bundleResult);
        }
    };

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.google_client_login);

        this.user = (EditText) this.findViewById(R.id.gclient_user);
        this.pass = (EditText) this.findViewById(R.id.gclient_pass);
        this.button = (Button) this.findViewById(R.id.gclientgo_button);
        this.output = (TextView) this.findViewById(R.id.gclient_output);

        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                GoogleClientLogin.this.output.setText("");

                GoogleClientLogin.this.performRequest(GoogleClientLogin.this.user.getText()
                        .toString(), GoogleClientLogin.this.pass.getText().toString());                
            }
        });
    };

    @Override
    public void onPause() {
        super.onPause();
        if (this.progressDialog != null && this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }

   
    private void performRequest(final String url, final String token) {

        // TODO update to HELPER here
         
    }
    
    private void getToken(final String user, String pass) {

        // TODO update to HELPER here
         
    }

   
}