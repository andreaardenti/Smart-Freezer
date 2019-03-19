package com.network;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Send_HTTP_Post_Request {
    public static void main(String[] args) {
        try {
            Send_HTTP_Post_Request.call_me();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void call_me() throws Exception {
        URL url = new URL("https://smart-freezers.herokuapp.com/freezers/1/purchases");

        StringBuilder postData = new StringBuilder();
        String finalString = "";
        String[] a = {"Ciao"};
        for (int i=0; i< a.length; i++) {
            finalString += "\"";
            finalString += a[i];
            finalString += "\"";
            if (i != a.length - 1) {
                finalString += ",";
            }
        }
        System.out.println(finalString);


        postData.append("{ \"purchase\": [" + finalString +" ] }");

        byte[] postDataBytes = postData.toString().getBytes("UTF-8");
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
        conn.setDoOutput(true);
        conn.getOutputStream().write(postDataBytes);
        Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (int c; (c = in.read()) >= 0;)
        sb.append((char)c);
        String response = sb.toString();
        System.out.println("RESPONSE: "+response);

    }
}