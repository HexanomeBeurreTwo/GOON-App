package com.example.goon.connection;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class Connection extends AppCompatActivity {

    private Button buttonConnection;
    private TextView text;
    private EditText username;
    private EditText password;
    private TextView loginLink;
    final DBHandler dbHandler = new DBHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text= (TextView)findViewById(R.id.textView);
        buttonConnection= (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        loginLink= (TextView) findViewById(R.id.link_login);


        buttonConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkInternetConnection()) {

                    if(validate()) {
                        new  JSONTask().execute("https://goonapp-dev.herokuapp.com/connection?username="+(((EditText) findViewById(R.id.userName))).getText().toString()+"&password="+((EditText) findViewById(R.id.password)).getText().toString());

                    }else{

                        Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SingUp.class);
                startActivityForResult(intent,0);
            }
        });
    }

    private boolean validate(){
        if(username.getText().toString().trim().equals(""))
            return false;
       else if(password.getText().toString().trim().equals(""))
            return false;
        else
            return true;    }



    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec =(ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if ( connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
          //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        }else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
           Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    public class JSONTask extends AsyncTask<String,String, String> {

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
            text.setText(result);
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
        //if the user is using the application with another account, the database is resetted
      /*  if (dbHandler.countUsers()==true){
            dbHandler.addUser(new User(id, userName, password, email, age, citizen, tags));
            resetted=true;

        } else if(!((dbHandler.getUser(id).getUserId()).equals(id))){
            dbHandler.addUser(new User(id, userName, password, email, age, citizen, tags));
            resetted= true;
        }
        */
        dbHandler.addUser(new User(id, userName, password, email, age, citizen, tags));
        return resetted;
    }

}
