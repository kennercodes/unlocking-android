package com.msi.manning.network;

import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.http.client.ResponseHandler;
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

import com.msi.manning.network.data.HTTPRequestHelper;
import com.msi.manning.network.data.xml.DeliciousHandler;
import com.msi.manning.network.data.xml.DeliciousPost;

/**
 * Android HTTP example demonstrating basic auth over Apache HttpClient 4 (using
 * del.icio.us API) - AND using custom HttpRequestHelper.
 * 
 * 
 * @author charliecollins
 * 
 */
public class ApacheHTTPViaHelper extends Activity {

    private static final String CLASSTAG = ApacheHTTPViaHelper.class.getSimpleName();
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
            ApacheHTTPViaHelper.this.output.setText(ApacheHTTPViaHelper.this.parseXMLResult(bundleResult));
        }
    };

    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        // inflate the SAME view XML layout file as ApacheHTTPWithAuth Activity (re-use it)
        this.setContentView(R.layout.apache_http);

        this.user = (EditText) this.findViewById(R.id.apache_user);
        this.pass = (EditText) this.findViewById(R.id.apache_pass);
        this.button = (Button) this.findViewById(R.id.apachego_button);
        this.output = (EditText) this.findViewById(R.id.apache_output);

        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(final View v) {
                ApacheHTTPViaHelper.this.output.setText("");

                ApacheHTTPViaHelper.this.performRequest(ApacheHTTPViaHelper.this.user.getText()
                        .toString(), ApacheHTTPViaHelper.this.pass.getText().toString());                
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

        final ResponseHandler<String> responseHandler = HTTPRequestHelper.getResponseHandlerInstance(this.handler);
        
        this.progressDialog = ProgressDialog.show(this, "working . . .", "performing HTTP post to del.icio.us");

        // do the HTTP dance in a separate thread (the responseHandler will fire when complete)    
        new Thread() {
            @Override
            public void run() {
                HTTPRequestHelper helper = new HTTPRequestHelper(responseHandler);
                helper.performPost(ApacheHTTPViaHelper.URL_GET_POSTS_RECENT, user, pass, null, null, null);
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
                Log.d(Constants.LOGTAG, " " + ApacheHTTPViaHelper.CLASSTAG + " DeliciousPost - " + p.getHref());
                result.append("\n" + p.getHref());
            }
        } catch (Exception e) {
            Log.e(Constants.LOGTAG, " " + ApacheHTTPViaHelper.CLASSTAG + " ERROR - " + e);
        }
        return result.toString();
    }

}