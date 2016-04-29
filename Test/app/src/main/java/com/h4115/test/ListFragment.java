package com.h4115.test;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


public class ListFragment extends Fragment {

    protected ListView lsv;
    protected SwipeRefreshLayout lrl;
    protected ArrayList<MainActivity.Happening> happenings;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list,null);

        lsv = (ListView) v.findViewById(R.id.listView);

        happenings = ((MainActivity)getActivity()).getHappeningList();
        HappeningAdapter hpa = new HappeningAdapter(getContext(), happenings);
        lsv.setAdapter(hpa);

        lrl = (SwipeRefreshLayout) v.findViewById(R.id.list_refresh_layout);
        lrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                RefreshTask refresh = new RefreshTask(((MainActivity)getActivity()));
                refresh.execute();
            }
        });

        return v;
    }

    public void refreshingFinished(){
        happenings = ((MainActivity)getActivity()).getHappeningList();
        HappeningAdapter hpa = new HappeningAdapter(getContext(), happenings);
        lsv.setAdapter(hpa);
        lrl.setRefreshing(false);
    }

    public void sortByRemainingTime(){
        System.out.println("Tri en cours");
    }

    public void sortByDistance(){
        System.out.println("Tri en cours");
    }

    public void sortByAlphabeticalOrder(){
        System.out.println("Tri en cours");
    }

    public class RefreshTask extends AsyncTask<Void, Void, Void> {

        protected MainActivity mainActivity;

        public RefreshTask(MainActivity mainActivity){
            this.mainActivity = mainActivity;
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            this.mainActivity.refreshHappeningList();
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... arg1) {
        }

        @Override
        protected void onPostExecute(Void arg2) {
            this.mainActivity.refreshingFinishedGeneral();
        }
    }
}