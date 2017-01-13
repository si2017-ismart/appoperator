package com.weguideoperator;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

/**
 * Created by Aline on 12/01/2017.
 */
public class AccepterMission extends AppCompatActivity implements View.OnClickListener{

    private Button buttonFinish;
    private ArrayList<String> sessionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accept_mission);
        buttonFinish = (Button) findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(this);
        sessionInfo = (ArrayList<String>) getIntent().getExtras().getSerializable("sessionInfo");

    }

    private void addNotification() {
        Intent resultIntent = new Intent(this, AccepterMission.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_operator)
                .setContentTitle("WeGuide")
                .setContentText("Changement Position au Quai 3")
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());


    }



    @Override
    public void onClick(View v) {
            WebService service = new WebService();
            int id = v.getId();
            switch (id){
                case R.id.buttonFinish:
                    service.finishSession(sessionInfo.get(0));
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    break;
            }

    }
}
