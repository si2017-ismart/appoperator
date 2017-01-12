package com.weguideoperator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Aline on 12/01/2017.
 */
public class DemandeAide  extends AppCompatActivity implements View.OnClickListener{

    private ImageButton buttonHelp;
    private ImageButton buttonCantHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demande_aide);

        buttonHelp = (ImageButton) findViewById(R.id.buttonHelp);
        buttonCantHelp = (ImageButton) findViewById(R.id.buttonCantHelp);

        buttonHelp.setOnClickListener(this);
        buttonCantHelp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.buttonHelp:
                break;
            case R.id.buttonCantHelp:
                break;
        }
    }
}
