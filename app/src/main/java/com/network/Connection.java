package com.network;


import android.content.Context;
import android.os.StrictMode;
import android.util.Base64;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


/**
 * Created by carloleonardi on 06/12/13.
 */
public class Connection {

    public static final String userIdentifier = "sportereduser";
    public static final String userSecret = "sporteredAPIkey";

    public static int deleteResource(String url){
        System.out.println("Send request DELETE to address:"+url);
        HttpClient httpclient = (new Connection()).getNewHttpClient();
        HttpDelete request = new HttpDelete(url);
        try {
            request.addHeader("accept", "application/json");
            request.addHeader("auth","UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");
            String credentials = userIdentifier + ":" + userSecret;
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            request.addHeader("accept", "application/json");
            request.setHeader("Accept-language", "it-IT");
            request.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(request);
            return response.getStatusLine().getStatusCode();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return 404;
        }

    }

    public static JSONObject putResource(String url, JSONObject jsonObject){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpclient = (new Connection()).getNewHttpClient();
        HttpPut request = new HttpPut(url);
        HttpResponse response = null;
        try {
            request.addHeader("auth","UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");
            StringEntity s = new StringEntity(jsonObject.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            request.setEntity(s);
            request.addHeader("accept", "application/json");
            String credentials = userIdentifier + ":" + userSecret;
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            response = httpclient.execute(request);
            //jsonObject = new JSONObject(response.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject postResource(String url, JSONObject jsonObject){
        HttpClient httpclient = (new Connection()).getNewHttpClient();
        System.out.println("url:"+url);
        HttpPost request = new HttpPost(url);
        HttpResponse response = null;
        try {
            request.addHeader("auth","UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");
            StringEntity s = new StringEntity(jsonObject.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            request.setEntity(s);
            request.addHeader("accept", "application/json");
            String credentials = userIdentifier + ":" + userSecret;
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
            request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            response = httpclient.execute(request);
            jsonObject = new JSONObject(response.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONArray getJsonArray(String URL) throws Exception{
        Object result = "";
        JSONArray jsonArray=null;
        String deviceId = "111";
        HttpClient httpclient = (new Connection()).getNewHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpGet request = new HttpGet(URL);
        request.addHeader("deviceId", deviceId);

        /* Aggiungo all'header le informazioni di base per l'autenticazione */
        String credentials = userIdentifier + ":" + userSecret;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        request.addHeader("auth","UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");
        ResponseHandler<String> handler = new BasicResponseHandler();
        result = httpclient.execute(request, handler);
        httpclient.getConnectionManager().shutdown();
        System.out.println(result);
        jsonArray=new JSONArray(result.toString());

        return jsonArray;
    }

    public static JSONObject putJson(String URL){
        Object result = "";
        JSONObject jsonObject=null;
        String deviceId = "111";
        HttpClient httpclient = (new Connection()).getNewHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpPut request = new HttpPut(URL);
        request.addHeader("deviceId", deviceId);

        /* Aggiungo all'header le informazioni di base per l'autenticazione */
        String credentials = userIdentifier + ":" + userSecret;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        request.addHeader("auth","UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");

        ResponseHandler<String> handler = new BasicResponseHandler();
        ArrayList<Object> listItems = new ArrayList<Object>();
        try {
            result = httpclient.execute(request, handler);
            httpclient.getConnectionManager().shutdown();
            jsonObject=new JSONObject(result.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getJson(String URL) throws Exception{
        System.out.println("Send request GET to address:"+URL);
        Object result = "";
        JSONObject jsonObject=null;
        String deviceId = "111";
        HttpClient httpclient = (new Connection()).getNewHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpGet request = new HttpGet(URL);
        request.addHeader("deviceId", deviceId);

        /* Aggiungo all'header le informazioni di base per l'autenticazione */
        String credentials = userIdentifier + ":" + userSecret;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        request.addHeader("auth","UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");

        ResponseHandler<String> handler = new BasicResponseHandler();
        result = httpclient.execute(request, handler);
        httpclient.getConnectionManager().shutdown();
        jsonObject=new JSONObject(result.toString());
        return jsonObject;
    }


    public static JSONObject postJson(String URL,JSONObject jsonObject, Context context) {

        /*RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Response:", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response:", error.toString());

                    }
                }
        );

        requestQueue.add(jsonRequest);
        return jsonObject;*/


        /*HttpClient httpclient = (new Connection()).getNewHttpClient();
        System.out.println("url:"+url);
        HttpPost request = new HttpPost(url);
        HttpResponse response;
        try {
            StringEntity s = new StringEntity(jsonObject.toString());
            s.setContentEncoding("UTF-8");
            s.setContentType("application/json");
            request.setEntity(s);
            request.addHeader("accept", "application/json");
            request.setHeader("Content-type", "application/json");
            response = httpclient.execute(request);
            jsonObject = new JSONObject(response.toString());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject;*/


        System.out.println("Send request POST to address:" + URL);

        HttpClient httpclient = (new Connection()).getNewHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpPost request = new HttpPost(URL);

        try {
            // request.addHeader("auth", "UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");
            StringEntity stringEntity = new StringEntity(jsonObject.toString());
            request.setEntity(stringEntity);
            request.setHeader("Accept", "application/json");
            request.setHeader("Accept-language", "it-IT");
            request.setHeader("Content-type", "application/json");
            HttpResponse result = httpclient.execute(request);
            String finalResult = EntityUtils.toString(result.getEntity());
            httpclient.getConnectionManager().shutdown();
            System.out.println(finalResult);
            jsonObject = new JSONObject(finalResult);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static String postString(String url, String code, TextView tv) {


        //System.out.println("Send request POST to address:" + url);

        HttpClient httpclient = (new Connection()).getNewHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpPost request = new HttpPost(url);

        try {
            // request.addHeader("auth", "UmVr4&n%$$t35MvDe2E*tYSe@tP4MmxD%4Sw8x-nhWJvybYCaP");
            // StringEntity stringEntity = new StringEntity(code);
            // tv.append("stringEntity:", stringEntity.getContent().);
            // request.setEntity(stringEntity);

            // request.setHeader("Accept", "application/json");
            // request.setHeader("Accept-language", "it-IT");
            // request.setHeader("Content-type", "application/json");

            //String json = "{\"id\":1, \"name\":\"John\"}";
            //StringEntity entity = new StringEntity(json);
            //tv.append("entity:" + entity);
            //request.setEntity(entity);

            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");
            HttpResponse result = httpclient.execute(request);
            tv.append("result:" + result);
            String finalResult = EntityUtils.toString(result.getEntity());
            httpclient.getConnectionManager().shutdown();
            return result.toString() + "|||" + finalResult;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return code;
    }

    public static JSONObject postJsonArray(String url,JSONArray jsonArrayToPost){
        System.out.println("Send request POST to address:"+url);
        JSONObject jsonObject = null;
        HttpClient httpclient = (new Connection()).getNewHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpPost request = new HttpPost(url);
        try {
            StringEntity s = new StringEntity(jsonArrayToPost.toString());
            request.setEntity(s);
            request.setHeader("Accept", "application/json");
            request.setHeader("Accept-language", "it-IT");
            request.setHeader("Content-type", "application/json");
            HttpResponse result = httpclient.execute(request);
            String finalResult = EntityUtils.toString(result.getEntity());
            httpclient.getConnectionManager().shutdown();
            System.out.println(finalResult);
            jsonObject = new JSONObject(finalResult);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static String excutePost(String targetURL, JSONObject urlParameters) {
        URL url;
        HttpURLConnection connection = null;
        try {

            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/raw");
            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.toString().getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            OutputStream out = new BufferedOutputStream(connection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(String.valueOf(urlParameters.toString().getBytes("UTF-8")));
            out.close();
            //connection.disconnect();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws Exception {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] { tm }, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public enum TypeOfConnection{PUT, POST, DELETE}



}
