package com.weguideoperator;

import android.app.Activity;
import android.os.Bundle;

import java.util.Timer;

/**
 * Created by jadhaddad on 1/12/17.
 */

public class NoDemands extends Activity {
    private WebService webService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aucune_demande);
        webService = new WebService();
    }

    public void setTimer() {
        Timer timer = new Timer(true);
        SessionCheckTimer timerTask = new SessionCheckTimer(webService, this, timer);
        //running timer task as daemon thread
        timer.scheduleAtFixedRate(timerTask, 0, 5 * 1000);
    }
}
