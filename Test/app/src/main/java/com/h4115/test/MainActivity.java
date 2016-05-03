package com.h4115.test;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v7.widget.AppCompatTextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class MainActivity extends AppCompatActivity {

    protected User userProfile;
    protected Button dpb; //date picker button
    protected Button sfb; //search field button
    protected FloatingActionButton sal; //sort or locate
    protected FloatingActionButton sbr; //sort by remaining time
    protected FloatingActionButton sbd; //sort by distance
    protected FloatingActionButton sba; //sort by alphabetical order
    protected EditText esf; //edit text search field
    protected AppCompatTextView wtv; //welcoming text view
    protected AppCompatTextView pnt; //profile name text view
    protected AppCompatTextView pet; //profile email text view

    protected Boolean isMapOpen, isSearchFieldVisible, isSortMenuOpen;
    protected Animation rotate_forward,rotate_backward;
    protected View headerView;
    protected DrawerLayout mDrawerLayout;
    protected NavigationView mNavigationView;
    protected FragmentManager mFragmentManager;
    protected FragmentTransaction mFragmentTransaction;
    protected ArrayList<Happening> happeningList = new ArrayList<>();
    protected ArrayList<Happening> filteredHappeningList = new ArrayList<>();
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

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container_view,new TabFragment()).commit();

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name, R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        setOnClickListeners();
    }

    private void getUserFromExtras(){
        Intent myIntent = getIntent();
        Integer id = myIntent.getIntExtra("id", 0);
        Integer age = myIntent.getIntExtra("age", 20);
        String username = myIntent.getStringExtra("username");
        String password= myIntent.getStringExtra("password");
        String email= myIntent.getStringExtra("email");
        String citizen= myIntent.getStringExtra("citizen");
        String tags= myIntent.getStringExtra("tags");

        userProfile = new User(id, username, password, email, age, citizen, tags);

        if(userProfile.getTags() == "" || userProfile.getTags() == "null" || userProfile.getTags() == null){
            GetChannels getChannels = new GetChannels(getApplicationContext(), dbHandler, userProfile);
            getChannels.execute("https://goonapp-dev.herokuapp.com/channel");
        }
    }

    private void findViewsById(){
        setContentView(R.layout.activity_main);
        esf = (EditText) findViewById(R.id.search_field);
        wtv = (AppCompatTextView) findViewById(R.id.welcoming_text);
        dpb = (Button) findViewById(R.id.date_picker_button);
        sfb = (Button) findViewById(R.id.search_field_button);
        sal = (FloatingActionButton) findViewById(R.id.sort_and_locate);
        sbd = (FloatingActionButton) findViewById(R.id.sort_by_distance);
        sbr = (FloatingActionButton) findViewById(R.id.sort_by_remaining_time);
        sba = (FloatingActionButton) findViewById(R.id.sort_by_alphabetical_order);

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);

        headerView = mNavigationView.inflateHeaderView(R.layout.drawer_header);

        pet = (AppCompatTextView) headerView.findViewById(R.id.profile_email);
        pnt = (AppCompatTextView) headerView.findViewById(R.id.profile_name);

        pet.setText(this.userProfile.getEmail());
        pnt.setText(this.userProfile.getUsername());
    }

    private void setOnClickListeners(){
        sbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
                ListFragment listFragment = tabFragment.getListFragment();
                listFragment.sortByDistance();
                sal.performClick();
            }
        });

        sbr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
                ListFragment listFragment = tabFragment.getListFragment();
                listFragment.sortByRemainingTime();
                sal.performClick();
            }
        });

        sba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
                ListFragment listFragment = tabFragment.getListFragment();
                listFragment.sortByAlphabeticalOrder();
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
                    userProfile = dbHandler.getUser();
                    GetChannels getChannels = new GetChannels(getApplicationContext(), dbHandler, userProfile);
                    getChannels.execute("https://goonapp-dev.herokuapp.com/channel");

                } else if (id == R.id.nav_paths) {

                } else if (id == R.id.nav_disconnect) {
                    dbHandler.addUser(null);
                    DasAbsichtGeschaftsfuhrer.LaunchLogin(getApplicationContext());
                }
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
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
            sbr.show();
            sba.show();
        }
        else {
            if(!switchFragment) {
                sal.setImageDrawable(getResources().getDrawable(R.drawable.sort, getApplicationContext().getTheme()));
                sal.startAnimation(rotate_backward);
            }
            sbd.hide();
            sbr.hide();
            sba.hide();
        }
    }

    public void refreshHappeningList(){
        userProfile = dbHandler.getUser();
    }

    public void filterHappeningList(boolean date, boolean text){
        ArrayList<Happening> filterByDate = new ArrayList<>();
        ArrayList<Happening> filterByText = new ArrayList<>();

        if(filteredHappeningList.isEmpty()) {
            if (date) {
                for (Happening hpg : happeningList) {
                    if (hpg.endingDay == null) {
                        filterByDate.add(hpg);
                    }
                    else if (hpg.endingDay.compareTo(Calendar.getInstance()) != -1) {
                        filterByDate.add(hpg);
                    }
                }
            }
            if (text) {
                String search = esf.getText().toString();
                for (Happening hpg : happeningList){
                    if(hpg.tags.contains(search) || hpg.name.contains(search) || hpg.location.contains(search)){
                        filterByDate.add(hpg);
                    }
                }
            }
            filteredHappeningList.addAll(filterByDate);
            filteredHappeningList.addAll(filterByText);
        }
    }

    public void refreshingFinishedGeneral(){
        TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.container_view);
        MapFragment mapFragment = tabFragment.getMapFragment();
        mapFragment.refreshingFinished();
        ListFragment listFragment = tabFragment.getListFragment();
        listFragment.refreshingFinished();
    }

    public ArrayList<Happening> getHappeningList(){
        return this.happeningList;
    }

    public static class Happening {
        protected int imageResource;
        protected String name;
        protected String location;

        protected Double latitude;
        protected Double longitude;

        protected ArrayList<String> tags = new ArrayList<>();
        protected Calendar endingDay;

        public Happening(int imageResource, String name, String location, Double latitude, Double longitude, ArrayList<String> tags) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.imageResource = imageResource;
            this.name = name;
            this.location = location;
            this.tags = tags;
            this.endingDay = null;
        }

        public Happening(int imageResource, String name, String location, Double latitude, Double longitude, ArrayList<String> tags, Calendar endingDay) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.imageResource = imageResource;
            this.name = name;
            this.location = location;
            this.tags = tags;
            this.endingDay = endingDay;
        }

        public int getImageResource(){
            return this.imageResource;
        }

        public String getName(){
            return this.name;
        }

        public String getLocation(){
            return this.location;
        }

        public Double getLatitude(){
            return this.latitude;
        }

        public Double getLongitude(){
            return this.longitude;
        }

        public Calendar getEndingDay(){
            return this.endingDay;
        }
    }
}
