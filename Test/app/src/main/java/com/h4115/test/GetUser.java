package com.h4115.test;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class GetUser extends AsyncTask<String,String, String> {

    protected LoginActivity logInActivity;
    protected SignUpActivity signUpActivity;
    protected SplashScreen splashScreen;

    public GetUser(LoginActivity logInActivity, SignUpActivity signUpActivity, SplashScreen splashScreen){
        this.logInActivity = logInActivity;
        this.signUpActivity = signUpActivity;
        this.splashScreen = splashScreen;
    }

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

            if(this.logInActivity != null) logInActivity.addUserToDB(data);
            if(this.splashScreen != null);
            if(this.signUpActivity != null) signUpActivity.addUserToDB(data);

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