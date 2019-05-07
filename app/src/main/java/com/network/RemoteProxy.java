package com.network;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.TextView;
import com.example.fridgelockdemo.MainActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RemoteProxy extends AsyncTask {

    private String urlAPI;
    private int fridge;
    private String user;
    private static final Logger LOGGER = Logger.getLogger( MainActivity.class.getName() );


    public RemoteProxy() {
        this.urlAPI = "https://smart-freezers.herokuapp.com/freezers/";
        this.fridge = 1;
        this.user = user;
    }

    public RemoteProxy(int fridge, String user) {
        this.urlAPI = "https://smart-freezers.herokuapp.com/freezers/";
        this.fridge = 1;
        this.user = user;
    }

    public RemoteProxy(String urlAPI, int fridge, String user) {
        this.urlAPI = urlAPI;
        this.fridge = fridge;
        this.user = user;
    }

    public boolean checkLockStatus(){
        boolean lockStatus=true;
        JSONObject response;
        try {
            System.out.println("URL: "+this.urlAPI+this.fridge+"/"+this.user);
            response = Connection.getJson(this.urlAPI+this.fridge+"/"+this.user);
            System.out.println("RESPONSE: "+response);
            lockStatus=response.getBoolean("lock");
        }catch (Exception e){
            System.out.println(e);
        }
        //end test connection
        return lockStatus;
    }

    public boolean setLockStatus(){
        boolean lockStatus=true;
        JSONObject response;
        try {
            //System.out.println("URL: "+this.urlAPI+this.fridge+"/"+this.user);
            response = Connection.getJson(this.urlAPI+this.fridge+"/"+this.user+"/lock");
            System.out.println("RESPONSE: "+response);
            lockStatus=response.getBoolean("lock");
        }catch (Exception e){
            System.out.println(e);
        }
        //end test connection
        return lockStatus;
    }

    public boolean setUnlockStatus(){
        boolean lockStatus=true;
        JSONObject response;
        try {
            response = Connection.getJson(this.urlAPI+this.fridge+"/"+this.user+"/unlock");
            System.out.println("RESPONSE: "+response);
            lockStatus=response.getBoolean("lock");
        }catch (Exception e){
            System.out.println(e);
        }
        return lockStatus;
    }

    public boolean postInventory(JSONObject json){
        boolean lockStatus=true;
        JSONObject response;
        try {
            System.out.println("URL: "+this.urlAPI+this.fridge+"/"+this.user);
            LOGGER.log(Level.INFO, "Send JSONArray: "+json);
            response = Connection.postJson(this.urlAPI+this.fridge+"/products",json);
            System.out.println("RESPONSE: "+response);
            lockStatus=response.getBoolean("lock");
        }catch (Exception e){
            System.out.println(e);
        }
        //end test connection
        return lockStatus;
    }

    public boolean postInventory2 (String code, TextView tv){
        boolean lockStatus=true;
        String response;
        try {
            String URL = this.urlAPI+this.fridge+"/products";
            System.out.println("URL: "+ URL);
            LOGGER.log(Level.INFO, "Send JSONArray: "+code);
        }catch (Exception e){
            System.out.println(e);
        }
        return lockStatus;
    }

    //ADDED BY ANDREA
    @Override
    protected Object doInBackground(Object[] objects) {
        try {


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean postInventory3 (String code, TextView tv){
        boolean lockStatus=true;
        //String response;
        try {
            URL url = new URL(this.urlAPI+this.fridge+"/products");
            StringBuilder postData = new StringBuilder();
            //String code = "";
            String[] a = {"MOUSE, DVD, MONITOR"};
            for (int i=0; i< a.length; i++) {
                code += "\"";
                code += a[i];
                code += "\"";
                if (i != a.length - 1) {
                    code += ",";
                }
            }
            postData.append("{ \"purchase\": [" + code +" ] }");
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (!(conn instanceof HttpURLConnection))
                throw new IOException("Not a HTTP connection");
            if (android.os.Build.VERSION.SDK_INT > 4) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty( "charset", "utf-8");
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects( false );
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.connect();
            conn.getOutputStream().write(postDataBytes);
            Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (int c; (c = in.read()) >= 0;)
                sb.append((char)c);
            String response = sb.toString();
            postData.append("{ \"purchase\": [" + code +" ] }");
            //lockStatus=response.getBoolean("lock");
        } catch (Exception e){
            LOGGER.log(Level.SEVERE, "e: "+e.getCause());
            System.out.println(e);
        }
        //end test connection
        return lockStatus;
    }

    private void writeStream(OutputStream out) throws IOException {
        String output = "Hello world";

        out.write(output.getBytes());
        out.flush();
    }

    public boolean postInventoryArray(JSONArray json, TextView tv){
        boolean lockStatus=true;
        JSONObject response;
        //String response2;
        try {
            String URL = this.urlAPI+this.fridge+"/products";
            LOGGER.log(Level.INFO, "Send JSONArray: "+json);
            response = Connection.postJsonArray(URL, json);
            tv.append(response.toString());
            //response2 = Connection.excutePost(this.urlAPI+this.fridge+"/purchases",json);
            System.out.println("RESPONSE: "+response);
            //lockStatus=response.getBoolean("lock");
        }catch (Exception e){
            System.out.println(e);
        }
        //end test connection
        return lockStatus;
    }

    public String getUrlAPI() {
        return urlAPI;
    }

    public void setUrlAPI(String urlAPI) {
        this.urlAPI = urlAPI;
    }

    public int getFridge() {
        return fridge;
    }

    public void setFridge(int fridge) {
        this.fridge = fridge;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
