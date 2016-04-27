package com.h4115.test;

        import android.app.DatePickerDialog;
        import android.app.Dialog;
        import android.app.TimePickerDialog;
        import android.content.DialogInterface;
        import android.os.Bundle;
        import android.support.design.widget.FloatingActionButton;
        import android.support.design.widget.NavigationView;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentTransaction;
        import android.support.v4.app.Fragment;
        import android.support.v4.view.GravityCompat;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.animation.Animation;
        import android.view.animation.AnimationUtils;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.Toast;

        import java.util.Calendar;

public class MainActivity extends AppCompatActivity{

    Button dpb;
    FloatingActionButton sal;
    Boolean isMapOpen;
    Animation rotate_forward,rotate_backward;

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dpb = (Button) findViewById(R.id.date_picker_button);
        sal = (FloatingActionButton) findViewById(R.id.sort_and_locate);
        isMapOpen = false;

        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.shitstuff) ;

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                if (menuItem.getItemId() == R.id.nav_item_inbox) {
                    FragmentTransaction xfragmentTransaction = mFragmentManager.beginTransaction();
                    xfragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();
                }

                return false;
            }

        });

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();

        sal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isMapOpen){
                    TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.containerView);
                    ListFragment listFragment = tabFragment.getListFragment();
                    listFragment.fabSort();
                }
                else {
                    TabFragment tabFragment = (TabFragment) getSupportFragmentManager().findFragmentById(R.id.containerView);
                    MapFragment mapFragment = tabFragment.getMapFragment();
                    mapFragment.fabLocate();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawerLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void changeFab(boolean isMapOpen){
        if(!isMapOpen){
            sal.setImageDrawable(getResources().getDrawable(R.drawable.gps_on, getApplicationContext().getTheme()));
            sal.startAnimation(rotate_forward);
        }
        else {
            sal.setImageDrawable(getResources().getDrawable(R.drawable.sort, getApplicationContext().getTheme()));
            sal.startAnimation(rotate_backward);
        }
        this.isMapOpen = !isMapOpen;
    }
}
