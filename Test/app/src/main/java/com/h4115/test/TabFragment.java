package com.h4115.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class TabFragment extends Fragment {

    protected boolean isMapOpen = false;

    protected static TabLayout tabLayout;
    protected static ViewPager viewPager;
    protected static int int_items = 2 ;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View x =  inflater.inflate(R.layout.tab_layout,null);
        tabLayout = (TabLayout) x.findViewById(R.id.tabs);
        viewPager = (ViewPager) x.findViewById(R.id.viewpager);

        viewPager.setAdapter(new MyAdapter(getChildFragmentManager()));

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                    if (!isMapOpen) {
                        ((MainActivity)getActivity()).switchFragment(false);
                        isMapOpen = true;
                    } else {
                        ((MainActivity)getActivity()).switchFragment(true);
                        isMapOpen = false;
                    }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return x;
    }

    public MapFragment getMapFragment(){
        MyAdapter viewPagerAdapter = (MyAdapter) viewPager.getAdapter();
        return viewPagerAdapter.getMapFragment();
    }

    public ListFragment getListFragment(){
        MyAdapter viewPagerAdapter = (MyAdapter) viewPager.getAdapter();
        return viewPagerAdapter.getListFragment();
    }

    class MyAdapter extends FragmentPagerAdapter {

        ListFragment listFragment;
        MapFragment mapFragment;

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 :
                    listFragment = new ListFragment();
                    return listFragment;
                case 1 :
                    mapFragment = new MapFragment();
                    return mapFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return int_items;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case 0 :
                    return "Liste";
                case 1 :
                    return "Carte";
            }
            return null;
        }

        public MapFragment getMapFragment(){
            return this.mapFragment;
        }

        public ListFragment getListFragment(){
            return this.listFragment;
        }
    }

}