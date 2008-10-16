package com.msi.manning.network.data;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.msi.manning.network.Constants;
import com.msi.manning.network.util.StringUtils;

/**
 * Wrapper to help make HTTP requests easier - after all, we want to make it
 * nice for the people.
 * 
 * @author charliecollins
 * 
 */
public class HttpRequestHelper {

    private static final String CLASSTAG = HttpRequestHelper.class.getSimpleName();

    private static final int POST_TYPE = 1;
    private static final int GET_TYPE = 2;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FORM_ENCODED = "application/x-www-form-urlencoded";

    private ResponseHandler<String> responseHandler;

    public HttpRequestHelper(ResponseHandler<String> responseHandler) {
        this.responseHandler = responseHandler;
    }

    /**
     * Perform an HTTP POST operation.
     * 
     */
    public void performPost(String url, final String user, final String pass, final String data,
            final Map<String, String> additionalHeaders, final Map<String, String> nameValueParams) {
        this.performRequest(url, user, pass, additionalHeaders, nameValueParams, POST_TYPE);
    }

    /**
     * Perform an HTTP GET operation.
     * 
     */
    public void performGet(final String url, final String user, final String pass, 
            final Map<String, String> additionalHeaders) {
        this.performRequest(url, user, pass, additionalHeaders, null, GET_TYPE);
    }

    /**
     * Private heavy lifting method that performs GET or POST with supplied url, user, 
     * pass, data, and headers.
     * 
     * TODO binary multi-part form data
     * 
     * @param url
     * @param user
     * @param pass
     * @param additionalHeaders
     * @param nameValueParams
     * @param requestType
     */
    private void performRequest(final String url, final String user, final String pass, 
            final Map<String, String> additionalHeaders, final Map<String, String> nameValueParams, int requestType) {

        // establish HttpClient
        DefaultHttpClient client = new DefaultHttpClient();
        
        // add user and pass to client credentials if present
        if (user != null && pass != null) {
            Log.d(Constants.LOGTAG, " " + CLASSTAG + " user and pass present, adding credentials to request");
            client.getCredentialsProvider().setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(user, pass));
        }

        // process additional headers using request interceptor
        final Map<String, String> headers = new HashMap<String, String>();
        if (additionalHeaders != null && additionalHeaders.size() > 0) {
            headers.putAll(additionalHeaders);
        }
        if (requestType == POST_TYPE) {
            headers.put(CONTENT_TYPE, FORM_ENCODED);
        }
        if (headers.size() > 0) {
            client.addRequestInterceptor(new HttpRequestInterceptor() {
                public void process(final HttpRequest request, final HttpContext context) throws HttpException,
                        IOException {
                    for (String key : headers.keySet()) {
                        if (!request.containsHeader(key)) {
                            request.addHeader(key, headers.get(key));
                        }
                    }
                }
            });
        }        

        // handle POST or GET request respectively
        if (requestType == POST_TYPE) {
            Log.d(Constants.LOGTAG, " " + CLASSTAG + " performRequest POST");    
            
            HttpPost method = new HttpPost(url);   
            
            // data - name/value params 
            // (mutli part form data not supported with helper, yet, but HttpClient can handle it fine)
            List <NameValuePair> nvps =  null;
            if (nameValueParams != null && nameValueParams.size() > 0) {
                nvps = new ArrayList <NameValuePair>();
                for (String key : nameValueParams.keySet()) {
                    nvps.add(new BasicNameValuePair(key, nameValueParams.get(key)));
                }            
            }           
            
            if (nvps != null) {
                try {
                method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    Log.e(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG, e);
                }
            }
            
            try {
                client.execute(method, this.responseHandler);
                Log.d(Constants.LOGTAG, " " + CLASSTAG + " request completed");            
            } catch (ClientProtocolException e) {
                Log.e(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG, e);
            } catch (IOException e) {
                Log.e(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG, e);
            }
        } else if (requestType == GET_TYPE) {
            Log.d(Constants.LOGTAG, " " + CLASSTAG + " performRequest GET");
            HttpGet method = new HttpGet(url);
            
            try {
                client.execute(method, this.responseHandler);
                Log.d(Constants.LOGTAG, " " + CLASSTAG + " request completed");            
            } catch (ClientProtocolException e) {
                Log.e(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG, e);
            } catch (IOException e) {
                Log.e(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG, e);
            }
        }
    }
    
    /**
     * Static utility method to create a default ResponseHandler that 
     * sends a Message to the passed in Handler with the response as a String, after
     * the request completes. 
     * 
     * @param handler
     * @return
     */
    public static ResponseHandler<String> getResponseHandlerInstance(final Handler handler) {
        final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(HttpResponse response) {
                StatusLine status = response.getStatusLine();
                Log.d(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG + " statusCode - " + status.getStatusCode());
                Log.d(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG + " statusReasonPhrase - "
                        + status.getReasonPhrase());
                HttpEntity entity = response.getEntity();
                String result = null;
                try {
                    result = StringUtils.inputStreamToString(entity.getContent());
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString("RESPONSE", result);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    Log.e(Constants.LOGTAG, " " + HttpRequestHelper.CLASSTAG, e);
                }
                return result;
            }
        };
        return responseHandler;
    }

}