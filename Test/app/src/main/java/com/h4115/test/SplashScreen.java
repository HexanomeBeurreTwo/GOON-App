package com.h4115.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

public class SplashScreen extends Activity {

    protected final int SPLASH_DISPLAY_LENGTH = 1500;
    protected final DBHandler dbHandler = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                User user = dbHandler.getUser();

                try {
                    System.out.println(user.toString());
                }
                catch (Exception e){
                    System.out.println(e.toString());
                }

                if(user != null) {
                    GetUser getUser = new GetUser();
                    getUser.execute("https://goonapp-dev.herokuapp.com/connection" + "?username=" + user.getUsername() + "&password=" + user.getPassword());
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    SplashScreen.this.startActivity(intent);
                    SplashScreen.this.finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    protected void EndOfTaskHandler(boolean success, User user){

        if(!success){
            Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }
        else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            intent.putExtra("id", user.getUserId());
            intent.putExtra("username",user.getUsername());
            intent.putExtra("password", user.getPassword());
            intent.putExtra("email", user.getEmail());
            intent.putExtra("age", user.getAge());
            intent.putExtra("citizen", user.getCitizen());
            intent.putExtra("tags", user.getTags());
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }
    }

    private class GetUser extends AsyncTask<String,String, String> {

        protected User connectedUser;
        protected boolean success = false;

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

                String line;
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String data = buffer.toString();

                JSONObject jsonObject = new JSONObject(data);
                Integer id = Integer.parseInt(jsonObject.optString("id").toString());
                String userName = jsonObject.optString("username");
                String email = jsonObject.optString("email");
                String password = jsonObject.optString("password");
                Integer age = Integer.parseInt((jsonObject.opt("age").toString()));
                String citizen = jsonObject.optString("citizen");
                String tags = jsonObject.optString("tags");
                connectedUser = new User(id, userName, password, email, age, citizen, tags);
                dbHandler.addUser(connectedUser);
                this.success = true;

            } catch (MalformedURLException e) {
                this.success = false;
            } catch (IOException e) {
                this.success = false;

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
                    this.success = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            EndOfTaskHandler(success, connectedUser);
        }
    }
}
