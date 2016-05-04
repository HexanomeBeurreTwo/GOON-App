package com.h4115.test;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class HappeningActivity extends AppCompatActivity {

    protected Happening happening;
    protected TextView happeningName;
    protected TextView happeningDistance;
    protected TextView happeningDescription;
    protected TextView happeningTags;
    protected Button happeningButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happening);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        happeningName = (TextView) findViewById(R.id.happening_name);
        happeningDistance = (TextView) findViewById(R.id.happening_distance);
        happeningDescription = (TextView) findViewById(R.id.happening_description);
        happeningTags = (TextView) findViewById(R.id.happening_tags);
        happeningButton = (Button) findViewById(R.id.happening_button);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent myIntent = getIntent();
        Integer id = myIntent.getIntExtra("id", 0);
        String name = myIntent.getStringExtra("name");
        String description = myIntent.getStringExtra("description");
        Double latitude = myIntent.getDoubleExtra("latitude", 10.0);
        Double longitude = myIntent.getDoubleExtra("longitude", 10.0);
        Boolean temporary = myIntent.getBooleanExtra("temporary", false);
        String tags = myIntent.getStringExtra("tags");
        happening = new Happening(id, name, description, latitude, longitude, temporary, tags);

        Double lastKnownLatitude = myIntent.getDoubleExtra("lastKnownLatitude", 0);
        Double lastKnownLongitude = myIntent.getDoubleExtra("lastKnownLongitude", 0);

        if(lastKnownLatitude != 0 && lastKnownLongitude != 0){
            Location latLng1 = new Location("");
            latLng1.setLatitude(lastKnownLatitude);
            latLng1.setLongitude(lastKnownLongitude);

            Location latLng2 = new Location("");
            latLng2.setLatitude(happening.getLatitude());
            latLng2.setLongitude(happening.getLongitude());

            float distance = (float)(Math.round(latLng1.distanceTo(latLng2)/100)/10);
            happeningDistance.setText("A " + distance + "km de votre position");
        }
        else {
            happeningDistance.setText("•");
        }

        String tagsBeautifier = happening.getTags();
        tagsBeautifier = tagsBeautifier.replace("[", "");
        tagsBeautifier = tagsBeautifier.replace("]", "");
        tagsBeautifier = tagsBeautifier.replace("\"", "");
        tagsBeautifier = tagsBeautifier.replace(",", " ");
        tagsBeautifier = tagsBeautifier.replace(";", " ");
        happeningTags.setText("Tags associés : " + tagsBeautifier);
        happeningName.setText(happening.getName().toUpperCase());
        happeningDescription.setText(happening.getDescription());

        happeningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HappeningActivity.this, "Vous avez sélectionné une activité", Toast.LENGTH_LONG).show();
                createNotification(happening);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        HappeningActivity.this.finish();
    }

    public void createNotification(Happening happening) {
        Intent intent = new Intent(this, HappeningActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        long[] v = {500,1000};
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Alors, c'était bien ?")
                .setContentText("Activité prévue : " + happening.getName())
                .setSmallIcon(R.drawable.pin)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setVibrate(v)
                .setSound(uri)
                .addAction(R.drawable.thumbs_up, "Oui", pIntent)
                .addAction(R.drawable.thumbs_down, "Non", pIntent).build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }
}
