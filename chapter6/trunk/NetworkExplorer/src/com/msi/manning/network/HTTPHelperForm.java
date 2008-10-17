package com.msi.manning.network;

import java.util.HashMap;

import org.apache.http.client.ResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.msi.manning.network.data.HTTPRequestHelper;

/**
 * Simple form to exercise the HttpRequestHelper class (which wraps HttpClient).
 * 
 * GET: http://www.yahoo.com
 * POST: http://www.snee.com/xml/crud/posttest.cgi (fname and lname params)
 * 
 * 
 * @author charliecollins
 * 
 */
public class HTTPHelperForm extends Activity {

   private static final String CLASSTAG = HTTPHelperForm.class.getSimpleName();

   private EditText url;
   private Spinner method;
   private EditText param1Name;
   private EditText param1Value;
   private EditText param2Name;
   private EditText param2Value;
   private EditText param3Name;
   private EditText param3Value;
   private EditText user;
   private EditText pass;
   private Button go;
   private EditText output;

   private ProgressDialog progressDialog;

   // use a handler to update the UI (send the handler messages from other threads)
   private final Handler handler = new Handler() {
      @Override
      public void handleMessage(final Message msg) {
         HTTPHelperForm.this.progressDialog.dismiss();
         String bundleResult = msg.getData().getString("RESPONSE");         
         HTTPHelperForm.this.output.setText(bundleResult);
      }
   };

   @Override
   public void onCreate(final Bundle icicle) {
      super.onCreate(icicle);
      // inflate the SAME view XML layout file as ApacheHTTPWithAuth Activity (re-use it)
      this.setContentView(R.layout.http_helper_form);

      this.url = (EditText) this.findViewById(R.id.htth_url);
      this.method = (Spinner) this.findViewById(R.id.htth_method);
      this.param1Name = (EditText) this.findViewById(R.id.htth_param1_name);
      this.param1Value = (EditText) this.findViewById(R.id.htth_param1_name);
      this.param2Name = (EditText) this.findViewById(R.id.htth_param2_name);
      this.param2Value = (EditText) this.findViewById(R.id.htth_param2_value);
      this.param3Name = (EditText) this.findViewById(R.id.htth_param3_name);
      this.param3Value = (EditText) this.findViewById(R.id.htth_param3_value);
      this.user = (EditText) this.findViewById(R.id.htth_user);
      this.pass = (EditText) this.findViewById(R.id.htth_pass);
      this.go = (Button) this.findViewById(R.id.htth_go_button);
      this.output = (EditText) this.findViewById(R.id.htth_output);

      ArrayAdapter<String> methods = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[] {
               "GET", "POST" });
      methods.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
      this.method.setAdapter(methods);

      this.go.setOnClickListener(new OnClickListener() {
         public void onClick(final View v) {
            HTTPHelperForm.this.output.setText("");

            HTTPHelperForm.this.performRequest(HTTPHelperForm.this.url.getText().toString(), HTTPHelperForm.this.method
                     .getSelectedItem().toString(), HTTPHelperForm.this.param1Name.getText().toString(),
                     HTTPHelperForm.this.param1Value.getText().toString(), HTTPHelperForm.this.param2Name.getText()
                              .toString(), HTTPHelperForm.this.param2Value.getText().toString(),
                     HTTPHelperForm.this.param3Name.getText().toString(), HTTPHelperForm.this.param3Value.getText()
                              .toString(), HTTPHelperForm.this.user.getText().toString(), HTTPHelperForm.this.pass
                              .getText().toString());
         }
      });
   };

   @Override
   public void onPause() {
      super.onPause();
      if ((this.progressDialog != null) && this.progressDialog.isShowing()) {
         this.progressDialog.dismiss();
      }
   }

   /**
    * Perform asynchronous HTTP using Apache <code>HttpClient</code> via <code>HttpRequestHelper</code> and <code>ResponseHandler</code>.
    * 
    * @param url
    * @param method
    * @param param1Name
    * @param param1Value
    * @param param2Name
    * @param param2Value
    * @param param3Name
    * @param param3Value
    * @param user
    * @param pass
    */
   private void performRequest(final String url, final String method, final String param1Name,
            final String param1Value, final String param2Name, final String param2Value, final String param3Name,
            final String param3Value, final String user, final String pass) {

      final HashMap<String, String> params = new HashMap<String, String>();
      if ((param1Name != null) && (param1Value != null)) {
         params.put(param1Name, param1Value);
      }
      if ((param2Name != null) && (param2Value != null)) {
         params.put(param2Name, param2Value);
      }
      if ((param3Name != null) && (param3Value != null)) {
         params.put(param3Name, param3Value);
      }

      final ResponseHandler<String> responseHandler = HTTPRequestHelper.getResponseHandlerInstance(this.handler);

      this.progressDialog = ProgressDialog.show(this, "working . . .", "performing HTTP request");

      // do the HTTP dance in a separate thread (the responseHandler will fire when complete)    
      new Thread() {
         @Override
         public void run() {
            HTTPRequestHelper helper = new HTTPRequestHelper(responseHandler);
            if (method.equals("GET")) {
               helper.performGet(url, user, pass, null, params);
            }
            else if (method.equals("POST")) {
               helper.performPost(url, user, pass, null, params);
            }
            else {
               Message msg = new Message();
               Bundle bundle = new Bundle();
               bundle.putString("RESPONSE", "ERROR - see logcat");
               msg.setData(bundle);               
               HTTPHelperForm.this.handler.sendMessage(msg);
               Log.w(Constants.LOGTAG, " " + HTTPHelperForm.CLASSTAG + " unknown method, nothing to do");
            }
            // TODO select operation based on inputs
            // this isn't in the book, and isn't done yet - but is on my TODO list
            // check POST and GET methods with various params, and eventually multi part form, etc
         }
      }.start();
   }

}