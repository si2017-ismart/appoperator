package com.weguideoperator;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    SharedPreferences sharedPreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String keyEtablissement = "keyEtablissement";
    public static final String keyLogin = "keyLogin";

    private EditText ETEtablisement;
    private EditText ETLogin;
    private EditText ETPassword;

    private Button BLoginup;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.getString(keyLogin,null)!=null) {
            Toast.makeText(this, "already login", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, NoDemands.class);
            startActivity(intent);
            finish();
        }

        ETEtablisement = (EditText)findViewById(R.id.etablissement);
        ETLogin  = (EditText)findViewById(R.id.login);
        ETPassword = (EditText) findViewById(R.id.password);

        BLoginup = (Button)findViewById(R.id.loginButton);
        BLoginup.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(checkEmptyFields()){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Send data to server ....
                        //
                        // ------------------------
                        if(saveData()){
                            Log.d("dkdll","okk");
                            Intent intent = new Intent(MainActivity.this, NoDemands.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setTitle("Wrong Logins");
                            alert.setMessage("Try again");
                            alert.setPositiveButton("OK", null);
                            alert.setCancelable(true);
                            alert.show();
                        }
                        // remove later
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }

                }
            }).start();
        }else{
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("Empty Fields");
            alert.setMessage("Please fill in all the fields to proceed");
            alert.setPositiveButton("OK", null);
            alert.setCancelable(true);
            alert.show();

        }
    }

    private boolean checkEmptyFields(){
        boolean allFilled;

        if(ETEtablisement.getText().toString().isEmpty() ||
                ETLogin.getText().toString().isEmpty() ||
                ETPassword.getText().toString().isEmpty()){
            allFilled = false;
        }else{
            allFilled = true;
        }
        return allFilled;
    }

    private boolean saveData(){
        String fetablissement, login;
        fetablissement = ETEtablisement.getText().toString();
        login = ETLogin.getText().toString();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keyEtablissement, fetablissement);
        editor.putString(keyLogin, login);
        editor.commit();

        WebService service = new WebService();
        ArrayList<String> ids = service.checkLogins(this,ETPassword.getText().toString());
        if( ids.size() != 0) {
            editor.putString(keyEtablissement, ids.get(0));
            editor.putString(keyLogin, ids.get(1));
            editor.commit();
            return true;
        }
        return false;


    }


}
