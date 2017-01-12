package com.weguideoperator;

import android.os.Bundle;
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

        sessionInfo = (ArrayList<String>) getIntent().getExtras().getSerializable("sessionInfo");
    }


    @Override
    public void onClick(View v) {
            WebService service = new WebService();
            int id = v.getId();
            switch (id){
                case R.id.buttonFinish:
                    Log.d("hjk", "onClick: "+sessionInfo);
                    service.finishSession(sessionInfo.get(0));
                    break;
            }

    }
}
