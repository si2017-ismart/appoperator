package com.weguideoperator;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
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
 * contient toute les méthodes pour communiquer avec le WebService. (requête POST et GET)
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

    private ArrayList<String> postArrayResult;

    public ArrayList<String> checkLogins(Context context, final String password){

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
            postArrayResult = new ArrayList<String>();
            JSONObject jsonObject = new JSONObject(postResult);
            String idetablissement = jsonObject.getString("id_etablissement");
            String intervenant = jsonObject.getString("id_intervenant");
            postArrayResult.add(idetablissement);
            postArrayResult.add(intervenant);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return postArrayResult;
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
            Log.d("debug","parameter :"+parameters.toString());
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(parameters.toString());

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();
            Log.d("debug","responseCode"+responseCode);
            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.d("debug","ifOK");
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    postResult+=line;
                    Log.d("resuslt",postResult);
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


    public ArrayList<String> sessionAvailable(String idEtablissement){

        String sessionID, sessionDate, userName, userGender, userType, beaconID, beaconName, positionX, positionY;
        ArrayList<String> resultArray = new ArrayList<String>();
        urlStr = ip+":3000/api/etablissements/operator_ack/"+idEtablissement;
        final ArrayList<String> beaconResult = new ArrayList<String>();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Get(beaconResult);
            }
        });

        t1.start();
        try{
            t1.join();
            String jsonstr = beaconResult.get(0);
            if(!jsonstr.equals("Nothing")) {

                JSONObject jsonObject = new JSONObject(jsonstr);
                JSONObject jsonObject1 = jsonObject.getJSONArray("sessions").getJSONObject(0);
                sessionID   = jsonObject1.getString("id");
                sessionDate = jsonObject1.getString("date");
                userName    = jsonObject1.getJSONObject("user").getString("nom");
                userGender  = jsonObject1.getJSONObject("user").getString("sexe");
                userType    = jsonObject1.getJSONObject("user").getString("type");
                beaconID    = jsonObject1.getJSONObject("beacon").getString("id");
                beaconName  = jsonObject1.getJSONObject("beacon").getString("nom");
                positionX   = jsonObject1.getJSONObject("beacon").getJSONObject("position").getString("x");
                positionY   = jsonObject1.getJSONObject("beacon").getJSONObject("position").getString("y");


                resultArray.add(sessionID);
                resultArray.add(sessionDate);
                resultArray.add(userName);
                resultArray.add(userGender);
                resultArray.add(userType);
                resultArray.add(beaconID);
                resultArray.add(beaconName);
                resultArray.add(positionX);
                resultArray.add(positionY);

            }else{
                resultArray.add("nothing");
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resultArray;
    }

    public void disponible(Context context,final String tokenSession){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String login = sharedPreferences.getString(keyLogin,null);

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject parameters   = new JSONObject();
                try {
                    parameters.put("id_intervenant", login);
                    parameters.put("token", tokenSession);
                    urlStr = ip+":3000/api/etablissements/intervenants/takeSession";
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

    }

    public boolean finishSession(final String token){

        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                JSONObject parameters   = new JSONObject();
                try {
                    parameters.put("token", token);
                    urlStr = ip+":3000/api/etablissements/intervenants/endSession";
                    //addLogIntervention(token);
                    Log.d("debug","send");
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
        Log.d("debug", "postResult" + postResult);
        if(postResult.equals("true")){
            return true;
        }
        return false;
    }


    public void addLogIntervention(final String token){

        JSONObject parameters   = new JSONObject();
        try {
            parameters.put("token", token);
            urlStr = ip+":3000/api/interventions/add";
            sendPost(parameters);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
