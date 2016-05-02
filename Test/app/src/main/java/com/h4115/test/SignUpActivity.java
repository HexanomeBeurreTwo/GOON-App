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
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class SignUpActivity extends AppCompatActivity {

    protected TextView lit; //Login text
    protected Button cpb; //Create profile button
    protected EditText eun; //EditText username
    protected EditText epw; //EditText password
    protected EditText ema; //EditText mail address
    protected EditText eag; //EditText age

    protected JSONObject jsonObject;
    protected final DBHandler dbHandler = new DBHandler(this);

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

    public void addUserToDB(String data) throws JSONException {
        JSONObject jsonObject = new JSONObject(data);
        Integer id =Integer.parseInt(jsonObject.optString("id").toString());
        String userName = jsonObject.optString("username");
        String email = jsonObject.optString("email");
        String password = jsonObject.optString("password");
        Integer age = Integer.parseInt((jsonObject.opt("age").toString()));
        String citizen = jsonObject.optString("citizen");
        String tags = jsonObject.optString("tags");
        dbHandler.addUser(new User(id, userName, password, email, age, citizen, tags));
    }

    private class PostUserTask extends AsyncTask<String, Void, String> {

        protected String username;
        protected String password;
        protected String email;
        protected String age;

        public PostUserTask(String username, String password, String email, String age){
            this.username = username;
            this.password = password;
            this.email = email;
            this.age = age;
        }

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

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
        }

        protected String convertInputStreamToString(InputStream inputStream)throws IOException{

            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line=bufferedReader.readLine())!=null){
                result+=line;
            }
            inputStream.close();
            return result;

        }

        private  String POST(final String url) throws JSONException, IOException {

            InputStream inputStream;
            String result = "";


            try {

                ArrayList<NameValuePair> pairs = new ArrayList<>();

                pairs.add(new BasicNameValuePair("username",username));
                pairs.add(new BasicNameValuePair("email",email));
                pairs.add(new BasicNameValuePair("password",password));
                pairs.add(new BasicNameValuePair("age",age));
                pairs.add(new BasicNameValuePair("citizen","lyon"));

                HttpClient httpClient= new DefaultHttpClient();
                HttpPost httpPost= new HttpPost(url);

                httpPost.setEntity(new UrlEncodedFormEntity(pairs));

                HttpResponse httpResponse= httpClient.execute(httpPost);

                inputStream=httpResponse.getEntity().getContent();

                /*if (inputStream != null) {

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

                    result ="Welcome "+ dbHandler.getUser(id).getUsername().toString()+"!";

                }else{
                    result="Muy mal!";
                }*/
            }
            catch(Exception e){}
            return result;
        }
    }
}


