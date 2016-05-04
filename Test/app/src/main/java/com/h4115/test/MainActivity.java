package com.h4115.test;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v7.widget.AppCompatTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    protected User userProfile;
    protected Button dpb; //date picker button
    protected Button sfb; //search field button
    protected FloatingActionButton sal; //sort or locate
    protected FloatingActionButton sbd; //sort by distance
    protected FloatingActionButton sba; //sort by alphabetical order
    protected EditText esf; //edit text search field
    protected AppCompatTextView wtv; //welcoming text view
    protected AppCompatTextView pnt; //profile name text view
    protected AppCompatTextView pet; //profile email text view

    protected MenuItem cultureItem;
    protected MenuItem sportItem;
    protected MenuItem familyItem;
    protected MenuItem mustSeeItem;
    protected MenuItem eveningItem;
    protected MenuItem romantismItem;
    protected MenuItem restaurantItem;
    protected MenuItem trendingItem;
    protected MenuItem musicItem;

    protected Boolean isMapOpen, isSearchFieldVisible, isSortMenuOpen;
    protected Animation rotate_forward,rotate_backward;
    protected View headerView;
    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected FragmentManager mFragmentManager;
    protected FragmentTransaction mFragmentTransaction;
    protected ArrayList<Happening> happeningList = new ArrayList<>();
    protected DatePickerDialog datePickerDialog;
    protected Calendar endingDayLimit;

    protected final DBHandler dbHandler = new DBHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getUserFromExtras();
        findViewsById();
        isMapOpen = false; isSearchFieldVisible = false; isSortMenuOpen = false;
        endingDayLimit = Calendar.getInstance();
        happeningList = dbHandler.getHappenings();


        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container_view,new TabFragment()).commit();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        setOnClickListeners();

        ArrayList<Channel> channels = dbHandler.getChannels();
        showMenuItem(channels);
        if(channels == null || channels.size() == 0){
            Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
            intent.putExtra("id", userProfile.getUserId());
            intent.putExtra("username", userProfile.getUsername());
            intent.putExtra("password", userProfile.getPassword());
            intent.putExtra("email", userProfile.getEmail());
            intent.putExtra("age", userProfile.getAge());
            intent.putExtra("citizen", userProfile.getCitizen());
            intent.putExtra("tags", userProfile.getTags());
            MainActivity.this.startActivity(intent);
        }
    }

    private void getUserFromExtras() {
        Intent myIntent = getIntent();
        Integer id = myIntent.getIntExtra("id", 0);
        Integer age = myIntent.getIntExtra("age", 20);
        String username = myIntent.getStringExtra("username");
        String password = myIntent.getStringExtra("password");
        String email = myIntent.getStringExtra("email");
        String citizen = myIntent.getStringExtra("citizen");
        String tags = myIntent.getStringExtra("tags");

        userProfile = new User(id, username, password, email, age, citizen, tags);
    }

    private void findViewsById(){
        setContentView(R.layout.activity_main);
        esf = (EditText) findViewById(R.id.search_field);
        wtv = (AppCompatTextView) findViewById(R.id.welcoming_text);
        dpb = (Button) findViewById(R.id.date_picker_button);
        sfb = (Button) findViewById(R.id.search_field_button);
        sal = (FloatingActionButton) findViewById(R.id.sort_and_locate);
        sbd = (FloatingActionButton) findViewById(R.id.sort_by_distance);
        sba = (FloatingActionButton) findViewById(R.id.sort_by_alphabetical_order);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.inflateMenu(R.menu.drawer_menu);
        Menu menu = mNavigationView.getMenu();

        headerView = mNavigationView.inflateHeaderView(R.layout.drawer_header);

        pet = (AppCompatTextView) headerView.findViewById(R.id.profile_email);
        pnt = (AppCompatTextView) headerView.findViewById(R.id.profile_name);

        pet.setText(this.userProfile.getEmail());
        pnt.setText(this.userProfile.getUsername());

        cultureItem = menu.findItem(R.id.channel_culture);
        sportItem = menu.findItem(R.id.channel_sport);
        familyItem = menu.findItem(R.id.channel_family);
        mustSeeItem = menu.findItem(R.id.channel_must_see);
        eveningItem = menu.findItem(R.id.channel_evening);
        romantismItem = menu.findItem(R.id.channel_romantism);
        restaurantItem = menu.findItem(R.id.channel_restaurant);
        trendingItem = menu.findItem(R.id.channel_trending);
        musicItem = menu.findItem(R.id.channel_music);
    }

    private void hideMenuItem(){
        cultureItem.setVisible(false);
        sportItem.setVisible(false);
        familyItem.setVisible(false);
        mustSeeItem.setVisible(false);
        eveningItem.setVisible(false);
        romantismItem.setVisible(false);
        restaurantItem.setVisible(false);
        trendingItem.setVisible(false);
        musicItem.setVisible(false);
    }

    private void showMenuItem(ArrayList<Channel> channels){
        hideMenuItem();
        System.out.println(channels.size());
        for(Channel channel : channels){
            System.out.println(channel.getName());
            if(channel.getName().contains("Culture")) cultureItem.setVisible(true);
            if(channel.getName().contains("Sport")) sportItem.setVisible(true);
            if(channel.getName().contains("Famille")) familyItem.setVisible(true);
            if(channel.getName().contains("A voir")) mustSeeItem.setVisible(true);
            if(channel.getName().contains("Soirées")) eveningItem.setVisible(true);
            if(channel.getName().contains("Romantisme")) romantismItem.setVisible(true);
            if(channel.getName().contains("Restaurant")) restaurantItem.setVisible(true);
            if(channel.getName().contains("Tendance")) trendingItem.setVisible(true);
            if(channel.getName().contains("Musique")) musicItem.setVisible(true);
        }
    }

    private void setOnClickListeners(){
        sbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
                MapFragment mapFragment = tabFragment.getMapFragment();
                Location lastKnownLocation = mapFragment.lastKnownLocation;

                if(lastKnownLocation == null) {
                    Toast.makeText(MainActivity.this, "Position actuelle non disponible", Toast.LENGTH_LONG).show();
                    return;
                }

                Collections.sort(happeningList, new Comparator<Happening>() {
                    @Override
                    public int compare(Happening h1, Happening h2) {

                        TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
                        MapFragment mapFragment = tabFragment.getMapFragment();
                        Location lastKnownLocation = mapFragment.lastKnownLocation;

                        Location latLng1 = new Location("");
                        latLng1.setLatitude(h1.getLatitude());
                        latLng1.setLongitude(h1.getLongitude());

                        Location latLng2 = new Location("");
                        latLng2.setLatitude(h2.getLatitude());
                        latLng2.setLongitude(h2.getLongitude());

                        System.out.println((int)(latLng1.distanceTo(lastKnownLocation) - latLng2.distanceTo(lastKnownLocation)));
                        return (int)(latLng1.distanceTo(lastKnownLocation) - latLng2.distanceTo(lastKnownLocation));
                    }
                });
                refreshingFinishedGeneral();


                sal.performClick();
            }
        });

        sba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Collections.sort(happeningList, new Comparator<Happening>() {
                    @Override
                    public int compare(Happening h1, Happening h2) {
                        return h1.getName().compareTo(h2.getName());
                    }
                });
                refreshingFinishedGeneral();
                sal.performClick();
            }
        });


        sal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isMapOpen) {
                    sortMenuVisibility(isSortMenuOpen, false);
                    isSortMenuOpen = !isSortMenuOpen;
                } else {
                    TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
                    MapFragment mapFragment = tabFragment.getMapFragment();
                    mapFragment.fabLocate();
                }
            }
        });

        sfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

                esf.setVisibility(View.VISIBLE);
                wtv.setVisibility(View.INVISIBLE);
                sfb.setVisibility(View.INVISIBLE);

                esf.requestFocus();

                sortMenuVisibility(true, !isSortMenuOpen);
                isSearchFieldVisible = true;
                isSortMenuOpen = false;
            }
        });

        dpb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                endingDayLimit.set(year, monthOfYear, dayOfMonth);
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if (id == R.id.nav_channels) {
                    Intent intent = new Intent(MainActivity.this, ChannelActivity.class);
                    intent.putExtra("id", userProfile.getUserId());
                    intent.putExtra("username", userProfile.getUsername());
                    intent.putExtra("password", userProfile.getPassword());
                    intent.putExtra("email", userProfile.getEmail());
                    intent.putExtra("age", userProfile.getAge());
                    intent.putExtra("citizen", userProfile.getCitizen());
                    intent.putExtra("tags", userProfile.getTags());
                    MainActivity.this.startActivity(intent);

                } else if (id == R.id.nav_paths) {

                    System.out.println(dbHandler.getCircles().size());
                    TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
                    MapFragment mapFragment = tabFragment.getMapFragment();

                    if (mapFragment.markPath) {
                        Toast.makeText(MainActivity.this, "Marquage du chemin suivi désactivé", Toast.LENGTH_LONG).show();
                        mapFragment.hidePath();
                    } else {
                        Toast.makeText(MainActivity.this, "Marquage du chemin suivi activé", Toast.LENGTH_LONG).show();
                        mapFragment.showPath(dbHandler.getCircles());
                    }

                } else if (id == R.id.nav_disconnect) {
                    dbHandler.addUser(null);
                    dbHandler.addChannels(null);
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    MainActivity.this.startActivity(intent);
                    MainActivity.this.finish();
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        esf.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filterByKeyword(esf.getText().toString());
                }
                return true;
            }
        });
    }

    private void filterByKeyword(String keyword){
        ArrayList<Happening> temporary = new ArrayList<>();
        for(Happening happening : happeningList){
            if(happening.getName().contains(keyword) || happening.getDescription().contains(keyword) || happening.getTags().contains(keyword)) temporary.add(happening);
        }
        happeningList.clear();
        for(Happening happening : temporary){
            happeningList.add(happening);
        }
        refreshingFinishedGeneral();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isSearchFieldVisible) {
            esf.setVisibility(View.INVISIBLE);
            wtv.setVisibility(View.VISIBLE);
            sfb.setVisibility(View.VISIBLE);
            isSearchFieldVisible = false;

            happeningList = dbHandler.getHappenings();
            refreshingFinishedGeneral();
        }
        else {
            super.onBackPressed();
        }
    }

    public void switchFragment(boolean isMapOpen){
        if (!isMapOpen) {
            sal.setImageDrawable(getResources().getDrawable(R.drawable.gps_on, getApplicationContext().getTheme()));
            sal.startAnimation(rotate_forward);

            sortMenuVisibility(true, true);
            isSortMenuOpen = false;
        } else {
            sal.setImageDrawable(getResources().getDrawable(R.drawable.sort, getApplicationContext().getTheme()));
            sal.startAnimation(rotate_backward);
        }
        this.isMapOpen = !isMapOpen;
    }

    public void sortMenuVisibility(boolean isSortMenuOpen, boolean switchFragment){
        if(!isSortMenuOpen){
            sal.setImageDrawable(getResources().getDrawable(R.drawable.close, getApplicationContext().getTheme()));
            sal.startAnimation(rotate_forward);
            sbd.show();
            sba.show();
        }
        else {
            if(!switchFragment) {
                sal.setImageDrawable(getResources().getDrawable(R.drawable.sort, getApplicationContext().getTheme()));
                sal.startAnimation(rotate_backward);
            }
            sbd.hide();
            sba.hide();
        }
    }

    public void refreshHappeningList(){
        ArrayList<Channel> channels = dbHandler.getChannels();
        happeningList.clear();
        dbHandler.addHappenings(null);
        for(Channel channel : channels){
            GetHappenings getHappenings = new GetHappenings();
            getHappenings.execute("https://goonapp-dev.herokuapp.com/channel/" + channel.getIdChannel() + "/activities");
        }
    }

    public void refreshingFinishedGeneral(){
        ArrayList<Channel> channels = dbHandler.getChannels();
        showMenuItem(channels);
        TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
        MapFragment mapFragment = tabFragment.getMapFragment();
        mapFragment.refreshingFinished();
        ListFragment listFragment = tabFragment.getListFragment();
        listFragment.refreshingFinished();
    }

    public void launchHappeningActivity(Happening happening){

        TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
        MapFragment mapFragment = tabFragment.getMapFragment();
        Location lastKnownLocation = mapFragment.lastKnownLocation;
        Intent intent = new Intent(MainActivity.this, HappeningActivity.class);

        if(lastKnownLocation == null){
            intent.putExtra("lastKnownLatitude", 0);
            intent.putExtra("lastKnownLongitude", 0);
        } else {
            intent.putExtra("lastKnownLatitude", lastKnownLocation.getLatitude());
            intent.putExtra("lastKnownLongitude", lastKnownLocation.getLongitude());
        }
        intent.putExtra("id", happening.getId());
        intent.putExtra("name", happening.getName());
        intent.putExtra("description", happening.getDescription());
        intent.putExtra("latitude", happening.getLatitude());
        intent.putExtra("longitude", happening.getLongitude());
        intent.putExtra("temporary", happening.getTemporary());
        intent.putExtra("tags", happening.getTags());
        MainActivity.this.startActivity(intent);
    }

    public ArrayList<Happening> getHappeningList(){
        return this.happeningList;
    }

    public void addCircle(LatLng center){
        dbHandler.addCircle(center);
    }

    protected void EndOfTaskHandler(String result){

        JSONArray jsonObject = null;

        try {
            jsonObject = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (jsonObject != null) {

            for (int i = 0; i < jsonObject.length(); i++) {
                JSONObject c = null;
                try {
                    c = jsonObject.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Integer id =Integer.parseInt(c.optString("id").toString());
                String name = c.optString("name").toString();
                String description = c.optString("description").toString();
                Double latitude=c.optDouble("latitude");
                Double longitude= c.optDouble("longitude");
                Boolean temporary = c.optBoolean("temporary");
                String tags = c.optString("tags").toString();

                Happening happening = new Happening(id, name, description, latitude, longitude, temporary, tags);
                Boolean alreadyInList = false;

                for(Happening happeningInList : happeningList) if(happeningInList.getName().toString().equals(happening.getName().toString())) alreadyInList = true;
                if(!alreadyInList) happeningList.add(happening);
            }
        }

        dbHandler.addHappenings(happeningList);
        refreshingFinishedGeneral();
    }

    private class GetHappenings extends AsyncTask<String,String, String> {

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
