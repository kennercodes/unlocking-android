package com.msi.manning.network;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.msi.manning.network.data.xml.DeliciousHandler;
import com.msi.manning.network.data.xml.DeliciousPost;
import com.msi.manning.network.util.StringUtils;

/**
 * Android HTTP example demonstrating basic auth over Apache HttpClient 4 (using
 * del.icio.us API).
 * 
 * 
 * @author charliecollins
 * 
 */
public class ApacheHTTPWithAuth extends Activity {

    private static final String CLASSTAG = ApacheHTTPWithAuth.class.getSimpleName();
    private static final String URL_GET_POSTS_RECENT = "https://api.del.icio.us/v1/posts/recent?";

    private EditText user;
    private EditText pass;
    private EditText output;
    private Button button;
    
    private ProgressDialog progressDialog;
    
    // use a handler to update the UI (send the handler messages from other threads)
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(final Message msg) {
            progressDialog.dismiss();
            String bundleResult = msg.getData().getString("RESPONSE");
            ApacheHTTPWithAuth.this.output.setText(bundleResult);
        }
    };

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        this.setContentView(R.layout.apache_http);

        this.user = (EditText) this.findViewById(R.id.apache_user);
        this.pass = (EditText) this.findViewById(R.id.apache_pass);
        this.button = (Button) this.findViewById(R.id.apachego_button);
        this.output = (EditText) this.findViewById(R.id.apache_output);

        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                ApacheHTTPWithAuth.this.output.setText("");

                ApacheHTTPWithAuth.this.performRequest(ApacheHTTPWithAuth.this.user.getText()
                        .toString(), ApacheHTTPWithAuth.this.pass.getText().toString());                
            }
        });
    };

    @Override
    public void onPause() {
        super.onPause();
        if (this.progressDialog.isShowing()) {
            this.progressDialog.dismiss();
        }
    }

    /**
     * Perform asynchronous HTTP using Apache <code>HttpClient</code> and <code>ResponseHandler</code>.
     * 
     * @param user
     * @param pass
     */
    private void performRequest(final String user, final String pass) {

        // use a response handler so we aren't blocking on the HTTP request
        final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(HttpResponse response) {
                // when the response happens close the notification and update UI
                StatusLine status = response.getStatusLine();
                Log.d(Constants.LOGTAG, " " + ApacheHTTPWithAuth.CLASSTAG + " statusCode - " + status.getStatusCode());
                Log.d(Constants.LOGTAG, " " + ApacheHTTPWithAuth.CLASSTAG + " statusReasonPhrase - "
                        + status.getReasonPhrase());
                HttpEntity entity = response.getEntity();
                String result = null;
                try {
                    result = ApacheHTTPWithAuth.this.parseXMLResult(
                            StringUtils.inputStreamToString(entity.getContent()));
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("RESPONSE", result);
                    message.setData(bundle);
                    handler.sendMessage(message);                    
                } catch (IOException e) {
                    Log.e(Constants.LOGTAG, " " + ApacheHTTPWithAuth.CLASSTAG, e);
                }
                return result;
            }
        };
        
        this.progressDialog = ProgressDialog.show(this, "working . . .", "performing HTTP post to del.icio.us");

        // do the HTTP dance in a separate thread (the responseHandler will fire when complete)    
        new Thread() {
            @Override
            public void run() {
                try {
                    DefaultHttpClient client = new DefaultHttpClient();
                    Credentials credentials = new UsernamePasswordCredentials(user, pass);
                    client.getCredentialsProvider().setCredentials(AuthScope.ANY, credentials);
                    HttpPost httpMethod = new HttpPost(ApacheHTTPWithAuth.URL_GET_POSTS_RECENT);
                    client.execute(httpMethod, responseHandler);
                } catch (ClientProtocolException e) {
                    Log.e(Constants.LOGTAG, " " + ApacheHTTPWithAuth.CLASSTAG, e);
                } catch (IOException e) {
                    Log.e(Constants.LOGTAG, " " + ApacheHTTPWithAuth.CLASSTAG, e);
                }
            }
        }.start();        
    }

    /**
     * Parse XML result into data objects. 
     * 
     * @param xmlString
     * @return
     */
    private String parseXMLResult(String xmlString) {
        StringBuilder result = new StringBuilder();

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xr = sp.getXMLReader();
            DeliciousHandler handler = new DeliciousHandler();
            xr.setContentHandler(handler);
            xr.parse(new InputSource(new StringReader(xmlString)));

            List<DeliciousPost> posts = handler.getPosts();
            for (DeliciousPost p : posts) {
                Log.d(Constants.LOGTAG, " " + ApacheHTTPWithAuth.CLASSTAG + " DeliciousPost - " + p.getHref());
                result.append("\n" + p.getHref());
            }
        } catch (Exception e) {
            Log.e(Constants.LOGTAG, " " + ApacheHTTPWithAuth.CLASSTAG + " ERROR - " + e);
        }
        return result.toString();
    }

}