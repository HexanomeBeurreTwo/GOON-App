package com.h4115.test;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class SubscribeChannels extends AppCompatActivity {

    protected TextView cct; // Choose channels text view
    protected ListView clv; //Channel list view
    protected Button scb; //Subscribe channels button

    protected final DBHandler dbHandler = new DBHandler(this);
    protected ChannelAdapter cna;
    protected ArrayList<Boolean> booleansChannels = new ArrayList<>();
    protected ArrayList<Channel> channels = new ArrayList<>();

    protected User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subscribe_channels);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        cct = (TextView) findViewById(R.id.text);
        clv = (ListView) findViewById(R.id.list_view);
        scb = (Button) findViewById(R.id.subscribe_channels_button);

        String result = getIntent().getStringExtra("result");

        Intent myIntent = getIntent();
        Integer userId = myIntent.getIntExtra("id", 0);
        Integer age = myIntent.getIntExtra("age", 20);
        String username = myIntent.getStringExtra("username");
        String password= myIntent.getStringExtra("password");
        String email= myIntent.getStringExtra("email");
        String citizen= myIntent.getStringExtra("citizen");
        String tags= myIntent.getStringExtra("tags");

        user = new User(userId, username, password, email, age, citizen, tags);

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

            for(Channel cnl : channels){
                if (user.getTags().contains(cnl.getName()))
                {
                    //System.out.println(user.getTags() + " contient bien : " + cnl.getName());
                    booleansChannels.add(true);
                }
                else {
                    //System.out.println(user.getTags() + " ne contient pas : " + cnl.getName());
                    booleansChannels.add(false);
                }
            }

            //System.out.println("Etat de la liste de booleans avant : ");
            //for(Boolean bl : booleansChannels) System.out.println(bl + ", ");

            //System.out.println("Etat de la liste de booleans apres : ");
        }

        cna = new ChannelAdapter(getApplicationContext(), channels, booleansChannels);
        clv.setAdapter(cna);



        scb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                booleansChannels = cna.subscriptions;
                user.setTags(new String());
                for(int i = 0; i < cna.getCount(); i++){
                    if(booleansChannels.get(i)) user.setTags(user.getTags() + channels.get(i).getName() + ", ");
                }
                dbHandler.addUser(user);
                SubscribeChannels.this.finish();
            }
        });
    }
}

