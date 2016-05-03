package com.h4115.test;
import android.content.Context;
import android.os.AsyncTask;
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

public class GetUser extends AsyncTask<String,String, String> {

    protected Context context;
    protected DBHandler dbHandler;
    protected User connectedUser;
    protected boolean success = false;

    public GetUser(Context context, DBHandler dbHandler){
        this.context = context;
        this.dbHandler = dbHandler;
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
        if(!success){
            DasAbsichtGeschaftsfuhrer.LaunchLogin(context);
        }
        else {
            DasAbsichtGeschaftsfuhrer.LaunchMain(context, connectedUser);
        }
    }
}