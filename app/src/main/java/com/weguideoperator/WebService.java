package com.weguideoperator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Aline on 12/01/2017.
 */
public class WebService {

    private static String ip = "http://192.168.12.228";
    private static String urlStr = "";
    private static final char PARAMETER_DELIMITER = '&';
    private static final char PARAMETER_EQUALS_CHAR = '=';
    private String postResult = "";
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String keyEtablissement = "keyEtablissement";
    public static final String keyLogin = "keyLogin";



    public boolean checkLogins(Context context, final String password){

        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String etablissement = sharedPreferences.getString(keyEtablissement, null);
        final String login = sharedPreferences.getString(keyLogin,null);
        final ArrayList<String> list = new ArrayList<String>();
        boolean loginValid = false;
        Log.d("checkLogins","methode appele");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("nom_etablissement", etablissement);
                parameters.put("identifiant", login);
                parameters.put("password", password);
                urlStr = ip+":3000/api/etablissements/intervenants/login";
                sendPost(parameters);
            }
        });

        t1.start();

        try{
            t1.join();
            String result = list.get(0);
            if(result.equals("true")){
                loginValid = true;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return loginValid;
    }

    private void Get(ArrayList<String> arraylist){
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urlStr);

            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();
            result = "";
            while (data != -1) {
                char current = (char) data;
                data = isw.read();
                result+= current;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        arraylist.add(result);
    }

    private void sendPost(Map<String, String> parameters){

        try {
            URL urlToRequest = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String postParameters = createQueryStringForParameters(parameters);
            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);

            //send the POST
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();

            String str = readInputStreamToString(urlConnection);
            postResult = str;
            Log.d("post response", str);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private String readInputStreamToString(HttpURLConnection connection) {
        String result = null;
        StringBuffer sb = new StringBuffer();
        InputStream is = null;

        try {
            is = new BufferedInputStream(connection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
        }
        catch (Exception e) {
            Log.i("webservcie", "Error reading InputStream");
            result = null;
        }
        finally {
            if (is != null) {
                try {
                    is.close();
                }
                catch (IOException e) {
                    Log.i("webservice", "Error closing InputStream");
                }
            }
        }

        return result;
    }

    private static String createQueryStringForParameters(Map<String, String> parameters) {
        StringBuilder parametersAsQueryString = new StringBuilder();
        if (parameters != null) {
            boolean firstParameter = true;

            for (String parameterName : parameters.keySet()) {
                if (!firstParameter) {
                    parametersAsQueryString.append(PARAMETER_DELIMITER);
                }

                try {
                    parametersAsQueryString.append(parameterName)
                            .append(PARAMETER_EQUALS_CHAR)
                            .append(URLEncoder.encode(parameters.get(parameterName), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                firstParameter = false;
            }
        }
        return parametersAsQueryString.toString();
    }

}
