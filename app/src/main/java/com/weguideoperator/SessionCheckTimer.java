package com.weguideoperator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jadhaddad on 1/12/17.
 */

public class SessionCheckTimer extends TimerTask{


    private WebService service;
    private ArrayList<String> sessionInfo;
    private Context context;
    private Timer timer;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String keyEtablissement = "keyEtablissement";
    public static final String keyLogin = "keyLogin";

    private String etablissement;

    SessionCheckTimer(WebService s, Context c, Timer timer){
        service = s;
        context = c;
        this.timer = timer;

        SharedPreferences sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        etablissement = sharedPreferences.getString(keyEtablissement, null);
    }

    @Override
    public void run() {
        System.out.println("Timer task started at:");
        completeTask();
        System.out.println("Timer task finished at:");
    }

    private void completeTask() {
        /*try {
            sessionInfo = service.sessionAvailable(etablissement);
            if(sessionInfo != null){
                timer.cancel();
                Intent intent = new Intent(context, DemandeAide.class);
                intent.putExtra("sessionInfo", sessionInfo);
                context.startActivity(intent);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }



}
