package com.weguideoperator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Aline on 12/01/2017.
 */
public class DemandeAide  extends AppCompatActivity implements View.OnClickListener{

    private ImageButton buttonHelp;
    private ImageButton buttonCantHelp;
    private ArrayList<String> sessionInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demande_aide);


        buttonHelp = (ImageButton) findViewById(R.id.buttonHelp);
        buttonCantHelp = (ImageButton) findViewById(R.id.buttonCantHelp);

        buttonHelp.setOnClickListener(this);
        buttonCantHelp.setOnClickListener(this);
        sessionInfo = (ArrayList<String>) getIntent().getExtras().getSerializable("sessionInfo");

        TextView text = (TextView) findViewById(R.id.needHelp);
        text.setText("On a besoin de vous au "+sessionInfo.get(6));
    }

    @Override
    public void onClick(View view) {
        WebService service = new WebService();
        int id = view.getId();
        switch (id){
            case R.id.buttonHelp:
                service.disponible(this,sessionInfo.get(0));
                Intent intent = new Intent(this, AccepterMission.class);
                Log.d("debug", "sessionInfo 2" + sessionInfo);
                intent.putExtra("sessionInfo", sessionInfo);
                startActivity(intent);
                finish();
                break;
            case R.id.buttonCantHelp:
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
                break;
        }
    }
}
