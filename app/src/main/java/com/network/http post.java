//
//
//private void PostData() {
//        String IPPORT = "www.android.com"; // or example 192.168.1.5:80
//        URL url = new URL("http://"+IPPORT+"/");
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        try {
//        urlConnection.setDoOutput(true);
//        urlConnection.setChunkedStreamingMode(0);
//
//        OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//        writeStream(out);
//
//        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//        readStream(in);
//
//        }
//        finally {
//        urlConnection.disconnect();
//        }
//        }
//
//
//private void writeStream(OutputStream out){
//        String output = "Hello world";
//
//        out.write(output.getBytes());
//        out.flush();
//        }






/*
private StringBuffer request(String urlString, JSONObject jsonObj) {
        // TODO Auto-generated method stub

        StringBuffer chaine = new StringBuffer("");
        try {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "");
        connection.setRequestMethod("POST");
        connection.setDoInput(true);
        connection.connect();

        Log.d("REQUESTOUTPUT", "requesting");
        byte[] b = jsonObj.toString().getBytes();
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(b);


        InputStream inputStream = connection.getInputStream();

        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        while ((line = rd.readLine()) != null) {
        chaine.append(line);
        }

        } catch (IOException e) {
        // writing exception to log
        e.printStackTrace();
        }

        return chaine;
        }*/






/*String urlParameters  = "param1=a&param2=b&param3=c";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = "http://example.com/index.php";
        URL    url            = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url.openConnection();
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
        wr.write( postData );
        }*/



/*class Retrievedata extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try{
            //Your code
        }
        return null;
    }
}*/





/*Thread thread = new Thread(new Runnable() {

@Override
public void run() {
        try  {
            //Your code goes here
            } catch (Exception e) {
                e.printStackTrace();
                }
            }
        });

        thread.start(); */

