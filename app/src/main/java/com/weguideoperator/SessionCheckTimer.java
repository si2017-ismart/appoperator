package com.weguideoperator;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jadhaddad on 1/12/17.
 */

public class SessionCheckTimer extends TimerTask{


    private WebService service;
    private String session;
    private Context context;
    private Timer timer;

    SessionCheckTimer(WebService s, Context c, Timer timer){
        service = s;
        context = c;
        this.timer = timer;
    }

    @Override
    public void run() {
        System.out.println("Timer task started at:");
        completeTask();
        System.out.println("Timer task finished at:");
    }

    private void completeTask() {
//        try {
//            Log.d("ji", "completeTask:jkj ");
//            if( !service.checkToken(token)){
//                Log.d("ji", ""+service.checkToken(token));
//                timer.cancel();
//                Intent intent = new Intent(context, DemandeAide.class);
//                context.startActivity(intent);
//            }
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }



}
