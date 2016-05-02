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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    protected TextView lit; //Login text
    protected Button cpb; //Create profile button
    protected EditText eun; //EditText username
    protected EditText epw; //EditText password
    protected EditText ema; //EditText mail address
    protected EditText eag; //EditText age

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eun = (EditText) findViewById(R.id.username);
        epw = (EditText) findViewById(R.id.password);
        ema = (EditText) findViewById(R.id.email);
        eag = (EditText) findViewById(R.id.age);

        lit = (TextView) findViewById(R.id.log_in_text);
        lit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.this.finish();
            }
        });

        cpb = (Button) findViewById(R.id.create_profile_button);
        cpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(eun.getText().toString().length() < 1 ||
                        epw.getText().toString().length() < 1 ||
                        ema.getText().toString().length() < 1 ||
                        eag.getText().toString().length() < 1){
                    Toast.makeText(SignUpActivity.this, "Veuillez remplir toutes les lignes", Toast.LENGTH_LONG).show();
                    return;
                }
                if(enableInternet()){
                    PostUserTask asyncRequest = new PostUserTask(eun.getText().toString(), epw.getText().toString(), ema.getText().toString(), eag.getText().toString());
                    asyncRequest.execute("https://goonapp-dev.herokuapp.com/user");
                }
            }
        });

        eun.clearFocus();
        cpb.requestFocus();
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
