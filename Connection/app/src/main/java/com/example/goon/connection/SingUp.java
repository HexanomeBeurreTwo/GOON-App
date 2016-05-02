package com.example.goon.connection;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class SingUp extends AppCompatActivity {
    private Button buttonConnection;
    private TextView text;
    private EditText username;
    private EditText password;
    private EditText email;
    private EditText age;
    private String usernameS = "";
    private String passwordS = "";
    private String emailS = "";
    private String ageS = "";
    private RadioButton citizenButton;
    private String citizen = "";
    private User user;
    private Switch aSwitchch;
    private TextView loginLink;


    private JSONObject jsonObject;
    final DBHandler dbHandler = new DBHandler(this);



    private static String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        inputStream.close();
        return result;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text = (TextView) findViewById(R.id.textView);
        buttonConnection = (Button) findViewById(R.id.button);
        username = (EditText) findViewById(R.id.userName);
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);
        age = (EditText) findViewById(R.id.age);
        citizenButton = (RadioButton) findViewById(R.id.citizen);
        loginLink = (TextView) findViewById(R.id.link_login);

        User user = new User();

        buttonConnection.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (checkInternetConnection()) {

                    if (validate()) {
                        new HttpAsyncTask().execute("https://goonapp-dev.herokuapp.com/user");
                    } else {
                        Toast.makeText(getBaseContext(), "Enter some data!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public String POST(final String url) throws JSONException, IOException {
        InputStream inputStream = null;
        String result = "";


        try {

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();

            pairs.add(new BasicNameValuePair("username", username.getText().toString()));
            pairs.add(new BasicNameValuePair("email", email.getText().toString()));
            pairs.add(new BasicNameValuePair("password", password.getText().toString()));
            pairs.add(new BasicNameValuePair("age", age.getText().toString()));
            pairs.add(new BasicNameValuePair("citizen", "lyon"));

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            httpPost.setEntity(new UrlEncodedFormEntity(pairs));

            //httpPost.setHeader("POST","/user");
            //httpPost.setHeader("Content-type", "application/x-www-from-urlencoded");
            // httpPost.setHeader("Accept","application/x-www-form-urlencoded");

            HttpResponse httpResponse = httpClient.execute(httpPost);

            inputStream = httpResponse.getEntity().getContent();

            if (inputStream != null) {

                //extract data  from server's response
                String data = convertInputStreamToString(inputStream);
                jsonObject = new JSONObject(data);
                Integer id =Integer.parseInt(jsonObject.optString("id").toString());
                String userName = jsonObject.optString("username");
                String email = jsonObject.optString("email");
                String password = jsonObject.optString("password");
                Integer age = Integer.parseInt((jsonObject.opt("age").toString()));
                String citizen = jsonObject.optString("citizen");
                String tags = jsonObject.optString("tags");
                dbHandler.addUser(new User(id, userName, password, email, age, citizen, tags));

                text.setText(dbHandler.getUser(id).getUserId().toString());

                result ="Welcome "+ dbHandler.getUser(id).getUsername().toString()+"!";
            } else {
                result = "Muy mal!";
            }
        } catch (Exception e) {

            Log.d("InputStream", e.getLocalizedMessage());

        }
        return result;
    }

    private boolean validate() {
        if (username.getText().toString().trim().equals(""))
            return false;
        else if (password.getText().toString().trim().equals(""))
            return false;
        else if (email.getText().toString().trim().equals(""))
            return false;
        else
            return true;
    }

    private boolean checkInternetConnection() {
        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||

                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
            //  Toast.makeText(this, " Connected ", Toast.LENGTH_LONG).show();
            return true;
        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
            Toast.makeText(this, " Not Connected ", Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            try {
                return POST(urls[0]);

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

        }

    }

}
