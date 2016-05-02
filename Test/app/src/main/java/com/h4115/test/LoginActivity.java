package com.h4115.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    protected TextView sut; //Sign up text
    protected Button cnb; //Connection button
    protected EditText eun; //EditText username
    protected EditText epw; //EditText password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eun = (EditText) findViewById(R.id.username);
        epw = (EditText) findViewById(R.id.password);

        sut = (TextView) findViewById(R.id.sign_up_text);
        sut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });

        cnb = (Button) findViewById(R.id.connection_button);
        cnb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eun.getText().toString().length() < 1 || epw.getText().toString().length() < 1){
                    Toast.makeText(LoginActivity.this, "Entrez un nom d'utilisateur et un mot de passe", Toast.LENGTH_LONG).show();
                    return;
                }

                if(enableInternet()) {
                    new GetUserTask().execute("https://goonapp-dev.herokuapp.com/connection" +
                            "?username=" + eun.getText().toString() +
                            "&password=" + epw.getText().toString());
                }
            }
        });

        eun.clearFocus();
        cnb.requestFocus();
    }

    public boolean enableInternet() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(!(netInfo != null && netInfo.isConnectedOrConnecting())) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Cette application a besoin d'une connexion internet, voulez vous l'activer maintenant ?").setCancelable(false)
                    .setPositiveButton("Oui",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                    startActivity(i);
                                }
                            }
                    ).setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return false;
        }
        return true;
    }
}