package com.weguideoperator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

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
        Log.d("checkLogins", "methode appele");
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                    JSONObject parameters   = new JSONObject();
                try {
                    parameters.put("nom_etablissement", etablissement);
                    parameters.put("identifiant", login);
                    parameters.put("password", password);
                    urlStr = ip+":3000/api/etablissements/intervenants/login";
                    sendPost(parameters);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(postResult.equals("true")){
            Log.d("testTrue","ok");
            return true;
        }
        return false;
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

    private void sendPost(JSONObject parameters){
        URL url;
        try {
            url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            postResult="";
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            Log.d("json",parameters.toString());
            writer.write(parameters.toString());

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    postResult+=line;
                    Log.d("testResult",postResult);
                }
            }
            else {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                while ((line=br.readLine()) != null) {
                    postResult+=line;
                    Log.d("resuslt",postResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
