package com.h4115.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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

public class ChannelActivity extends AppCompatActivity {

    protected User user;

    protected TextView cct; // Choose channels text view
    protected ListView clv; //Channel list view
    protected Button scb; //Subscribe channels button

    protected final DBHandler dbHandler = new DBHandler(this);
    protected ChannelAdapter cna;
    protected ArrayList<Channel> channels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent myIntent = getIntent();
        Integer userId = myIntent.getIntExtra("id", 0);
        Integer age = myIntent.getIntExtra("age", 20);
        String username = myIntent.getStringExtra("username");
        String password= myIntent.getStringExtra("password");
        String email= myIntent.getStringExtra("email");
        String citizen= myIntent.getStringExtra("citizen");
        String tags= myIntent.getStringExtra("tags");

        user = new User(userId, username, password, email, age, citizen, tags);

        cct = (TextView) findViewById(R.id.text);
        clv = (ListView) findViewById(R.id.list_view);
        scb = (Button) findViewById(R.id.subscribe_channels_button);

        GetChannels getChannels = new GetChannels();
        getChannels.execute("https://goonapp-dev.herokuapp.com/channel");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == android.R.id.home) onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        ArrayList<Channel> staticChannels = cna.staticChannels;

        if (staticChannels.size() == 0 || staticChannels == null) {
            Toast.makeText(getApplicationContext(), "Souscrivez à au moins un élément et validez", Toast.LENGTH_LONG).show();
            return;
        }
        else ChannelActivity.this.finish();
    }

    protected void EndOfTaskHandler(String result){
        JSONArray jsonObject = null;
        try {
            jsonObject = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {

            for(int i = 0; i < jsonObject.length(); i++) {
                JSONObject c = null;
                try {
                    c = jsonObject.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Integer id = Integer.parseInt(c.optString("id"));

                String name = c.optString("name");

                String description = c.optString("description");

                Channel channel = new Channel(id, name, description);
                channels.add(channel);
            }
        }

        ArrayList<Channel> formerChannels = dbHandler.getChannels();
        cna = new ChannelAdapter(getApplicationContext(), channels, formerChannels);
        clv.setAdapter(cna);

        scb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Channel> dynamicChannels = cna.dynamicChannels;

                if (dynamicChannels.size() == 0 || dynamicChannels == null) {
                    Toast.makeText(getApplicationContext(), "Souscrivez à au moins un élément avant de valider", Toast.LENGTH_LONG).show();
                    return;
                }

                dbHandler.addChannels(dynamicChannels);
                ChannelActivity.this.finish();
            }
        });
    }

    private class GetChannels extends AsyncTask<String,String, String> {

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
                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
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
            EndOfTaskHandler(result);
        }
    }
}
