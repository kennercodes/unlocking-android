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
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpResponse;
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
 * TODO cookies TODO multi-part form data
 * 
 * @author charliecollins
 * 
 */
public class HTTPRequestHelper {

    private static final String CLASSTAG = HTTPRequestHelper.class.getSimpleName();

    private static final int POST_TYPE = 1;
    private static final int GET_TYPE = 2;
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String FORM_ENCODED = "application/x-www-form-urlencoded";
   
    private final ResponseHandler<String> responseHandler;

    public HTTPRequestHelper(final ResponseHandler<String> responseHandler) {
        this.responseHandler = responseHandler;
    }

    /**
     * Perform an HTTP GET operation.
     * 
     */
    public void performGet(final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders) {
        this.performRequest(url, user, pass, additionalHeaders, null, HTTPRequestHelper.GET_TYPE);
    }

    /**
     * Perform an HTTP POST operation.
     * 
     */
    public void performPost(final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders, final Map<String, String> params) {
        this.performRequest(url, user, pass, additionalHeaders, params, HTTPRequestHelper.POST_TYPE);
    }

    /**
     * Private heavy lifting method that performs GET or POST with supplied url,
     * user, pass, data, and headers.
     * 
     * 
     * @param url
     * @param user
     * @param pass
     * @param additionalHeaders
     * @param params
     * @param requestType
     */
    private void performRequest(final String url, final String user, final String pass,
            final Map<String, String> additionalHeaders, final Map<String, String> params, final int requestType) {

        Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " making HTTP request to url - " + url);

        // establish HttpClient
        DefaultHttpClient client = new DefaultHttpClient();

        // create a response specifically for errors
        BasicHttpResponse errorResponse = new BasicHttpResponse(new ProtocolVersion("HTTP_ERROR", 1, 1), 500, "ERROR");

        // add user and pass to client credentials if present
        if ((user != null) && (pass != null)) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG
                    + " user and pass present, adding credentials to request");
            client.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        }

        // process additional headers using request interceptor
        final Map<String, String> headers = new HashMap<String, String>();
        if ((additionalHeaders != null) && (additionalHeaders.size() > 0)) {
            Log
                    .d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG
                            + " additional headers present, adding to request");
            headers.putAll(additionalHeaders);
        }
        if (requestType == HTTPRequestHelper.POST_TYPE) {
            headers.put(HTTPRequestHelper.CONTENT_TYPE, HTTPRequestHelper.FORM_ENCODED);
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
        if (requestType == HTTPRequestHelper.POST_TYPE) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest POST");

            HttpPost method = new HttpPost(url);

            // data - name/value params 
            // (mutli part form data not supported with helper, yet, but HttpClient can handle it fine)
            List<NameValuePair> nvps = null;
            if ((params != null) && (params.size() > 0)) {
                Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " params present, adding to request");
                nvps = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    nvps.add(new BasicNameValuePair(key, params.get(key)));
                }
            }

            if (nvps != null) {
                try {
                    method.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                }
            }

            try {
                client.execute(method, this.responseHandler);
                Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " request completed");
            } catch (Exception e) {
                Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                errorResponse.setReasonPhrase(e.getMessage());
                try {
                    this.responseHandler.handleResponse(errorResponse);
                } catch (Exception ex) {
                    Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, ex);
                }
            }
        } else if (requestType == HTTPRequestHelper.GET_TYPE) {
            Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " performRequest GET");
            HttpGet method = new HttpGet(url);

            try {
                client.execute(method, this.responseHandler);
                Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " request completed");
            } catch (Exception e) {
                Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                errorResponse.setReasonPhrase(e.getMessage());
                try {
                    this.responseHandler.handleResponse(errorResponse);
                } catch (Exception ex) {
                    Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, ex);
                }
            }
        }
    }
    
    /**
     * Static utility method to create a default ResponseHandler that sends a
     * Message to the passed in Handler with the response as a String, after the
     * request completes.
     * 
     * @param handler
     * @return
     */
    public static ResponseHandler<String> getResponseHandlerInstance(final Handler handler) {
        final ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            public String handleResponse(final HttpResponse response) {
                Message message = new Message();
                Bundle bundle = new Bundle();
                StatusLine status = response.getStatusLine();
                Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " statusCode - " + status.getStatusCode());
                Log.d(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG + " statusReasonPhrase - "
                        + status.getReasonPhrase());
                HttpEntity entity = response.getEntity();
                String result = null;
                if (entity != null) {
                    try {
                        result = StringUtils.inputStreamToString(entity.getContent());
                        bundle.putString("RESPONSE", result);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    } catch (IOException e) {
                        Log.e(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG, e);
                        bundle.putString("RESPONSE", "Error - " + e.getMessage());
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                } else {
                    Log.w(Constants.LOGTAG, " " + HTTPRequestHelper.CLASSTAG
                            + " empty response entity, HTTP error occurred");
                    bundle.putString("RESPONSE", "Error - " + response.getStatusLine().getReasonPhrase());
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
                return result;
            }
        };
        return responseHandler;
    }
}