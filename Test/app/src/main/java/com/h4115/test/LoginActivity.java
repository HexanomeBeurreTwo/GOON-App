package com.h4115.test;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    protected TextView sut; //Sign up text
    protected Button cnb; //Connection button
    protected EditText eun; //EditText username
    protected EditText epw; //EditText password
    final DBHandler dbHandler = new DBHandler(this);

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
                    new JSONTask().execute("https://goonapp-dev.herokuapp.com/connection" +
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

    private class JSONTask extends AsyncTask<String,String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String data = buffer.toString();

                Boolean resetted =  dbVerification(data);
                if (resetted== true ) {
                    data="true";
                } else {
                    data = "false";
                }

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private Boolean dbVerification(String data) throws JSONException {
        Boolean resetted = false;
        JSONObject jsonObject = new JSONObject(data);
        Integer id =Integer.parseInt(jsonObject.optString("id").toString());
        String userName = jsonObject.optString("username");
        String email = jsonObject.optString("email");
        String password = jsonObject.optString("password");
        Integer age = Integer.parseInt((jsonObject.opt("age").toString()));
        String citizen = jsonObject.optString("citizen");
        String tags = jsonObject.optString("tags");
        dbHandler.addUser(new User(id, userName, password, email, age, citizen, tags));
        return resetted;
    }
}